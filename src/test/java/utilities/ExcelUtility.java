package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

public class ExcelUtility {

	private static final Logger logger = LogManager.getLogger(ExcelUtility.class);

	private String path;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

//	public ExcelUtility(String path) throws Exception {
//		this.path = path;
//
//		logger.info("Opening Excel file: {}", path);
//
//		try {
//			FileInputStream fi = new FileInputStream(path);
//			workbook = new XSSFWorkbook(fi);
//			fi.close();
//		} catch (Exception e) {
//			logger.error("Failed to open Excel file: {}", path, e);
//			throw e;
//		}
//	}

	public ExcelUtility(String path) throws Exception {

		this.path = path;

		FileInputStream fi = new FileInputStream(path);

		workbook = new XSSFWorkbook(fi);

		fi.close();
	}

	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public int getRowCount(String sheetName) {

		logger.debug("Fetching row count for sheet: {}", sheetName);

		sheet = workbook.getSheet(sheetName);

		if (sheet == null) {
			logger.error("Sheet not found: {}", sheetName);
			throw new RuntimeException("Sheet not found: " + sheetName);
		}

		return sheet.getLastRowNum();
	}

	public int getCellCount(String sheetName, int rowNum) {

		logger.debug("Fetching cell count for sheet: {} row: {}", sheetName, rowNum);

		sheet = workbook.getSheet(sheetName);
		XSSFRow row = sheet.getRow(rowNum);

		if (row == null) {
			logger.warn("Row {} not found in sheet {}", rowNum, sheetName);
			return 0;
		}

		return row.getLastCellNum();
	}

	public String getCellData(String sheetName, int rowNum, int colNum) {

		logger.debug("Reading cell data from Sheet: {} Row: {} Column: {}", sheetName, rowNum, colNum);

		sheet = workbook.getSheet(sheetName);
		XSSFRow row = sheet.getRow(rowNum);

		if (row == null) {
			logger.warn("Row {} not found in sheet {}", rowNum, sheetName);
			return "";
		}

		XSSFCell cell = row.getCell(colNum);

		DataFormatter formatter = new DataFormatter();

		try {
			return formatter.formatCellValue(cell);
		} catch (Exception e) {
			logger.error("Error reading cell value at row {} column {}", rowNum, colNum, e);
			return "";
		}
	}

	public void setCellData(String sheetName, int rowNum, int colNum, String data) throws Exception {

		logger.info("Writing data to Excel | Sheet: {} Row: {} Column: {} Value: {}", sheetName, rowNum, colNum, data);

		sheet = workbook.getSheet(sheetName);

		if (sheet == null) {
			logger.info("Sheet not found. Creating new sheet: {}", sheetName);
			sheet = workbook.createSheet(sheetName);
		}

		XSSFRow row = sheet.getRow(rowNum);

		if (row == null) {
			row = sheet.createRow(rowNum);
		}

		XSSFCell cell = row.createCell(colNum);
		cell.setCellValue(data);

		FileOutputStream fo = new FileOutputStream(path);
		workbook.write(fo);
		fo.close();
	}

	public String[][] getSheetData(String sheetName) {

		logger.info("Reading full sheet data for: {}", sheetName);

		sheet = workbook.getSheet(sheetName);

		if (sheet == null) {
			logger.error("Sheet not found: {}", sheetName);
			throw new RuntimeException("Sheet not found: " + sheetName);
		}

		int rowCount = sheet.getLastRowNum();
		int colCount = sheet.getRow(1).getLastCellNum();

		String[][] data = new String[rowCount][colCount];

		DataFormatter formatter = new DataFormatter();

		for (int i = 1; i <= rowCount; i++) {

			XSSFRow row = sheet.getRow(i);

			for (int j = 0; j < colCount; j++) {

				XSSFCell cell = row.getCell(j);

				data[i - 1][j] = formatter.formatCellValue(cell);
			}
		}

		logger.info("Sheet read completed. Rows: {} Columns: {}", rowCount, colCount);

		return data;
	}

	public void fillGreenColor(String sheetName, int rowNum, int colNum) throws Exception {

		logger.debug("Applying GREEN color to Sheet: {} Row: {} Column: {}", sheetName, rowNum, colNum);

		sheet = workbook.getSheet(sheetName);
		XSSFRow row = sheet.getRow(rowNum);
		XSSFCell cell = row.getCell(colNum);

		XSSFCellStyle style = workbook.createCellStyle();

		style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cell.setCellStyle(style);

		FileOutputStream fo = new FileOutputStream(path);
		workbook.write(fo);
		fo.close();
	}

	public void fillRedColor(String sheetName, int rowNum, int colNum) throws Exception {

		logger.debug("Applying RED color to Sheet: {} Row: {} Column: {}", sheetName, rowNum, colNum);

		sheet = workbook.getSheet(sheetName);
		XSSFRow row = sheet.getRow(rowNum);
		XSSFCell cell = row.getCell(colNum);

		XSSFCellStyle style = workbook.createCellStyle();

		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cell.setCellStyle(style);

		FileOutputStream fo = new FileOutputStream(path);
		workbook.write(fo);
		fo.close();
	}
}