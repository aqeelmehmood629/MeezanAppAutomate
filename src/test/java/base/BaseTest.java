package base;

import driver.DriverFactory;
import io.appium.java_client.AppiumDriver;

public class BaseTest {
	
	
	protected AppiumDriver driver;

    // Getter method for driver
    public AppiumDriver getDriver() {
        if (driver == null) {
            driver = DriverFactory.initializeDriver();
        }
        return driver;
    }

}

