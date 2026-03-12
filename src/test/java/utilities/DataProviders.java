//package utilities;
//
//import testBase.BaseClass;
//import java.lang.reflect.Method;
//import org.apache.logging.log4j.Logger;
//import org.testng.annotations.DataProvider;
//import org.apache.logging.log4j.LogManager;
//
//public class DataProviders {
//
//	private static final Logger logger = LogManager.getLogger(DataProviders.class);
//
//	@DataProvider(name = "ExcelData")
//	public String[][] getData(Method method) throws Exception {
//
//		String sheetName = method.getName();
//
//		logger.info("Fetching test data for sheet: {}", sheetName);
//
//		String fileName = BaseClass.p.getProperty("testDataFile");
//
//		String path = PathManager.getTestDataPath(fileName);
//
//		ExcelUtility xl = new ExcelUtility(path);
//
//		return xl.getSheetData(sheetName);
//	}
//}
package utilities;

import testBase.BaseClass;
import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;

public class DataProviders {

    private static final Logger logger = LogManager.getLogger(DataProviders.class);

    @DataProvider(name = "ExcelData", parallel=false)
    public Object[][] getData(Method method) throws Exception {

        String sheetName = method.getName();

        logger.info("Fetching data for sheet: {}", sheetName);

        String fileName = BaseClass.p.getProperty("testDataFile");

        String path = PathManager.getTestDataPath(fileName);

        ExcelUtility xl = new ExcelUtility(path);

        XSSFSheet sheet = xl.getWorkbook().getSheet(sheetName);

        int rowCount = sheet.getLastRowNum();
        int colCount = sheet.getRow(1).getLastCellNum();

        Object[][] data = new Object[rowCount][colCount + 2];

        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= rowCount; i++) {

            XSSFRow row = sheet.getRow(i);

            for (int j = 0; j < colCount; j++) {

                data[i - 1][j] = formatter.formatCellValue(row.getCell(j));
            }

            // row number
            data[i - 1][colCount] = i;

            // ExcelUtility instance
            data[i - 1][colCount + 1] = xl;
        }

        return data;
    }
}