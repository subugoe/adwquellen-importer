package sub.adw;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import static org.mockito.Mockito.*;

public class CatalogParserTest {

	private CatalogParser parser = new CatalogParser();
	private CatalogPpnResolver resolverMock = mock(CatalogPpnResolver.class);

	@Before
	public void setUp() throws Exception {
		parser.setPpnResolver(resolverMock);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void should() throws Exception {
		String xml = convert("test.xml");
		System.out.println(xml);
	}

	private String convert(String fileName) throws Exception {
		String mods = FileUtils.readFileToString(new File("src/test/resources/catalog/" + fileName), "UTF-8");
		when(resolverMock.fetchByPpn(anyString(), anyString())).thenReturn(mods);
		MapToXmlConverter converter = new MapToXmlConverter();
		return converter.convertToSolrXml(parser.toMap("dummy-ppn"));
	}

	@Test
	public void shouldParseEntryWithThreePpns() throws Exception {
		parser.setPpnResolver(new CatalogPpnResolver());
		List<ListMultimap<String, String>> maps = parser.convertCatalogEntriesToMaps(excelEntryWithThreePpns());
		assertEquals(3, maps.size());
		String title1 = maps.get(0).get("titel").get(0);
		String title2 = maps.get(1).get("titel").get(0);
		String title3 = maps.get(2).get("titel").get(0);
		assertEquals("Die Gedichte Heinrichs des Teichners", title1);
		assertEquals("Die Gedichte Heinrichs des Teichners", title2);
		assertEquals("Die Gedichte Heinrichs des Teichners", title3);
	}

	@Test
	public void shouldParseTwoEntries() throws Exception {
		parser.setPpnResolver(new CatalogPpnResolver());
		List<ListMultimap<String, String>> maps = parser.convertCatalogEntriesToMaps(excelEntries());
		String title1 = maps.get(0).get("titel").get(0);
		String title2 = maps.get(1).get("titel").get(0);
		assertEquals("Friedrich von Schwaben", title1);
		assertEquals("Kirchen- und religiöse Lieder aus dem zwölften bis fünfzehnten Jahrhundert", title2);
	}

	private List<ListMultimap<String, String>> excelEntryWithThreePpns() {
		List<ListMultimap<String, String>> entries = new ArrayList<>();
		ListMultimap<String, String> map1 = ArrayListMultimap.create();
		map1.put("ppn", "176825541; 17682555X; 176825568");
		entries.add(map1);
		return entries;
	}

	private List<ListMultimap<String, String>> excelEntries() {
		List<ListMultimap<String, String>> entries = new ArrayList<>();
		ListMultimap<String, String> map1 = ArrayListMultimap.create();
		ListMultimap<String, String> map2 = ArrayListMultimap.create();
		map1.put("ppn", "145520943");
		map2.put("ppn", "022714251");
		entries.add(map1);
		entries.add(map2);
		return entries;
	}

}
