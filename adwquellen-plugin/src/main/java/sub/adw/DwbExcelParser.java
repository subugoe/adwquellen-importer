package sub.adw;

import static sub.adw.SolrFieldMappings.BIBLIO;
import static sub.adw.SolrFieldMappings.ORIGIN;
import static sub.adw.SolrFieldMappings.SIGLE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class DwbExcelParser {

	private final int SIGLE_EXCEL = 1;
	private final int BIBLIO_EXCEL = 2;

	private PrintStream out = System.out;

	public void setOut(PrintStream newOut) {
		out = newOut;
	}

	public List<ListMultimap<String, String>> convertExcelToMaps(File excelFile) throws IOException {
		FileInputStream file = new FileInputStream(excelFile);
		List<ListMultimap<String, String>> resultList = new ArrayList<>();
		HSSFWorkbook workbook = new HSSFWorkbook(file);

		HSSFSheet sheet = workbook.getSheetAt(0);

		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			ListMultimap<String, String> resultMap = ArrayListMultimap.create();

			resultMap.put(ORIGIN, "dwb");

			Row row = sheet.getRow(i);

			String sigle = asString(row.getCell(SIGLE_EXCEL));
			String[] sigles = sigle.split("\\|");
			for (String s : sigles) {
				resultMap.put(SIGLE, s.trim());
			}

			String biblio = asString(row.getCell(BIBLIO_EXCEL));
			resultMap.put(BIBLIO, biblio);

			resultList.add(resultMap);
			if ((i + 1) % 2000 == 0) {
				out.println("    ... " + (i + 1));
			}
		}
		workbook.close();
		out.println("    ... " + resultList.size());
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

}
