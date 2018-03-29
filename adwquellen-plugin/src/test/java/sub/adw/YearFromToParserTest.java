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
		String[] years = yearParser.parse(writtenDate);
		
		assertEquals(fromDate, years[0]);
		assertEquals(toDate, years[1]);
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
	public void onlyYear_fromAndToAreTheSame() {
		assertParses("1777", "1777", "1777");
	}

	@Test
	public void firstHalfOfCentury_fromYear1ToYear50() {
		assertParses("1.h.15. Jh.", "1401", "1450");
	}

}
