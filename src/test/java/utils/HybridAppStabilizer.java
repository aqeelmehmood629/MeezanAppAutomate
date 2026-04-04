package utils;

import io.appium.java_client.android.AndroidDriver;
import java.util.Set;

public class HybridAppStabilizer {

    private static final int MAX_WAIT_SECONDS = 30; // Max wait time for WebView
    private static final int POLL_INTERVAL_MS = 1000; // Check every 1 second

    /**
     * Switch context dynamically depending on current screen
     * Keeps user in WebView if dashboard or screen is WebView
     * Switches to Native if screen is native
     */
    public static void switchContextDynamic(AndroidDriver driver) {
        try {
            String currentContext = driver.getContext();
            Set<String> contexts = driver.getContextHandles();
            boolean switched = false;

            System.out.println("🔹 Current context: " + currentContext);
            System.out.println("🔹 Available contexts: " + contexts);

            // 1️⃣ Prefer WebView if available and currently in Native
            for (String context : contexts) {
                if (context.toLowerCase().contains("webview") && currentContext.equals("NATIVE_APP")) {
                    Thread.sleep(1500); // wait for WebView to load
                    driver.context(context);
                    System.out.println("🌐 Switched to WEBVIEW: " + context);
                    switched = true;
                    break;
                }
            }

            // 2️⃣ Switch to Native if currently in WebView and Native needed
            if (!switched && currentContext.toLowerCase().contains("webview") && contexts.contains("NATIVE_APP")) {
                driver.context("NATIVE_APP");
                System.out.println("📱 Switched to NATIVE_APP");
                switched = true;
            }

            // 3️⃣ Already in correct context → do nothing
            if (!switched) {
                System.out.println("⚡ Context already correct, staying in: " + currentContext);
            }

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed during dynamic context switch", e);
        }
    }

    /**
     * Optional explicit switch to WebView
     */
    public static void switchToWebView(AndroidDriver driver) {
        long startTime = System.currentTimeMillis();
        boolean switched = false;

        while ((System.currentTimeMillis() - startTime) < MAX_WAIT_SECONDS * 1000) {
            try {
                driver.getPageSource(); // refresh contexts
                Set<String> contexts = driver.getContextHandles();

                for (String context : contexts) {
                    if (context.toLowerCase().contains("webview")) {
                        Thread.sleep(1500);
                        driver.context(context);
                        System.out.println("✅ Switched to WEBVIEW: " + context);
                        switched = true;
                        return;
                    }
                }
                Thread.sleep(POLL_INTERVAL_MS);

            } catch (Exception e) {
                System.out.println("Retrying WebView switch...");
            }
        }

        if (!switched) {
            throw new RuntimeException("❌ WebView not found after " + MAX_WAIT_SECONDS + " seconds!");
        }
    }

    /**
     * Optional explicit switch to Native App
     */
    public static void switchToNative(AndroidDriver driver) {
        try {
            driver.context("NATIVE_APP");
            System.out.println("✅ Switched to NATIVE_APP");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to switch to NATIVE_APP", e);
        }
    }

    /**
     * Hide keyboard safely
     */
    public static void hideKeyboard(AndroidDriver driver) {
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
                System.out.println("✅ Keyboard hidden");
            }
        } catch (Exception e) {
            System.out.println("Keyboard not present, skipping...");
        }
    }
}