package bot.linkedin;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static bot.linkedin.Locations.*;
import static bot.utils.ThroatUtils.*;
import static bot.utils.Utils.*;

@Log4j2
@Component
public class LinkedInBot extends BasePage implements ApplicationRunner {

	private final WebDriver driver;
	private final Tasks tasks;
	private final JobFilter filter;
	private final Scheduler scheduler;

	private final SignalGiver signalGiver;

	public LinkedInBot(WebDriver driver, Tasks tasks, JobFilter filter, Scheduler scheduler, SignalGiver signalGiver) {
		super(driver);
		this.tasks = tasks;
		this.filter = filter;
		this.driver = driver;
		this.scheduler = scheduler;
		this.signalGiver = signalGiver;
	}

	@Override
	public void run(ApplicationArguments args) {
		throatMedium();
		tasks.searchJobs("java");
		tasks.applyFilter(filter);
		throatMedium();

		easyApplicationProcessChecker();
		throatMedium();

		tryCatch(() -> clickEachJobs(this::easyApply));
		sleep(100000);
	}

	public void easyApply(WebElement element) {
		log.info("Applying job... ");
		Optional<WebElement> optional = findOptional(EASY_APPLY);
		if (optional.isPresent()) {
			optional.get().click();
			signalGiver.waitForSignal();
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
		continuesClicker(SUBMIT_APPLICATION, Duration.ofSeconds(3), "Clicking submit application option", signalGiver::signal);
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
			} catch (Exception exception) {
//				log.error(exception.getMessage());
			}
		};
		scheduler.schedule(task, period);
	}

	public void continuesClicker(By location, Duration period, String message) {
		continuesClicker(location, period, message, null);
	}

	public void clickEachJobs(Consumer<WebElement> consumer) {
		log.info("Clicking each job in jobs list...");

		By jobsLocator = new By.ByCssSelector("div.job-card-container");
		Set<WebElement> elements = new LinkedHashSet<>(driver.findElements(jobsLocator));
		log.info("Found {} jobs", elements.size());

		Set<String> processed = new HashSet<>();

		int page = 1;
		while (!elements.isEmpty()) {
			for (WebElement element : elements) {
				element.click();
				consumer.accept(element);
				processed.add(element.getText());

				if (processed.size() == 5) {
					System.out.println("Processed: " + processed.size());
					return;
				}
				throatLow();
			}

			scrollDown();
			elements = getWebElements(elements, jobsLocator, processed);
			if (elements.isEmpty()) {
				if (!nextPage(++page)) break;
				elements = getWebElements(elements, jobsLocator, processed);
			}

			log.info("Found {} jobs", elements.size());
		}
	}

	private Set<WebElement> getWebElements(Set<WebElement> elements, By jobsLocator, Set<String> processed) {
		elements = new HashSet<>(driver.findElements(jobsLocator));
		elements.removeIf(element -> processed.contains(element.getText()));
		return elements;
	}

	public boolean nextPage(int page) {
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
