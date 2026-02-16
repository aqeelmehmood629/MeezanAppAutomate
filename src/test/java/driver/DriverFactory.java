package driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.util.Set;

public class DriverFactory {

    private static AndroidDriver driver;

    public static AndroidDriver initializeDriver() {
        try {
            // ===== 1️⃣ Pre-launch location check =====
            enableLocationIfDisabled();

            DesiredCapabilities caps = new DesiredCapabilities();

            // ===== BASIC CAPS =====
            caps.setCapability("platformName", "Android");
            caps.setCapability("automationName", "UiAutomator2");
            caps.setCapability("deviceName", "Android Device");
            caps.setCapability("udid", "082653732Q003887");

            // ===== CHROMEDRIVER FOR WEBVIEW =====
            caps.setCapability("chromedriverExecutable", "C:\\Users\\ma\\Documents\\Aqeel QA\\Automation\\chromedriver-win64 (1)\\chromedriver-win64\\chromedriver.exe");

            // ===== STABILITY CAPS =====
            caps.setCapability("noReset", true); // keep login state
            caps.setCapability("newCommandTimeout", 300);
            caps.setCapability("adbExecTimeout", 60000);
            caps.setCapability("uiautomator2ServerInstallTimeout", 60000);
            caps.setCapability("uiautomator2ServerLaunchTimeout", 60000);
            caps.setCapability("ignoreHiddenApiPolicyError", true);
            caps.setCapability("disableWindowAnimation", true);
            caps.setCapability("autoGrantPermissions", true);

            // ===== APP DETAILS =====
            caps.setCapability("appPackage", "invo8.meezan.mb");
            caps.setCapability("appActivity", "com.ofss.digx.mobile.android.SplashActivity");

            caps.setCapability("unlockType", "pin");
            caps.setCapability("unlockKey", "1234");
            caps.setCapability("enforceXPath1", true);


            // ===== INITIALIZE DRIVER =====
            driver = new AndroidDriver(
                    new URL("http://127.0.0.1:4723"),  // Appium v2
                    caps
            );

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // ===== SWITCH TO WEBVIEW IF AVAILABLE =====
            switchToWebViewIfAvailable();

            return driver;

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Appium Driver", e);
        }
    }

    /**
     * Pre-launch method: check location status and enable if OFF
     */
    private static void enableLocationIfDisabled() {
        try {
            // 1️⃣ Check current location mode
            Process checkLocation = Runtime.getRuntime().exec("adb shell settings get secure location_mode");
            checkLocation.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(checkLocation.getInputStream()));
            String currentMode = reader.readLine().trim();

            System.out.println("Current location mode: " + currentMode);

            // 2️⃣ If location OFF (0), enable High Accuracy mode (3)
            if (currentMode.equals("0")) {
                System.out.println("Location is OFF. Enabling location...");
                Process enableLocation = Runtime.getRuntime().exec("adb shell settings put secure location_mode 3");
                enableLocation.waitFor();
                System.out.println("Location enabled successfully!");
            } else {
                System.out.println("Location already ON. No action needed.");
            }

        } catch (Exception e) {
            System.out.println("Error checking/enabling location: " + e.getMessage());
        }
    }

    /**
     * Switch to WEBVIEW context if available with retry
     */
    private static void switchToWebViewIfAvailable() {
        try {
            int attempts = 0;
            while (attempts < 5) { // retry 5 times, 2 seconds interval
                Set<String> contexts = driver.getContextHandles();
                System.out.println("Available contexts: " + contexts);

                for (String context : contexts) {
                    if (context.toLowerCase().contains("webview")) {
                        driver.context(context);
                        System.out.println("Switched to WEBVIEW: " + context);

                        // Optional: wait for login field to appear
                        waitForElement(By.id("login_username"), 5);
                        return;
                    }
                }

                Thread.sleep(2000);
                attempts++;
            }

            System.out.println("No WEBVIEW found, continuing in NATIVE_APP");

        } catch (Exception e) {
            System.out.println("Context switch skipped: " + e.getMessage());
        }
    }

    /**
     * Explicit wait for element presence
     */
    private static void waitForElement(By locator, int timeoutInSeconds) {
        int attempts = 0;
        while (attempts < timeoutInSeconds) {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) {
                    return;
                }
            } catch (Exception ignored) {}
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            attempts++;
        }
    }

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
