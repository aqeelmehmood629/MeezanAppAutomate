package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Set;

public class HybridAppStabilizer {

    private static final int MAX_WAIT = 30;   // 30 seconds max (reduced to allow faster retries)
    private static final int POLL = 1000;      // 1 second poll interval

    /**
     * 🚀 MAIN ENTRY METHOD
     * App launch → context detect → login screen ready
     * Only call this when app is freshly launched (not logged in)
     */
    public static void stabilizeApp(AndroidDriver driver) {

        System.out.println("🚀 App stabilization started...");

        int maxRetries = 5; // Retries up to 5 times
        boolean webViewFound = false;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            if (attempt > 1) {
                System.out.println("🔄 Relaunching app (Attempt " + attempt + " of " + maxRetries + ")...");
                try {
                    driver.terminateApp("com.ofss.tx.meezan");
                    sleep(2000);
                    driver.activateApp("com.ofss.tx.meezan");
                    sleep(3000); // Give the app a moment to launch
                } catch (Exception e) {
                    System.out.println("⚠️ App relaunch failed: " + e.getMessage());
                }
            }

            // 👆 Click the app logo on the splash screen to trigger WebView loading
            clickSplashLogo(driver);

            waitForAnyContext(driver);

            webViewFound = switchToWebViewIfAvailable(driver);

            if (webViewFound) {
                System.out.println("✅ WebView detected and switched successfully.");
                break; // Break out of the retry loop
            } else {
                System.out.println("❌ WebView not detected in attempt " + attempt + ".");
            }
        }

        if (!webViewFound) {
            System.out.println("🚨 CRITICAL: WebView could not be loaded after " + maxRetries + " attempts!");
        }

        // ✅ Changed: don't throw if login screen not found
        boolean loginReady = waitForLoginScreenSafe(driver);

        if (loginReady) {
            System.out.println("✅ App is stable — login screen ready");
        } else {
            System.out.println("⚠️ Login screen not found — app may already be logged in");
        }
    }

    /**
     * 👆 Click app logo on splash screen to trigger WebView initialization
     */
    public static void clickSplashLogo(AndroidDriver driver) {
        System.out.println("👆 Attempting to click app logo on splash screen...");
        try {
            // First ensure we are in native context
            ensureNative(driver);
            
            // Give the splash screen a moment to render
            sleep(3000);

            // Attempt to click the logo (ImageView)
            WebElement logo = driver.findElement(By.className("android.widget.ImageView"));
            logo.click();
            System.out.println("✅ App logo clicked successfully. WebView should start loading now.");
            
            // Wait briefly for the WebView context to be generated
            sleep(2000);
        } catch (Exception e) {
            System.out.println("⚠️ App logo not clicked (might already be dismissed or not found).");
        }
    }

    /**
     * ⏳ Wait until app exposes contexts
     */
    public static void waitForAnyContext(AndroidDriver driver) {

        for (int i = 0; i < MAX_WAIT; i++) {

            Set<String> contexts = driver.getContextHandles();

            if (contexts != null && !contexts.isEmpty()) {
                System.out.println("📌 Contexts detected: " + contexts);
                return;
            }

            sleep(POLL);
        }

        System.out.println("⚠️ No contexts detected (rare case)");
    }

    /**
     * 🌐 Switch to WebView if available
     */
    public static boolean switchToWebViewIfAvailable(AndroidDriver driver) {

        for (int i = 0; i < MAX_WAIT; i++) {

            Set<String> contexts = driver.getContextHandles();

            for (String context : contexts) {

                if (context.toLowerCase().contains("webview")) {

                    try {
                        driver.context(context);
                        sleep(1500);

                        System.out.println("🌐 Switched to WebView: " + context);
                        return true;

                    } catch (Exception e) {
                        System.out.println("⚠️ WebView found but not ready yet");
                    }
                }
            }

            sleep(POLL);
        }

        System.out.println("📱 WebView not found → staying in NATIVE_APP");
        driver.context("NATIVE_APP");
        return false;
    }

    /**
     * 🔐 Wait for login screen — SAFE version (returns boolean, never throws)
     */
    public static boolean waitForLoginScreenSafe(AndroidDriver driver) {

        System.out.println("⏳ Waiting for login screen...");

        for (int i = 0; i < 30; i++) {  // 30 seconds max for login screen

            try {
                WebElement login = driver.findElement(
                        By.id("login_username")
                );

                if (login.isDisplayed()) {
                    System.out.println("🔐 Login screen found");
                    return true;
                }

            } catch (Exception ignored) {}

            sleep(POLL);
        }

        System.out.println("⚠️ Login screen not found within timeout");
        return false;
    }

    /**
     * 🔐 Original waitForLoginScreen (kept for backward compat, now uses safe version)
     */
    public static void waitForLoginScreen(AndroidDriver driver) {
        if (!waitForLoginScreenSafe(driver)) {
            System.out.println("⚠️ Login screen not found — continuing anyway");
        }
    }

    /**
     * 🏠 Wait for dashboard to be visible (used after login)
     */
    public static boolean waitForDashboard(AndroidDriver driver) {

        System.out.println("⏳ Waiting for dashboard...");

        // Make sure we're in NATIVE context for dashboard elements
        switchToNative(driver);

        By homeIcon = By.xpath("//android.widget.TextView[@text='SHOW BALANCE']");

        for (int i = 0; i < MAX_WAIT; i++) {

            try {
                WebElement home = driver.findElement(homeIcon);

                if (home.isDisplayed()) {
                    System.out.println("🏠 Dashboard is visible");
                    return true;
                }

            } catch (Exception ignored) {}

            sleep(POLL);
        }

        System.out.println("⚠️ Dashboard not visible after timeout");
        return false;
    }

    /**
     * 🌐 Ensure we are in WebView context (safe — handles already-in-WebView)
     */
    public static void ensureWebView(AndroidDriver driver) {
        try {
            String current = driver.getContext();
            if (current != null && current.toLowerCase().contains("webview")) {
                System.out.println("🌐 Already in WebView");
                return;
            }

            // Try to switch to WebView
            Set<String> contexts = driver.getContextHandles();
            for (String context : contexts) {
                if (context.toLowerCase().contains("webview")) {
                    driver.context(context);
                    sleep(500);
                    System.out.println("🌐 Switched to WebView: " + context);
                    return;
                }
            }

            System.out.println("⚠️ No WebView available");
        } catch (Exception e) {
            System.out.println("⚠️ ensureWebView failed: " + e.getMessage());
        }
    }

    /**
     * 📱 Ensure we are in Native context (safe — handles already-in-Native)
     */
    public static void ensureNative(AndroidDriver driver) {
        try {
            String current = driver.getContext();
            if ("NATIVE_APP".equals(current)) {
                return; // already native
            }
            driver.context("NATIVE_APP");
            System.out.println("📱 Switched to NATIVE_APP");
        } catch (Exception e) {
            System.out.println("⚠️ ensureNative failed: " + e.getMessage());
        }
    }

    /**
     * 🔄 Safe context printer (debug helper)
     */
    public static void printContext(AndroidDriver driver) {
        System.out.println("📌 Current: " + driver.getContext());
        System.out.println("📌 All Contexts: " + driver.getContextHandles());
    }

    /**
     * 🔁 Switch back to native
     */
    public static void switchToNative(AndroidDriver driver) {
        try {
            driver.context("NATIVE_APP");
            System.out.println("📱 Switched to NATIVE_APP");
        } catch (Exception e) {
            System.out.println("⚠️ Native switch failed");
        }
    }

    /**
     * ⌨️ Hide keyboard safely
     */
    public static void hideKeyboard(AndroidDriver driver) {
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
                System.out.println("⌨️ Keyboard hidden");
            }
        } catch (Exception ignored) {}
    }

    /**
     * 💤 Sleep helper
     */
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {}
    }
}