package sub.adw;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ListMultimap;

public class FwbExcelParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		FwbExcelParser excelParser = new FwbExcelParser();
		List<ListMultimap<String, String>> results = excelParser
				.convertExcelToMaps(new File("src/test/resources/sources.xlsx"));

		assertEquals("Biblio1", results.get(0).get("biblio").get(0));
		assertEquals("145520943", results.get(0).get("ppn").get(0));
		assertEquals("022714251", results.get(1).get("ppn").get(0));
		assertEquals("LastName1", results.get(0).get("herausgeber").get(0));
		assertEquals("1", results.get(0).get("sigle").get(0));
		assertEquals("LastName1, Friedr. v. Schwaben", results.get(0).get("kurztitel").get(0));

		assertEquals("schwäb.", results.get(0).get("raum_ort").get(0));
		assertEquals("schwäb.", results.get(0).get("raum_karte").get(0));
		assertEquals("westoberdt.", results.get(0).get("grossraum").get(0));
	}

}
