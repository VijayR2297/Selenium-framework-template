package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);

	int retryCount = 0;

	int maxRetryCount = 2;

	@Override
	public boolean retry(ITestResult result) {

		if (retryCount < maxRetryCount) {

			retryCount++;

			logger.warn("Retrying test {} for the {} time", result.getName(), retryCount);

			return true;
		}

		return false;
	}
}