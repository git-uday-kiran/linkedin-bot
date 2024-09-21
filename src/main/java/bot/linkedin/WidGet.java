package bot.linkedin;

import bot.linkedin.services.QuestionAnswerService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.IntStream;

import static bot.linkedin.Locations.*;
import static bot.utils.ThroatUtils.*;
import static bot.utils.Utils.*;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Log4j2
public class WidGet extends BasePage {

	private final QuestionAnswerService questionAnswer;

	public WidGet(WebDriver driver, QuestionAnswerService questionAnswer) {
		super(driver);
		this.questionAnswer = questionAnswer;
	}

	public void init() {
		checkWorkExperienceQuestions();
		checkRadioButtonQuestions();
		checkSelectionOptionQuestions();
		checkCheckBoxOptions();
		checkNormalQuestions();
	}

	private void checkNormalQuestions() {
		IntStream.range(0, 10).forEach(q -> {
			By questionLabel = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + q + ") > .jobs-easy-apply-form-element > div > div > div > label");
			By questionInput = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + q + ") > .jobs-easy-apply-form-element > div > div > div > :is(input,textarea)");
			findOptional(questionLabel).ifPresent(question -> {
				var input = find(questionInput);
				String answer = questionAnswer.ask(question.getText());
				input.clear();
				input.sendKeys(answer);
			});
		});
	}

	private void checkLabel(String label) {
		By locator = By.xpath("//label[text()='" + label + "']");
		findOptional(locator).ifPresent(WebElement::click);
	}

	private void checkSelectionOptionQuestions() {
		IntStream.range(0, 10).forEach(questionNo -> {
			By questionLabel = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > div > label > span:first-child");
			findOptional(questionLabel).ifPresent(question -> {

				Function<Integer, By> locator = optionNo -> By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > div > select > option:nth-of-type(" + optionNo + ")");
				Map<String, WebElement> options = findAllSelectOptions(locator).stream().collect(toMap(WebElement::getText, identity()));

				String answer = questionAnswer.ask(question.getText(), options.keySet().toArray(new String[0]));
				question.click();
				throatLow();

				if (question.getText().trim().equals("City")) {
					fillCity(answer, questionNo);
				} else {
					options.get(answer).click();
				}
			});
		});
	}

	private void fillCity(String answer, int questionNo) {
		By inputLocation = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > div > div > :is(input,textarea)");
		By arrowDown = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > div > div > div:nth-of-type(2)");
		find(inputLocation).sendKeys(answer, Keys.ENTER);
		throatMedium();
		find(arrowDown).sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
	}

	private Set<WebElement> findAllSelectOptions(Function<Integer, By> locationGiver) {
		Set<WebElement> elements = new HashSet<>();
		IntStream.range(0, 20).forEach(n -> tryCatchGet(() -> driver.findElements(locationGiver.apply(n))).ifPresent(elements::addAll));
		return elements;
	}

	private void checkRadioButtonQuestions() {
		IntStream.range(0, 20).forEach(q -> {
			By questionLabel = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + q + ") > .jobs-easy-apply-form-element > fieldset > legend > span > span:first-child");
			findOptional(questionLabel).ifPresent(question -> {
				Map<String, WebElement> buttons = findAllRadioButtons(q).stream().collect(toMap(WebElement::getText, identity()));
				String answer = questionAnswer.ask(question.getText(), buttons.keySet().toArray(new String[0]));
				buttons.get(answer).click();
			});
		});
	}

	private void checkCheckBoxOptions() {
		IntStream.range(0, 20).forEach(q -> {
			By questionLabel = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + q + ") > .jobs-easy-apply-form-element > fieldset > legend >div> span:first-child");
			findOptional(questionLabel).ifPresent(question -> {
				Map<String, WebElement> boxes = findAllCheckBoxes(q).stream().collect(toMap(WebElement::getText, identity()));
				Set<String> answers = questionAnswer.askCheckBoxOptionsAndNoCache(question.getText(), boxes.keySet());
				for (var entry : boxes.entrySet()) {
					if (!answers.contains(entry.getKey())) continue;
					WebElement element = entry.getValue();
					Boolean isStale = ExpectedConditions.stalenessOf(element).apply(driver);
					if (isStale) {
						log.error("Staleness of {} is staleness of {}", entry.getKey(), true);
					} else {
						element.click();
					}
				}
			});
		});
	}

	private Set<WebElement> findAllCheckBoxes(int questionNo) {
		Set<WebElement> elements = new HashSet<>();
		IntStream.range(0, 20).forEach(n -> {
			By location = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > fieldset > div:nth-of-type(" + n + ") > label");
			tryCatchGet(() -> driver.findElements(location)).ifPresent(elements::addAll);
		});
		return elements;
	}

	private Set<WebElement> findAllRadioButtons(int questionNo) {
		Set<WebElement> elements = new LinkedHashSet<>();
		IntStream.range(0, 20).forEach(n -> {
			By location = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > fieldset > div:nth-of-type(" + n + ") > label");
			tryCatchGet(() -> driver.findElements(location)).ifPresent(elements::addAll);
		});
		return elements;
	}

	private void checkWorkExperienceQuestions() {
		IntStream.range(0, 20).forEach(q -> {
			By titleLocation = By.cssSelector("form>div>div>span:nth-of-type(" + (q * 2 + 1) + ")");
			By subTitleLocation = By.cssSelector("form>div>div>span:nth-of-type(" + (q * 2 + 2) + ")");
			tryIgnore(() -> {
				int questionId = q + 1;
				String title = find(titleLocation).getText();
				String subTitle = find(subTitleLocation).getText();
				String question = "\n" + title + "\n" + subTitle;

				By inputLocation = By.cssSelector("form>div>div>div:nth-of-type(" + questionId + ")>div>div>div>div>:is(input,textarea)");
				findOptional(inputLocation).ifPresent(input -> {
					String answer = questionAnswer.ask(question);
					set(inputLocation, answer);
				});

				By selectOptionLocation = By.cssSelector("form >div>div>div:nth-of-type(" + questionId + ")>div>div>select");
				Function<Integer, By> optionLocationGiver = optionId -> By.cssSelector("form >div>div>div:nth-of-type(" + questionId + ")>div>div>select>option:nth-of-type(" + optionId + ")");
				findOptional(selectOptionLocation).ifPresent(select -> {
					Map<String, WebElement> options = findAllSelectOptions(optionLocationGiver).stream().collect(toMap(WebElement::getText, identity()));
					String answer = questionAnswer.ask(question, options.keySet());
					click(select);
					click(options.get(answer));
				});
			});
		});
	}

	public Optional<WidGet> nextWidget() {
		BooleanSupplier submit = () -> tryIgnore(() -> click(SUBMIT_APPLICATION));
		BooleanSupplier review = () -> tryIgnore(() -> click(REVIEW));
		BooleanSupplier next = () -> tryIgnore(() -> click(NEXT));
		BooleanSupplier continueApplying = () -> tryIgnore(() -> click(CONTINUE_APPLYING));
		BooleanSupplier anyOne = () -> submit.getAsBoolean() || review.getAsBoolean() || next.getAsBoolean() || continueApplying.getAsBoolean();

		if (anyOne.getAsBoolean()) {
			throatMedium();
			return Optional.of(new WidGet(driver, questionAnswer));
		}
		tryCatch(() -> find(CLOSE_WIDGET).click());
		return Optional.empty();
	}

}
