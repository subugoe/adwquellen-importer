package sub.adw;

import static org.junit.Assert.*;

import java.io.File;

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
		assertParses("....", "", "");
		assertParses("____", "", "");
	}

	@Test
	public void onlyYear_fromAndToAreTheSame() {
		assertParses("1777", "1777", "1777");
		assertParses("1777.", "1777", "1777");
	}

	@Test
	public void firstHalfOfCentury_fromYear1ToYear50() {
		assertParses("1.h.15. Jh.", "1401", "1450");
		assertParses("(1.h.15. Jh.)", "1401", "1450");
		assertParses("(1.h15.jh.)", "1401", "1450");
	}

	@Test
	public void inApproximately_fromYearMinus20ToYearPlus20() {
		assertParses("u1400", "1380", "1420");
		assertParses("(u1400)", "1380", "1420");
		assertParses("u980", "960", "1000");
	}

	@Test
	public void beforeYear_fromYearMinus50ToSameYear() {
		assertParses("v1022", "972", "1022");
		assertParses("(v1022)", "972", "1022");
		assertParses("v 1022", "972", "1022");
	}

	@Test
	public void afterYear_fromSameYearToPlus50() {
		assertParses("n1022", "1022", "1072");
	}

	@Test
	public void beginningOfCentury_fromYear1ToYear33() {
		assertParses("A15.jh.", "1401", "1433");
		assertParses("A9.jh.", "801", "833");
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

	
	// TODO: Testfälle, die noch nicht funktionieren
	
	//@Test
	public void hs_dr() {
		assertParses("hs./dr.___", "", "");
		assertParses("hs.____", "", "");
		assertParses("(hs.)....", "", "");
	}
	
	//@Test
	public void z_j() {
		assertParses("z.j.", "", "");
		assertParses("z.j.____", "", "");
		assertParses("z.j. ....", "", "");
	}

	//@Test
	public void edr() {
		assertParses("(Edr.)", "", "");
	}
	
	//@Test
	public void minus() {
		assertParses("-1785", "", "1785");
	}
	
	//@Test
	public void z_j_year() {
		assertParses("z.j.1585", "", "");
	}
	
	//@Test
	public void z_j_two_slashes() {
		assertParses("z.j.1439//40", "", "");
	}
	
	//@Test
	public void slash_twoDigits() {
		assertParses("1508/16", "1508", "1516");
		assertParses("(1508/16)", "1508", "1516");
	}

	//@Test
	public void slash_oneDigit() {
		assertParses("902/5", "902", "905");
	}

	//@Test
	public void slash_fourDigit() {
		assertParses("996/1002", "996", "1002");
	}

	//@Test
	public void slash_threeDigits_threeDigits() {
		assertParses("790/802", "790", "802");
	}

	//@Test
	public void year_questionmark() {
		assertParses("1678?", "", "");
	}

	//@Test
	public void three_years_with_pipes() {
		assertParses("-1567| (1568)| 1569", "", "");
	}

	//@Test
	public void dr_year() {
		assertParses("dr.1512", "", "");
	}

	//@Test
	public void u_dr() {
		assertParses("(u1460)dr.1530", "", "");
	}

	//@Test
	public void or() {
		assertParses("1488(or.1392)", "", "");
	}

	//@Test
	public void or_century() {
		assertParses("1472(or.14.jh.)", "", "");
	}

	//@Test
	public void nd_or() {
		assertParses("1588(nd.or.1584)", "", "");
	}

	//@Test
	public void or_exclamationmark() {
		assertParses("1612(or.1563)!", "", "");
	}

	//@Test
	public void parentheses_dr_slash_twoDigits() {
		assertParses("(1473)dr.1524/39", "", "");
	}

	//@Test
	public void u_slash_twoDigits() {
		assertParses("u1220/30", "1200", "1250");
	}
	
	//@Test
	public void u_slash_oneDigit() {
		assertParses("u1170/3", "", "");
	}
	
	//@Test
	public void hs_u() {
		assertParses("hs.u1350", "1330", "1370");
	}
	
	//@Test
	public void hs_n() {
		assertParses("hs.n1163", "1163", "1213");
	}
	
	//@Test
	public void n_parens_pipe_n_parens() {
		assertParses("(n1150)| (n1172)", "", "");
	}
	
	//@Test
	public void v_questionmark() {
		assertParses("v1485?", "", "");
	}
	
	//@Test
	public void crazy_stuff() {
		assertParses("(1343/5) (nd./md.or.1250/75)", "", "");
	}
	
	//@Test
	public void u_hs_end_century() {
		assertParses("(u1170)hs.E12.jh.", "1150", "1190");
	}
	
	//@Test
	public void u_hs_begin_century() {
		assertParses("(u1180)hs.A13.jh.", "", "");
	}
	
	//@Test
	public void century() {
		assertParses("14.jh.", "1301", "1400");
	}
	
	//@Test
	public void century_questionmark() {
		assertParses("12.jh.?", "", "");
	}
	
	//@Test
	public void hs_beginning_century() {
		assertParses("hs.A13.jh.", "1201", "1233");
	}
	
	//@Test
	public void beginning_century_questionmark() {
		assertParses("A?15.jh.", "", "");
	}
	
	//@Test
	public void end_century_slash_beginning_century() {
		assertParses("E 15./A 16.jh.", "", "");
	}
	
	//@Test
	public void two_centuries_questionmark() {
		assertParses("11./12.jh.?", "", "");
	}
	
	//@Test
	public void middle_century() {
		assertParses("M14.jh.", "1333", "1366");
	}
	
	//@Test
	public void second_half_century() {
		assertParses("2.h14.jh.", "1351", "1400");
	}
	
	//@Test
	public void second_half_century_questionmark() {
		assertParses("2.h13.jh.?", "", "");
	}
	
	//@Test
	public void hs_slash_two_centuries() {
		assertParses("hs.13./14.jh.", "1201", "1400");
	}
	
	//@Test
	public void hs_slash_centuries_questionmark() {
		assertParses("hs. 15/ 16. jh.?", "", "");
	}
	
	//@Test
	public void second_half_hs_u_year() {
		assertParses("(2.h13.jh.)hs.u1430", "", "");
	}
	
	//@Test
	public void questionmark_parentheses_hs_centuries() {
		assertParses("(11.jh.?)hs.13.jh.", "", "");
	}
	
	//@Test
	public void crazy_stuff_2() {
		assertParses("(1.h14.jh.)hs.15./16.jh.", "", "");
	}
	
	
//	@Test
//	public void printUnrecognized() throws Exception {
//		DwbExcelParser parser = new DwbExcelParser();
//		parser.convertExcelToMaps(new File("/home/dennis/adwquellen/excel/ADW_Zieldatei.xls"));
//	}

}
