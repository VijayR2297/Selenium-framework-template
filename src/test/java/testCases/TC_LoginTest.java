package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.DataProviders;
import utilities.ExcelUtility;

public class TC_LoginTest extends BaseClass {

	@Test(groups = { "sanity", "regression" }, dataProvider = "ExcelData", dataProviderClass = DataProviders.class)
	public void verifyValidLogin(String username, String password, String expected, int rowNum, ExcelUtility xl)
			throws Exception {

		logger.info("Starting login test");

		LoginPage lp = new LoginPage();

		lp.enterUserName(username);
		lp.enterPassword(password);
		lp.clickLoginButton();

		boolean loginSuccess = lp.isLoginSuccessful();

		boolean expectedResult = expected.equalsIgnoreCase("valid");

		boolean testResult = (loginSuccess == expectedResult);

		//This code writes into excel sheet
		
		//String sheetName = new Object(){}.getClass().getEnclosingMethod().getName();
//
//		if (testResult) {
//
//			xl.setCellData(sheetName, rowNum, 3, "PASS");
//			xl.fillGreenColor(sheetName, rowNum, 3);
//
//		} else {
//
//			xl.setCellData(sheetName, rowNum, 3, "FAIL");
//			xl.fillRedColor(sheetName, rowNum, 3);
//		}
		
		
		// logout only if login successful
	    if(loginSuccess){
	    	lp.clickLogoutMenu();
	        lp.clickLogOut();
	    }

		Assert.assertTrue(testResult);
	}

}
