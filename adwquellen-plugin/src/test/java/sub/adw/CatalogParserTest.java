package sub.adw;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

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
	public void shouldAddAlternativeTitle() throws Exception {
		String solrXml = convert("titleWithAlternative.xml");
		assertXpathEvaluatesTo("alternative title", "//field[@name='alternativtitel']", solrXml);
	}

	@Test
	public void shouldAddSubTitle() throws Exception {
		String solrXml = convert("titleWithSubTitle.xml");
		assertXpathEvaluatesTo("Gedichte", "//field[@name='titel']", solrXml);
		assertXpathEvaluatesTo(" : subtitel", "//field[@name='titelzusatz']", solrXml);
	}

	@Test
	public void shouldAddNonSortToTitle() throws Exception {
		String solrXml = convert("titleWithNonSort.xml");
		assertXpathEvaluatesTo("catalog", "//field[@name='origin']", solrXml);
		assertXpathEvaluatesTo("Die Gedichte", "//field[@name='titel']", solrXml);
	}

	private String convert(String fileName) throws Exception {
		String mods = FileUtils.readFileToString(new File("src/test/resources/catalog/" + fileName), "UTF-8");
		when(resolverMock.fetchByPpn(anyString(), anyString())).thenReturn(mods);
		MapToXmlConverter converter = new MapToXmlConverter();
		return converter.convertToSolrXml(parser.toMap("dummy-ppn"));
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
