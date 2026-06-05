package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import driver.DriverFactory;
import utils.HybridAppStabilizer;
import utils.PopupHandler;

import java.time.Duration;

/**
 * 🛡️ BasePage — Foundation class for all Page Objects.
 *
 * ── HYBRID APP CONTEXT SAFETY ────────────────────────────────────────────────
 * This app is a hybrid (WEBVIEW + NATIVE_APP). All safe methods preserve the
 * caller's context:
 *   1. Context is captured at the start of every safe method call.
 *   2. Before each retry attempt, the original context is restored.
 *   3. After handleFailure() (which calls PopupHandler → NATIVE_APP), the
 *      context is restored to what it was before the failure.
 *
 * This ensures that:
 *   - safeSendKeys() on a WEBVIEW element always retries IN WebView
 *   - safeClick() on a NATIVE element always retries in NATIVE
 *   - PopupHandler cannot permanently destroy the caller's context
 *
 * ── SAFE clear() ─────────────────────────────────────────────────────────────
 * safeSendKeys() wraps element.clear() in a try-catch because clear() throws
 * on some WebView input elements (StaleElementReference, or framework-level
 * restriction). Failure to clear is non-fatal — sendKeys() still delivers text.
 *
 * Usage: Extend this class in any Page Object:
 *
 *   public class MyPage extends BasePage {
 *       public MyPage(AndroidDriver driver) { super(driver); }
 *       public void doSomething() { safeClick(myLocator); }
 *   }
 */
public class BasePage {

    protected AndroidDriver driver;
    protected WebDriverWait wait;

    // ── Standardized Timeouts ─────────────────────────────────────────────────
    /** Short timeout for quick checks (element existence, popup detection) */
    public static final int TIMEOUT_SHORT = 10;

    /** Default timeout for most interactions (click, sendKeys, visibility) */
    public static final int TIMEOUT_DEFAULT = 30;

    /** Long timeout for operations that genuinely take time (transaction, login) */
    public static final int TIMEOUT_LONG = 60;

    /** How many times to retry an action after dismissing a popup or recovering driver */
    private static final int MAX_RETRIES = 2;

    // ── Constructors ──────────────────────────────────────────────────────────

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_DEFAULT));
    }

    public BasePage(AndroidDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    // ── Safe Click ────────────────────────────────────────────────────────────

    /**
     * 🛡️ Click with popup-aware retry + driver recovery. Default timeout (30s).
     */
    public void safeClick(By locator) {
        safeClick(locator, TIMEOUT_DEFAULT);
    }

    /**
     * 🛡️ Click with popup-aware retry + context preservation.
     *
     * Flow:
     *   1. Capture current context (WebView or NATIVE)
     *   2. Before each attempt, restore that context
     *   3. Attempt the click
     *   4. On failure → handleFailure() (popup check, driver recovery)
     *   5. Restore context again before retry
     */
    public void safeClick(By locator, int timeoutSeconds) {
        // ── Capture starting context for restoration ──
        String startContext = captureContext();

        Exception lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            // ── Restore context before every attempt ──
            restoreStartingContext(startContext);

            try {
                WebDriverWait actionWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                actionWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
                return; // ✅ Success
            } catch (Exception e) {
                lastException = e;
                System.out.println("⚠️ safeClick attempt " + attempt + "/" + MAX_RETRIES
                        + " failed for: " + locator.toString());

                if (!handleFailure(attempt)) break;
            }
        }

        throw new RuntimeException("safeClick failed after " + MAX_RETRIES
                + " attempts for locator: " + locator, lastException);
    }

    // ── Safe SendKeys ─────────────────────────────────────────────────────────

    /**
     * 🛡️ SendKeys with popup-aware retry + context preservation. Default timeout (30s).
     */
    public void safeSendKeys(By locator, String text) {
        safeSendKeys(locator, text, TIMEOUT_DEFAULT);
    }

    /**
     * 🛡️ SendKeys with popup-aware retry + context preservation.
     *
     * ── KEY FIX: clear() is wrapped in try-catch ──
     * On certain WebView input fields, element.clear() throws even when the
     * element is found and clickable. This is a known Appium/ChromeDriver quirk.
     * Wrapping clear() prevents a successful element find from turning into a
     * failed attempt, which would trigger an unnecessary context switch.
     */
    public void safeSendKeys(By locator, String text, int timeoutSeconds) {
        // ── Capture starting context for restoration ──
        String startContext = captureContext();

        Exception lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            // ── Restore context before every attempt ──
            restoreStartingContext(startContext);

            try {
                WebDriverWait actionWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                WebElement element = actionWait.until(
                        ExpectedConditions.visibilityOfElementLocated(locator));
                element.click();
                // ✅ FIX: clear() wrapped in try-catch — failure is non-fatal
                try {
                    element.clear();
                } catch (Exception clearEx) {
                    System.out.println("ℹ️ clear() skipped (non-fatal): " + clearEx.getMessage());
                }
                element.sendKeys(text);
                return; // ✅ Success
            } catch (Exception e) {
                lastException = e;
                System.out.println("⚠️ safeSendKeys attempt " + attempt + "/" + MAX_RETRIES
                        + " failed for: " + locator.toString());

                if (!handleFailure(attempt)) break;
            }
        }

        throw new RuntimeException("safeSendKeys failed after " + MAX_RETRIES
                + " attempts for locator: " + locator, lastException);
    }

    // ── Safe Wait ─────────────────────────────────────────────────────────────

    /**
     * 🛡️ Wait for element visibility with popup-aware retry. Default timeout (30s).
     */
    public WebElement safeWait(By locator) {
        return safeWait(locator, TIMEOUT_DEFAULT);
    }

    /**
     * 🛡️ Wait for element visibility with popup-aware retry + context preservation.
     */
    public WebElement safeWait(By locator, int timeoutSeconds) {
        // ── Capture starting context for restoration ──
        String startContext = captureContext();

        Exception lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            // ── Restore context before every attempt ──
            restoreStartingContext(startContext);

            try {
                WebDriverWait actionWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                return actionWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (Exception e) {
                lastException = e;
                System.out.println("⚠️ safeWait attempt " + attempt + "/" + MAX_RETRIES
                        + " failed for: " + locator.toString());

                if (!handleFailure(attempt)) break;
            }
        }

        throw new RuntimeException("safeWait failed after " + MAX_RETRIES
                + " attempts for locator: " + locator, lastException);
    }

    // ── Safe Context Switch ───────────────────────────────────────────────────

    /**
     * 🔄 Guarded context switch — replaces raw driver.context() calls.
     */
    public void safeContextSwitch(String targetContext) {
        try {
            String current = driver.getContext();
            if (targetContext.equals(current)) return;
            driver.context(targetContext);
            System.out.println("🔄 Context switched to: " + targetContext);
        } catch (Exception e) {
            System.out.println("⚠️ Context switch to " + targetContext + " failed: " + e.getMessage());
        }
    }

    /**
     * 📱 Convenience: ensure NATIVE_APP context.
     */
    public void ensureNativeContext() {
        HybridAppStabilizer.ensureNative(driver);
    }

    /**
     * 🌐 Convenience: ensure WebView context.
     */
    public void ensureWebViewContext() {
        HybridAppStabilizer.ensureWebView(driver);
    }

    // ── Utility Methods ───────────────────────────────────────────────────────

    /**
     * Check for and dismiss any popup. Safe to call anywhere.
     */
    public boolean dismissPopupIfPresent() {
        return PopupHandler.handlePopupIfPresent(driver);
    }

    /**
     * ⌨️ Hide keyboard safely.
     */
    public void hideKeyboard() {
        HybridAppStabilizer.hideKeyboard(driver);
    }

    /**
     * 💤 Safe sleep — wraps Thread.sleep without throwing.
     */
    protected void sleep(long ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }

    // ── Private: Context Helpers ──────────────────────────────────────────────

    /**
     * Safely captures the current driver context.
     * Returns null if context cannot be determined (never throws).
     */
    private String captureContext() {
        try {
            return driver.getContext();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ✅ Restores the driver to its starting context before a retry.
     *
     * This is the KEY fix for hybrid app context safety:
     *   - If we started in WebView → use HybridAppStabilizer.ensureWebView()
     *     (more reliable than a raw driver.context() call with a hardcoded name)
     *   - If we started in NATIVE → use HybridAppStabilizer.ensureNative()
     *   - If unknown → do nothing
     */
    private void restoreStartingContext(String startContext) {
        if (startContext == null) return;

        try {
            String current = driver.getContext();

            if (startContext.toLowerCase().contains("webview")) {
                // We need to be in WebView — use the stabilizer (handles name changes)
                if (current == null || !current.toLowerCase().contains("webview")) {
                    HybridAppStabilizer.ensureWebView(driver);
                }
            } else if ("NATIVE_APP".equals(startContext)) {
                // We need to be in NATIVE
                if (!"NATIVE_APP".equals(current)) {
                    HybridAppStabilizer.ensureNative(driver);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not restore starting context (" + startContext + "): " + e.getMessage());
        }
    }

    // ── Private: Failure Handling ─────────────────────────────────────────────

    /**
     * Handles a failure during a safe action.
     *
     * Note: PopupHandler internally switches to NATIVE_APP.
     * Context restoration after this call is handled by restoreStartingContext()
     * at the top of the retry loop — NOT here.
     *
     * @return true if the caller should retry, false if retries should stop
     */
    private boolean handleFailure(int attempt) {
        // ── Check 1: Is a popup blocking? ──
        boolean popupHandled = PopupHandler.handlePopupIfPresent(driver);
        if (popupHandled) {
            System.out.println("🔄 Popup dismissed — retrying action...");
            return true;
        }

        // ── Check 2: Is the driver dead? ──
        if (!DriverFactory.isDriverResponsive()) {
            System.out.println("🚨 Driver is unresponsive — attempting recovery...");
            try {
                this.driver = DriverFactory.recoverDriver();
                this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_DEFAULT));
                System.out.println("✅ Driver recovered — but session state is lost");
            } catch (Exception e) {
                System.out.println("❌ Driver recovery failed: " + e.getMessage());
            }
            return false; // Don't retry — session state is lost
        }

        // ── Check 3: No popup, driver alive — regular failure ──
        if (attempt < MAX_RETRIES) {
            System.out.println("ℹ️ No popup, driver alive — will retry once more...");
            return true;
        }
        return false;
    }
}
