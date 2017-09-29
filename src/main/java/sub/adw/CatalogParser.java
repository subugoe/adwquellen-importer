package sub.adw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class CatalogParser {

	public List<Map<String, String>> convertCatalogEntriesToMaps(List<Map<String, String>> excelEntries)
			throws MalformedURLException, IOException, SAXException, ParserConfigurationException,
			XPathExpressionException {
		List<Map<String, String>> allMaps = new ArrayList<>();
		CatalogPpnResolver resolver = new CatalogPpnResolver();
		for (Map<String, String> excelEntry : excelEntries) {
			Map<String, String> modsMap = new HashMap<>();
			String ppn = excelEntry.get("ppn");
			String mods = resolver.fetchByPpn(ppn, CatalogPpnResolver.MODS_FORMAT);
			Xpath xpath = new Xpath(mods);
			modsMap.put("titel", xpath.getString("//titleInfo/title"));
			allMaps.add(modsMap);
		}
		return allMaps;
	}
}
