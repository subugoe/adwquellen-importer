package sub.adw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public class CatalogParser {

	public List<Map<String, String>> convertCatalogEntriesToMaps(List<Map<String, String>> excelEntries) throws MalformedURLException, IOException {
		CatalogPpnResolver resolver = new CatalogPpnResolver();
		for (Map<String, String> excelEntry : excelEntries) {
			String ppn = excelEntry.get("ppn");
			String mods = resolver.fetchByPpn(ppn, CatalogPpnResolver.MODS_FORMAT);
		}
		return null;
	}
}
