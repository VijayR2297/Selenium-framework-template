package utilities;

import java.awt.Desktop;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {

	private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
	
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;

	ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	String repName;

	public void onStart(ITestContext context) {

		logger.info("Starting Extent Report initialization");

		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		repName = "Test-Report-" + timeStamp + ".html";

		sparkReporter = new ExtentSparkReporter("./reports/" + repName);

		sparkReporter.config().setDocumentTitle(BaseClass.p.getProperty("report.title"));
		sparkReporter.config().setReportName(BaseClass.p.getProperty("report.name"));
		String theme = BaseClass.p.getProperty("report.theme");

		if(theme.equalsIgnoreCase("dark")) {
		    sparkReporter.config().setTheme(Theme.DARK);
		} else {
		    sparkReporter.config().setTheme(Theme.STANDARD);
		}

		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);

		extent.setSystemInfo("Application",BaseClass.p.getProperty("app.name"));
		extent.setSystemInfo("User Name", "Vijay");

		String os = context.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);
		
		extent.setSystemInfo("Environment", BaseClass.p.getProperty("environment"));

		String browser = context.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		extent.setSystemInfo("Java Version", System.getProperty("java.version"));
		extent.setSystemInfo("OS Architecture", System.getProperty("os.arch"));

		List<String> groups = context.getCurrentXmlTest().getIncludedGroups();

		if (!groups.isEmpty()) {
			extent.setSystemInfo("Groups", groups.toString());
		}

		logger.info("Extent report initialized successfully");
	}

	public void onTestStart(ITestResult result) {

		logger.info("Starting test: {}", result.getMethod().getMethodName());

		ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());

		extentTest.assignCategory(result.getMethod().getGroups());
		
		String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
		String os = result.getTestContext().getCurrentXmlTest().getParameter("os");

		extentTest.assignDevice(browser + " - " + os);

		test.set(extentTest);
	}

	public void onTestSuccess(ITestResult result) {

		logger.info("Test Passed: {}", result.getMethod().getMethodName());

		test.get().log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
	}

	public void onTestFailure(ITestResult result) {

		logger.error("Test Failed: {}", result.getMethod().getMethodName());

		test.get().log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());

		test.get().log(Status.INFO, result.getThrowable());

		try {

			logger.info("Capturing screenshot for failed test");
			logger.info("Driver instance: {}", BaseClass.getDriver());

			String imgPath = ScreenshotUtility.captureScreenshot(BaseClass.getDriver(),
					result.getMethod().getMethodName());

			test.get().addScreenCaptureFromPath(imgPath);

			logger.info("Screenshot attached to report: {}", imgPath);

		} catch (Exception e) {

			logger.error("Failed to capture screenshot", e);
		}
	}

	public void onTestSkipped(ITestResult result) {

		logger.warn("Test Skipped: {}", result.getMethod().getMethodName());

		test.get().log(Status.SKIP, "Test Skipped: " + result.getMethod().getMethodName());
	}

	public void onFinish(ITestContext context) {

		logger.info("Flushing Extent Report");

		extent.flush();

		String path = System.getProperty("user.dir") + "/reports/" + repName;

		File reportFile = new File(path);

		try {

			logger.info("Opening Extent Report automatically");

			Desktop.getDesktop().browse(reportFile.toURI());

		} catch (Exception e) {

			logger.error("Unable to open report automatically", e);
		}
	}
	
	// This blocks send the email to any person automatically once the test is
	// finished
	// Add a dependency for activating this
	// https://mvnrepository.com/artifact/org.apache.commons/commons-email

	/*
	 * try { URL url = new
	 * URL("file:///"+System.getProperty("user.dir")+"/reports/"+repName); // Create
	 * the email message ImageHtmlEmail email = new ImageHtmlEmail();
	 * email.setDataSourceResolver(new DataSourceUrlResolver(url));
	 * email.setHostName("smtp.googlemail.com"); email.setSmtpPort(465);
	 * email.setAuthenticator(new
	 * DefaultAuthenticator("vijaytesterEmail@gmail.com","password"));
	 * email.setsSLOnConnect(true); email.setFrom("Vijaytestingemail@gmail.com");
	 * //Sender email.setSubject("Test Results");
	 * email.setMsg("Please find Attached Report....");
	 * email.addTo("receiverQAEmail@gmail.com"); //Receiver email.attach(url,
	 * "extent report", "please check report..."); email.send(); // send the email }
	 * catch(Exception e) { e.printStackTrace();
	 * 
	 * }
	 */
}