package bot.linkedin.question_solvers;

import bot.linkedin.BasePage;
import bot.linkedin.services.QuestionAnswerService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Log4j2
@Service
public class CheckBoxQuestions extends BasePage {

	private final QuestionAnswerService qaService;

	private final By questionGroupLocation = By.xpath("//div[contains(@class, 'jobs-easy-apply-form-section__grouping')][.//fieldset][.//input[@type='checkbox']]");
	private final By directLabelLocation = By.xpath(".//legend/div[1]/span[1]");
	private final By titleLocation = By.xpath("preceding-sibling::span[contains(@class, 'jobs-easy-apply-form-section__group-title')][1]");
	private final By subTitleLocation = By.xpath("preceding-sibling::span[contains(@class, 'jobs-easy-apply-form-section__group-subtitle')][1]");
	private final By checkBoxLocation = By.xpath(".//div[@data-test-text-selectable-option]//label");

	public CheckBoxQuestions(WebDriver driver, QuestionAnswerService qaService) {
		super(driver);
		this.qaService = qaService;
	}

	public void scan() {
		if (isPresentInDOM(questionGroupLocation)) {
			findAll(questionGroupLocation).stream()
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
		solveQuestionWithOnlyCheckBoxes(question, label);
	}

	private void solveDirectQuestion(WebElement question) {
		String label = question.findElement(directLabelLocation).getText();
		solveQuestionWithOnlyCheckBoxes(question, label);
	}

	private void solveQuestionWithOnlyCheckBoxes(WebElement question, String label) {
		var boxes = question.findElements(checkBoxLocation);
		var boxesMap = mapOf(boxes);
		var options = boxesMap.keySet().stream().toList();

		var boxesSelected = qaService.askCheckBoxOptionsAndNoCache(label, options);
		boxesSelected.stream()
				.map(boxesMap::get)
				.forEach(WebElement::click);
	}

	private Map<String, WebElement> mapOf(List<WebElement> questions) {
		return questions.stream().collect(toMap(WebElement::getText, Function.identity()));
	}

	private boolean isDirectQuestion(WebElement question) {
		return !question.findElements(directLabelLocation).isEmpty();
	}

}
