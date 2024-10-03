package bot.linkedin;

import bot.enums.EasyApplyOption;
import bot.linkedin.filters.JobSearchFilter;
import bot.linkedin.models.CanApply;
import bot.linkedin.models.JobsApplied;
import bot.linkedin.question_solvers.CheckBoxQuestions;
import bot.linkedin.question_solvers.InputQuestions;
import bot.linkedin.question_solvers.RadioOptionsQuestions;
import bot.linkedin.question_solvers.SelectOptionsQuestions;
import bot.linkedin.services.*;
import bot.utils.ThroatUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static bot.linkedin.Locations.*;
import static bot.utils.ThroatUtils.*;
import static io.vavr.control.Try.run;
import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;

@Log4j2
@Service
public class EasyJobApplier extends BasePage {

	private final Tasks tasks;
	private final QuestionAnswerService questionAnswer;
	private final JobsAppliedRepo appliedRepo;
	private final Sounds sounds;
	private final JobApplyFilterService filterService;
	private final CanApplyRepo canApplyRepo;
	private final JobsDayCountRepo jobsDayCountRepo;

	private final SelectOptionsQuestions selectOptionsQuestions;
	private final RadioOptionsQuestions radioOptionsQuestions;
	private final InputQuestions inputQuestions;
	private final CheckBoxQuestions checkBoxQuestions;

	public EasyJobApplier(WebDriver driver, Tasks tasks, QuestionAnswerService questionAnswer, JobsAppliedRepo appliedRepo, Sounds sounds, JobApplyFilterService filterService, CanApplyRepo canApplyRepo, JobsDayCountRepo jobsDayCountRepo, SelectOptionsQuestions selectOptionsQuestions, RadioOptionsQuestions radioOptionsQuestions, InputQuestions inputQuestions, CheckBoxQuestions checkBoxQuestions) {
		super(driver);
		this.tasks = tasks;
		this.questionAnswer = questionAnswer;
		this.appliedRepo = appliedRepo;
		this.sounds = sounds;
		this.filterService = filterService;
		this.canApplyRepo = canApplyRepo;
		this.jobsDayCountRepo = jobsDayCountRepo;
		this.selectOptionsQuestions = selectOptionsQuestions;
		this.radioOptionsQuestions = radioOptionsQuestions;
		this.inputQuestions = inputQuestions;
		this.checkBoxQuestions = checkBoxQuestions;
	}

	public void apply(JobSearchFilter filter) {
		log.info("Applying easy apply jobs by applying advanced filters");
		String searchQuery = requireNonNull(filter.getSearchQuery(), "Search query can not be null");
		Assert.state(filter.getEasyApply() == EasyApplyOption.ENABLE, "Can only apply to easy apply jobs");

		tasks.goToHomePage();
		tasks.clickJobs();
		tasks.performSearchQuery(searchQuery);
		tasks.applyFilter(filter);

		run(this::startCheckingJobs).orElseRun(logError());
		sounds.finished();
	}

	public void applyWithoutSearchFilter(String searchQuery) {
		log.info("Applying jobs without search filter");
		requireNonNull(searchQuery, "Search query can not be null");
		tasks.goToHomePage();
		tasks.clickJobs();
		tasks.performSearchQuery(searchQuery);
		throatMedium();
		tryClickEasyApplyFilter();
		run(this::startCheckingJobs).orElseRun(logError());
		sounds.finished();
	}

	public void applyJobsInHomePage() {
		log.info("Applying jobs in home page");
		By showAllLocation = By.cssSelector(".discovery-templates-vertical-list__footer > a");
		tasks.goToHomePage();
		tasks.clickJobs();

		throatMedium();
		Set<WebElement> processed = new HashSet<>();
		IntStream.range(0, 10).forEach(e -> {
			findAll(showAllLocation).stream()
					.filter(not(processed::contains))
					.peek(processed::add)
					.forEach(this::tryProcessShowAll);
			scrollPageVertically(1000);
		});
		sounds.finished();
	}

	private void tryProcessShowAll(WebElement showAll) {
		run(() -> processShowAll(showAll)).orElseRun(logError());
	}

	private void processShowAll(WebElement showAll) {
		highlight(showAll);
		showAll.sendKeys(Keys.CONTROL, Keys.RETURN);
		log.info("Clicked: {}", showAll.getText());

		String currentTab = driver.getWindowHandle();
		List<String> tabs = new java.util.ArrayList<>(driver.getWindowHandles());
		tabs.removeIf(currentTab::equals);
		driver.switchTo().window(tabs.getFirst());

		throatMedium();
		tryClickEasyApplyFilter();
		run(this::startCheckingJobs).orElseRun(logError());
		driver.close();
		driver.switchTo().window(currentTab);
		sounds.finished();

		log.info("Processing show all completed.");
		throatMedium();
	}


	private void applyJob(WebElement job) {
		String jobTitle = job.getText().replace('\n', '\t');
		if (jobTitle.contains("Applied")) return;
//		if (jobTitle.contains("Viewed")) return;

		click(job);
//		throatLow();
		if (filterService.canProcess(jobTitle, getJobDescription())) {
			tryFindElement(EASY_APPLY).ifPresentOrElse(this::processEasyApplyElement, easyApplyNotFound(jobTitle));
		}
	}

	private Runnable easyApplyNotFound(String jobTitle) {
		return () -> {
			log.warn("Easy Apply not found.");
			saveJobToDb(jobTitle);
		};
	}

	private void saveJobToDb(String jobTitle) {
		log.info("Saving this job and job url to database.");
		String currentUrl = driver.getCurrentUrl();
		canApplyRepo.save(new CanApply(jobTitle, getJobDescription(), currentUrl));
	}

	private void processEasyApplyElement(WebElement easyApplyElement) {
		String jobDesc = getJobDescription();
		applyEasyApplyJob(easyApplyElement);
		sounds.appliedJob();
		appliedRepo.save(new JobsApplied(jobDesc));
		jobsDayCountRepo.incrementCountSafely(LocalDate.now());
	}

	private void applyEasyApplyJob(WebElement easyApplyElement) {
		log.info("Applying job... ");
		click(easyApplyElement);
		waitForElementPresence(EASY_APPLY_MODEL);
		Optional<WidGet> opWidGet = Optional.of(new WidGet(driver, questionAnswer, radioOptionsQuestions, selectOptionsQuestions, inputQuestions, checkBoxQuestions));
		while (opWidGet.isPresent()) {
			WidGet widGet = opWidGet.get();
			widGet.init();
			opWidGet = widGet.nextWidget();
		}
	}

	private void startCheckingJobs() {
		scanJobs(this::applyJob);
	}

	private void scanJobs(Consumer<WebElement> jobConsumer) {
		log.info("Clicking each job in jobs list...");

		for (int page = 1; page <= 100; page++) {
			List<WebElement> jobs = getNewJobs();
			log.info("Found {} jobs", jobs.size());
			jobs.stream().peek(this::scrollIntoView).forEach(jobConsumer);
			if (!tryGotToPage(page + 1)) break;
		}
	}

	public void tryClickEasyApplyFilter() {
		tryClick(By.cssSelector("#search-reusables__filters-bar>ul>li:nth-child(7)>div>button"))
				.orElse(tryClick(By.cssSelector(".scaffold-layout-toolbar>div>section>div>div>div>ul>li:nth-child(8)>div>button")))
				.orElse(tryClick(By.linkText("Easy Apply")))
				.andThen(ThroatUtils::throatMedium)
				.orElseRun(logError("Tried finding and clicking Easy Apply button."));
	}

	private List<WebElement> getNewJobs() {
		return waitForPresence(JOB_CARDS_LOCATION);
	}

	private boolean tryGotToPage(int page) {
		log.info("Going to next page: {}", page);
		By locator = By.xpath("//button[@aria-label='Page " + page + "']");
		Optional<WebElement> opNextPage = tryFindElement(locator);
		if (opNextPage.isPresent()) {
			WebElement nextPage = opNextPage.get();
			scrollIntoView(nextPage);
			click(nextPage);
			log.info("New page: {}", page);
			return true;
		} else {
			log.warn("Next page {} not found.", page);
			return false;
		}
	}

	private String getJobDescription() {
		return waitForElementPresence(JOB_DESCRIPTION).getText().replace('\n', '\t');
	}


	private Consumer<Throwable> logError(String message) {
		return throwable -> log.error(message, throwable);
	}

	private Consumer<Throwable> logError() {
		return logError("Something went wrong.");
	}

}
