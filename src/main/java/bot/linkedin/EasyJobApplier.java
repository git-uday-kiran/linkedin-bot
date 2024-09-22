package bot.linkedin;

import bot.enums.EasyApplyOption;
import bot.linkedin.services.*;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static bot.linkedin.Locations.EASY_APPLY;
import static bot.utils.ThroatUtils.*;
import static bot.utils.Utils.*;
import static java.util.Objects.requireNonNull;

@Log4j2
@Service
public class EasyJobApplier extends BasePage {

	private final Tasks tasks;
	private final QuestionAnswerService questionAnswer;
	private final JobsAppliedRepo appliedRepo;
	private final Sounds sounds;
	private final JobApplyFilterService filterService;
	private final CanApplyRepo canApplyRepo;

	public EasyJobApplier(WebDriver driver, Tasks tasks, QuestionAnswerService questionAnswer, JobsAppliedRepo appliedRepo, Sounds sounds, JobApplyFilterService filterService, CanApplyRepo canApplyRepo) {
		super(driver);
		this.tasks = tasks;
		this.questionAnswer = questionAnswer;
		this.appliedRepo = appliedRepo;
		this.sounds = sounds;
		this.filterService = filterService;
		this.canApplyRepo = canApplyRepo;
	}

	public void apply(JobSearchFilter filter) {
		String searchQuery = requireNonNull(filter.getSearchQuery(), "query can not be null");
		Assert.state(filter.getEasyApply() == EasyApplyOption.ENABLE, "Can only apply to easy apply jobs");

		tasks.goToHomePage();
		tasks.clickJobs();
		tasks.performSearchQuery(searchQuery);
		tasks.applyFilter(filter);

		tryCatch(this::startCheckingJobs);
		sounds.finished();
	}

	public void applyJobsInHomePage() {
		By showAllLocation = By.cssSelector(".discovery-templates-vertical-list__footer > a");
		tasks.goToHomePage();
		tasks.clickJobs();
		throatMedium();
		IntStream.range(0, 10).forEach(e -> {
			scrollBottomOfPage();
			findAllWait(showAllLocation).forEach(showAll -> {
				tryIgnore(() -> processShowAll(showAll));
			});
		});
		sounds.finished();
	}

	private void processShowAll(WebElement showAll) {
		showAll.sendKeys(Keys.CONTROL, Keys.RETURN);
		log.info("Clicked: {}", showAll.getText());

		String currentTab = driver.getWindowHandle();
		List<String> tabs = new java.util.ArrayList<>(driver.getWindowHandles());
		tabs.removeIf(currentTab::equals);
		driver.switchTo().window(tabs.get(0));

		tryCatch(this::startCheckingJobs);
		driver.close();
		sounds.finished();
	}

	private void startCheckingJobs() {
		scanJobs(job -> {
			String jobTitle = job.getText();
			if (filterService.canProcess(jobTitle, getJobDescription())) {
				findOptional(EASY_APPLY).ifPresentOrElse(this::processEasyApplyElement, easyApplyNotFound());
			} else {
				log.info("Saving this job and job url to database.");
				String currentUrl = driver.getCurrentUrl();
				canApplyRepo.save(new CanApply(jobTitle, currentUrl));
			}
		});
	}

	private void processEasyApplyElement(WebElement easyApplyElement) {
		String jobDesc = getJobDescription();
		applyEasyApplyJob(easyApplyElement);
		sounds.appliedJob();
		appliedRepo.save(new JobsApplied(jobDesc));
	}

	private Runnable easyApplyNotFound() {
		return () -> log.warn("Easy Apply not found.");
	}

	private void applyEasyApplyJob(WebElement easyApplyElement) {
		log.info("Applying job... ");
		click(easyApplyElement);
		throatMedium();
		Optional<WidGet> opWidGet = Optional.of(new WidGet(driver, questionAnswer));
		while (opWidGet.isPresent()) {
			WidGet widGet = opWidGet.get();
			widGet.init();
			opWidGet = widGet.nextWidget();
		}
	}

	private void scanJobs(Consumer<WebElement> jobConsumer) {
		log.info("Clicking each job in jobs list...");

		for (int page = 1; page <= 100; page++) {
			Set<WebElement> jobs = getNewJobs();
			log.info("Found {} jobs", jobs.size());

			for (WebElement job : jobs) {
				click(job);
				if (appliedRepo.existsByJobDesc(getJobDescription())) continue;
				jobConsumer.accept(job);
				throatLow();
			}

			scrollDown();
			if (!goToPageIfExist(page + 1)) break;
		}
	}

	private Set<WebElement> getNewJobs() {
		return new HashSet<>(findAllWait(Locations.JOB_CARDS_LOCATION));
	}

	public boolean goToPageIfExist(int page) {
		try {
			log.info("Going to next page: {}", page);
			By locator = By.xpath("//button[@aria-label='Page " + page + "']");
			scrollJS(locator);
			click(locator);
			throatMedium();
			log.info("New page: {}", page);
			return true;
		} catch (Exception exception) {
			log.warn("Next page wasn't found: {}, Message: {}", page, exception.getLocalizedMessage());
			return false;
		}
	}

	private String getJobDescription() {
		By jobDescLocation = By.cssSelector(".jobs-details");
		return find(jobDescLocation).getText().replace('\n', '\t');
	}

}
