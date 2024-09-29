package bot.linkedin;

import bot.linkedin.services.QuestionAnswerService;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import static bot.linkedin.Locations.*;
import static bot.utils.ThroatUtils.throatLow;
import static bot.utils.ThroatUtils.throatMedium;
import static io.vavr.control.Try.ofCallable;
import static io.vavr.control.Try.run;
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
		IntStream.range(0, 25).forEach(q -> {
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

	private void checkSelectionOptionQuestions() {
		IntStream.range(0, 25).forEach(questionNo -> {
			By questionLabel = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > div > label > span:first-child");
			findOptional(questionLabel).ifPresent(question -> {

				Function<Integer, By> locator = optionNo -> By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > div > select > option:nth-of-type(" + optionNo + ")");
				Map<String, WebElement> options = findAllSelectOptions(locator)
						.stream()
						.collect(toMap(WebElement::getText, identity(), (a, b) -> a));

				question.click();
				throatLow();

				if (question.getText().trim().equals("City")) {
					String answer = questionAnswer.ask(question.getText(), options.keySet().stream().toList());
					while (tryFillCity(answer, questionNo).isFailure()) {
						answer = questionAnswer.ask(question.getText(), options.keySet().stream().toList());
					}
				} else {
					String answer = questionAnswer.ask(question.getText(), options.keySet().stream().toList());
					options.get(answer).click();
				}
			});
		});
	}

	private Try<Void> tryFillCity(String answer, int questionNo) {
		return Try.run(() -> {
			By inputLocation = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > div > div > :is(input,textarea)");
			By arrowDown = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > div > div > div:nth-of-type(2)");
			find(inputLocation).sendKeys(answer, Keys.ENTER);
			throatMedium();
			find(arrowDown).sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
		});
	}

	private Set<WebElement> findAllSelectOptions(Function<Integer, By> locationGiver) {
		Set<WebElement> elements = new HashSet<>();
		IntStream.range(0, 25).forEach(n -> {
			By location = locationGiver.apply(n);
			ofCallable(findAll(location)).andThen(elements::addAll);
		});
		return elements;
	}

	private void checkRadioButtonQuestions() {
		IntStream.range(0, 25).forEach(q -> {
			By questionLabel = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + q + ") > .jobs-easy-apply-form-element > fieldset > legend > span > span:first-child");
			findOptional(questionLabel).ifPresent(question -> {
				Map<String, WebElement> buttons = getAllCheckBoxesAsMap(findAllRadioButtons(q));
				String answer = questionAnswer.ask(question.getText(), buttons.keySet().stream().toList());
				buttons.get(answer).click();
			});
		});
	}

	private void checkCheckBoxOptions() {
		IntStream.range(0, 25).forEach(q -> {
			By questionLabel = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + q + ") > .jobs-easy-apply-form-element > fieldset > legend >div> span:first-child");
			findOptional(questionLabel).ifPresent(question -> {
				List<String> checkBoxOptions = findAllCheckBoxes(q).stream().map(WebElement::getText).toList();
				Set<String> answers = questionAnswer.askCheckBoxOptionsAndNoCache(question.getText(), checkBoxOptions);
				for (var entry : getAllCheckBoxesAsMap(findAllCheckBoxes(q)).entrySet()) {
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

	private Map<String, WebElement> getAllCheckBoxesAsMap(Set<WebElement> q) {
		return q.stream().collect(toMap(WebElement::getText, identity()));
	}

	private Set<WebElement> findAllCheckBoxes(int questionNo) {
		Set<WebElement> elements = new HashSet<>();
		IntStream.range(0, 25).forEach(n -> {
			By location = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > fieldset > div:nth-of-type(" + n + ") > label");
			ofCallable(findAll(location)).andThen(elements::addAll);
		});
		return elements;
	}

	private Set<WebElement> findAllRadioButtons(int questionNo) {
		Set<WebElement> elements = new LinkedHashSet<>();
		IntStream.range(0, 25).forEach(n -> {
			By location = By.cssSelector(".jobs-easy-apply-form-section__grouping:nth-of-type(" + questionNo + ") > .jobs-easy-apply-form-element > fieldset > div:nth-of-type(" + n + ") > label");
			ofCallable(findAll(location)).andThen(elements::addAll);
		});
		return elements;
	}

	private void checkWorkExperienceQuestions() {
		IntStream.range(0, 25).forEach(q -> {
			By titleLocation = By.cssSelector("form>div>div>span:nth-of-type(" + (q * 2 + 1) + ")");
			By subTitleLocation = By.cssSelector("form>div>div>span:nth-of-type(" + (q * 2 + 2) + ")");
			run(() -> {
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
					Map<String, WebElement> options = getAllCheckBoxesAsMap(findAllSelectOptions(optionLocationGiver));
					String answer = questionAnswer.ask(question, options.keySet().stream().toList());
					click(select);
					click(options.get(answer));
				});
			});
		});
	}

	public Optional<WidGet> nextWidget() {
		Try<Void> tried = tryClick(SUBMIT_APPLICATION)
				.orElse(tryClick(REVIEW))
				.orElse(tryClick(NEXT))
				.orElse(tryClick(CONTINUE_APPLYING));

		if (tried.isSuccess()) {
			throatMedium();
			return Optional.of(new WidGet(driver, questionAnswer));
		}
		tryClick(CLOSE_WIDGET).orElseRun(log::error);
		return Optional.empty();
	}

}
