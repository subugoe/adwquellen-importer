package sub.adw;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MwbXmlParserTest {

	private MwbXmlParser parser = new MwbXmlParser();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void shouldAddBiblio() throws Exception {
		String solrXml = convert("bibl.xml");
		System.out.println(solrXml);
		assertXpathEvaluatesTo("Test bibliography with a linebreak.", "//field[@name='biblio']", solrXml);
	}

	@Test
	public void shouldAddSigle() throws Exception {
		String solrXml = convert("sigle.xml");
		assertXpathEvaluatesTo("MySigle", "//field[@name='sigle']", solrXml);
	}

	private String convert(String fileName) throws Exception {
		MapToXmlConverter converter = new MapToXmlConverter();
		File f = new File("src/test/resources/mwb/" + fileName);
		return converter.convertToSolrXml(parser.convertXmlToMaps(f).get(0));
	}

}
