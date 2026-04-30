package steps;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.DashboardPage;
import utils.LoginHelper;

public class DashboardSteps {

    private AndroidDriver driver;
    private DashboardPage dashboard;

    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }

        if (dashboard == null) {
            dashboard = new DashboardPage(driver);
        }
    }




    @And("user clicks on Show Balance")
    public void userClicksShowBalance() {
        init();
        dashboard.clickShowBalance();
    }

    @Then("user should see updated balance")
    public void userShouldSeeUpdatedBalance() {
        System.out.println("👁️ Show Balance clicked successfully");
    }
    public void ensureDashboard() {
    	init();

        List<WebElement> homeIcons = driver.findElements(
            By.xpath("//android.widget.Image[@text='home-icon-purple']")
        );

        if (homeIcons.isEmpty()) {
            System.out.println("Not on dashboard → navigating...");
            dashboard.goToDashboard();
        } else {
            System.out.println("Already on dashboard");
        }
    }
}