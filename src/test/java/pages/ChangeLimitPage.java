package pages;

import io.appium.java_client.AppiumDriver;
import utils.CSVUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ChangeLimitPage {

    AppiumDriver driver;
    WebDriverWait wait;

    public ChangeLimitPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // =========================
    // LOCATORS
    // =========================

    By limitManagement = By.xpath("//android.widget.Button[@text='Limit Management']");
    By meezanBymeezan = By.xpath("//android.widget.TextView[@text='Meezan to Meezan']");
    By meezanBy1Link = By.xpath("//android.widget.TextView[@text='Meezan to Other Banks (1-Link)']");
    By meezanByRaast = By.xpath("//android.widget.TextView[@text='Meezan to Other Banks (Raast)']");
    By selectLimit = By.xpath("//android.widget.Button[@text='Select your Limit']");
    By applyBtn = By.xpath("//android.widget.Button[@text='Apply']");
    By increaseBtn = By.xpath("//android.widget.TextView[@text=\"Proceed\"]");
    By decreaseBtn = By.xpath("//android.widget.TextView[@text=\"Close\"]");

    public void clickLimitManagement() {

        By limitBtn = By.xpath("//android.widget.Button[@text='Limit Management']");
        wait.until(ExpectedConditions.elementToBeClickable(limitBtn)).click();
    }

    public void selectLimitType(String type) {

        switch (type.toLowerCase()) {

            case "meezantomeezan":
                wait.until(ExpectedConditions.elementToBeClickable(meezanBymeezan)).click();
                break;

            case "meezanto1link":
                wait.until(ExpectedConditions.elementToBeClickable(meezanBy1Link)).click();
                break;

            case "meezantoraast":
                wait.until(ExpectedConditions.elementToBeClickable(meezanByRaast)).click();
                break;

            default:
                throw new IllegalArgumentException("Invalid limit type: " + type);
        }
    }
    public String formatAmount(String amount) {
        return String.format("%,d", Integer.parseInt(amount));
    }

    public By getAmountLocator(String amount) {
        String formatted = formatAmount(amount);
        return By.xpath("//android.widget.TextView[contains(@text,'PKR " + formatted + "')]");
    }

    public void selectAmount(String amount) {
        wait.until(ExpectedConditions.elementToBeClickable(getAmountLocator(amount))).click();
    }

    public void clickApply() {
        wait.until(ExpectedConditions.elementToBeClickable(applyBtn)).click();
    }
    public void updateLimitsFromCSV(String csvPath) {

        List<Map<String, String>> data = CSVUtils.getAllData(csvPath);

        for (Map<String, String> row : data) {

            String type = row.get("type");
            String amount = row.get("Amount Limit");

            clickLimitManagement();
            selectLimitType(type);
            selectAmount(amount);
            clickApply();
        }
    }

    public boolean clickProceed() {

        try {

            // Try Increase first
            try {
                wait.until(ExpectedConditions.elementToBeClickable(increaseBtn)).click();
                return true;
            } catch (Exception e) {
                System.out.println("Increase button not found, trying decrease...");
            }

            // Try Decrease if Increase fails
            try {
                wait.until(ExpectedConditions.elementToBeClickable(decreaseBtn)).click();
                return true;
            } catch (Exception e) {
                System.out.println("Decrease button also not found");
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }
    
}