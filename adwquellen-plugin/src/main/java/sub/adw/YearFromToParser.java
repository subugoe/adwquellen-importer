package sub.adw;


public class YearFromToParser {

	private String[] splitWrittenDates;

	public String[] parse(String writtenDate) {
		if (writtenDate == null || writtenDate.isEmpty()) {
			return new String[] { "", "" };
		}

		splitWrittenDates = writtenDate.split("\\.\\s*");

		if (splitWrittenDates.length == 1) {
			return new String[] { writtenDate, writtenDate };
		}

		if (entry(0, "1") && entry(1, "h") && entry(3, "jh")) {
			int century = Integer.parseInt(splitWrittenDates[2]);
			int xHundred = century - 1;
			String fromYear = xHundred + "01";
			String toYear = xHundred + "50";
			return new String[] { fromYear, toYear };
		}

		return null;
	}

	private boolean entry(int arrayIndex, String s) {
		if (splitWrittenDates.length < arrayIndex + 1) {
			return false;
		}
		return splitWrittenDates[arrayIndex].trim().toLowerCase().equals(s);
	}

}
