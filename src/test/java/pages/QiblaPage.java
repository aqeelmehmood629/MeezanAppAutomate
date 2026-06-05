package pages;

import org.openqa.selenium.By;
import driver.DriverFactory;

public class QiblaPage extends BasePage {

    public QiblaPage() {
        super(DriverFactory.getDriver(), 250);
    }

    // ================= LOCATORS =================

    private By sideMenu =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By qiblaOption =
            By.xpath("//android.widget.TextView[@text='Qibla']");

    private By qiblaScreen =
            By.xpath("//android.widget.TextView[@resource-id=\"comp-head\"]");
    private By clickHomeIconQiblaBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    // ================= ACTIONS =================

    public void clickSideMenu() {
        safeClick(sideMenu, 250);
        System.out.println("✅ Side menu clicked");
    }

    public void clickQibla() {
        safeClick(qiblaOption, 250);
        System.out.println("✅ Qibla clicked");
    }

    public void verifyQiblaScreen() {
        safeWait(qiblaScreen, 250);
        System.out.println("✅ Qibla screen displayed");
    }
    public void clickHomeIconQibla() {
    	safeWait(clickHomeIconQiblaBtn, 250);
    	
    }
}