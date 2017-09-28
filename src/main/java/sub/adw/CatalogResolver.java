package sub.adw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class CatalogResolver {

	public static final String URL_TEMPLATE = "http://unapi.gbv.de/?id=gvk:ppn:__PPN__&format=__FORMAT__";
	public static final String MODS_FORMAT = "mods";
	public static final String PICA_FORMAT = "pp";

	public String fetchByPpn(String ppn, String format) throws MalformedURLException, IOException {
		String url = URL_TEMPLATE.replace("__PPN__", ppn).replace("__FORMAT__", format);
		return IOUtils.toString(new URL(url).openStream());
	}
}
