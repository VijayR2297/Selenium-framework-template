package utilities;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SmartActions {

	private static final Logger logger = LogManager.getLogger(SmartActions.class);

	public static WebElement smartWaitForElement(WebDriver driver, WebElement element) {

		int attempts = 0;

		while (attempts < 3) {

			try {

				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

				wait.until(ExpectedConditions.visibilityOf(element));

				wait.until(ExpectedConditions.elementToBeClickable(element));

				return element;

			} catch (StaleElementReferenceException e) {

				attempts++;

				logger.warn("Retrying wait due to stale element: {}", e.getMessage());

				sleep(1);
			}
		}

		logger.error("Element not stable after retries");

		throw new RuntimeException("Element not stable after retries");
	}

	public static void smartClick(WebDriver driver, WebElement element) {

		int attempts = 0;

		while (attempts < 3) {

			try {

				WebElement el = smartWaitForElement(driver, element);

				el.click();

				return;

			} catch (StaleElementReferenceException | ElementClickInterceptedException e) {

				attempts++;

				logger.warn("Retrying click due to exception: {}", e.getMessage());

				sleep(1);
			}
		}

		logger.info("Fallback to JavaScript click");

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].click();", element);
	}

	public static void smartSendKeys(WebDriver driver, WebElement element, String text) {

		try {

			WebElement el = smartWaitForElement(driver, element);

			el.clear();

			el.sendKeys(text);

		} catch (Exception e) {

			logger.error("Unable to send keys", e);

			throw e;
		}
	}

	public static void scrollAndClick(WebDriver driver, WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].scrollIntoView(true);", element);

		smartClick(driver, element);
	}

	public static void scrollToElement(WebDriver driver, WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	private static void sleep(int seconds) {

		try {

			Thread.sleep(seconds * 1000);

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();
		}
	}
}