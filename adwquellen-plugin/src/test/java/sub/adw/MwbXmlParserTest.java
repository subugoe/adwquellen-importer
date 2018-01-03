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
	public void shouldAddNoPpns() throws Exception {
		String solrXml = convert("ppnsEmpty.xml");
		assertXpathEvaluatesTo("", "//field[@name='ppn']", solrXml);
	}

	@Test
	public void shouldAddTwoPpns() throws Exception {
		String solrXml = convert("ppns.xml");
		assertXpathEvaluatesTo("12345678", "//field[@name='ppn'][1]", solrXml);
		assertXpathEvaluatesTo("123456789", "//field[@name='ppn'][2]", solrXml);
	}

	@Test
	public void shouldIgnoreInnerElementAndLinebreakInBiblio() throws Exception {
		String solrXml = convert("biblWithElementAndBreak.xml");
		assertXpathEvaluatesTo("Test bibliography with an inner element and a line break.", "//field[@name='biblio']", solrXml);
	}

	@Test
	public void shouldIgnoreInnerElementInBiblio() throws Exception {
		String solrXml = convert("biblWithElement.xml");
		assertXpathEvaluatesTo("Test bibliography with an inner element.", "//field[@name='biblio']", solrXml);
	}

	@Test
	public void shouldAddBiblio() throws Exception {
		String solrXml = convert("bibl.xml");
		assertXpathEvaluatesTo("Test bibliography with a linebreak.", "//field[@name='biblio']", solrXml);
	}

	@Test
	public void shouldAddSigle() throws Exception {
		String solrXml = convert("sigle.xml");
		assertXpathEvaluatesTo("MySigle", "//field[@name='sigle']", solrXml);
		assertXpathEvaluatesTo("mwb", "//field[@name='origin']", solrXml);
	}

	private String convert(String fileName) throws Exception {
		MapToXmlConverter converter = new MapToXmlConverter();
		File f = new File("src/test/resources/mwb/" + fileName);
		return converter.convertToSolrXml(parser.convertXmlToMaps(f).get(0));
	}

}
