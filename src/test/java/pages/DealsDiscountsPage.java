package pages;

import org.openqa.selenium.By;
import driver.DriverFactory;

public class DealsDiscountsPage extends BasePage {

    public DealsDiscountsPage() {
        super(DriverFactory.getDriver());
    }

    // ================= LOCATORS =================

    private By sideMenu =
            By.xpath("//android.widget.Image[@resource-id='hamBurger']");

    private By dealsOption =
            By.xpath("//android.widget.TextView[contains(@text,'Deals')]");
    
    private By backBtn =
            By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[2]/com.horcrux.svg.SvgView/com.horcrux.svg.GroupView/com.horcrux.svg.PathView");
    // ================= ACTIONS =================

    public void clickSideMenu() {
        safeClick(sideMenu, 250);
        System.out.println("✅ Side menu clicked");
    }

    public void clickDeals() {
        safeClick(dealsOption, 250);
        System.out.println("✅ Deals clicked");
    }

    public void clickBackBtn() {
    	safeClick(backBtn, 250);
        System.out.println("✅ Back Button clicked");	
    }
}
