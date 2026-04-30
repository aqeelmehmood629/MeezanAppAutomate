package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import pages.DashboardPage;
import pages.LoginPage;
import utils.HybridAppStabilizer;
import utils.CSVUtils;

import java.util.Map;

public class LoginSteps {

    private AndroidDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardpage;
    
    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }
        if (loginPage == null && driver != null) {
            loginPage = new LoginPage(driver);
        }
        if (dashboardpage == null && driver != null) {
            dashboardpage = new DashboardPage(driver);
        }
    }


    @Given("the Meezan Bank app is launched")
    public void appLaunch() {
        init();
        
        // ✅ Check if already logged in first
        if (dashboardpage.isDashboardVisible()) {
            System.out.println("✅ App already logged in — skipping stabilization");
            return;
        }
        
        HybridAppStabilizer.stabilizeApp(driver);

        if (driver.getSessionId() == null) {
            throw new RuntimeException("Driver not initialized!");
        }

        System.out.println("App launched");
    }

    @When("user enters credentials from {string}")
    public void enterCredentialsFromCSV(String row) {
    	init();
    	if (dashboardpage.isDashboardVisible()) {
            System.out.println("Already logged in, skipping login");
            return;
        }

        int index = Integer.parseInt(row);

        Map<String, String> data = CSVUtils.getLoginData(index);

        // ✅ Ensure WebView context for login form
        HybridAppStabilizer.ensureWebView(driver);

        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));

        System.out.println("Using CSV Data → " + data.get("username"));
    }

    @When("user enters credentials from csv")
    public void enterCredentialsFromCSVDefault() {
    	init();
    	
    	if (dashboardpage.isDashboardVisible()) {
            System.out.println("Already logged in, skipping login");
            return;
        }

        // ✅ Ensure WebView context for login form
        HybridAppStabilizer.ensureWebView(driver);

        Map<String, String> data = CSVUtils.getLoginData(0);

        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));

        System.out.println("Using CSV Default User → " + data.get("username"));
    }

    @And("user taps on login button")
    public void tapLogin() {
    	init();
    	
    	if (dashboardpage.isDashboardVisible()) {
            System.out.println("Already logged in, skipping login tap");
            return;
        }
    	
        loginPage.clickLogin();
    }

    @Then("user should be logged in successfully")
    public void verifyLogin() {
    	init();

        // ✅ Wait for dashboard to appear after login
        boolean dashboardReady = HybridAppStabilizer.waitForDashboard(driver);

        if (!dashboardReady) {
            // Fallback check
            if (!dashboardpage.isDashboardVisible()) {
                throw new AssertionError("Login failed - Dashboard not visible");
            }
        }

        System.out.println("✅ Login successful");
    }
}

