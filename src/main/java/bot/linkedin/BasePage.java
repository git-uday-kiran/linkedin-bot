package bot.linkedin;


import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static io.vavr.control.Try.run;

@Getter
@Log4j2
public class BasePage {

	protected final WebDriver driver;
	protected final Actions actions;
	protected final WebDriverWait wait;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.actions = new Actions(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	public WebElement findElement(By locator) {
		return driver.findElement(locator);
	}

	public List<WebElement> findAll(By locator) {
		return driver.findElements(locator);
	}

	public void scrollPageVertically(int pixels) {
		WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin.fromViewport();
		actions.scrollFromOrigin(scrollOrigin, 0, pixels).perform();
	}

	public boolean isPresentInDOM(By location) {
		return !driver.findElements(location).isEmpty();
	}

	public List<WebElement> waitForPresence(By location) {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(location));
	}

	public Optional<WebElement> waitForElementPresence(By location, int waitSeconds) {
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
			WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(location));
			return Optional.ofNullable(element);
		} catch (TimeoutException _) {
		}
		return Optional.empty();
	}

	public WebElement waitForElementPresence(By location) {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(location)).getFirst();
	}

	public WebElement waitForClickable(WebElement element) {
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public WebElement waitForVisible(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	public WebElement scrollInsideVertically(WebElement element, int pixels) {
		return scrollInside(element, 0, pixels);
	}

	public WebElement scrollInside(WebElement element, int x, int y) {
		WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin.fromElement(element);
		actions.scrollFromOrigin(scrollOrigin, x, y).perform();
		return element;
	}

	public WebElement scrollIntoView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		run(() -> waitForVisible(element));
		return element;
	}

	public Try<Void> tryClick(By locator) {
		return run(() -> click(findElement(locator)));
	}

	public Try<Void> tryClick(WebElement element) {
		return run(() -> click(element));
	}

	public void click(WebElement element) {
		String clicked = element.getText().replace('\n', ' ');
		if (clicked.isEmpty()) clicked = element.getAccessibleName();

		scrollIntoView(element);
		waitForClickable(element);
		highlight(element);

		element.click();
		log.info("Clicked: {}", clicked);
	}

	public void highlight(WebElement... webElements) {
		String script = "arguments[0].setAttribute('style', 'border: 2px solid yellow')";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (WebElement webElement : webElements) {
			js.executeScript(script, webElement);
		}
	}

	public Optional<WebElement> tryFindElement(By location) {
		List<WebElement> elements = driver.findElements(location);
		return elements.isEmpty() ? Optional.empty() : Optional.of(elements.getFirst());
	}

	public void findAndClick(By location) {
		WebElement element = driver.findElement(location);
		click(element);
	}

	public void doUntilSuccess(CheckedRunnable action) {
		while (run(action).isFailure()) ;
	}

}
