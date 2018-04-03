package sub.adw;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class YearFromToParserTest {

	private YearFromToParser yearParser;

	@Before
	public void beforeEach() {
		yearParser = new YearFromToParser();

	}

	private void assertParses(String writtenDate, String fromDate, String toDate) {
		ParsedDateRange years = yearParser.parse(writtenDate);
		
		assertEquals(fromDate, years.from);
		assertEquals(toDate, years.to);
	}
	
	@Test
	public void canDealWithNull() {
		assertParses(null, "", "");
	}

	@Test
	public void canDealWithEmptyString() {
		assertParses("", "", "");
	}

	@Test
	public void unrecognizedDate() {
		assertParses("test", "", "");
		assertParses("not a date", "", "");
		assertParses("not.a.date", "", "");
	}

	@Test
	public void onlyYear_fromAndToAreTheSame() {
		assertParses("1777", "1777", "1777");
		assertParses("1777.", "1777", "1777");
	}

	@Test
	public void firstHalfOfCentury_fromYear1ToYear50() {
		assertParses("1.h.15. Jh.", "1401", "1450");
	}

	@Test
	public void inApproximately_fromYearMinus20ToYearPlus20() {
		assertParses("u1400", "1380", "1420");
		assertParses("u980", "960", "1000");
	}

	@Test
	public void beforeYear_fromYearMinus50ToSameYear() {
		assertParses("v1022", "972", "1022");
	}

	@Test
	public void afterYear_fromSameYearToPlus50() {
		assertParses("n1022", "1022", "1072");
	}

	@Test
	public void beginningOfCentury_fromYear1ToYear33() {
		assertParses("A15.jh.", "1401", "1433");
	}

	@Test
	public void endOfCentury_fromYear67ToYearPlus100() {
		assertParses("E15.jh.", "1467", "1500");
	}

	@Test
	public void beginningOrMiddleOfCentury_fromYear1ToYear66() {
		assertParses("A/M16. Jh.", "1501", "1566");
	}

	@Test
	public void middleOrEndOfCentury_fromYear33ToYear100() {
		assertParses("M/E16. Jh.", "1533", "1600");
	}

}
