package bot.linkedin;

import bot.linkedin.services.QuestionAnswerService;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

import static bot.linkedin.Locations.*;
import static bot.utils.ThroatUtils.throatLow;

@Log4j2
public class WidGet extends BasePageV1 {

	private final QuestionAnswerService questionAnswer;
	private final RadioOptionsQuestions radioOptionsQuestions;
	private final SelectOptionsQuestions selectOptionsQuestions;
	private final InputQuestions inputQuestions;
	private final CheckBoxQuestions checkBoxQuestions;

	public WidGet(WebDriver driver, QuestionAnswerService questionAnswer, RadioOptionsQuestions radioOptionsQuestions, SelectOptionsQuestions selectOptionsQuestions, InputQuestions inputQuestions, CheckBoxQuestions checkBoxQuestions) {
		super(driver);
		this.questionAnswer = questionAnswer;
		this.radioOptionsQuestions = radioOptionsQuestions;
		this.selectOptionsQuestions = selectOptionsQuestions;
		this.inputQuestions = inputQuestions;
		this.checkBoxQuestions = checkBoxQuestions;
	}

	public void init() {
		selectOptionsQuestions.scan();
		radioOptionsQuestions.scan();
		inputQuestions.scan();
		checkBoxQuestions.scan();
	}

	public Optional<WidGet> nextWidget() {
		Try<Void> tried = tryClick(SUBMIT_APPLICATION)
				.orElse(tryClick(REVIEW))
				.orElse(tryClick(NEXT))
				.orElse(tryClick(CONTINUE_APPLYING));

		if (tried.isSuccess()) {
			throatLow();
			return Optional.of(new WidGet(driver, questionAnswer, radioOptionsQuestions, selectOptionsQuestions, inputQuestions, checkBoxQuestions));
		}
		WebElement closeWidget = waitForPresence(CLOSE_WIDGET).getFirst();
		waitForClickable(closeWidget).click();
		return Optional.empty();
	}

}
