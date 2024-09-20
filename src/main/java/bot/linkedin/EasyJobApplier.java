package bot.linkedin;

import bot.linkedin.question_answer.QuestionAnswerService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static bot.linkedin.Locations.*;
import static bot.utils.ThroatUtils.*;
import static java.util.Objects.requireNonNull;

@Log4j2
@Service
public class EasyJobApplier extends BasePage {

	private final Tasks tasks;
	private final QuestionAnswerService questionAnswer;

	public EasyJobApplier(WebDriver driver, Tasks tasks, QuestionAnswerService questionAnswer) {
		super(driver);
		this.tasks = tasks;
		this.questionAnswer = questionAnswer;
	}

	public void apply(JobFilter filter) {
		tasks.goToHomePage();
		tasks.clickJobs();
		tasks.performSearchQuery(requireNonNull(filter.getSearchQuery(), "query can not be null"));
		throatMedium();
		tasks.applyFilter(filter);
		throatLow();
		applyRunAway();
	}

	public void applyRunAway() {
		clickEachJobs(element -> findOptional(EASY_APPLY).ifPresentOrElse(this::applyJob, easyApplyNotFound()));
	}

	private Runnable easyApplyNotFound() {
		return () -> log.warn("Easy Apply not found.");
	}

	public void applyJob(WebElement easyApplyElement) {
		log.info("Applying a job... ");
		easyApplyElement.click();
		throatMedium();
		Optional<WidGet> opWidGet = Optional.of(new WidGet(driver, questionAnswer));
		while (opWidGet.isPresent()) {
			WidGet widGet = opWidGet.get();
			widGet.init();
			opWidGet = widGet.nextWidget();
		}
	}

	public void clickEachJobs(Consumer<WebElement> consumer) {
		log.info("Clicking each job in jobs list...");

		Set<WebElement> elements = new LinkedHashSet<>(driver.findElements(JOB_CARDS_LOCATION));
		Set<String> processed = new HashSet<>();
		log.info("Found {} jobs", elements.size());

		int page = 1;
		while (!elements.isEmpty()) {
			for (WebElement element : elements) {
				element.click();
				consumer.accept(element);
				processed.add(element.getText());
				throatLow();
			}

			scrollDown();
			elements.addAll(getNewJobs(processed));
			if (elements.isEmpty()) {
				if (!goToPageIfExist(++page)) break;
				elements.addAll(getNewJobs(processed));
			}
			log.info("Found {} jobs", elements.size());
		}
	}

	private Set<WebElement> getNewJobs(Set<String> processed) {
		Set<WebElement> elements = new HashSet<>(driver.findElements(Locations.JOB_CARDS_LOCATION));
		elements.removeIf(element -> processed.contains(element.getText()));
		return elements;
	}

	public boolean goToPageIfExist(int page) {
		try {
			log.info("Going to next page {}", page);
			By locator = By.xpath("//button[@aria-label='Page " + page + "']");
			scrollJS(locator);
			click(locator);
			throatMedium();
			log.info("New page {}", page);
			return true;
		} catch (Exception exception) {
			log.warn("Next page wasn't found: {}", page, exception);
			return false;
		}
	}
}
