package bot.linkedin.services;

import bot.linkedin.filters.JobApplyConfig;
import bot.linkedin.models.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExperienceChecker {

	private final String regex = "((\\b\\d{1,3}\\b ?(to|-) ?\\b\\d{1,3}\\b)|(\\b\\d{1,3}\\b) *\\+?) *(yrs|years?|YOE)";
	private final char LINE_SEPARATOR = '\n';

	private final JobApplyConfig applyConfig;
	private final QuestionAnswerService qaService;

	private final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

	public boolean checkExperience(char lineSeparator, String... input) {
		log.info("Checking experience in terms of years");
		for (String text : input) {
			String target = text.trim().replace(lineSeparator, LINE_SEPARATOR);
			boolean isOkay = checkWithPattern(target);
			if (!isOkay) return false;
		}
		return true;
	}

	private boolean checkWithPattern(String text) {
		Matcher matcher1 = pattern.matcher(text);

		while (matcher1.find()) {
			String yoe = matcher1.group();
			String line = prefixUntilLineEnd(text, matcher1.start() - 1) + yoe + suffixUntilLineEnd(text, matcher1.end());
			log.info("Found YOE: {}, Matched line: {}", yoe, line);
			int year = getMinYearByMatcher1(matcher1);
			boolean okayToProceed = isOkayToProceed(year, line);

			if (!okayToProceed) {
				log.warn("Not proceeding with YOE: {}", yoe);
				return false;
			}
		}
		return true;
	}

	private boolean isOkayToProceed(int year, String line) {
		if (year <= applyConfig.getAllowMaxYOE()) return true;
		if (line.matches(".*\\bexp.*")) {
			log.info("Found 'exp' word with undesired experience, bypassing confirmation.");
			return false;
		}
		return confirmProceed(line);
	}

	private static int getMinYearByMatcher1(Matcher matcher1) {
		String yearString;
		String single = matcher1.group(4); // 1, 2
		if (single != null) {
			yearString = single;
			log.info("YOE: {}", single);
		} else {
			String range = matcher1.group(2); // 1-2, 1 - 2
			log.info("YOE range: {}", range);
			yearString = range.split("(-|to)")[0].trim();
		}
		return Integer.parseInt(yearString.trim());
	}

	private boolean confirmProceed(String text) {
		log.warn("Found experience greater than {} YOE", applyConfig.getAllowMaxYOE());
		String answer = qaService.ask(text, List.of("Cancel", "Proceed"), Tag.YOE_CHECK);
		return switch (answer) {
			case "Proceed" -> true;
			case "Cancel" -> false;
			default -> throw new IllegalStateException("Unexpected value: " + answer);
		};
	}

	private String prefixUntilLineEnd(String data, int position) {
		var builder = new StringBuilder();
		while (position >= 0 && data.charAt(position) != LINE_SEPARATOR) {
			builder.append(data.charAt(position--));
		}
		return builder.reverse().toString();
	}

	private String suffixUntilLineEnd(String data, int position) {
		var builder = new StringBuilder();
		while (position < data.length() && data.charAt(position) != LINE_SEPARATOR) {
			builder.append(data.charAt(position++));
		}
		return builder.toString();
	}

}
