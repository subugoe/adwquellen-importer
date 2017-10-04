package sub.adw;

import java.io.FileNotFoundException;
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
			String[] ppnArray = excelEntry.get("ppn").split("[;\\s]+");
			for (String ppn : ppnArray) {
				if ("".equals(ppn)) {
					// TODO: warning
					continue;
				}
				//System.out.println(ppn);
				try {
					String mods = resolver.fetchByPpn(ppn, CatalogPpnResolver.MODS_FORMAT);
					Xpath xpath = new Xpath(mods);
					modsMap.put("origin", "catalog");
					modsMap.put("titel", xpath.getString("//titleInfo/title"));
					modsMap.put("ppn", ppn);
					allMaps.add(modsMap);
				} catch (FileNotFoundException e) {
					System.out.println("Not found in catalog: " + e.getMessage());
				} catch (IOException e) {
					if (e.getMessage().startsWith("Server returned HTTP response code: 400")) {
						System.out.println(e.getMessage());
					} else {
						throw e;
					}
				}
			}
		}
		return allMaps;
	}
}
