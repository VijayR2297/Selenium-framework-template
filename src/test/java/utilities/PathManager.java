package utilities;

public class PathManager {

	private static final String PROJECT_PATH = System.getProperty("user.dir");

	// Config.properties folder
	public static String getConfigPath(String fileName) {
		return PROJECT_PATH + "/src/test/resources/Config.properties";
	}

	// Test data folder
	public static String getTestDataPath(String fileName) {
		return PROJECT_PATH + "/src/test/resources/testData/" + fileName;
	}

	// Screenshot folder
	public static String getScreenshotPath() {
		return PROJECT_PATH + "/screenshots/";
	}

	// Reports folder
	public static String getReportPath() {
		return PROJECT_PATH + "/reports/";
	}
}
