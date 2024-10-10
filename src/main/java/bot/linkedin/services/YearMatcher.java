package bot.linkedin.services;

import bot.linkedin.filters.JobApplyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
@RequiredArgsConstructor
public class YearMatcher {

	private final String regex = ".{0,50}((\\d{1,3} ?- ?\\d{1,3})|(\\d{1,3})\\+?) *(yrs|years?|YOE).{0,50}";

	private final JobApplyConfig applyConfig;
	private final QuestionAnswerService qaService;

	private final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

	public boolean checkExperience(char lineSeparator, String... input) {
		for (String text : input) {
			String target = text.trim().replace(lineSeparator, '\n');
			boolean isOkay = checkWithPattern(target);
			if (!isOkay) return false;
		}
		return true;
	}

	private boolean checkWithPattern(String text) {
		Matcher matcher1 = pattern.matcher(text);

		while (matcher1.find()) {
			String matched = matcher1.group();
			int year = getMinYearByMatcher1(matcher1);
			boolean isOkay = (year <= applyConfig.getAllowMaxYOE()) || confirmProceed(matched);

			if (!isOkay) {
				log.warn("Experience is not okay: {}", matched);
				return false;
			}
		}
		return true;
	}

	private static int getMinYearByMatcher1(Matcher matcher1) {
		String yearString;
		String single = matcher1.group(3); // 1, 2
		if (single != null) {
			yearString = single;
			log.info("Exp years: {}", single);
		} else {
			String range = matcher1.group(2); // 1-2, 1 - 2
			log.info("Exp years range: {}", range);
			yearString = range.split("-")[0].trim();
		}
		return Integer.parseInt(yearString.trim());
	}

	private boolean confirmProceed(String text) {
		log.warn("Found a number greater than {}YOE", applyConfig.getAllowMaxYOE());
		String answer = qaService.ask(text, List.of("Cancel", "Proceed"));
		return switch (answer) {
			case "Proceed" -> true;
			case "Cancel" -> false;
			default -> throw new IllegalStateException("Unexpected value: " + answer);
		};
	}

}
