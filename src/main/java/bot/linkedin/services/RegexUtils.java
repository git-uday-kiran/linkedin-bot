package bot.linkedin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

@Log4j2
@Service
@RequiredArgsConstructor
public class RegexUtils {

	public boolean containsWords(String data, Collection<String> words) {
		for (String word : words) {
			if (!containsWord(data, word)) {
				return false;
			}
		}
		return true;
	}

	public boolean containsWord(String data, String word) {
		checkNonWordCharsAtCorners(word);
		Pattern compile = Pattern.compile("\\b" + findAndReplaceEscapeCharacters(word) + "\\b", Pattern.CASE_INSENSITIVE);
		Matcher matcher = compile.matcher(data);
		return matcher.find();
	}


	private void checkNonWordCharsAtCorners(String data) {
		char start = data.charAt(0), end = data.charAt(data.length() - 1);
		if (!(isAlphabetic(start) || isDigit(start)) || !(isAlphabetic(end) || isDigit(end))) {
			throw new IllegalStateException("Escape character at corners are not allowed, data: " + data);
		}
	}

	private String findAndReplaceEscapeCharacters(String data) {
		char[] escapeChars = {'\\', '.', '?', '*', '+', '[', ']', '{', '}', '(', ')', '^', '$', '|', '-'};
		data = replace(data, escapeChars);
		return data;
	}

	private String replace(String data, char... escapeCharacters) {
		for (var escape : escapeCharacters) {
			data = data.replace("" + escape, "\\" + escape);
		}
		return data;
	}

}
