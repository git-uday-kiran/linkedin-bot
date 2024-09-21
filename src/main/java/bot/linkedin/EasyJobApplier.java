package bot.linkedin;

import bot.enums.EasyApplyOption;
import bot.linkedin.services.JobsApplied;
import bot.linkedin.services.JobsAppliedRepo;
import bot.linkedin.services.QuestionAnswerService;
import bot.linkedin.services.Sounds;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static bot.linkedin.Locations.EASY_APPLY;
import static bot.utils.ThroatUtils.*;
import static bot.utils.Utils.tryCatch;
import static java.util.Objects.requireNonNull;

@Log4j2
@Service
public class EasyJobApplier extends BasePage {

	private final Tasks tasks;
	private final QuestionAnswerService questionAnswer;
	private final JobsAppliedRepo appliedRepo;
	private final Sounds sounds;

	public EasyJobApplier(WebDriver driver, Tasks tasks, QuestionAnswerService questionAnswer, JobsAppliedRepo appliedRepo, Sounds sounds) {
		super(driver);
		this.tasks = tasks;
		this.questionAnswer = questionAnswer;
		this.appliedRepo = appliedRepo;
		this.sounds = sounds;
	}

	public void apply(JobFilter filter) {
		String searchQuery = requireNonNull(filter.getSearchQuery(), "query can not be null");
		Assert.state(filter.getEasyApply() == EasyApplyOption.ENABLE, "Can only apply to easy apply jobs");

		tasks.goToHomePage();
		tasks.clickJobs();
		tasks.performSearchQuery(searchQuery);
		throatMedium();

		tasks.applyFilter(filter);
		throatLow();

		tryCatch(this::startCheckingJobs);
		sounds.finished();
	}

	private void startCheckingJobs() {
		scanJobs(
			job -> findOptional(EASY_APPLY).ifPresentOrElse(this::processEasyApplyElement, easyApplyNotFound())
		);
	}

	private void processEasyApplyElement(WebElement easyApplyElement) {
		String jobDesc = getJobDescription();
		log.info("Job description: {}", jobDesc);
		applyEasyApplyJob(easyApplyElement);
		sounds.appliedJob();
		appliedRepo.save(new JobsApplied(jobDesc));
	}

	private Runnable easyApplyNotFound() {
		return () -> log.warn("Easy Apply not found.");
	}

	public void applyEasyApplyJob(WebElement easyApplyElement) {
		log.info("Applying a job... ");
		click(easyApplyElement);
		throatMedium();
		Optional<WidGet> opWidGet = Optional.of(new WidGet(driver, questionAnswer));
		while (opWidGet.isPresent()) {
			WidGet widGet = opWidGet.get();
			widGet.init();
			opWidGet = widGet.nextWidget();
		}
	}

	public void scanJobs(Consumer<WebElement> jobConsumer) {
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
		return new HashSet<>(driver.findElements(Locations.JOB_CARDS_LOCATION));
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
			log.warn("Next page wasn't found: {}", page, exception);
			return false;
		}
	}

	private String getJobDescription() {
		By jobDescLocation = By.cssSelector(".jobs-details");
		return find(jobDescLocation).getText().replace('\n', '\t');
	}

}
