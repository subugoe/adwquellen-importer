package sub.adw;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CatalogParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		CatalogParser parser = new CatalogParser();
		List<Map<String, String>> maps = parser.convertCatalogEntriesToMaps(excelEntries());
		String title1 = maps.get(0).get("titel");
		String title2 = maps.get(1).get("titel");
		assertEquals("Friedrich von Schwaben", title1);
		assertEquals("Kirchen- und religiöse Lieder aus dem zwölften bis fünfzehnten Jahrhundert", title2);
	}

	private List<Map<String, String>> excelEntries() {
		List<Map<String, String>> entries = new ArrayList<>();
		Map<String, String> map1 = new HashMap<>();
		Map<String, String> map2 = new HashMap<>();
		map1.put("ppn", "145520943");
		map2.put("ppn", "022714251");
		entries.add(map1);
		entries.add(map2);
		return entries;
	}

}
