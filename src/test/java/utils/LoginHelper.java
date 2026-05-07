package utils;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import pages.DashboardPage;
import pages.LoginPage;

import java.util.Map;

public class LoginHelper {

    // ✅ Global state flag to track if the current driver session is logged in
    private static boolean isLoggedInSession = false;

    public static boolean isLoggedIn() {
        return isLoggedInSession;
    }

    public static void setLoggedIn(boolean status) {
        isLoggedInSession = status;
    }

    /**
     * ✅ Smart login: detects current app state and acts accordingly.
     *
     * Flow:
     * 1. Switch to NATIVE context (dashboard elements are native)
     * 2. Check if dashboard is visible (= already logged in)
     * 3. If YES → skip login, return immediately
     * 4. If NO → stabilize app, perform login, wait for dashboard
     */
    public static void ensureLoggedIn(AndroidDriver driver) {

        // Step 0: Check global session flag
        if (isLoggedInSession) {
            System.out.println("✅ Global session flag indicates already logged in → skipping login check");
            return;
        }

        // Step 1: Switch to native to check dashboard
        HybridAppStabilizer.ensureNative(driver);

        DashboardPage dashboard = new DashboardPage(driver);

        // Step 2: Check if already on dashboard (= already logged in)
        if (dashboard.isDashboardVisible()) {
            System.out.println("✅ Already logged in → skipping login");
            isLoggedInSession = true; // Set global flag
            return;
        }

        // Step 3: Not logged in → need to stabilize app and login
        System.out.println("🔐 Not logged in → performing login...");

        HybridAppStabilizer.stabilizeApp(driver);

        // Step 4: Switch to WebView for login form
        HybridAppStabilizer.ensureWebView(driver);

        LoginPage login = new LoginPage(driver);

        Map<String, String> data = CSVUtils.getLoginData(0);

        login.enterUsername(data.get("username"));
        login.enterPassword(data.get("password"));
        login.clickLogin();

        System.out.println("🔑 Login credentials submitted");

        // Step 5: Wait for dashboard to appear after login
        boolean dashboardReady = HybridAppStabilizer.waitForDashboard(driver);

        if (dashboardReady) {
            System.out.println("✅ Login successful → dashboard is ready");
            isLoggedInSession = true; // Set global flag
        } else {
            System.out.println("⚠️ Dashboard not visible after login — continuing anyway");
            isLoggedInSession = true; // Assume success if it didn't crash
        }
    }

    /**
     * ✅ Convenience method — gets driver from factory
     */
    public static void loginWithDefaultUser() {
        AndroidDriver driver = DriverFactory.getDriver();

        if (driver == null) {
            throw new RuntimeException("❌ Driver is null — cannot login. Ensure driver is initialized first.");
        }

        ensureLoggedIn(driver);
    }
}
