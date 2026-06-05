package pages;

import org.openqa.selenium.By;
import io.appium.java_client.android.AndroidDriver;
import utils.HybridAppStabilizer;

/**
 * LoginPage — handles the Meezan Bank login screen.
 *
 * Hybrid app context notes:
 *   - Username and Password fields are in WEBVIEW context.
 *   - Log In button is in NATIVE_APP context.
 *
 * Context flow:
 *   1. enterUsername() / enterPassword() → caller must be in WEBVIEW (enforced by LoginHelper)
 *   2. clickLogin() → explicitly switches to NATIVE_APP before clicking the button
 *
 * safeSendKeys() in BasePage handles:
 *   - Restoring WEBVIEW context before every retry
 *   - Popup detection between retries
 *   - clear() failures (non-fatal)
 */
public class LoginPage extends BasePage {

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Locators ──────────────────────────────────────────────────────────────
    // WEBVIEW elements (require WebView context)
    private By usernameField = By.id("login_username");
    private By passwordField = By.id("login_password");

    // NATIVE element (requires NATIVE_APP context)
    private By loginButton = By.xpath("//android.widget.Button[@text='Log In']");

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Enters the username into the WebView login field.
     * Caller must ensure WebView context before calling (done by LoginHelper).
     * BasePage.safeSendKeys() preserves WebView context across retries.
     */
    public void enterUsername(String username) {
        safeSendKeys(usernameField, username, TIMEOUT_LONG);
        System.out.println("✅ Username entered");
    }

    /**
     * Enters the password into the WebView login field.
     */
    public void enterPassword(String password) {
        safeSendKeys(passwordField, password, TIMEOUT_LONG);
        System.out.println("✅ Password entered");
    }

    /**
     * Clicks the Log In button.
     * Explicitly switches to NATIVE_APP before clicking because the button
     * is a native overlay element, not inside the WebView.
     */
    public void clickLogin() {
        hideKeyboard();
        // Must switch to NATIVE — login button is not in WebView
        HybridAppStabilizer.switchToNative(driver);
        safeClick(loginButton, TIMEOUT_LONG);
        System.out.println("✅ Login button clicked");
    }
}