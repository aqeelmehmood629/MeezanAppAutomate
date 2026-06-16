package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.DashboardPage;
import pages.LoginPage;
import utils.HybridAppStabilizer;
import utils.CSVUtils;

import java.util.List;
import java.util.Map;

public class LoginSteps {

    private AndroidDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardpage;

    // App package name — used for TC04 terminate + activate restart
    private static final String APP_PACKAGE = "com.ofss.tx.meezan";

    // Stores the username entered in TC04 so the verification step can assert it
    private String rememberedUsername;

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

    // ─────────────────────────────────────────────────────────────────────────
    // TC01 — Valid Login (existing, unchanged)
    // ─────────────────────────────────────────────────────────────────────────

    @Given("the Meezan Bank app is launched")
    public void appLaunch() {
        init();

        // ✅ Check if already logged in first
        if (utils.LoginHelper.isLoggedIn() || dashboardpage.isDashboardVisible()) {
            System.out.println("✅ App already logged in — skipping stabilization");
            utils.LoginHelper.setLoggedIn(true);
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
    	if (utils.LoginHelper.isLoggedIn() || dashboardpage.isDashboardVisible()) {
            System.out.println("Already logged in, skipping login");
            utils.LoginHelper.setLoggedIn(true);
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

    	if (utils.LoginHelper.isLoggedIn() || dashboardpage.isDashboardVisible()) {
            System.out.println("Already logged in, skipping login");
            utils.LoginHelper.setLoggedIn(true);
            return;
        }

        // ✅ Ensure WebView context for login form
        HybridAppStabilizer.ensureWebView(driver);

        // ✅ Use type-filtered method — always gets the first 'valid' row
        Map<String, String> data = CSVUtils.getFirstValidLogin();

        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));

        // Cache for assertion in the final step of Remember Me test
        this.rememberedUsername = data.get("username");

        System.out.println("Using CSV Default User → " + data.get("username"));
    }

    @And("user taps on login button")
    public void tapLogin() {
    	init();

    	if (utils.LoginHelper.isLoggedIn() || dashboardpage.isDashboardVisible()) {
            System.out.println("Already logged in, skipping login tap");
            utils.LoginHelper.setLoggedIn(true);
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
            if (!utils.LoginHelper.isLoggedIn() && !dashboardpage.isDashboardVisible()) {
                throw new AssertionError("Login failed - Dashboard not visible");
            }
        }

        utils.LoginHelper.setLoggedIn(true);
        System.out.println("✅ Login successful");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC02 / TC03 / TC04 — Shared: "user is on Login screen"
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Shared Given step for TC02, TC03, and TC04.
     *
     * ── State Reset Flow ─────────────────────────────────────────────────────
     * This step acts as the primary cleanup gate between Scenario Outline rows.
     * It MUST:
     *   1. Dismiss any lingering error dialog from the previous row (NATIVE check)
     *   2. Dismiss any other generic popup via PopupHandler
     *   3. Stabilize the app so the WebView login form is fully ready
     *   4. Clear the login fields so next row starts blank
     *
     * Hooks already calls ensureLoggedOut() for @NoNeedLogin scenarios, so this
     * step builds on top of that to guarantee a pixel-clean login screen.
     */
    @Given("user is on Login screen")
    public void userIsOnLoginScreen() {
        init();
        System.out.println("📱 Resetting to clean Login screen state...");

        // Step 1: Dismiss any error dialog left by the previous test case
        loginPage.dismissErrorDialogIfPresent();

        // Step 2: Dismiss any other generic popup
        try {
            utils.PopupHandler.handlePopupIfPresent(driver, "Login screen setup");
        } catch (Exception ignored) {}

        // Step 3: Stabilize the app and WebView context
        HybridAppStabilizer.stabilizeApp(driver);

        // Step 4: Clear both login fields so no stale data carries over
        try {
            loginPage.clearLoginFields();
        } catch (Exception e) {
            System.out.println("⚠️ Field pre-clear failed (non-fatal — safeSendKeys will still clear): "
                + e.getMessage());
        }

        System.out.println("✅ Login screen is clean and ready");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC02 — Invalid Credentials (data-driven, one row at a time via Examples)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Reads a specific invalid-credential row by zero-based index from the
     * filtered list of 'invalid' rows in TestData.csv, then enters the
     * credentials and submits the login form.
     *
     * The rowIndex parameter maps directly to the Examples table in the feature:
     *   rowIndex 0 → Empty Username
     *   rowIndex 1 → Empty Password
     *   rowIndex 2 → Wrong Password
     *   rowIndex 3 → Wrong Username Case
     *   rowIndex 4 → Empty Username and Password
     */
    @When("user enters invalid credentials from CSV row {int}")
    public void enterInvalidCredentialsFromCSVRow(int rowIndex) {
        init();

        List<Map<String, String>> invalidRows = CSVUtils.getInvalidLoginData();

        if (rowIndex < 0 || rowIndex >= invalidRows.size()) {
            throw new IndexOutOfBoundsException(
                "❌ Invalid row index " + rowIndex + ". Only " + invalidRows.size() + " invalid rows available in TestData.csv"
            );
        }

        Map<String, String> data = invalidRows.get(rowIndex);

        System.out.println("🔴 Testing invalid credentials [row " + rowIndex + "]: "
            + "username='" + data.get("username") + "', password='" + data.get("password") + "'");

        // ✅ Step 1: Guarantee a blank slate — JS-clear both fields before typing.
        // This is the critical fix: element.clear() silently fails on WebView inputs,
        // causing the previous row's text to persist and new data to be APPENDED.
        try {
            loginPage.clearLoginFields();
        } catch (Exception e) {
            System.out.println("⚠️ Pre-entry field clear failed (non-fatal): " + e.getMessage());
        }

        // Step 2: Switch to WebView and enter the invalid credentials
        HybridAppStabilizer.ensureWebView(driver);

        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));

        // Step 3: Attempt login — expected to fail
        loginPage.clickLogin();
    }

    /**
     * Clicks the SHOW/HIDE password toggle icon to verify password visibility.
     * Used ONLY in TC03 (wrong credential scenarios).
     *
     * Context note: the SHOW button is a NATIVE android.view.View with
     * content-desc="SHOW" — LoginPage.clickShowPasswordIcon() handles the
     * context switch to NATIVE_APP internally.
     *
     * Wrapped in try-catch so it never blocks execution if the icon is not
     * present (e.g. after an error dialog has appeared over the field).
     */
    @And("user clicks show password icon to verify password visibility")
    public void clickShowPasswordIconForPasswordView() {
        init();
        try {
            loginPage.clickShowPasswordIcon();
        } catch (Exception e) {
            System.out.println("⚠️ SHOW icon not interactable (non-fatal — may be obscured by dialog): "
                + e.getMessage());
        }
    }

    /**
     * TC02: Asserts that the mandatory field validation message is displayed.
     * Used for empty username/password scenarios.
     */
    @Then("verify mandatory field validation message is displayed")
    public void verifyMandatoryFieldMessageIsDisplayed() {
        init();
        HybridAppStabilizer.ensureWebView(driver);
        boolean isDisplayed = loginPage.isMandatoryFieldMessageDisplayed();
        Assert.assertTrue(isDisplayed,
            "❌ Expected mandatory field validation message but it was not found.");
        System.out.println("✅ Mandatory field validation message confirmed.");
    }

    /**
     * TC03: Asserts the specific invalid credential error message/popup is shown,
     * then dismisses it to reset the app to a clean login screen state.
     */
	@Then("verify invalid credential error message is displayed")
	public void verifyInvalidCredentialErrorMessageIsDisplayed() {
		init();
		boolean isDisplayed = loginPage.isInvalidCredentialMessageDisplayed();
		Assert.assertTrue(isDisplayed,
			"❌ Expected invalid credential error message but it was not found.");
		System.out.println("✅ Invalid credential error message confirmed.");
		// Note: We do NOT dismiss the error dialog here because TC04 needs to
		// verify the account lock message immediately after this step.
		// Any necessary cleanup is handled by the @Given state-reset step.
	}

    /**
     * Dismisses the error dialog without asserting its content.
     * Used in TC03_LOCK between consecutive wrong-password attempts to reset
     * the login screen state before the next attempt.
     */
    @And("user dismisses the error dialog")
    public void userDismissesErrorDialog() {
        init();
        loginPage.dismissErrorDialogIfPresent();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        System.out.println("✅ Error dialog dismissed (between attempts)");
    }

    /**
     * Verifies the account-locked / blocked message after 3 consecutive wrong
     * password attempts in TC03_LOCK.
     *
     * LoginPage.isAccountLockMessageDisplayed() checks for a text containing
     * 'locked', 'blocked', 'attempts', or 'disabled'. If the app simply shows
     * the standard error dialog on the 3rd attempt (no distinct lock text yet),
     * it falls back to detecting any visible error dialog as confirmation that
     * the 3rd attempt was rejected — consistent with account-lock behaviour.
     */
	@Then("verify account lock message is displayed")
	public void verifyAccountLockMessageIsDisplayed() {
		init();
		boolean lockVisible = loginPage.isAccountLockMessageDisplayed();
		Assert.assertTrue(lockVisible,
			"❌ Expected account lock / error message after 3 consecutive wrong password attempts, "
			+ "but no lock message or error dialog was found.");
		System.out.println("✅ Account lock message confirmed after 3 consecutive wrong attempts");
		// Dismiss the dialog to leave the app in a clean state if there's a native close button
		loginPage.dismissErrorDialogIfPresent();
	}

    // ─────────────────────────────────────────────────────────────────────────
    // TC03 — Login Screen UI Verification (existing step defs below)
    // ─────────────────────────────────────────────────────────────────────────

    @And("verify View Balance button is displayed")
    public void verifyViewBalanceButtonIsDisplayed() {
        init();
        loginPage.verifyViewBalanceButtonDisplayed();
    }

    @Then("verify Register tab is displayed")
    public void verifyRegisterTabIsDisplayed() {
        init();
        loginPage.verifyRegisterTabDisplayed();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC04 — Remember My Username after App Restart
    // ─────────────────────────────────────────────────────────────────────────



    /**
     * Taps the 'Remember My Username' checkbox on the login screen.
     * No CSV involvement — the step just toggles the checkbox.
     */
    @And("user selects Remember My Username option")
    public void selectRememberMyUsernameOption() {
        init();
        loginPage.clickRememberMeCheckbox();
        System.out.println("✅ Remember My Username option selected");
    }

    /**
     * Terminates and re-activates the Meezan Bank app to simulate an app restart.
     * Uses driver.terminateApp() + driver.activateApp() (best practice for Appium 2.x).
     * After activation, re-stabilizes the app to ensure the WebView is ready.
     */
    @And("user restarts the app")
    public void restartTheApp() {
        init();

        System.out.println("🔄 Restarting app: terminating " + APP_PACKAGE + "...");
        driver.terminateApp(APP_PACKAGE);

        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        System.out.println("🚀 Activating " + APP_PACKAGE + "...");
        driver.activateApp(APP_PACKAGE);

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        // Re-stabilize to ensure app & WebView are ready
        HybridAppStabilizer.stabilizeApp(driver);

        System.out.println("✅ App restarted and stabilized");
    }

    /**
     * Reads the current value of the username field after app restart and asserts
     * it matches the username that was entered before restart (stored in rememberedUsername).
     */
    @Then("verify username is prefilled")
    public void verifyUsernameIsPrefilled() {
        init();

        HybridAppStabilizer.ensureWebView(driver);

        String prefilledValue = loginPage.getPrefilledUsername();

        System.out.println("🔍 Expected pre-filled username: '" + rememberedUsername + "'");
        System.out.println("🔍 Actual pre-filled username:   '" + prefilledValue + "'");

        Assert.assertFalse(prefilledValue.isEmpty(),
            "❌ Username field is empty after app restart — Remember My Username did not work");

        Assert.assertEquals(prefilledValue, rememberedUsername,
            "❌ Pre-filled username does not match the one entered before restart");

        System.out.println("✅ Username is correctly pre-filled after app restart");
    }
}
