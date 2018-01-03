package sub.adw;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Text;
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
		for (Element catalogEntry : root.getChildren()) {
			ListMultimap<String, String> resultMap = ArrayListMultimap.create();
			resultMap.put(ORIGIN, "mwb");
			resultMap.put(SIGLE, catalogEntry.getChildText("sigle", NS));
			resultMap.put(BIBLIO, getAllTextNormalize(catalogEntry.getChild("bibl", NS)));
			addPpns(catalogEntry, resultMap);
			resultList.add(resultMap);
		}
		out.println("    ... " + resultList.size());
		return resultList;
	}

	private String getAllTextNormalize(Element el) {
		String text = "";
		if (el != null) {
			List<Content> allSubs = el.getContent();
			for (int i = 0; i < allSubs.size(); i++) {
				Content sub = allSubs.get(i);
				if (sub instanceof Text) {
					String potentialPreSpace = "";
					String potentialPostSpace = "";
					if (i == 0 && sub.getValue().endsWith(" ")) {
						potentialPostSpace = " ";
					} else if (i == allSubs.size() - 1 && sub.getValue().startsWith(" ")) {
						potentialPreSpace = " ";
					}
					text += potentialPreSpace + ((Text) sub).getTextNormalize() + potentialPostSpace;
				} else if (sub instanceof Element) {
					text += getAllTextNormalize((Element)sub);
				}
			}
		}
		return text;
	}

	private void addPpns(Element catalogEntry, ListMultimap<String, String> resultMap) {
		Element ppns = catalogEntry.getChild("ppns", NS);
		if (ppns != null) {
			for (Element ppn : ppns.getChildren("ppn", NS)) {
				resultMap.put(PPN, ppn.getTextTrim());
			}
		}
	}
}
