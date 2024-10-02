package bot.linkedin;


import io.vavr.control.Try;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Getter
public class BasePageV1 {

	protected final WebDriver driver;
	protected final Actions actions;
	protected final WebDriverWait wait;

	public BasePageV1(WebDriver driver) {
		this.driver = driver;
		this.actions = new Actions(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public WebElement find(By locator) {
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
		actions.scrollToElement(element).perform();
		return element;
	}

	public WebElement moveMouseToElement(WebElement element) {
		actions.moveToElement(element).perform();
		return element;
	}


	public Try<Void> tryClick(By locator) {
		return Try.run(() -> click(find(locator)));
	}

	public WebElement click(WebElement element) {
		actions.click(element).perform();
		return element;
	}


}
