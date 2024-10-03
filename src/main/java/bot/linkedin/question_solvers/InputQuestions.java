package bot.linkedin.question_solvers;

import bot.linkedin.BasePageV1;
import bot.linkedin.services.QuestionAnswerService;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class InputQuestions extends BasePageV1 {

	private final QuestionAnswerService qaService;

	private final By inputQuestionGroupLocation = By.xpath("//div[contains(@class, 'jobs-easy-apply-form-section__grouping')][descendant::input[@type='text'] or descendant::textarea]");
	private final By directLabelLocation = By.xpath(".//*[self::label/span[1] or self::label]");
	private final By titleLocation = By.xpath("preceding-sibling::span[contains(@class, 'jobs-easy-apply-form-section__group-title')][1]");
	private final By subTitleLocation = By.xpath("preceding-sibling::span[contains(@class, 'jobs-easy-apply-form-section__group-subtitle')][1]");
	private final By inputLocation = By.xpath(".//*[self::input[@type='text'] or self::textarea]");
	private final By citySuggestionsLocation = By.xpath("following-sibling::div[1]");

	public InputQuestions(WebDriver driver, QuestionAnswerService qaService) {
		super(driver);
		this.qaService = qaService;
	}

	public void scan() {
		if (isPresentInDOM(inputQuestionGroupLocation)) {
			findAll(inputQuestionGroupLocation).stream()
					.map(this::scrollIntoView)
					.map(this::waitForClickable)
					.forEach(this::solveQuestion);
		}
	}

	private void solveQuestion(WebElement question) {
		if (isDirectQuestion(question)) solveDirectQuestion(question);
		else solveTitleSubTitleQuestion(question);
	}

	private void solveTitleSubTitleQuestion(WebElement question) {
		String title = question.findElement(titleLocation).getText();
		String subTitle = question.findElement(subTitleLocation).getText();
		String label = title + '\n' + subTitle;
		solveQuestionWithOnlyInput(question, label);
	}

	private void solveDirectQuestion(WebElement question) {
		String label = question.findElement(directLabelLocation).getText();
		if (label.startsWith("City")) solveCityQuestion(question, label);
		else solveQuestionWithOnlyInput(question, label);
	}

	private void solveQuestionWithOnlyInput(WebElement question, String label) {
		String answer = qaService.ask(label);
		WebElement input = question.findElement(inputLocation);
		input.clear();
		input.sendKeys(answer);
	}

	private void solveCityQuestion(WebElement question, String label) {
		String city = qaService.ask(label);
		WebElement input = question.findElement(inputLocation);
		input.sendKeys(city, Keys.ENTER);

		WebElement citySuggestions = waitForCitySuggestionsPresence(input);
		waitForVisible(citySuggestions).sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
	}

	private boolean isDirectQuestion(WebElement question) {
		return !question.findElements(directLabelLocation).isEmpty();
	}

	private WebElement waitForCitySuggestionsPresence(WebElement input) {
		wait.until(_ -> Try.run(() -> input.findElement(citySuggestionsLocation)).isSuccess());
		return input.findElement(citySuggestionsLocation);
	}

}
