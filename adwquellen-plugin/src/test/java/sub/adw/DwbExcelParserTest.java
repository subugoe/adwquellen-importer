package sub.adw;

import static org.junit.Assert.*;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.io.File;

import org.junit.Test;

public class DwbExcelParserTest {

	private MapToXmlConverter converter = new MapToXmlConverter();
	private DwbExcelParser parser = new DwbExcelParser();

	@Test
	public void test() throws Exception {
		File excel = new File("src/test/resources/dwb.xls");
		String xml = converter.convertToSolrXml(parser.convertExcelToMaps(excel).get(0));
		
		assertXpathEvaluatesTo("dwb", "//field[@name='origin']", xml);
		assertXpathEvaluatesTo("34.0", "//field[@name='sigle'][1]", xml);
		assertXpathEvaluatesTo("67.0", "//field[@name='sigle'][2]", xml);
		assertXpathEvaluatesTo("biblio1", "//field[@name='biblio']", xml);
		
		
		System.out.println(xml);
	}

}
