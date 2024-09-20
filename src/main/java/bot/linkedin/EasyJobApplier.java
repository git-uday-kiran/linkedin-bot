package bot.linkedin;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static bot.linkedin.Locations.*;
import static bot.utils.ThroatUtils.*;
import static bot.utils.Utils.tryCatch;
import static java.util.Objects.requireNonNull;

@Log4j2
@Service
public class EasyJobApplier extends BasePage {

	private final Tasks tasks;
	private final Scheduler scheduler;
	private final SignalManager signalManager;

	public EasyJobApplier(WebDriver driver, SignalManager signalManager, Scheduler scheduler, Tasks tasks) {
		super(driver);
		this.tasks = tasks;
		this.scheduler = scheduler;
		this.signalManager = signalManager;
	}

	public void init() {
		easyApplicationProcessChecker();
	}

	public void apply(JobFilter filter) {
		tasks.goToHomePage();
		tasks.clickJobs();
		tasks.performSearchQuery(requireNonNull(filter.getSearchQuery(), "query can not be null"));
		throatMedium();
		tasks.applyFilter(filter);
		throatLow();
		tryCatch(() -> clickEachJobs(this::applyJob));
	}

	public void applyJob(WebElement element) {
		log.info("Applying job... ");
		Optional<WebElement> optional = findOptional(EASY_APPLY);
		if (optional.isPresent()) {
			optional.get().click();
			signalManager.waitForSignal(SignalManager.Signals.SUBMIT);
			closeWidget();
			throatLow();
		} else {
			log.warn("Easy Apply option is not there");
		}
	}

	public void easyApplicationProcessChecker() {
		continuesClicker(CONTINUE_APPLYING, Duration.ofSeconds(3), "Clicking continue applying option");
		continuesClicker(NEXT, Duration.ofSeconds(3), "Clicking next option");
		continuesClicker(REVIEW, Duration.ofSeconds(3), "Clicking review option");
		continuesClicker(SUBMIT_APPLICATION, Duration.ofSeconds(3), "Clicking submit application option", () -> signalManager.signal(SignalManager.Signals.SUBMIT));
	}

	public void closeWidget() {
		log.info("Closing widget...");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(CLOSE_WIDGET));
		doneButton.click();
		log.info("Widget closed");
	}

	public void continuesClicker(By location, Duration period, String message, Runnable action) {
		Runnable task = () -> {
			try {
				findOptional(location).ifPresent(element -> {
					if (tryCatch(() -> element.isDisplayed() && element.isEnabled())) {
						log.info(message);
						element.click();
						if (action != null) action.run();
					}
				});
			} catch (Exception ignored) {
			}
		};
		scheduler.schedule(task, period);
	}

	public void continuesClicker(By location, Duration period, String message) {
		continuesClicker(location, period, message, null);
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
