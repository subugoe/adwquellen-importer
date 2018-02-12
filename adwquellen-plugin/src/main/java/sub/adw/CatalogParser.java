package sub.adw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import static sub.adw.SolrFieldMappings.*;

public class CatalogParser {

	private CatalogPpnResolver resolver = new CatalogPpnResolver();
	private PrintStream out = System.out;
	private Set<String> searchedPpns = new HashSet<>();

	public List<ListMultimap<String, String>> convertCatalogEntriesToMaps(
			List<ListMultimap<String, String>> excelEntries) throws MalformedURLException, IOException, SAXException,
					ParserConfigurationException, XPathExpressionException {
		List<ListMultimap<String, String>> allMaps = new ArrayList<>();
		int i = 0;
		for (ListMultimap<String, String> excelEntry : excelEntries) {
			List<String> ppnList = excelEntry.get("ppn");
			for (String ppn : ppnList) {
				if ("".equals(ppn)) {
					// TODO: warning
					continue;
				}
				if (alreadySearchedInCatalog(ppn)) {
					continue;
				}
				// System.out.println(ppn);
				try {
					allMaps.add(toMap(ppn));
					i++;
					if (i % 2000 == 0) {
						out.println("    ... " + i);
					}
				} catch (FileNotFoundException e) {
					out.println("WARNING: Not found in catalog: " + e.getMessage());
				} catch (IOException e) {
					if (e.getMessage().startsWith("Server returned HTTP response code: 400")) {
						out.println("WARNING: " + e.getMessage());
					} else {
						throw e;
					}
				}
			}
		}
		out.println("    ... " + i);
		return allMaps;
	}

	private boolean alreadySearchedInCatalog(String ppn) {
		if (searchedPpns.contains(ppn)) {
			return true;
		} else {
			searchedPpns.add(ppn);
			return false;
		}
	}

	public ListMultimap<String, String> toMap(String ppn) throws XPathExpressionException, MalformedURLException,
			IOException, SAXException, ParserConfigurationException {
		ListMultimap<String, String> modsMap = ArrayListMultimap.create();
		String mods = resolver.fetchByPpn(ppn, CatalogPpnResolver.MODS_FORMAT);
		Xpath xpath = new Xpath(mods);

		modsMap.put(ORIGIN, "catalog");

		String title = mergeParts(xpath, "/mods/titleInfo[not(@type='alternative')]/*[self::nonSort or self::title]");
		modsMap.put(TITLE, title);

		String titleAppendix = mergeParts(xpath,
				"/mods/titleInfo[not(@type='alternative')]/*[self::subTitle or self::partNumber or self::partName]");
		modsMap.put(TITLE_APPENDIX, titleAppendix);

		modsMap.put(ALTERNATIVE_TITLE, xpath.getString("/mods/titleInfo[@type='alternative']/title"));

		List<String> names = xpath.getList("/mods/name");
		for (String name : names) {
			modsMap.put(NAME, normalizeWhitespace(name));
		}

		modsMap.put(PPN, ppn);

		modsMap.put(PLACE, xpath.getString("/mods/originInfo/place/placeTerm"));
		modsMap.put(PUBLISHER, xpath.getString("/mods/originInfo/publisher"));
		modsMap.put(DATE_ISSUED, xpath.getString("/mods/originInfo/dateIssued"));

		List<String> langs = xpath.getList("/mods/language/languageTerm");
		for (String lang : langs) {
			modsMap.put(LANGUAGE, lang);
		}

		modsMap.put(FORM, xpath.getString("/mods/physicalDescription/form"));
		modsMap.put(EXTENT, xpath.getString("/mods/physicalDescription/extent"));
		modsMap.put(NOTE, xpath.getString("/mods/note"));

		String picaText = resolver.fetchByPpn(ppn, CatalogPpnResolver.PICA_FORMAT);
		Pica picaReader = new Pica();
		picaReader.init(picaText);

		modsMap.put(CREATION_METHOD, picaReader.getCreationMethod());
		if (!picaReader.getParentPpn().equals("unknown")) {
			modsMap.put(PARENT_PPN, picaReader.getParentPpn());
		}

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

	public void setOut(PrintStream newOut) {
		out = newOut;
	}

	// for unit tests
	void setPpnResolver(CatalogPpnResolver newResolver) {
		resolver = newResolver;
	}
}
