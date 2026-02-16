package steps;

import org.testng.Assert;

import base.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.DashboardPage;

public class DashboardSteps extends BaseTest {

    private DashboardPage dashboardPage;

    public DashboardSteps() {
        // Initialize driver and DashboardPage
        AndroidDriver driver = (AndroidDriver) getDriver();
        this.dashboardPage = new DashboardPage(driver);
    }

    @Then("dashboard should be visible")
    public void dashboard_should_be_visible() {
        // Just check dashboard visibility, assumes login already done
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard is not visible!");
    }

    
}
