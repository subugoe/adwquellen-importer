package sub.adw;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pica {

	private String picaEntry;

	public void init(String newPicaEntry) {
		picaEntry = newPicaEntry;
	}

	public String getCreationMethod() {
		String s = extractUsingRegex("002@ \\$0..(.)", picaEntry);
		if ("u".equals(s)) {
			return "autopsy";
		} else if ("n".equals(s)) {
			return "converted";
		} else if ("x".equals(s)) {
			return "from_other_db";
		} else if ("r".equals(s)) {
			return "retrospective";
		} else {
			return "unknown";
		}
	}

	public String getParentPpn() {
		String ppn = extractUsingRegex("036D .*\\$9(.+?)\\$", picaEntry);
		if (ppn.isEmpty()) {
			return "unknown";
		} else {
			return ppn;
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
