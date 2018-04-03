package sub.adw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YearFromToParser {

	private String[] splitWrittenDates;

	public ParsedDateRange parse(String writtenDate) {
		if (writtenDate == null || writtenDate.isEmpty()) {
			return new ParsedDateRange("", "");
		}

		splitWrittenDates = writtenDate.split("\\.\\s*");

		if (splitWrittenDates.length == 1 && splitWrittenDates[0].matches("[0-9]{3,4}")) {
			return new ParsedDateRange(splitWrittenDates[0], splitWrittenDates[0]);
		}

		if (entry(0, "1") && entry(1, "h") && entry(3, "jh")) {
			int century = Integer.parseInt(splitWrittenDates[2]);
			return centuryRange(century, 1, 50);
		}

		if (writtenDate.matches("u[0-9]{3,4}")) {
			int year = Integer.parseInt(deleteFirstChar(writtenDate));
			return yearRange(year, -20, 20);
		}

		if (writtenDate.matches("v[0-9]{3,4}")) {
			int year = Integer.parseInt(deleteFirstChar(writtenDate));
			return yearRange(year, -50, 0);
		}

		if (writtenDate.matches("n[0-9]{3,4}")) {
			int year = Integer.parseInt(deleteFirstChar(writtenDate));
			return yearRange(year, 0, 50);
		}

		if (writtenDate.matches("A[0-9]{2}\\..*")) {
			int century = Integer.parseInt(writtenDate.substring(1, 3));
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

		System.out.println(writtenDate);
		return new ParsedDateRange("", "");
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

	private boolean entry(int arrayIndex, String s) {
		if (splitWrittenDates.length < arrayIndex + 1) {
			return false;
		}
		return splitWrittenDates[arrayIndex].trim().toLowerCase().equals(s);
	}

	private String deleteFirstChar(String s) {
		return s.substring(1);
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
