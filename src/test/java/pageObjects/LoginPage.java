package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import utilities.SmartActions;

public class LoginPage extends BasePage {

	// Call BasePage Constructor to initialize driver

	public LoginPage() {
		super();
		waitForLoginPage(); //login page visibility/loading
	}

	// Page object elements

	@FindBy(xpath = "//input[@placeholder='Username']")
	WebElement userNameField;

	@FindBy(xpath = "//input[@placeholder='Password']")
	WebElement passwordField;

	@FindBy(css = "button[type='submit']")
	WebElement loginButton;

	@FindBy(xpath = "//img[@alt='client brand banner']")
	WebElement dashboardLogo;

	@FindBy(xpath = "//span[@class='oxd-userdropdown-tab']")
	WebElement logoutMenu;

	@FindBy(xpath = "//a[normalize-space()='Logout']")
	WebElement logoutButton;

	// Actions

	//This method ensures to wait for login page is visible
	private void waitForLoginPage() {

		waitForVisibility(userNameField);
		waitForVisibility(passwordField);
	}

	public void enterUserName(String userName) {
		//type(userNameField, userName);
		SmartActions.smartSendKeys(driver, userNameField, userName);
	}

	public void enterPassword(String password) {
		type(passwordField, password);
	}

	public void clickLoginButton() {
		click(loginButton);
		waitForPageLoad();
	}

	public boolean isLoginSuccessful() {
		return driver.getCurrentUrl().contains("dashboard");
	}

	public void clickLogoutMenu() {
		SmartActions.smartClick(driver, logoutMenu);
		//click(logoutMenu);
	}

	public void clickLogOut() {
		click(logoutButton);
	}

}
