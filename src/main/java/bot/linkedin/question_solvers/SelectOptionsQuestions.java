package bot.linkedin.question_solvers;


import bot.linkedin.BasePageV1;
import bot.linkedin.services.QuestionAnswerService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;

import java.util.List;


@Log4j2
@Service
public class SelectOptionsQuestions extends BasePageV1 {

	private final QuestionAnswerService qaService;

	private final By questionGroupLocation = By.xpath("//div[contains(@class,'jobs-easy-apply-form-section__grouping')][.//select]");
	private final By labelLocation = By.xpath(".//label/span[1]");
	private final By selectionLocation = By.xpath(".//select");

	public SelectOptionsQuestions(WebDriver driver, QuestionAnswerService qaService) {
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
		String label = question.findElement(labelLocation).getText();
		Select select = new Select(question.findElement(selectionLocation));

		List<String> options = select.getOptions().stream()
				.map(WebElement::getText)
				.toList();
		String answer = qaService.ask(label, options);
		select.selectByValue(answer);
	}

}
