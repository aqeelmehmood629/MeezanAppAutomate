package pages;

import io.appium.java_client.android.AndroidDriver;
import utils.CSVUtils;

import org.openqa.selenium.By;
import java.util.List;
import java.util.Map;

public class ChangeLimitPage extends BasePage {

    public ChangeLimitPage(AndroidDriver driver) {
        super(driver);
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
    By clickHomeIconLimitBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    public void clickLimitManagement() {

        By limitBtn = By.xpath("//android.widget.Button[@text='Limit Management']");
        safeClick(limitBtn);
    }

    public void selectLimitType(String type) {

        switch (type.toLowerCase()) {

            case "meezantomeezan":
                safeClick(meezanBymeezan);
                break;

            case "meezanto1link":
                safeClick(meezanBy1Link);
                break;

            case "meezantoraast":
                safeClick(meezanByRaast);
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
        safeClick(getAmountLocator(amount));
    }

    public void clickApply() {
        safeClick(applyBtn);
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
                safeClick(increaseBtn);
                return true;
            } catch (Exception e) {
                System.out.println("Increase button not found, trying decrease...");
            }

            // Try Decrease if Increase fails
            try {
                safeClick(decreaseBtn);
                return true;
            } catch (Exception e) {
                System.out.println("Decrease button also not found");
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }
    public void clickHomeIconLimit() {
    	safeClick(clickHomeIconLimitBtn);
    	
    }
    
}