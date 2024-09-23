package bot.linkedin.services;

import bot.linkedin.BasePage;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.IntStream;

import static bot.utils.Utils.*;

@Log4j2
@Service
public class QuestionAnswerService extends BasePage {

	private final Sounds sounds;
	private final QuestionAnswerRepo repo;
	private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public QuestionAnswerService(WebDriver driver, Sounds sounds, QuestionAnswerRepo repo) {
		super(driver);
		this.repo = repo;
		this.sounds = sounds;
	}

	public String ask(String question, String message, Collection<String> options) {
		System.out.println(message);
		return ask(question, options);
	}

	public String ask(String question, Collection<String> options) {
		return ask(question, options.toArray(new String[0]));
	}

	public String ask(String question, String... options) {
		System.out.println("Question: " + question);
		var byQuestion = repo.findByQuestion(question);
		if (byQuestion.isEmpty()) {
			if (options.length == 0) {
				askQuestionByInput(question);
			} else {
				askQuestionByOption(question, options);
			}
		}
		String answer = repo.findByQuestion(question).orElseThrow().getAnswer();
		System.out.println("Answer: " + answer);
		System.out.println();
		return answer;
	}

	public void store(String question, String answer) {
		repo.save(new QuestionAnswer(question, answer));
	}

	private void askQuestionByInput(String question) {
		sounds.alert();
		System.out.print("Input: ");
		String answer = tryOrThrow(reader::readLine);
		store(question, answer);
	}

	public Set<String> askCheckBoxOptionsAndNoCache(String question, Collection<String> optionsCollection) {
		if (optionsCollection.size() == 1) {
			return Set.of(ask(question, optionsCollection));
		}

		String[] options = optionsCollection.toArray(new String[0]);
		StringJoiner joiner = new StringJoiner("\n");
		IntStream.range(0, options.length).forEach(id -> joiner.add(id + ". " + options[id]));

		System.out.println("Question: " + question);
		System.out.println("Enter empty line stop taking inputs.");
		System.out.println(joiner);

		String line;
		Set<String> result = new HashSet<>();
		while (!(line = tryOrThrow(reader::readLine)).isBlank()) {
			String input = line.trim();
			tryCatchGet(() -> Integer.parseInt(input))
				.ifPresentOrElse(e -> result.add(options[e]), () -> System.out.println("Enter number format input."));
		}
		return result;
	}

	private void askQuestionByOption(String question, String[] options) {
		StringJoiner joiner = new StringJoiner("\n");
		int id = 0;
		for (String option : options) {
			joiner.add(id + ". " + option);
			id++;
		}
		sounds.alert();
		System.out.println(joiner);
		System.out.print("Input: ");
		int input = askInteger();
		store(question, options[input]);
	}

	private int askInteger() {
		int result = Integer.MIN_VALUE;
		while (result == Integer.MIN_VALUE) {
			try {
				result = Integer.parseInt(reader.readLine());
			} catch (NumberFormatException ignored) {
				log.info("Please enter number format input.");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

}
