package pageObjects;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testBase.BaseClass;

public class BasePage {

	protected WebDriver driver;
	protected WebDriverWait wait;

	public BasePage() {

		this.driver = BaseClass.getDriver();

		// Driver defensive check
		if (driver == null) {
			throw new RuntimeException("Driver not initialized. Check BaseClass setup.");
		}

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		PageFactory.initElements(driver, this);
	}
	
	
	
	//**** Methods to use with Elements ****

	// Click an element
	public void click(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	// Type some value
	public void type(WebElement element, String text) {
		wait.until(ExpectedConditions.visibilityOf(element));
		element.clear();
		if(text != null) {
	        element.sendKeys(text);
	    }
	}

	// Get any text from element
	public String getText(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
		return element.getText();
	}

	// Wait for an element to be visible
	public void waitForVisibility(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	// Scroll to an element using javaScript
	public void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	// Click an element using javaScript
	public void jsClick(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}

	// Drop-down helper
	public void selectByVisibleText(WebElement element, String text) {
		Select select = new Select(element);
		select.selectByVisibleText(text);
	}

	// wait for the page to load method
	public void waitForPageLoad() {
		new WebDriverWait(driver, Duration.ofSeconds(20)).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));
	}

	// Stale element click retry method
	public void staleElementClick(WebElement element) {
		int attempts = 0;

		while (attempts < 3) {
			try {
				wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));
				element.click();
				return;
			} catch (StaleElementReferenceException e) {
				attempts++;
			}
		}

		throw new RuntimeException("Unable to click element after retries");
	}
}