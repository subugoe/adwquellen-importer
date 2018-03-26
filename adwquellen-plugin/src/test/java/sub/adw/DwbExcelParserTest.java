package sub.adw;

import static org.junit.Assert.*;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.io.File;

import org.junit.Test;

public class DwbExcelParserTest {

	private MapToXmlConverter converter = new MapToXmlConverter();
	private DwbExcelParser parser = new DwbExcelParser();

	@Test
	public void firstXml() throws Exception {
		File excel = new File("src/test/resources/dwb.xls");
		String xml = converter.convertToSolrXml(parser.convertExcelToMaps(excel).get(0));
		
		assertXpathEvaluatesTo("dwb", "//field[@name='origin']", xml);
		assertXpathEvaluatesTo("34.0", "//field[@name='sigle'][1]", xml);
		assertXpathEvaluatesTo("67.0", "//field[@name='sigle'][2]", xml);
		assertXpathEvaluatesTo("biblio1", "//field[@name='biblio']", xml);
		assertXpathEvaluatesTo("....", "//field[@name='erscheinungsdatum']", xml);
		assertXpathEvaluatesTo("130291390", "//field[@name='ppn'][1]", xml);
		assertXpathEvaluatesTo("167790676", "//field[@name='ppn'][3]", xml);
		assertXpathEvaluatesTo("http://books.google.de/book1", "//field[@name='link'][1]", xml);
		assertXpathEvaluatesTo("https://de.wikisource.org/wiki", "//field[@name='link'][4]", xml);
		
		
		System.out.println(xml);
		System.out.println(converter.convertToSolrXml(parser.convertExcelToMaps(excel).get(1)));
	}

	@Test
	public void secondXml() throws Exception {
		File excel = new File("src/test/resources/dwb.xls");
		String xml = converter.convertToSolrXml(parser.convertExcelToMaps(excel).get(1));
		
		assertXpathEvaluatesTo("1777", "//field[@name='erscheinungsdatum']", xml);
		assertXpathEvaluatesTo("140590544", "//field[@name='ppn']", xml);
		
	}

}
