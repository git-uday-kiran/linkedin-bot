package bot.linkedin;

import bot.linkedin.question_solvers.CheckBoxQuestions;
import bot.linkedin.question_solvers.InputQuestions;
import bot.linkedin.question_solvers.RadioOptionsQuestions;
import bot.linkedin.question_solvers.SelectOptionsQuestions;
import bot.linkedin.services.QuestionAnswerService;
import io.vavr.control.Try;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

import static bot.linkedin.Locations.*;
import static bot.utils.ThroatUtils.throatLow;

public class WidGet extends BasePage {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(WidGet.class);
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
        throatLow();
        doUntilSuccess(() -> tryClickDismiss().orElse(this::tryClickDone));
        return Optional.empty();
    }

    private Try<Void> tryClickDismiss() {
        return Try.run(() -> click(waitForElementPresence(CLOSE_WIDGET)));
    }

    private Try<Void> tryClickDone() {
        return Try.run(() -> click(waitForElementPresence(CLOSE_BUTTON)));
    }

}
