package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import io.appium.java_client.android.AndroidDriver;
import utils.HybridAppStabilizer;

/**
 * LoginPage — handles the Meezan Bank login screen.
 *
 * Hybrid app context notes: - Username and Password fields are in WEBVIEW
 * context. - Log In button is in NATIVE_APP context.
 *
 * Context flow: 1. enterUsername() / enterPassword() → caller must be in
 * WEBVIEW (enforced by LoginHelper) 2. clickLogin() → explicitly switches to
 * NATIVE_APP before clicking the button
 *
 * safeSendKeys() in BasePage handles: - Restoring WEBVIEW context before every
 * retry - Popup detection between retries - clear() failures (non-fatal)
 */
public class LoginPage extends BasePage {

	public LoginPage(AndroidDriver driver) {
		super(driver);
	}

	// ── Validation Wait Constants ─────────────────────────────────────────────

	/**
	 * Maximum seconds to poll the WebView DOM for a validation message.
	 * After submitting invalid credentials, the server response and DOM
	 * update are not instant — 15 seconds covers normal network latency.
	 * Increase if the test environment has a slow backend.
	 */
	private static final int TIMEOUT_VALIDATION = 15;

	/**
	 * Interval between each JS DOM poll, in milliseconds.
	 * 1000ms (1 second) gives up to 15 checks within TIMEOUT_VALIDATION.
	 */
	private static final long POLL_INTERVAL_MS = 1000L;

	// ── Locators ──────────────────────────────────────────────────────────────
	// WEBVIEW elements (require WebView context)
	private By usernameField      = By.id("login_username");
	private By passwordField      = By.id("login_password");

	// ── NATIVE-context elements confirmed from XML UI hierarchy ──────────────

	/**
	 * SHOW/HIDE password toggle.
	 * From XML: android.view.View with content-desc="SHOW" at bounds [889,1109][981,1177].
	 * This is a NATIVE element — switch to NATIVE_APP before clicking.
	 */
	private final By showPasswordIcon = By.xpath("//android.view.View[@content-desc='SHOW']");

	/**
	 * Remember My Username checkbox.
	 * From XML: android.widget.CheckBox with resource-id="ui-id-22|cb", text="Remember my Username".
	 * Although it lives inside the WebView DOM, Appium UiAutomator2 sees it as a native widget.
	 * Use NATIVE_APP context and the resource-id locator (most stable, survives text localisation).
	 * Fallback XPath by text is kept as a comment for reference:
	 *   By.xpath("//android.widget.CheckBox[@text='Remember my Username']")
	 */
	private final By rememberMeCheckbox =
			By.xpath("//android.widget.CheckBox[@resource-id='ui-id-22|cb']");

	// Log In button (NATIVE)
	private final By loginButton = By.xpath("//android.widget.Button[@text='Log In']");

	// ─────────────────────────────────────────────────────────────────────────
	// ❌ NEGATIVE SCENARIO LOCATORS
	// ─────────────────────────────────────────────────────────────────────────
	//
	// ⚠️ CRITICAL CONTEXT NOTE (derived from UI hierarchy XML analysis):
	// All three validation messages below are rendered INSIDE the WebView page
	// as inline HTML sections — they are NOT native Android dialogs.
	//
	// The hierarchy appears to show native widget classes (android.widget.TextView)
	// because Android Accessibility Service translates WebView HTML nodes for the
	// inspector dump. However, UiAutomator2's findElement() in NATIVE_APP context
	// CANNOT traverse into WebView children at runtime.
	//
	// All three methods below MUST switch to WEBVIEW context before searching.
	// JavaScript executor is used as the primary strategy because it bypasses
	// Appium locator limitations entirely and reads the live DOM directly.
	// XPath (WebView-style) is provided as the secondary/fallback strategy.
	// ─────────────────────────────────────────────────────────────────────────

	/**
	 * TC02 — Mandatory field validation.
	 *
	 * Appears as an inline HTML error section inside the WebView login form
	 * when the user submits with empty username or password.
	 *
	 * JS primary  : checks document body text for known validation phrases.
	 * XPath fallback: searches for the visible error element by text content.
	 *
	 * TODO: Replace the JS keyword string and/or XPath text with the exact
	 *       phrase your app shows (inspect via browser DevTools on the WebView).
	 */
	private static final String MANDATORY_FIELD_JS_KEYWORD = "required";
	private final By MANDATORY_FIELD_MSG =
			By.xpath("//*[contains(text(),'required') or contains(text(),'mandatory') "
					+ "or contains(text(),'Please enter') or contains(text(),'field is required')]");

	/**
	 * TC03 — Invalid credential error message.
	 *
	 * Appears as an inline HTML error section inside the WebView login form
	 * when the user submits with a wrong username or password.
	 *
	 * JS primary  : checks for known invalid-credential phrases in the DOM.
	 * XPath fallback: text-based XPath in WebView context.
	 *
	 * TODO: Replace the JS keyword string and/or XPath text with the exact
	 *       error text your app displays for wrong credentials.
	 */
	private static final String INVALID_CRED_JS_KEYWORD = "Invalid";
	private final By INVALID_CREDENTIAL_MSG =
			By.xpath("//*[contains(text(),'Invalid') or contains(text(),'incorrect') "
					+ "or contains(text(),'wrong') or contains(text(),'does not match')]");

	/**
	 * TC04 — Account blocked / Recover Account message.
	 *
	 * This is the 'Recover Account' in-page panel (view index=13 in the XML
	 * hierarchy). It renders INSIDE the WebView at the same URL as the login
	 * form — it overlays the form after 3 consecutive wrong-password attempts.
	 *
	 * CONFIRMED from XML: The panel contains three child TextViews:
	 *   • "Recover Account"  (title)
	 *   • "You've entered incorrect credentials multiple times..."
	 *   • "Reset Password"   (action link)
	 *
	 * Why NATIVE locators fail: UiAutomator2's findElement() cannot reach
	 * children of android.webkit.WebView at runtime even though the inspector
	 * dump shows them as android.widget.TextView nodes.
	 *
	 * JS primary  : checks for "Recover Account" text in the live DOM.
	 * XPath fallback: WebView-context XPath using the confirmed visible text.
	 *
	 * NOTE: Do NOT use contains(@text,'You'\''ve') — apostrophes break XPath.
	 *       Use the "Recover Account" title or "Reset Password" link instead.
	 */
	private static final String ACCOUNT_LOCK_JS_KEYWORD = "Recover Account";
	private final By ACCOUNT_LOCK_MSG =
			By.xpath("//*[contains(text(),'Recover Account') or contains(text(),'Reset Password') "
					+ "or contains(text(),'account has been blocked')]");

	// Generic Error Dialog Buttons — NATIVE (used only for dismiss between steps)
	private final By ERROR_CLOSE_BUTTON = By.xpath("//android.widget.Button[@text='Close']");
	private final By ERROR_TRY_AGAIN    = By.xpath("//android.widget.TextView[@text='Try Again']");

	// View Balance — NATIVE (android.widget.TextView confirmed in hierarchy)
	private final By VIEW_BALANCE_BUTTON =
			By.xpath("//android.widget.TextView[@text='View Balance']");

	// Register tab — NATIVE (android.view.View confirmed in hierarchy)
	private final By REGISTER_TAB =
			By.xpath("//android.view.View[@text='Register']");

	// ── Actions ───────────────────────────────────────────────────────────────

	/**
	 * Enters the username into the WebView login field. Caller must ensure WebView
	 * context before calling (done by LoginHelper). BasePage.safeSendKeys()
	 * preserves WebView context across retries.
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
	 * Clicks the Log In button. Explicitly switches to NATIVE_APP before clicking
	 * because the button is a native overlay element, not inside the WebView.
	 */
	public void clickLogin() {
		hideKeyboard();
		// Must switch to NATIVE — login button is not in WebView
		HybridAppStabilizer.switchToNative(driver);
		safeClick(loginButton, TIMEOUT_LONG);
		System.out.println("✅ Login button clicked");
	}

	/**
	 * Clears both username and password fields using JavaScript.
	 *
	 * Why JavaScript instead of element.clear():
	 * ─────────────────────────────────────────
	 * In hybrid Appium sessions, element.clear() silently fails on many WebView
	 * input fields (StaleElementReference or ChromeDriver quirk). This causes
	 * old text to persist and new text to be APPENDED — making each consecutive
	 * CSV row test case inherit the previous row's data.
	 *
	 * The JS approach:
	 *   1. Sets element.value = '' (forces empty regardless of clear() support)
	 *   2. Dispatches 'input' + 'change' events so React/Angular sees the reset
	 *   3. Never throws — all errors are caught and logged
	 *
	 * Must be called BEFORE entering new credentials for every test case.
	 */
	public void clearLoginFields() {
		HybridAppStabilizer.ensureWebView(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		String clearScript =
			"var el = arguments[0];" +
			"el.value = '';" +
			"el.dispatchEvent(new Event('input', { bubbles: true }));" +
			"el.dispatchEvent(new Event('change', { bubbles: true }));"
		;

		// ── Clear username ─────────────────────────────────────────────────────
		try {
			WebElement usernameEl = driver.findElement(usernameField);
			js.executeScript(clearScript, usernameEl);
			System.out.println("✅ Username field cleared via JS");
		} catch (Exception e) {
			System.out.println("⚠️ Username clear (JS) failed — fallback to sendKeys CTRL+A+DELETE");
			try {
				driver.findElement(usernameField).sendKeys(
					org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"),
					org.openqa.selenium.Keys.DELETE);
			} catch (Exception ignored) {}
		}

		// ── Clear password ─────────────────────────────────────────────────────
		try {
			WebElement passwordEl = driver.findElement(passwordField);
			js.executeScript(clearScript, passwordEl);
			System.out.println("✅ Password field cleared via JS");
		} catch (Exception e) {
			System.out.println("⚠️ Password clear (JS) failed — fallback to sendKeys CTRL+A+DELETE");
			try {
				driver.findElement(passwordField).sendKeys(
					org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"),
					org.openqa.selenium.Keys.DELETE);
			} catch (Exception ignored) {}
		}
	}

	/**
	 * Safe, silent cleanup: dismisses the login error dialog if it is currently
	 * visible. Never throws — designed to be called as a state-reset guard at
	 * the start of every test case or step that requires a clean login screen.
	 *
	 * Uses NATIVE_APP context (the error dialog is a native overlay).
	 * Restores WebView context afterwards.
	 */
	public void dismissErrorDialogIfPresent() {
		try {
			HybridAppStabilizer.ensureNative(driver);

			// Check for Close button (primary dismiss target)
			List<org.openqa.selenium.WebElement> closeButtons =
					driver.findElements(ERROR_CLOSE_BUTTON);
			if (!closeButtons.isEmpty() && closeButtons.get(0).isDisplayed()) {
				closeButtons.get(0).click();
				Thread.sleep(500);
				System.out.println("✅ Error dialog dismissed (Close button)");
				return;
			}

			// Fallback: Try Again button (also dismisses / resets dialog)
			List<org.openqa.selenium.WebElement> tryAgainButtons =
					driver.findElements(ERROR_TRY_AGAIN);
			if (!tryAgainButtons.isEmpty() && tryAgainButtons.get(0).isDisplayed()) {
				// Don't click Try Again (it would re-attempt the failed login).
				// Use Back key to dismiss the dialog instead.
				driver.navigate().back();
				Thread.sleep(500);
				System.out.println("✅ Error dialog dismissed via Back key (Try Again was visible)");
				return;
			}

			System.out.println("ℹ️ No error dialog present — nothing to dismiss");

		} catch (Exception e) {
			System.out.println("⚠️ dismissErrorDialogIfPresent() encountered an error (non-fatal): "
				+ e.getMessage());
		}
	}

	/**
	 * Clicks the SHOW/HIDE password toggle icon.
	 *
	 * Context note: although the toggle sits visually inside the password field,
	 * Appium's UiAutomator2 sees it as a NATIVE android.view.View with
	 * content-desc="SHOW" — it is NOT accessible via the WebView context.
	 * We must switch to NATIVE_APP before clicking.
	 */
	public void clickShowPasswordIcon() {
		HybridAppStabilizer.ensureNative(driver);
		safeClick(showPasswordIcon, TIMEOUT_SHORT);
		System.out.println("✅ Show-password icon clicked (NATIVE context)");
	}

	/**
	 * Checks whether the post-login error dialog is visible in NATIVE context.
	 * Looks for the 'Close' or 'Try Again' buttons confirmed by the user.
	 * @return true if either button is visible (error dialog is up)
	 */
	public boolean isLoginErrorDisplayed() {
		HybridAppStabilizer.ensureNative(driver);
		try {
			boolean closeVisible    = !driver.findElements(ERROR_CLOSE_BUTTON).isEmpty();
			boolean tryAgainVisible = !driver.findElements(ERROR_TRY_AGAIN).isEmpty();
			return closeVisible || tryAgainVisible;
		} catch (Exception e) {
			System.out.println("⚠️ Error while checking login error dialog: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Dismisses the post-login error dialog by tapping the 'Close' button.
	 */
	public void clickCloseErrorButton() {
		HybridAppStabilizer.ensureNative(driver);
		safeClick(ERROR_CLOSE_BUTTON, TIMEOUT_LONG);
		System.out.println("✅ Error dialog closed");
	}

	/**
	 * Toggles the 'Remember My Username' checkbox.
	 *
	 * Uses JavascriptExecutor in WEBVIEW context as the primary strategy.
	 * Tapping the Native AccessibilityNode (CheckBox) often toggles the UI visually
	 * but fails to trigger the underlying Javascript 'change' event in Oracle JET,
	 * meaning the app never actually saves the credential to secure storage.
	 */
	public void clickRememberMeCheckbox() {
		// Primary: JavaScript click (guarantees event listeners fire)
		try {
			HybridAppStabilizer.ensureWebView(driver);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Find the checkbox input (usually the only one on the login screen)
			js.executeScript(
				"var cb = document.querySelector('input[type=\"checkbox\"]'); " +
				"if (cb) { cb.click(); } else { throw 'Checkbox not found in DOM'; }"
			);
			System.out.println("✅ Remember My Username checkbox toggled (WEBVIEW JS click)");
			return; // Success, skip native fallback
		} catch (Exception e) {
			System.out.println("⚠️ WEBVIEW JS click failed on checkbox, trying NATIVE fallback: " + e.getMessage());
		}

		// Fallback: NATIVE click
		HybridAppStabilizer.ensureNative(driver);
		safeClick(rememberMeCheckbox, TIMEOUT_LONG);
		System.out.println("✅ Remember My Username checkbox toggled (NATIVE context fallback)");
	}

	/**
	 * Reads the current value from the username WebView field.
	 * Used for TC05 to verify the username is pre-filled after app restart.
	 *
	 * Uses JavascriptExecutor to poll the .value property. In hybrid apps,
	 * data from secure storage is injected asynchronously AFTER the UI renders.
	 * Polling for up to 10 seconds ensures we wait for the framework to populate it.
	 */
	public String getPrefilledUsername() {
		long endTime = System.currentTimeMillis() + (10 * 1000); // 10s timeout

		// Primary: JavaScript executor (most reliable for WebView properties)
		try {
			HybridAppStabilizer.ensureWebView(driver);
			JavascriptExecutor js = (JavascriptExecutor) driver;

			while (System.currentTimeMillis() < endTime) {
				Object jsValue = js.executeScript("return document.getElementById('login_username|input').value;");
				if (jsValue != null) {
					String valStr = jsValue.toString().trim();
					// Safety check: if it still returned the binding by some fluke, ignore it
					if (!valStr.contains("{{") && !valStr.isEmpty()) {
						System.out.println("✅ Pre-filled username (WebView JS value): " + valStr);
						return valStr;
					}
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println("⚠️ WebView JS value polling failed, trying NATIVE fallback: " + e.getMessage());
		}

		// Fallback: NATIVE EditText
		long nativeEndTime = System.currentTimeMillis() + (5 * 1000); // 5s extra for native
		try {
			HybridAppStabilizer.ensureNative(driver);
			By nativeUsername = By.xpath("//android.widget.EditText[@resource-id='login_username|input']");

			while (System.currentTimeMillis() < nativeEndTime) {
				String text = driver.findElement(nativeUsername).getText();
				if (text != null && !text.trim().isEmpty() && !text.contains("{{")) {
					System.out.println("✅ Pre-filled username (NATIVE getText): " + text);
					return text.trim();
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println("⚠️ NATIVE getText polling also failed: " + e.getMessage());
		}

		System.out.println("⚠️ Polling finished, username field remains empty.");
		return "";
	}

	/**
	 * TC02: Waits up to TIMEOUT_VALIDATION seconds for the mandatory field
	 * validation message to appear in the WebView DOM.
	 *
	 * After tapping 'Log In' with an empty field, the app needs a brief moment
	 * to render the inline validation error — a one-shot check fires too early.
	 * This method polls the DOM every second until the keyword is found or the
	 * timeout expires, then falls back to an XPath presence check.
	 *
	 * @return true if the mandatory field validation message appears within timeout
	 */
	public boolean isMandatoryFieldMessageDisplayed() {
		HybridAppStabilizer.ensureWebView(driver);
		System.out.println("⏳ Waiting for mandatory field message (up to " + TIMEOUT_VALIDATION + "s)...");

		// ── Primary: poll JS DOM until keyword appears ──────────────────────
		if (waitForDomTextContains(MANDATORY_FIELD_JS_KEYWORD, TIMEOUT_VALIDATION)) {
			System.out.println("✅ Mandatory field message appeared (JS DOM poll)");
			return true;
		}

		// ── Fallback: WebDriverWait on XPath ──────────────────────────────
		try {
			new org.openqa.selenium.support.ui.WebDriverWait(
					driver, java.time.Duration.ofSeconds(TIMEOUT_VALIDATION))
					.until(org.openqa.selenium.support.ui.ExpectedConditions
							.visibilityOfElementLocated(MANDATORY_FIELD_MSG));
			System.out.println("✅ Mandatory field message found via XPath wait (WebView)");
			return true;
		} catch (Exception e) {
			System.out.println("❌ Mandatory field message NOT found after " + TIMEOUT_VALIDATION + "s");
			return false;
		}
	}

	/**
	 * TC03: Waits up to TIMEOUT_VALIDATION seconds for the invalid credential
	 * error message to appear in the WebView DOM.
	 *
	 * After a failed login with wrong credentials, the server response and
	 * DOM update take time — polling is essential to avoid a premature miss.
	 * Polls JS DOM every second; falls back to WebDriverWait on XPath.
	 *
	 * @return true if the invalid credential message appears within timeout
	 */
	public boolean isInvalidCredentialMessageDisplayed() {
		HybridAppStabilizer.ensureWebView(driver);
		System.out.println("⏳ Waiting for invalid credential message (up to " + TIMEOUT_VALIDATION + "s)...");

		// ── Primary: poll JS DOM ────────────────────────────────────────────
		// Check multiple keywords — exact text depends on server error message
		if (waitForDomTextContainsAny(
				new String[]{ INVALID_CRED_JS_KEYWORD, "incorrect", "wrong", "does not match" },
				TIMEOUT_VALIDATION)) {
			System.out.println("✅ Invalid credential message appeared (JS DOM poll)");
			return true;
		}

		// ── Fallback: WebDriverWait on XPath ──────────────────────────────
		try {
			new org.openqa.selenium.support.ui.WebDriverWait(
					driver, java.time.Duration.ofSeconds(TIMEOUT_VALIDATION))
					.until(org.openqa.selenium.support.ui.ExpectedConditions
							.visibilityOfElementLocated(INVALID_CREDENTIAL_MSG));
			System.out.println("✅ Invalid credential message found via XPath wait (WebView)");
			return true;
		} catch (Exception e) {
			System.out.println("❌ Invalid credential message NOT found after " + TIMEOUT_VALIDATION + "s");
			return false;
		}
	}

	/**
	 * TC04: Waits up to TIMEOUT_VALIDATION seconds for the 'Recover Account'
	 * account-blocked panel to appear in the WebView DOM.
	 *
	 * This panel only renders after the server processes 3 consecutive wrong
	 * attempts and pushes a new DOM section into the login page. Server latency
	 * means a one-shot check will almost always miss it — polling is required.
	 *
	 * Apostrophe note: "You've entered..." cannot be used in XPath string
	 * literals without concat() workaround. We use "Recover Account" and
	 * "Reset Password" (both apostrophe-free, confirmed in the XML) instead.
	 *
	 * @return true if the account-blocked panel appears within timeout
	 */
	public boolean isAccountLockMessageDisplayed() {
		HybridAppStabilizer.ensureWebView(driver);
		System.out.println("⏳ Waiting for account lock panel (up to " + TIMEOUT_VALIDATION + "s)...");

		// ── Primary: poll JS DOM ────────────────────────────────────────────
		// Case-sensitive: "Recover Account", "Reset Password", "account has been blocked"
		// are all confirmed in the XML hierarchy — no apostrophes, safe to use directly.
		if (waitForDomTextContainsAny(
				new String[]{ ACCOUNT_LOCK_JS_KEYWORD, "Reset Password", "account has been blocked" },
				TIMEOUT_VALIDATION)) {
			System.out.println("🔒 Account lock panel appeared (JS DOM poll)");
			return true;
		}

		// ── Fallback: WebDriverWait on XPath ──────────────────────────────
		try {
			new org.openqa.selenium.support.ui.WebDriverWait(
					driver, java.time.Duration.ofSeconds(TIMEOUT_VALIDATION))
					.until(org.openqa.selenium.support.ui.ExpectedConditions
							.visibilityOfElementLocated(ACCOUNT_LOCK_MSG));
			System.out.println("🔒 Account lock panel found via XPath wait (WebView)");
			return true;
		} catch (Exception e) {
			System.out.println("❌ Account lock panel NOT found after " + TIMEOUT_VALIDATION + "s");
			return false;
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Private wait helpers — shared by all three validation methods
	// ─────────────────────────────────────────────────────────────────────────

	/**
	 * Polls document.body.innerText every POLL_INTERVAL_MS milliseconds until
	 * the given keyword is found (case-insensitive) or timeoutSeconds elapses.
	 *
	 * @param keyword        text to search for in the DOM body
	 * @param timeoutSeconds maximum number of seconds to wait
	 * @return true if keyword found before timeout
	 */
	private boolean waitForDomTextContains(String keyword, int timeoutSeconds) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);
		String lowerKeyword = keyword.toLowerCase();
		int attempt = 0;

		while (System.currentTimeMillis() < deadline) {
			attempt++;
			try {
				Object result = js.executeScript("return document.body ? document.body.innerText : '';");
				if (result != null && result.toString().toLowerCase().contains(lowerKeyword)) {
					System.out.println("✅ Keyword '" + keyword + "' found in DOM (attempt " + attempt + ")");
					return true;
				}
			} catch (Exception e) {
				System.out.println("⚠️ DOM poll attempt " + attempt + " failed: " + e.getMessage());
			}
			try { Thread.sleep(POLL_INTERVAL_MS); } catch (InterruptedException ignored) {}
		}
		System.out.println("⚠️ Keyword '" + keyword + "' not found after " + attempt + " polls");
		return false;
	}

	/**
	 * Polls document.body.innerText every POLL_INTERVAL_MS milliseconds until
	 * ANY of the given keywords is found (case-insensitive) or timeout elapses.
	 *
	 * @param keywords       array of text strings to search for
	 * @param timeoutSeconds maximum number of seconds to wait
	 * @return true if any keyword found before timeout
	 */
	private boolean waitForDomTextContainsAny(String[] keywords, int timeoutSeconds) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);
		int attempt = 0;

		while (System.currentTimeMillis() < deadline) {
			attempt++;
			try {
				Object result = js.executeScript("return document.body ? document.body.innerText : '';");
				if (result != null) {
					String bodyText = result.toString();
					for (String keyword : keywords) {
						if (bodyText.toLowerCase().contains(keyword.toLowerCase())) {
							System.out.println("✅ Keyword '" + keyword
								+ "' found in DOM (attempt " + attempt + ")");
							return true;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("⚠️ DOM poll attempt " + attempt + " failed: " + e.getMessage());
			}
			try { Thread.sleep(POLL_INTERVAL_MS); } catch (InterruptedException ignored) {}
		}
		System.out.println("⚠️ None of the keywords found after " + attempt + " polls");
		return false;
	}

	public void verifyViewBalanceButtonDisplayed() {
		// View Balance is a NATIVE android.widget.TextView
		HybridAppStabilizer.ensureNative(driver);
	    Assert.assertTrue(
	            safeWait(VIEW_BALANCE_BUTTON).isDisplayed(),
	            "View Balance button is not displayed");
	}

	public void verifyRegisterTabDisplayed() {
		// Register tab is a NATIVE android.view.View
		HybridAppStabilizer.ensureNative(driver);
	    Assert.assertTrue(
	            safeWait(REGISTER_TAB).isDisplayed(),
	            "Register tab is not displayed");
	}
}