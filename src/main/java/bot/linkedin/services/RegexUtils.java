package bot.linkedin.services;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

@Service
public class RegexUtils {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(RegexUtils.class);

    public RegexUtils() {
    }

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
