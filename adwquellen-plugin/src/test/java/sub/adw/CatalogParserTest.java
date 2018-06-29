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
	public void shouldAddNote() throws Exception {
		String solrXml = convert("note.xml");
		assertXpathEvaluatesTo("my note", "//field[@name='notiz']", solrXml);
	}

	@Test
	public void shouldAddPhysicalDescriptions() throws Exception {
		String solrXml = convert("physicalDescription.xml");
		assertXpathEvaluatesTo("print", "//field[@name='format']", solrXml);
		assertXpathEvaluatesTo("1000 S.", "//field[@name='umfang']", solrXml);
	}

	@Test
	public void shouldAddLanguages() throws Exception {
		String solrXml = convert("language.xml");
		assertXpathEvaluatesTo("fre", "//field[@name='sprache'][1]", solrXml);
		assertXpathEvaluatesTo("ger", "//field[@name='sprache'][2]", solrXml);
	}

	@Test
	public void shouldAddOriginInfos() throws Exception {
		String solrXml = convert("originInfo.xml");
		assertXpathEvaluatesTo("Berlin", "//field[@name='ort']", solrXml);
		assertXpathEvaluatesTo("my publisher", "//field[@name='herausgeber']", solrXml);
		assertXpathEvaluatesTo("1956", "//field[@name='erscheinungsdatum']", solrXml);
	}

	@Test
	public void shouldSetTwoDates() throws Exception {
		String solrXml = convert("dateIssued_long.xml");
		assertXpathEvaluatesTo("1956", "//field[@name='erscheinungsdatum'][1]", solrXml);
		assertXpathEvaluatesTo("1222", "//field[@name='erscheinungsdatum'][2]", solrXml);
	}

	@Test
	public void shouldSplitDatesOnMinus() throws Exception {
		String solrXml = convert("dateIssued_withMinus.xml");
		assertXpathEvaluatesTo("1222", "//field[@name='erscheinungsdatum'][1]", solrXml);
		assertXpathEvaluatesTo("1223", "//field[@name='erscheinungsdatum'][2]", solrXml);
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
		return new MapToXmlConverter().convertToSolrXml(parser.toMap("dummy-ppn"));
	}

	@Test
	public void shouldParseTwoEntries_readingFromRealOnlineCatalog() throws Exception {
		parser.setPpnResolver(new CatalogPpnResolver());
		List<ListMultimap<String, String>> maps = parser.convertCatalogEntriesToMaps(dictionaryEntries("145520943", "022714251"));
		String title1 = maps.get(0).get("titel").get(0);
		String title2 = maps.get(1).get("titel").get(0);
		assertEquals("Friedrich von Schwaben", title1);
		assertEquals("Kirchen- und religiöse Lieder aus dem zwölften bis fünfzehnten Jahrhundert", title2);
	}
	
	@Test
	public void twoDifferentDictionaryPpns_createsTwoMaps() throws Exception {
		String mods = FileUtils.readFileToString(new File("src/test/resources/catalog/note.xml"), "UTF-8");
		when(resolverMock.fetchByPpn("0001", CatalogPpnResolver.MODS_FORMAT)).thenReturn(mods);
		String mods2 = FileUtils.readFileToString(new File("src/test/resources/catalog/originInfo.xml"), "UTF-8");
		when(resolverMock.fetchByPpn("0002", CatalogPpnResolver.MODS_FORMAT)).thenReturn(mods2);
		
		when(resolverMock.fetchByPpn("0001", CatalogPpnResolver.PICA_FORMAT)).thenReturn("");
		when(resolverMock.fetchByPpn("0002", CatalogPpnResolver.PICA_FORMAT)).thenReturn("");
		
		List<ListMultimap<String, String>> maps = parser.convertCatalogEntriesToMaps(dictionaryEntries("0001", "0002"));
		assertEquals(2, maps.size());
	}

	@Test
	public void twoEqualDictionaryPpns_createsOneMap() throws Exception {
		String mods = FileUtils.readFileToString(new File("src/test/resources/catalog/note.xml"), "UTF-8");
		when(resolverMock.fetchByPpn("0001", CatalogPpnResolver.MODS_FORMAT)).thenReturn(mods);
		
		when(resolverMock.fetchByPpn("0001", CatalogPpnResolver.PICA_FORMAT)).thenReturn("");
		
		
		List<ListMultimap<String, String>> dictionaryEntries = dictionaryEntries("0001", "0001");
		dictionaryEntries.get(0).removeAll("origin");
		dictionaryEntries.get(0).put("origin", "fwb");
		dictionaryEntries.get(1).removeAll("origin");
		dictionaryEntries.get(1).put("origin", "dwb");
		List<ListMultimap<String, String>> maps = parser.convertCatalogEntriesToMaps(dictionaryEntries);
		assertEquals(1, maps.size());
		
		String xml = new MapToXmlConverter().convertToSolrXml(maps.get(0));
		assertXpathEvaluatesTo("fwb", "//field[@name='dictionary'][1]", xml);
		assertXpathEvaluatesTo("dwb", "//field[@name='dictionary'][2]", xml);
	}

	private List<ListMultimap<String, String>> dictionaryEntries(String... ppns) {
		List<ListMultimap<String, String>> entries = new ArrayList<>();
		for (String ppn : ppns) {
			ListMultimap<String, String> map = ArrayListMultimap.create();
			map.put("ppn", ppn);
			map.put("origin", "fwb");
			entries.add(map);
		}
		return entries;
	}

}
