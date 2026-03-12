package utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtility {

	private static final Logger logger = LogManager.getLogger(ScreenshotUtility.class);

	public static String captureScreenshot(WebDriver driver, String testName) throws Exception {
		{

			logger.info("Capturing screenshot for test: {}", testName);

			if (driver == null) {

				logger.error("WebDriver is null. Cannot capture screenshot.");

				throw new RuntimeException("Driver is null");
			}

			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

			String folderPath = PathManager.getScreenshotPath();

			File folder = new File(folderPath);

			if (!folder.exists()) {

				folder.mkdirs();
			}

			TakesScreenshot ts = (TakesScreenshot) driver;

			File sourceFile = ts.getScreenshotAs(OutputType.FILE);

			String targetFilePath = folderPath + testName + "_" + timeStamp + ".png";

			File targetFile = new File(targetFilePath);

			FileUtils.copyFile(sourceFile, targetFile);

			logger.info("Screenshot saved successfully at: {}", targetFilePath);

			return targetFilePath;
		}
	}
}
