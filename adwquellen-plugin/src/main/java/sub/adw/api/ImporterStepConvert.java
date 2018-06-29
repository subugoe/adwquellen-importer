package sub.adw.api;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.ListMultimap;

import sub.adw.CatalogParser;
import sub.adw.DwbExcelParser;
import sub.adw.FwbExcelParser;
import sub.adw.MapToXmlConverter;
import sub.ent.api.ImporterStep;
import sub.ent.backend.FileAccess;

public class ImporterStepConvert extends ImporterStep {

	private FileAccess fileAccess = new FileAccess();
	private FwbExcelParser excelParser = new FwbExcelParser();
	private DwbExcelParser dwbParser = new DwbExcelParser();
	private CatalogParser catalogParser = new CatalogParser();
	private MapToXmlConverter mapConverter = new MapToXmlConverter();

	@Override
	public void execute(Map<String, String> params) throws Exception {
		String gitDir = params.get("gitDir");
		String solrXmlDir = params.get("solrXmlDir");
		File outputDir = new File(solrXmlDir);
		File inputDir = new File(gitDir);
		fileAccess.cleanDir(outputDir);

		File inputExcel = new File(inputDir, "FWB-Quellenliste.xlsx");
		out.println("    Processing FWB Excel entries:");
		excelParser.setOut(out);
		List<ListMultimap<String, String>> entries = excelParser.convertExcelToMaps(inputExcel);

		File dwbFile = new File(inputDir, "ADW_Zieldatei.xls");
		out.println("    Processing DWB Excel entries:");
		dwbParser.setOut(out);
		List<ListMultimap<String, String>> dwbEntries = dwbParser.convertExcelToMaps(dwbFile);
		entries.addAll(dwbEntries);
		
		out.println("    Processing catalog entries:");
		catalogParser.setOut(out);
		List<ListMultimap<String, String>> catalogEntries = catalogParser.convertCatalogEntriesToMaps(entries);
		entries.addAll(catalogEntries);
		int i = 0;
		out.println("    Converting all entries:");
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
		if (currentNumber % 2000 == 0 || currentNumber == lastNumber) {
			out.println("    ... " + currentNumber);
		}
	}

	@Override
	public String getStepDescription() {
		return "Datenkonvertierung";
	}

}
