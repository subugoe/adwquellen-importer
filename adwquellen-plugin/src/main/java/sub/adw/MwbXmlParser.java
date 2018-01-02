package sub.adw;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import static sub.adw.SolrFieldMappings.*;

public class MwbXmlParser {
	private PrintStream out = System.out;
	private static final Namespace NS = Namespace.getNamespace("http://www.mhdwb-online.de/quellen");

	public void setOut(PrintStream newOut) {
		out = newOut;
	}

	public List<ListMultimap<String, String>> convertXmlToMaps(File xmlFile) throws JDOMException, IOException {
		List<ListMultimap<String, String>> resultList = new ArrayList<>();
		SAXBuilder jdomBuilder = new SAXBuilder();
		Document jdomDocument = jdomBuilder.build(xmlFile);
		Element root = jdomDocument.getRootElement();
		for (Element el : root.getChildren()) {
			ListMultimap<String, String> resultMap = ArrayListMultimap.create();
			resultMap.put(SIGLE, el.getChildText("sigle", NS));
			resultMap.put(BIBLIO, el.getChildTextNormalize("bibl", NS));
			resultList.add(resultMap);
		}
		return resultList;
	}
}
