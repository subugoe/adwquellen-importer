package sub.adw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class CatalogParser {

	public List<ListMultimap<String, String>> convertCatalogEntriesToMaps(List<ListMultimap<String, String>> excelEntries)
			throws MalformedURLException, IOException, SAXException, ParserConfigurationException,
			XPathExpressionException {
		List<ListMultimap<String, String>> allMaps = new ArrayList<>();
		CatalogPpnResolver resolver = new CatalogPpnResolver();
		for (ListMultimap<String, String> excelEntry : excelEntries) {
			String[] ppnArray = excelEntry.get("ppn").get(0).split("[;\\s]+");
			for (String ppn : ppnArray) {
				if ("".equals(ppn)) {
					// TODO: warning
					continue;
				}
				//System.out.println(ppn);
				try {
					ListMultimap<String, String> modsMap = ArrayListMultimap.create();
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
