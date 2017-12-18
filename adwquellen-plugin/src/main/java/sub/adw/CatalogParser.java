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

	private CatalogPpnResolver resolver = new CatalogPpnResolver();

	public List<ListMultimap<String, String>> convertCatalogEntriesToMaps(
			List<ListMultimap<String, String>> excelEntries) throws MalformedURLException, IOException, SAXException,
					ParserConfigurationException, XPathExpressionException {
		List<ListMultimap<String, String>> allMaps = new ArrayList<>();
		for (ListMultimap<String, String> excelEntry : excelEntries) {
			String[] ppnArray = excelEntry.get("ppn").get(0).split("[;\\s]+");
			for (String ppn : ppnArray) {
				if ("".equals(ppn)) {
					// TODO: warning
					continue;
				}
				// System.out.println(ppn);
				try {
					allMaps.add(toMap(ppn));
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

	public ListMultimap<String, String> toMap(String ppn) throws XPathExpressionException, MalformedURLException,
			IOException, SAXException, ParserConfigurationException {
		ListMultimap<String, String> modsMap = ArrayListMultimap.create();
		String mods = resolver.fetchByPpn(ppn, CatalogPpnResolver.MODS_FORMAT);
		Xpath xpath = new Xpath(mods);
		modsMap.put("origin", "catalog");
		
		String title = mergeParts(xpath, "/mods/titleInfo[not(@type='alternative')]/*[self::nonSort or self::title]");
		modsMap.put("titel", title);
		
		String titleAppendix = mergeParts(xpath, "/mods/titleInfo[not(@type='alternative')]/*[self::subTitle or self::partNumber or self::partName]");
		modsMap.put("titelzusatz", titleAppendix);
		
		modsMap.put("alternativtitel", xpath.getString("/mods/titleInfo[@type='alternative']/title"));
		
		List<String> names = xpath.getList("/mods/name");
		for (String name : names) {
			modsMap.put("name", normalizeWhitespace(name));
		}
		
		modsMap.put("ppn", ppn);
		return modsMap;
	}

	private String mergeParts(Xpath xpath, String path) throws XPathExpressionException {
		List<String> parts = xpath.getList(path);
		String whole = "";
		for (String part : parts) {
			whole += part;
		}
		return whole;
	}

	private String normalizeWhitespace(String str) {
		return str.replaceAll("\\s+", " ").trim();
	}

	// for unit tests
	void setPpnResolver(CatalogPpnResolver newResolver) {
		resolver = newResolver;
	}
}
