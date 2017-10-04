package sub.adw;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServerException;

public class Importer {

	private PrintStream out = System.out;
	private FileAccess fileAccess = new FileAccess();
	private FwbExcelParser excelParser = new FwbExcelParser();
	private CatalogParser catalogParser = new CatalogParser();
	private MapToXmlConverter mapConverter = new MapToXmlConverter();
	private Uploader uploader = new Uploader();

	public void convert(String inputExcel, String solrXmlDir) throws Exception {
		File outputDir = new File(solrXmlDir);
		fileAccess.cleanDir(outputDir);
		List<Map<String, String>> entries = excelParser.convertExcelToMaps(new File(inputExcel));
		List<Map<String, String>> catalogEntries = catalogParser.convertCatalogEntriesToMaps(entries);
		entries.addAll(catalogEntries);
		int i = 0;
		for (Map<String, String> mapEntry : entries) {
			i++;
			mapEntry.put("id", "" + i);
			String solrXmlString = mapConverter.convertToSolrXml(mapEntry);
			File outputFile = new File(outputDir, i + ".xml");
			FileUtils.writeStringToFile(outputFile, solrXmlString, "UTF-8");
		}
	}

	public void upload(String solrXmlDir, String solrUrl, String core) throws IOException {
		uploader.setSolrEndpoint(solrUrl, core);
		try {
			List<File> xmls = fileAccess.getAllXmlFilesFromDir(new File(solrXmlDir));
			out.println();
			out.println("    Cleaning the import core.");
			uploader.cleanSolr();
			out.println("    Reloading the import core.");
			uploader.reloadCore();
			out.println("    Uploading index files:");
			int i = 1;
			for (File x : xmls) {
				printCurrentStatus(i, xmls.size());
				uploader.add(x);
				i++;
			}
			uploader.commitToSolr();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			out.println();
			out.println("Performing a rollback due to errors.");
			uploader.rollbackChanges();
			throw new IOException(e);
		}
	}

	private void printCurrentStatus(int currentNumber, int lastNumber) {
		if (currentNumber % 10000 == 0 || currentNumber == lastNumber) {
			out.println("    ... " + currentNumber);
		}
	}

}
