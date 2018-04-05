package sub.adw;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YearFromToParser {

	private HashSet<String> set = new HashSet<>();

	public ParsedDateRange parse(String writtenDate) {
		if (writtenDate == null || writtenDate.isEmpty()) {
			return new ParsedDateRange("", "");
		}

		writtenDate = stripParentheses(writtenDate);

		// 1777
		// 1777.
		String regex = "([0-9]{3,4})\\.?";
		if (writtenDate.matches(regex)) {
			String year = extract(regex, writtenDate);
			return new ParsedDateRange(year, year);
		}

		// 1.h.15. Jh.
		// 1.h15.jh.
		regex = "1\\.h\\.?([0-9]{2})\\.[^?]*";
		if (writtenDate.matches(regex)) {
			int century = Integer.parseInt(extract(regex, writtenDate));
			return centuryRange(century, 1, 50);
		}

		if (writtenDate.matches("u[0-9]{3,4}")) {
			int year = Integer.parseInt(deleteFirstChar(writtenDate));
			return yearRange(year, -20, 20);
		}

		// v1234
		// v 1234
		regex = "v\\s?([0-9]{3,4})";
		if (writtenDate.matches("v\\s?([0-9]{3,4})")) {
			int year = Integer.parseInt(extract(regex, writtenDate));
			return yearRange(year, -50, 0);
		}

		if (writtenDate.matches("n[0-9]{3,4}")) {
			int year = Integer.parseInt(deleteFirstChar(writtenDate));
			return yearRange(year, 0, 50);
		}

		regex = "A([0-9]{1,2})\\..*";
		if (writtenDate.matches(regex)) {
			int century = Integer.parseInt(extract(regex, writtenDate));
			return centuryRange(century, 1, 33);
		}

		if (writtenDate.matches("E[0-9]{2}\\..*")) {
			int century = Integer.parseInt(writtenDate.substring(1, 3));
			return centuryRange(century, 67, 100);
		}

		if (writtenDate.matches("A/M[0-9]{2}\\..*")) {
			int century = Integer.parseInt(writtenDate.substring(3, 5));
			return centuryRange(century, 1, 66);
		}

		if (writtenDate.matches("M/E[0-9]{2}\\..*")) {
			int century = Integer.parseInt(writtenDate.substring(3, 5));
			return centuryRange(century, 33, 100);
		}

		printIfUnknownYet(writtenDate);

		return new ParsedDateRange("", "");
	}

	private void printIfUnknownYet(String writtenDate) {
		addToSet("....", "...", "___", "____", "hs./dr.___", "z.j.____", "z.j. ....");
		
		String regexForSet = "";
		if (writtenDate.matches("-[0-9]{3,4}")) {
			regexForSet = "-[0-9]{3,4}";
			addToSet(regexForSet);
		}
		// 1508/16
		if (writtenDate.matches("[0-9]{4}/[0-9]{1,2}")) {
			regexForSet = "[0-9]{4}/[0-9]{1,2}";
			addToSet(regexForSet);
		}
		// 14.jh.
		if (writtenDate.matches("[0-9]{2}\\.jh\\.")) {
			regexForSet = "[0-9]{2}\\.jh\\.";
			addToSet(regexForSet);
		}
		if (!set.contains(writtenDate) && !set.contains(regexForSet))
			System.out.println(writtenDate);
	}

	private void addToSet(String... strings) {
		for (String s : strings) {
			set.add(s);
		}
	}

	private String stripParentheses(String writtenDate) {
		if (writtenDate.startsWith("(") && writtenDate.endsWith(")")) {
			return deleteLastChar(deleteFirstChar(writtenDate));
		}
		return writtenDate;
	}

	private ParsedDateRange centuryRange(int century, int upperRange, int lowerRange) {
		int year = (century - 1) * 100;
		return yearRange(year, upperRange, lowerRange);
	}

	private ParsedDateRange yearRange(int year, int upperRange, int lowerRange) {
		int fromYear = year + upperRange;
		int toYear = year + lowerRange;
		return new ParsedDateRange("" + fromYear, "" + toYear);
	}

	private String deleteFirstChar(String s) {
		return s.substring(1);
	}

	private String deleteLastChar(String s) {
		return s.substring(0, s.length() - 1);
	}

	private String extract(String regex, String s) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}
}
