package testBase;

import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import utilities.ConfigLoader;

public class BaseClass {

	// private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static final ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> null);

	public static Logger logger;
	public static Properties p = ConfigLoader.loadProperties();

	// Get WebDriver to open URL
	public static WebDriver getDriver() {
		return driver.get();
	}

	// Set driver
	public static void setDriver(WebDriver webDriver) {
		driver.set(webDriver);
	}

	@Parameters({ "browser", "os" })
	@BeforeMethod(alwaysRun = true)
	public void setup(String browser, String os) throws Exception {
		

		if (p == null) {
			throw new RuntimeException("Config properties not loaded");
		}

		logger = LogManager.getLogger(this.getClass());

		logger.info("Execution environment: " + p.getProperty("execution_env"));

		logger.info("Browser selected: " + browser);

		try {
			// LOCAL
			if (p.getProperty("execution_env").equalsIgnoreCase("local")) {

				logger.info("Initializing LOCAL execution on browser: {}", browser);

				// Gets parameter from xml and loads browser

				switch (browser.toLowerCase()) {
				case "chrome":
					setDriver(new ChromeDriver());
					break;
				case "safari":
					setDriver(new SafariDriver());
					break;
				case "firefox":
					setDriver(new FirefoxDriver());
					break;
				default:
					throw new RuntimeException("Invalid browser");
				}

			}

			// GRID-STANDALONE
			else if (p.getProperty("execution_env").equalsIgnoreCase("grid")) {

				logger.info("Initializing GRID execution");

				DesiredCapabilities cap = new DesiredCapabilities();

				// OS

				if (os.equalsIgnoreCase("windows")) {
					cap.setPlatform(Platform.WIN11);

				} else if (os.equalsIgnoreCase("mac")) {
					cap.setPlatform(Platform.MAC);
				} else if (os.equalsIgnoreCase("linux")) {
					cap.setPlatform(Platform.LINUX);
				} else {
					logger.error("No matching OS: {}", os);
					return;
				}

				// Browser

				switch (browser.toLowerCase()) {
				case "chrome":
					cap.setBrowserName("chrome");
					break;
				case "safari":
					cap.setBrowserName("safari");
					break;
				case "firefox":
					cap.setBrowserName("firefox");
					break;
				case "opera":
					cap.setBrowserName("opera");
					break;
				default:
					logger.error("No matching browser: {}", browser);
					return;
				}

				setDriver(new RemoteWebDriver(new URL(p.getProperty("standalone_grid_url")), cap));

			}

			// DOCKER-GRID
			else if (p.getProperty("execution_env").equalsIgnoreCase("docker")) {

				logger.info("Initializing DOCKER GRID execution");

				@SuppressWarnings("deprecation")
				URL gridURL = new URL(p.getProperty("dockerGrid_url"));

				switch (browser.toLowerCase()) {

				case "chrome":
					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.addArguments("--no-sandbox");
					chromeOptions.addArguments("--disable-dev-shm-usage");
					chromeOptions.addArguments("--remote-allow-origins=*");
					setDriver(new RemoteWebDriver(gridURL, chromeOptions));
					break;

				case "firefox":
					FirefoxOptions firefoxOptions = new FirefoxOptions();
					setDriver(new RemoteWebDriver(gridURL, firefoxOptions));
					break;

				default:
					logger.error("No matching browser: {}", browser);
					return;
				}
			}

			logger.info("Driver initialized successfully");
			logger.info("OS -->",os, "BROWSER -->", browser);

			// Maximize window
			getDriver().manage().window().maximize();

			// open application URL
			getDriver().get(p.getProperty("appUrl"));

			logger.info("Application launched: " + p.getProperty("appUrl"));
		} catch (Exception e) {

			logger.error("Driver setup failed", e);

			if (getDriver() != null) {
				driver.remove();
			}

			throw e;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		WebDriver webDriver = getDriver();

		if (webDriver != null) {

			logger.info("Closing browser session");

			webDriver.quit();
			driver.remove();

			logger.info("Browser closed successfully");
		}
	}

}
