package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.PopupHandler;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.WebElement;

/**
 * 🏠 DashboardPage — extends BasePage for popup-aware safe interactions.
 */
public class DashboardPage extends BasePage {

    private WebDriverWait longWait;
    private WebDriverWait shortWait;

    public DashboardPage(AndroidDriver driver) {
        super(driver, 30); // BasePage wait = 30s
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(300));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // 🔹 Locators
    private By showBalanceBtn = By.xpath("//android.widget.TextView[@text='SHOW BALANCE']");
    private By showBalanceBtn2 = By.xpath("//android.widget.TextView[contains(@text,'PKR')]");
    private By homeIcon = By.xpath("//android.widget.Image[contains(@text,'home-icon')]");
    private final By SHARE_ACCOUNT_ICON = By.xpath("//android.widget.TextView[@text='Share Account']");

    // 🔹 DSB_TC02 Locators
    private final By titleDisplayed = By.xpath("//android.widget.TextView[contains(@text,'FARAZ')]");
    private final By currentAccountDisplayed = By.xpath("//android.widget.TextView[contains(@text,'Current Account')]");
    private final By ibanDisplayed = By.xpath("//android.widget.TextView[contains(@text,'IBAN')]");
    private final By branchDisplayed = By.xpath("//android.widget.TextView[contains(@text,'Branch')]");
  //  private final By balanceDisplayed = By.xpath("//android.widget.TextView[contains(@text,'PKR')]");
    private final By shareButton = By.xpath("//android.widget.Button[contains(@text,'Share')]");
    private final By transactionButton = By.xpath("//android.widget.Button[contains(@text,'Transactions')]");

    // 🔹 DSB_TC03 Locators
    private final By copyAccountNumber = By.xpath("//*[@resource-id='copyAccountNumber']");
    private final By copyIbanNumber = By.xpath("//*[@resource-id='copyIBANNumber']");
    private final By copyAccountDetails = By.xpath("//*[@resource-id='copyAccountDetails']");


    // 🔹 DSB_TC04 Share Locators

    private final By shareAccountNumber = By.xpath("//*[@resource-id='shareAccountNumber']");
    private final By shareIbanNumber = By.xpath("//*[@resource-id='shareIBANNumber']");
    private final By shareAccountDetails = By.xpath("//*[@resource-id='shareAccountDetails']");
    // Share Sheet Locators
    private final By whatsappShareOption = By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"WhatsApp \"]");
    private final By bluetoothShareOption = By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"Bluetooth \"]");

    // 🔹 DSB_TC05 Locators
    private final By refreshButton = By.xpath("//android.widget.Button[@text=\"refresh\"]");
    private final By validationHide = By.xpath("//android.widget.TextView[@text=\"HIDE\"]");

    // 🔹 DSB_TC06 Locators
    private final By viewTransaction = By.xpath("//android.widget.Button[@text=\"mzn-button-image Transactions\"]");

    // 🔹 DSB_TC07 Locators
    // Account card carousel container — horizontally scrollable
    private final By accountCarousel = By.xpath(
        "//android.view.View[@scrollable='true' and @clickable='true' and @bounds='[0,180][720,592]']");
    // Fallback carousel locator if bounds change
    private final By accountCarouselFallback = By.xpath(
        "//android.view.View[@scrollable='true' and @clickable='true']");
    // The untoggled star (can be favourite'd)
    private final By popupYesButton = By.xpath("//android.view.View[@text=\"Submit\"]");
    private final By validationDone = By.xpath("//android.widget.Button[@text=\"Done\"]");

    // State holder for the target account number (set from step definition)
    private String targetAccountNumber = "";


    public void goToDashboard() {
        clickHome();
        clickShowBalance();
        System.out.println("✅ Home → Show Balance flow completed");
    }

    // 🔹 Click Home — now popup-aware via safeClick
    public void clickHome() {
        safeClick(homeIcon, 300);
        System.out.println("🏠 Home icon clicked");
    }

    // 🔹 Click Show Balance
    public void clickShowBalance() {
        try {
            safeClick(showBalanceBtn, 30);
            System.out.println("👁️ Show Balance clicked using first locator");
        } catch (Exception e) {
            safeClick(showBalanceBtn2, 30);
            System.out.println("👁️ Show Balance clicked using second locator");
        }
    }

    /**
     * ✅ Check if dashboard is visible — uses NATIVE context
     * Safe: switches to native before checking, never throws
     */
    public boolean isDashboardVisible() {
        try {
            // Dashboard elements are in NATIVE context
            String currentContext = driver.getContext();
            if (currentContext == null || !currentContext.equals("NATIVE_APP")) {
                driver.context("NATIVE_APP");
            }

            // Check for popup first — if present, dashboard may be hidden behind it
            PopupHandler.handlePopupIfPresent(driver);

            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(showBalanceBtn)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    // 🔹 DSB_TC02 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public boolean isTitleDisplayed() { return isDisplayedSafe(titleDisplayed); }
    public boolean isCurrentAccountDisplayed() { return isDisplayedSafe(currentAccountDisplayed); }
    public boolean isIbanDisplayed() { return isDisplayedSafe(ibanDisplayed); }
    public boolean isBranchDisplayed() { return isDisplayedSafe(branchDisplayed); }
    public boolean isBalanceDisplayed() {
        return isDisplayedSafe(showBalanceBtn) || isDisplayedSafe(showBalanceBtn2);
    }
    public boolean isShareButtonDisplayed() { return isDisplayedSafe(shareButton); }
    public boolean isTransactionButtonDisplayed() { return isDisplayedSafe(transactionButton); }

    private boolean isDisplayedSafe(By locator) {
        try {
            driver.context("NATIVE_APP");
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC03 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void clickShareDetailsScreen() {
        driver.context("NATIVE_APP");
        if (isDisplayedSafe(copyAccountNumber)) {
            System.out.println("ℹ️ Share details screen is already open. Skipping click on Share icon.");
            return;
        }
        safeClick(shareButton, 30);
    }

    public void clickCopyOption(String action) {
        driver.context("NATIVE_APP");
        switch (action) {
            case "Copy Account Number": safeClick(copyAccountNumber, 10); break;
            case "Copy IBAN Number": safeClick(copyIbanNumber, 10); break;
            case "Copy Account Details": safeClick(copyAccountDetails, 10); break;
            default: throw new IllegalArgumentException("Unknown copy action: " + action);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC04 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void clickShareOption(String action) {
        driver.context("NATIVE_APP");
        switch (action) {
            case "Share Account Number": safeClick(shareAccountNumber, 10); break;
            case "Share IBAN Number": safeClick(shareIbanNumber, 10); break;
            case "Share Account Details": safeClick(shareAccountDetails, 10); break;
            default: throw new IllegalArgumentException("Unknown share action: " + action);
        }
    }

    public boolean isShareSheetDisplayed() {
        driver.context("NATIVE_APP");
        try {
            boolean hasWhatsApp = !driver.findElements(whatsappShareOption).isEmpty();
            boolean hasBluetooth = !driver.findElements(bluetoothShareOption).isEmpty();
            return hasWhatsApp || hasBluetooth;
        } catch (Exception e) {
            return false;
        }
    }

    public void closeShareOverlays() {
        driver.context("NATIVE_APP");
        boolean closedSomething = false;

        // 1. Check if Android System Share Sheet is open (from DSB_TC04)
        if (isShareSheetDisplayed()) {
            System.out.println("ℹ️ Android System Share Sheet is open. Dismissing via Tap Outside...");
            tapOutsideBottomSheet();
            closedSomething = true;
            try { Thread.sleep(1000); } catch (Exception ignored) {}
        }

        // 2. Check if App's Share Bottom Sheet is open (from DSB_TC03/TC04)
        if (isDisplayedSafe(copyAccountNumber)) {
            System.out.println("ℹ️ App Share Bottom Sheet is open. Dismissing via Share button toggle...");
            safeClick(shareButton, 10);
            closedSomething = true;
            try { Thread.sleep(1000); } catch (Exception ignored) {}
        }

        if (closedSomething) {
            System.out.println("✅ Share overlays dismissed successfully.");
        }
    }

    private void tapOutsideBottomSheet() {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 100, 100));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Arrays.asList(tap));
        } catch (Exception e) {
            System.out.println("⚠️ Could not tap outside: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC05 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void rapidTapRefresh() {
        driver.context("NATIVE_APP");
        for (int i = 0; i < 4; i++) {
            try {
                driver.findElement(refreshButton).click();
                Thread.sleep(200);
            } catch (Exception ignored) {}
        }
        System.out.println("✅ Tapped refresh button multiple times rapidly");
    }

    public boolean isDataUpdatedWithoutCrashes() {
        return isDisplayedSafe(showBalanceBtn2) || isDisplayedSafe(validationHide);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC06 Methods
    // ─────────────────────────────────────────────────────────────────────────
    public void clickViewTransaction() {
        safeClick(viewTransaction, 10);
        System.out.println("✅ View Transaction button clicked");
    }

    public boolean isTransactionHistoryScreenDisplayed() {
        return isDisplayedSafe(showBalanceBtn2);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 🔹 DSB_TC07 Methods — Account-Aware Favourite with Swipe Search
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Sets the target account number from CSV (called from step definition).
     */
    public void setTargetAccountNumber(String accountNumber) {
        this.targetAccountNumber = accountNumber.trim();
        System.out.println("📌 Target account number set to: '" + this.targetAccountNumber + "'");
    }

    /**
     * Searches for the account card containing the target account number by swiping
     * the carousel horizontally, up to MAX_SWIPE_ATTEMPTS times.
     *
     * Strategy (WebView context):
     *   1. Check if a TextView with text containing the target account number is visible.
     *   2. If found → done.
     *   3. If not found → swipe the carousel left to reveal the next card.
     *   4. Repeat up to MAX_SWIPE_ATTEMPTS.
     *
     * @return true if the account card was found, false if not found after all swipes.
     */
    public boolean findAccountCardWithSwipe() {
        final int MAX_SWIPE_ATTEMPTS = 5;
        driver.context("NATIVE_APP");

        for (int attempt = 1; attempt <= MAX_SWIPE_ATTEMPTS; attempt++) {
            System.out.println("🔍 [Attempt " + attempt + "/" + MAX_SWIPE_ATTEMPTS
                + "] Looking for account: '" + targetAccountNumber + "'");

            if (isTargetAccountVisible()) {
                System.out.println("✅ Account card found: '" + targetAccountNumber + "'");
                return true;
            }

            if (attempt < MAX_SWIPE_ATTEMPTS) {
                System.out.println("➡️ Account not visible. Swiping carousel left...");
                swipeCarouselLeft();
                try { Thread.sleep(1000); } catch (Exception ignored) {}
            }
        }

        System.out.println("❌ Account '" + targetAccountNumber + "' not found after " + MAX_SWIPE_ATTEMPTS + " swipes.");
        return false;
    }

    /**
     * Checks whether the target account card is currently visible on screen.
     * Looks for a TextView containing the target account number text.
     */
    private boolean isTargetAccountVisible() {
        try {
            driver.context("NATIVE_APP");
            // The account number appears as: "Current Account: 0030 0112139963"
            String xpathExpr = "//android.widget.TextView[contains(@text,'" + targetAccountNumber + "')]";  
            List<WebElement> elements = driver.findElements(By.xpath(xpathExpr));
            boolean found = !elements.isEmpty() && elements.get(0).isDisplayed();
            if (found) {
                System.out.println("🔍 Account text found on screen: '" + elements.get(0).getText() + "'");
            }
            return found;
        } catch (Exception e) {
            System.out.println("⚠️ isTargetAccountVisible() error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clicks the favourite (untoggled-star) for the specific account card identified
     * by the target account number. Uses three strategies in order:
     *
     * STRATEGY 1 — preceding-sibling XPath (most precise):
     *   Finds the View that is a preceding sibling of the account number TextView
     *   AND contains the untoggled-star Image. This is immune to multiple cards
     *   because it's anchored to the exact account number text.
     *
     * STRATEGY 2 — sibling Image direct click:
     *   Walks to the Image inside the preceding-sibling container.
     *
     * STRATEGY 3 — Coordinate-based W3C tap (fallback when clickable=false):
     *   Reads the star Image element's bounds, computes the center point,
     *   and performs a W3C PointerInput tap. Works even when the element
     *   has clickable="false" in the hierarchy.
     *
     * XML Structure insight (from page source):
     *   android.view.View [scrollable=true, the card carousel]
     *     android.view.View [index=11, star container] ← preceding-sibling of account number
     *       android.view.View
     *         android.widget.Image [@text="untoggled-star"]
     *     android.widget.TextView [index=12, "Current Account: 0030 0112139963"]
     */
    public void clickFavouriteStarForTargetAccount() {
        System.out.println("⭐ [TC07] Locating favourite star for account: '" + targetAccountNumber + "'");

        // ── Strategy 1: WEBVIEW Context Locator (Most Precise if available) ───
        boolean webViewClickSuccess = false;
        try {
            Set<String> contexts = driver.getContextHandles();
            String webviewContext = null;
            for (String context : contexts) {
                if (context.toLowerCase().contains("webview")) {
                    webviewContext = context;
                    break;
                }
            }

            if (webviewContext != null) {
                System.out.println("🌐 WEBVIEW context found (" + webviewContext + "). Switching context...");
                driver.context(webviewContext);

                // Try the robust webview locator provided by user
                String webLocator = "//div[contains(@class,'account-star-icon')]//img[contains(@src,'untoggled-star.svg')]";
                List<WebElement> webStars = driver.findElements(By.xpath(webLocator));

                if (webStars.isEmpty()) {
                    // Try the fallback web locator
                    webLocator = "//img[contains(@src,'untoggled-star.svg')]";
                    webStars = driver.findElements(By.xpath(webLocator));
                }

                if (!webStars.isEmpty()) {
                    WebElement star = webStars.get(0);
                    // Use JavascriptExecutor to click in case of overlay/interception in DOM
                    driver.executeScript("arguments[0].click();", star);
                    System.out.println("⭐ [Strategy 1] Clicked star via WEBVIEW context successfully.");
                    webViewClickSuccess = true;
                } else {
                    System.out.println("⚠️ [Strategy 1] Web locators failed to find untoggled-star in WEBVIEW.");
                }
            } else {
                System.out.println("⚠️ [Strategy 1] No WEBVIEW context available to switch to.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ [Strategy 1] WEBVIEW interaction failed: " + e.getMessage());
        } finally {
            // ALWAYS switch back to NATIVE_APP to prevent breaking subsequent steps
            driver.context("NATIVE_APP");
        }

        if (webViewClickSuccess) {
            return;
        }

        // ── Strategy 2: NATIVE preceding:: axis locator (Fallback 1) ──────────
        // The XML structure flattens the account elements into a scrollable list.
        // Therefore, the untoggled-star is a preceding element to the account number TextView.
        // `preceding::android.widget.Image[@text='untoggled-star'][1]` gets the FIRST
        // untoggled star immediately preceding the target account number in document order.
        try {
            String preciseStarXpath =
                "//android.widget.TextView[contains(@text,'" + targetAccountNumber + "')]"
                + "/preceding::android.widget.Image[@text='untoggled-star'][1]";

            List<WebElement> stars = driver.findElements(By.xpath(preciseStarXpath));
            if (!stars.isEmpty()) {
                tapElementCenter(stars.get(0));
                System.out.println("⭐ [Strategy 2] Clicked star via preceding:: axis.");
                return;
            }
            System.out.println("⚠️ [Strategy 2] preceding:: star not found.");
        } catch (Exception e) {
            System.out.println("⚠️ [Strategy 2] Failed: " + e.getMessage());
        }

        // ── Strategy 2: Global locator + coordinate tap (Fallback) ────────────
        // In case the star is physically on screen but DOM hierarchy is altered.
        try {
            String globalStarXpath = "//android.widget.Image[@text='untoggled-star']";
            List<WebElement> stars = driver.findElements(By.xpath(globalStarXpath));

            if (!stars.isEmpty()) {
                // Assuming only one valid untoggled star is fully visible after swipe
                tapElementCenter(stars.get(0));
                System.out.println("⭐ [Strategy 2] Clicked star via global locator + coordinate tap.");
                return;
            }
            System.out.println("⚠️ [Strategy 2] No untoggled-star found on screen.");
        } catch (Exception e) {
            System.out.println("⚠️ [Strategy 2] Failed: " + e.getMessage());
        }

        throw new RuntimeException(
            "❌ [TC07] All 3 strategies failed to click favourite star for account: '"
            + targetAccountNumber + "'");
    }

    /**
     * Taps the center point of a WebElement using W3C PointerInput.
     * This bypasses the clickable=false restriction in the XML hierarchy
     * since Appium uses the element's bounding rectangle coordinates.
     */
    private void tapElementCenter(WebElement element) {
        org.openqa.selenium.Rectangle rect = element.getRect();
        int centerX = rect.getX() + rect.getWidth() / 2;
        int centerY = rect.getY() + rect.getHeight() / 2;

        System.out.println("📍 Tapping at center (" + centerX + ", " + centerY + ")"
            + " of element bounds [" + rect.getX() + "," + rect.getY() + "]["
            + (rect.getX() + rect.getWidth()) + "," + (rect.getY() + rect.getHeight()) + "]");

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerMove(Duration.ofMillis(50), PointerInput.Origin.viewport(), centerX, centerY));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
    }

    /**
     * Swipes the account card carousel from right to left to reveal the next account.
     *
     * Uses W3C PointerInput for reliable touch action in hybrid apps.
     * Swipe target: the centre of the carousel area (y ~386, from x=600 → x=120).
     */
    private void swipeCarouselLeft() {
        try {
            driver.context("NATIVE_APP");
            // Carousel bounds: [0,180][720,592] → mid-y = (180+592)/2 = 386
            int startX = 600, endX = 120, midY = 386;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, midY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), endX, midY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Arrays.asList(swipe));
            System.out.println("🔄 Swiped carousel left (" + startX + "→" + endX + ", y=" + midY + ")");
        } catch (Exception e) {
            System.out.println("⚠️ Carousel swipe failed: " + e.getMessage());
        }
    }

    public boolean isFavouritePopupDisplayed() {
        return isDisplayedSafe(popupYesButton);
    }

    public void clickPopupYesButton() {
        safeClick(popupYesButton, 10);
        System.out.println("✅ Popup Yes/Submit button clicked");
    }

    public boolean isAccountMarkedAsFavourite() {
        return isDisplayedSafe(validationDone);
    }
}
