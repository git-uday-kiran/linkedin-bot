package bot.linkedin.services;

import bot.linkedin.models.QuestionAnswer;
import bot.linkedin.models.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static io.vavr.control.Try.ofCallable;

@Log4j2
@Service
public class QuestionAnswerService {

	private final Sounds sounds;
	private final QuestionAnswerRepo repo;
	private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public QuestionAnswerService(Sounds sounds, QuestionAnswerRepo repo) {
		this.repo = repo;
		this.sounds = sounds;
	}

	public String ask(String question, Tag tag) {
		return ask(question, Collections.emptyList(), tag);
	}

	public String ask(String question, List<String> options, Tag tag) {
		System.out.println("Question: " + question);
		if (options.isEmpty()) {
			askQuestionByInput(question, tag);
		} else {
			askQuestionByOption(question, options, tag);
		}
		String answer = repo.findByQuestion(question).orElseThrow().getAnswer();
		System.out.println("Answer: " + answer);
		System.out.println();
		return answer;
	}

	public void store(String question, String answer, Tag tag) {
		if (repo.findByQuestion(question).isPresent()) repo.deleteByQuestion(question);
		repo.saveAndFlush(new QuestionAnswer(question, answer, tag));
	}

	public Set<String> askCheckBoxOptionsAndNoCache(String question, List<String> options, Tag tag) {
		if (options.size() == 1) {
			return Set.of(ask(question, options, tag));
		}

		StringJoiner joiner = new StringJoiner("\n");
		IntStream.range(0, options.size()).forEach(id -> joiner.add(id + ". " + options.get(id)));

		sounds.alert();
		System.out.println("Enter multiple options, enter an empty line to stop taking inputs.");
		System.out.println("Question: " + question);
		System.out.println(joiner);

		String line;
		Set<String> result = new HashSet<>();
		while (!(line = ofCallable(reader::readLine).get()).isBlank()) {
			ofCallable(parseAsInteger(line))
					.andThen(addOptionToResult(options, result))
					.orElseRun(handleNumberFormatException());
		}
		return result;
	}

	private static Consumer<Throwable> handleNumberFormatException() {
		return _ -> System.out.println("Enter number format input.");
	}

	private static Callable<Integer> parseAsInteger(String line) {
		return () -> Integer.parseInt(line.trim());
	}

	private static Consumer<Integer> addOptionToResult(List<String> options, Set<String> result) {
		return input -> result.add(options.get(input));
	}


	private void askQuestionByInput(String question, Tag tag) {
		if (repo.findByQuestion(question).isEmpty()) {
			sounds.alert();
			System.out.print("Input: ");
			String answer = ofCallable(reader::readLine).get();
			store(question, answer, tag);
		}
	}

	private void askQuestionByOption(String question, List<String> options, Tag tag) {
		Optional<QuestionAnswer> byQuestion = repo.findByQuestion(question);
		if (byQuestion.isEmpty() || !options.contains(byQuestion.get().getAnswer())) {
			StringJoiner joiner = new StringJoiner("\n");
			int id = 0;
			for (String option : options) {
				joiner.add(id + ". " + option);
				id++;
			}
			sounds.alert();
			System.out.println(joiner);
			int input = askRangeInteger(options.size() - 1);
			store(question, options.get(input), tag);
		}
	}

	private int askRangeInteger(int max) {
		int answer;
		while (!isInRange((answer = askInteger()), max)) {
			System.out.println("Please enter an integer between " + 0 + " and " + max + ".");
		}
		return answer;
	}

	private boolean isInRange(int value, int max) {
		return 0 <= value && value <= max;
	}

	private int askInteger() {
		int result = Integer.MIN_VALUE;
		while (result == Integer.MIN_VALUE) {
			try {
				System.out.print("Input: ");
				result = Integer.parseInt(reader.readLine().trim());
			} catch (NumberFormatException ignored) {
				System.out.println("Please enter number format input.");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

}
