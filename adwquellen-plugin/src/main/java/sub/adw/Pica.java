package sub.adw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pica {

	private String picaEntry;

	public void init(String newPicaEntry) {
		picaEntry = newPicaEntry;
	}

	public String isParent() {
		String s = extractUsingRegex("002@ \\$0.(.).", picaEntry);
		if ("a".equals(s) || "c".equals(s)) {
			return "true";
		} else {
			return "false";
		}
	}

	private String extractUsingRegex(String regex, String s) {
		String result = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			result = matcher.group(1);
		}

		return result;
	}
}
