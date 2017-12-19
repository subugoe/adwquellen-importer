package sub.adw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import static sub.adw.SolrFieldMappings.*;

public class FwbExcelParser {

	private String[] headers = { "A 0 sort", "B 1 sigle", "C 2 kraftliste", "D 3 kurztitel", "E 4 ort und zeit", "F 5",
			"G 6 raum / ort", "H 7 raum (karte)", "I 8", "J 9 zeit", "K 10 zeit numerisch", "L 11", "M 12 pdf",
			"N 13 epdf", "O 14 digitalisat online", "P 15 eonline", "Q 16 permalink", "R 17 biblio", "S 18 ppn",
			"T 19 zitierweise", "U 20 textsorte", "V 21 name", "W 22 sinnwelt", "X 23 klassifikation",
			"Y 24 kommunikationsintention" };

	private final int SIGLE_EXCEL = 1;
	private final int KRAFTLISTE = 2;
	private final int KURZTITEL_KLARSCHRIFT = 5;
	private final int RAUM_ORT = 6;
	private final int RAUM_KARTE = 7;
	private final int GROSSRAUM = 8;
	private final int DIGITALISAT_ONLINE = 14;
	private final int PERMALINK = 16;
	private final int BIBLIO_EXCEL = 17;
	private final int PPN_EXCEL = 18;
	private final int ZITIERWEISE = 19;
	private final int NAME_EXCEL = 21;

	public List<ListMultimap<String, String>> convertExcelToMaps(File excelFile) throws IOException {
		FileInputStream file = new FileInputStream(excelFile);
		List<ListMultimap<String, String>> resultList = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		XSSFSheet sheet = workbook.getSheetAt(0);

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			ListMultimap<String, String> resultMap = ArrayListMultimap.create();

			resultMap.put(ORIGIN, "fwb");

			Row row = sheet.getRow(i);
			String sigle = asString(row.getCell(SIGLE_EXCEL));
			resultMap.put(SIGLE, sigle);
			String biblio = asString(row.getCell(BIBLIO_EXCEL));
			resultMap.put(BIBLIO, biblio);
			String shortTitle = asString(row.getCell(KURZTITEL_KLARSCHRIFT));
			resultMap.put(SHORT_TITLE, shortTitle);

			String ppns = asString(row.getCell(PPN_EXCEL));
			String[] ppnArray = ppns.split("[;\\s]+");
			for (String ppn : ppnArray) {
				if (ppn.matches("[0-9A-Z]+")) {
					resultMap.put(PPN, ppn);
				}
			}

			appendFromPowerList(row, resultMap);

			String area_place = asString(row.getCell(RAUM_ORT));
			resultMap.put(AREA_PLACE, area_place);

			String area_map = asString(row.getCell(RAUM_KARTE));
			resultMap.put(AREA_MAP, area_map);

			String wide_area = asString(row.getCell(GROSSRAUM));
			resultMap.put(WIDE_AREA, wide_area);

			resultList.add(resultMap);
		}
		workbook.close();
		return resultList;
	}

	private String asString(Cell cell) {
		String result = "";
		if (cell != null && !isEmpty(cell)) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				result += cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				result += new Double(cell.getNumericCellValue()).intValue();
			} else {
				throw new RuntimeException("Unknown cell type: " + cell.getCellType() + ".");
			}
		}
		return result.replace("<", "&lt;").replace(">", "&gt;").trim();
	}

	private boolean isEmpty(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue().isEmpty();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return true;
		}
		return false;
	}

	private void appendFromPowerList(Row row, ListMultimap<String, String> resultMap) {
		String entryKind = asString(row.getCell(NAME_EXCEL));
		String fieldName = "";
		if ("1".equals(entryKind)) {
			fieldName = AUTHOR;
		} else if ("2".equals(entryKind)) {
			fieldName = AUTHOR_SECONDARY;
		} else if ("3".equals(entryKind)) {
			fieldName = PUBLISHER;
		} else if ("4".equals(entryKind)) {
			fieldName = TITLE;
		} else {
			return;
		}
		String powerList = asString(row.getCell(KRAFTLISTE));
		String entryValue = extractUsingRegex("\\$c(.*?)#", powerList).get(0);
		resultMap.put(fieldName, entryValue);
	}

	private List<String> extractUsingRegex(String regex, String s) {
		List<String> results = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			results.add(matcher.group(1));
		}

		if (results.isEmpty()) {
			results.add("");
		}
		return results;
	}

}
