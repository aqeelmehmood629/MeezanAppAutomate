package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import driver.DriverFactory;

import java.util.List;

/**
 * 🔍 ScreenDetector — Detects the current app screen.
 *
 * Used by Hooks to make smart, screen-aware navigation decisions:
 *   - If already on Dashboard → skip navigation entirely
 *   - If on Login screen → skip dashboard navigation
 *   - If on unknown screen → navigate to Dashboard
 *
 * ── HYBRID APP CONTEXT DESIGN ───────────────────────────────────────────────
 * This app is a hybrid (NATIVE_APP + WEBVIEW). Screen indicators belong to
 * different contexts:
 *
 *   Dashboard indicators  → NATIVE_APP elements (SHOW BALANCE, PKR text)
 *   Login indicator       → WEBVIEW element    (login_username field)
 *
 * Detection flow:
 *   1. Save original context
 *   2. Switch to NATIVE_APP → check Dashboard indicators
 *   3. If not Dashboard → switch to WEBVIEW → check Login indicator
 *   4. Restore original context
 *   5. Return detected screen (DASHBOARD / LOGIN / UNKNOWN)
 *
 * ── PERFORMANCE ─────────────────────────────────────────────────────────────
 * Uses driver.findElements() (non-blocking, never throws NoSuchElement).
 * No WebDriverWait — detection completes in < 1 second in normal conditions.
 *
 * ── EXTENSIBILITY ───────────────────────────────────────────────────────────
 * To add a new screen:
 *   1. Add a value to the AppScreen enum
 *   2. Add its indicator locator as a private static final By
 *   3. Add detection logic inside detectCurrentScreen() with the correct context
 */
public class ScreenDetector {

    /**
     * Enum of detectable app screens.
     * Add new values here as new modules are added to the app.
     */
    public enum AppScreen {
        DASHBOARD,  // Main dashboard — NATIVE: "SHOW BALANCE" or "PKR" text visible
        LOGIN,      // Login screen  — WEBVIEW: login_username field visible
        UNKNOWN     // Could not determine — sub-page, transition, or detection failed
    }

    // ── Dashboard indicators — NATIVE_APP elements ───────────────────────────
    // ⚠️ CRITICAL: Indicators must be UNIQUE to the Dashboard.
    // Generic elements like "PKR" or "home-icon" often appear on sub-screens
    // (like Funds Transfer or bottom nav bars) causing false positives.
    private static final By DASHBOARD_INDICATOR_1 = By.xpath(
            "//android.widget.TextView[@text='SHOW BALANCE']");
    
    // Additional unique dashboard element if 'SHOW BALANCE' is hidden after tap
    private static final By DASHBOARD_INDICATOR_2 = By.xpath(
            "//android.widget.TextView[@text='My Accounts']");

    // ── Login indicator — WEBVIEW element ────────────────────────────────────
    // The login form lives inside a WebView — must be in WebView context to find it.
    private static final By LOGIN_INDICATOR = By.id("login_username");

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Detects which screen the app is currently showing.
     *
     * Context-safe: saves the driver's current context and restores it after detection.
     * Never throws: returns UNKNOWN on any failure.
     * Fast: uses findElements() with no explicit wait.
     */
    public static AppScreen detectCurrentScreen(AndroidDriver driver) {
        try {
            if (driver == null || !DriverFactory.isDriverResponsive()) {
                return AppScreen.UNKNOWN;
            }

            // ── Save original context for restoration at the end ──
            String originalContext = null;
            try {
                originalContext = driver.getContext();
            } catch (Exception ignored) {}

            // ── Step 1: Check NATIVE_APP for Dashboard indicators ──────────────
            // Dashboard elements are NATIVE elements.
            HybridAppStabilizer.ensureNative(driver);

            if (isElementPresent(driver, DASHBOARD_INDICATOR_1)) {
                System.out.println("📍 Screen detected: DASHBOARD (Matched indicator: SHOW BALANCE)");
                restoreContext(driver, originalContext);
                return AppScreen.DASHBOARD;
            }
            if (isElementPresent(driver, DASHBOARD_INDICATOR_2)) {
                System.out.println("📍 Screen detected: DASHBOARD (Matched indicator: My Accounts)");
                restoreContext(driver, originalContext);
                return AppScreen.DASHBOARD;
            }

            // ── Step 2: Check WEBVIEW for Login indicator ──────────────────────
            // The login form (login_username) is inside a WebView.
            try {
                HybridAppStabilizer.ensureWebView(driver);
                if (isElementPresent(driver, LOGIN_INDICATOR)) {
                    System.out.println("📍 Screen detected: LOGIN (Matched indicator: login_username)");
                    restoreContext(driver, originalContext);
                    return AppScreen.LOGIN;
                }
            } catch (Exception ignored) {
                // WebView not available at this moment — not on Login screen
            }

            // ── Step 3: Nothing matched ────────────────────────────────────────
            System.out.println("📍 Screen detected: UNKNOWN");
            restoreContext(driver, originalContext);
            return AppScreen.UNKNOWN;

        } catch (Exception e) {
            System.out.println("⚠️ Screen detection failed: " + e.getMessage());
            return AppScreen.UNKNOWN;
        }
    }

    /**
     * Convenience: returns true if the app is currently on the Dashboard.
     */
    public static boolean isDashboard(AndroidDriver driver) {
        return detectCurrentScreen(driver) == AppScreen.DASHBOARD;
    }

    /**
     * Convenience: returns true if the app is currently on the Login screen.
     */
    public static boolean isLoginScreen(AndroidDriver driver) {
        return detectCurrentScreen(driver) == AppScreen.LOGIN;
    }

    // ── Private Helpers ──────────────────────────────────────────────────────

    /**
     * Fast, non-blocking element presence check.
     * Uses findElements() — returns false instead of throwing NoSuchElementException.
     */
    private static boolean isElementPresent(AndroidDriver driver, By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Restores the driver to its original context after detection.
     *
     * ✅ Uses HybridAppStabilizer instead of raw driver.context(name).
     * A hardcoded WebView handle name (e.g. "WEBVIEW_com.ofss.tx.meezan")
     * can become stale. The stabilizer dynamically discovers the live handle.
     */
    private static void restoreContext(AndroidDriver driver, String originalContext) {
        if (originalContext == null) return;
        try {
            if (originalContext.toLowerCase().contains("webview")) {
                HybridAppStabilizer.ensureWebView(driver);
            } else {
                // Was NATIVE_APP — ensure we're back in native
                HybridAppStabilizer.ensureNative(driver);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not restore context after screen detection: " + e.getMessage());
        }
    }
}
