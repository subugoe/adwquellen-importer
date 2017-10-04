package sub.adw;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class Importer {

	private FileAccess fileAccess = new FileAccess();
	private FwbExcelParser excelParser = new FwbExcelParser();
	private CatalogParser catalogParser = new CatalogParser();
	private MapToXmlConverter mapConverter = new MapToXmlConverter();

	public void convert(String inputExcel, String solrXmlDir) throws Exception {
		File outputDir = new File(solrXmlDir);
		fileAccess.cleanDir(outputDir);
		List<Map<String, String>> entries = excelParser.convertExcelToMaps(new File(inputExcel));
		List<Map<String, String>> catalogEntries = catalogParser.convertCatalogEntriesToMaps(entries);
		entries.addAll(catalogEntries);
		int i = 0;
		for (Map<String, String> mapEntry : entries) {
			i++;
			String solrXmlString = mapConverter.convertToSolrXml(mapEntry);
			File outputFile = new File(outputDir, i + ".xml");
			FileUtils.writeStringToFile(outputFile, solrXmlString, "UTF-8");
		}
	}
}
