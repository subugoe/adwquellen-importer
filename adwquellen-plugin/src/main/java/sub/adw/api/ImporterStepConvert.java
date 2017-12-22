package sub.adw.api;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.ListMultimap;

import sub.adw.CatalogParser;
import sub.adw.FwbExcelParser;
import sub.adw.MapToXmlConverter;
import sub.ent.api.ImporterStep;
import sub.ent.backend.FileAccess;

public class ImporterStepConvert extends ImporterStep {

	private FileAccess fileAccess = new FileAccess();
	private FwbExcelParser excelParser = new FwbExcelParser();
	private CatalogParser catalogParser = new CatalogParser();
	private MapToXmlConverter mapConverter = new MapToXmlConverter();

	@Override
	public void execute(Map<String, String> params) throws Exception {
		String gitDir = params.get("gitDir");
		String solrXmlDir = params.get("solrXmlDir");
		File outputDir = new File(solrXmlDir);
		File inputDir = new File(gitDir);
		File inputExcel = new File(inputDir, "FWB-Quellenliste.xlsx");

		fileAccess.cleanDir(outputDir);
		out.println("    Converting Excel.");
		List<ListMultimap<String, String>> entries = excelParser.convertExcelToMaps(inputExcel);
		out.println("    Converting catalog entries:");
		List<ListMultimap<String, String>> catalogEntries = catalogParser.convertCatalogEntriesToMaps(entries);
		entries.addAll(catalogEntries);
		int i = 0;
		for (ListMultimap<String, String> mapEntry : entries) {
			i++;
			mapEntry.put("id", "" + i);
			String solrXmlString = mapConverter.convertToSolrXml(mapEntry);
			File outputFile = new File(outputDir, i + ".xml");
			FileUtils.writeStringToFile(outputFile, solrXmlString, "UTF-8");
			printCurrentStatus(i, entries.size());
		}
	}

	private void printCurrentStatus(int currentNumber, int lastNumber) {
		if (currentNumber % 10000 == 0 || currentNumber == lastNumber) {
			out.println("    ... " + currentNumber);
		}
	}

	@Override
	public String getStepDescription() {
		return "Datenkonvertierung";
	}

}
