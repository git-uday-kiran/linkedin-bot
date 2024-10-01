package bot.linkedin;

import io.vavr.control.Try;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import static io.vavr.control.Try.ofCallable;
import static io.vavr.control.Try.run;

@Setter
@Getter
@Log4j2
public class BasePage {

	public final WebDriver driver;
	private WebDriverWait wait;
	public Actions actions;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.actions = new Actions(driver);
	}

	public WebElement find(By locator) {
		return driver.findElement(locator);
	}

	public Callable<List<WebElement>> findAll(By locator) {
		return () -> driver.findElements(locator);
	}

	public List<WebElement> findAllWait(By locator) {
		return ofCallable(() -> wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator)))
				.getOrElseGet(throwable -> {
					log.error("Error while finding by waiting", throwable);
					return Collections.emptyList();
				});
	}

	public WebElement findWait(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public Optional<WebElement> findOptional(By locator) {
		return Optional.ofNullable(ofCallable(() -> find(locator)).getOrNull());
	}

	public List<WebElement> findAllDisplayedAndEnabled(By locator) {
		return ofCallable(findAll(locator))
				.map(list -> list.stream()
						.filter(this::displayedAndEnabled)
						.toList())
				.getOrElse(Collections.emptyList());
	}

	public boolean displayedAndEnabled(WebElement element) {
		return element.isDisplayed() && element.isEnabled();
	}

	public void visibilityWait(By locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void clickableWait(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	@PostConstruct
	public void postConstruct() {
		driver.manage().timeouts().pageLoadTimeout(Duration.ofHours(1));
	}

	public void set(By locator, CharSequence... keys) {
		WebElement element = find(locator);
		element.clear();
		element.sendKeys(keys);
	}

	public void clickWait(By... locators) {
		for (By locator : locators) {
			clickWait(findWait(locator));
		}
	}

	public void clickWait(WebElement element) {
		WebElement target = wait.until(ExpectedConditions.elementToBeClickable(element));
		click(target);
	}

	public Try<Void> tryClick(By locator) {
		return Try.run(() -> click(find(locator)));
	}

	public Try<Void> tryClick(WebElement element) {
		return run(() -> click(element));
	}

	public void click(By... locators) {
		for (By locator : locators) {
			click(find(locator));
		}
	}

	public void click(WebElement... elements) {
		for (WebElement element : elements) {
			String clicked = element.getText().replace('\n', ' ');
			if (clicked.isEmpty()) clicked = element.getAccessibleName();
			scrollJS(element);
			highlight(elements);
//			actions.scrollToElement(element).moveToElement(element).click(element).perform();
			element.click();
			log.info("Clicked: {}", clicked);
		}
	}

	public void clickJS(By... locators) {
		for (By locator : locators) {
			clickJS(find(locator));
		}
	}

	public void clickJS(WebElement... webElements) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (WebElement element : webElements) {
			scrollJS(element);
			highlight(webElements);
			js.executeScript("arguments[0].click();", element);
		}
	}

	public void highlight(WebElement... webElements) {
		String script = "arguments[0].setAttribute('style', 'border: 2px solid yellow')";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (WebElement webElement : webElements) {
			js.executeScript(script, webElement);
		}
	}


	public void scrollJS(By locator) {
		scrollJS(find(locator));
	}

	public void scrollJS(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}

	public void scrollDown(double pixels) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0," + pixels + ")", "");
	}

	public void scrollDown() {
		scrollDown(500);
	}

	public void scrollBottomOfPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}


}
