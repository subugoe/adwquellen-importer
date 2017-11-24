package sub.adw;

import static org.junit.Assert.*;
import static org.custommonkey.xmlunit.XMLAssert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapToXmlConverterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		MapToXmlConverter converter = new MapToXmlConverter();
		String solrXml = converter.convertToSolrXml(mapEntries());
		assertXpathEvaluatesTo("testTitle", "//field[@name='titel']", solrXml);
		assertXpathEvaluatesTo("1234", "//field[@name='ppn']", solrXml);
	}

	private Map<String, String> mapEntries() {
		Map<String, String> map = new HashMap<>();
		map.put("ppn", "1234");
		map.put("titel", "testTitle");
		return map;
	}

}
