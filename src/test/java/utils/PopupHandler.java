package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import driver.DriverFactory;

import java.util.List;

/**
 * 🛡️ PopupHandler — Centralized error popup detection and dismissal.
 *
 * The Meezan Bank app displays error popups with a heading and a Close button.
 * This handler detects, logs, screenshots, and dismisses them automatically.
 *
 * Usage:
 *   PopupHandler.handlePopupIfPresent(driver);  // safe — never throws
 *
 * Locators:
 *   - Error heading:  //android.widget.TextView[@resource-id="errorHeading"]
 *   - Close button:   //android.widget.TextView[@text="Close"]
 */
public class PopupHandler {

    // ── Locators ──────────────────────────────────────────────────────────────
    private static final By ERROR_HEADING = By.xpath(
            "//android.widget.TextView[@resource-id='errorMessage']");

    private static final By CLOSE_BUTTON = By.xpath(
            "//android.widget.Button[@text='Close']");

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Quick, safe check for popup presence (never throws).
     * Switches to NATIVE_APP context to inspect the UI tree.
     */
    public static boolean isPopupPresent(AndroidDriver driver) {
        try {
            if (driver == null || !DriverFactory.isDriverResponsive()) return false;

            // Popups are native elements — must be in NATIVE_APP context
            HybridAppStabilizer.ensureNative(driver);

            List<WebElement> elements = driver.findElements(ERROR_HEADING);
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the error heading text from the popup.
     * Returns a fallback string if the element can't be read.
     */
    public static String getPopupMessage(AndroidDriver driver) {
        try {
            WebElement heading = driver.findElement(ERROR_HEADING);
            String text = heading.getText();
            return (text != null && !text.isEmpty()) ? text : "(empty heading)";
        } catch (Exception e) {
            return "(unable to read popup message)";
        }
    }

    /**
     * 🛡️ MAIN ENTRY POINT — Detect, log, screenshot, and dismiss the popup.
     *
     * Returns:
     *   true  — popup was found and dismissed
     *   false — no popup was present, or handling was skipped
     *
     * This method NEVER throws. Safe to call from hooks, page objects, or anywhere.
     */
    public static boolean handlePopupIfPresent(AndroidDriver driver) {
        try {
            if (driver == null || !DriverFactory.isDriverResponsive()) {
                return false;
            }

            // ── 1. Save current context so we can restore it later ──
            String originalContext = null;
            try {
                originalContext = driver.getContext();
            } catch (Exception ignored) {}

            // ── 2. Switch to NATIVE_APP (popups are native overlays) ──
            HybridAppStabilizer.ensureNative(driver);

            // ── 3. Check for popup existence (non-blocking) ──
            List<WebElement> headings = driver.findElements(ERROR_HEADING);
            if (headings.isEmpty()) {
                restoreContext(driver, originalContext);
                return false; // No popup — silently continue
            }

            // Verify it's actually displayed
            WebElement heading = headings.get(0);
            if (!heading.isDisplayed()) {
                restoreContext(driver, originalContext);
                return false;
            }

            // ── 4. Popup detected! Log the message ──
            String message = "(empty)";
            try {
                message = heading.getText();
            } catch (Exception ignored) {}

            System.out.println("═══════════════════════════════════════════");
            System.out.println("🚨 ERROR POPUP DETECTED");
            System.out.println("📝 Message: " + message);
            System.out.println("═══════════════════════════════════════════");

            // Log to Extent Report
            try {
                ReportManager.log("🚨 Error Popup Detected — Message: " + message);
            } catch (Exception ignored) {}

            // ── 5. Capture screenshot of the popup (before dismissal) ──
            try {
                String screenshotPath = ScreenshotUtil.captureScreenshot(
                        "error_popup_" + System.currentTimeMillis(), "POPUP");
                if (screenshotPath != null) {
                    System.out.println("📸 Popup screenshot saved: " + screenshotPath);
                    try {
                        ReportManager.getTest().addScreenCaptureFromPath(
                                screenshotPath, "Error Popup Screenshot");
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}

            // ── 6. Click the Close button ──
            boolean closed = false;
            try {
                List<WebElement> closeButtons = driver.findElements(CLOSE_BUTTON);
                if (!closeButtons.isEmpty() && closeButtons.get(0).isDisplayed()) {
                    closeButtons.get(0).click();
                    closed = true;
                    System.out.println("✅ Popup closed via Close button");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Close button click failed: " + e.getMessage());
            }

            // Fallback: press Android Back key if Close button didn't work
            if (!closed) {
                try {
                    driver.navigate().back();
                    System.out.println("✅ Popup closed via Android Back key (fallback)");
                    closed = true;
                } catch (Exception e) {
                    System.out.println("⚠️ Back key fallback also failed: " + e.getMessage());
                }
            }

            // ── 7. Brief wait for popup dismiss animation ──
            sleep(500);

            // ── 8. Verify popup is gone ──
            try {
                List<WebElement> remaining = driver.findElements(ERROR_HEADING);
                if (!remaining.isEmpty() && remaining.get(0).isDisplayed()) {
                    System.out.println("⚠️ Popup may still be visible after dismiss attempt");
                } else {
                    System.out.println("✅ Popup confirmed dismissed");
                }
            } catch (Exception ignored) {}

            // ── 9. Restore original context ──
            restoreContext(driver, originalContext);

            return true;

        } catch (Exception e) {
            System.out.println("⚠️ PopupHandler encountered an error (non-fatal): " + e.getMessage());
            return false;
        }
    }

    /**
     * Handles popup with a custom label for logging (e.g., "Before Scenario", "After Failure").
     */
    public static boolean handlePopupIfPresent(AndroidDriver driver, String phase) {
        System.out.println("🔍 Checking for error popup (" + phase + ")...");
        boolean handled = handlePopupIfPresent(driver);
        if (!handled) {
            System.out.println("✅ No popup detected (" + phase + ")");
        }
        return handled;
    }

    // ── Private Helpers ───────────────────────────────────────────────────────

    /**
     * Restores the driver to its original context after popup inspection.
     *
     * ✅ FIX: Uses HybridAppStabilizer.ensureWebView() instead of a raw
     * driver.context(name) call. The WebView handle name (e.g.
     * "WEBVIEW_com.ofss.tx.meezan") can become stale between the time it was
     * captured and the restore attempt. The stabilizer dynamically discovers
     * the live WebView handle — making the restore reliable.
     */
    private static void restoreContext(AndroidDriver driver, String originalContext) {
        if (originalContext != null && !originalContext.equals("NATIVE_APP")) {
            // Original context was a WebView — use the stabilizer for a reliable switch
            HybridAppStabilizer.ensureWebView(driver);
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }
}
