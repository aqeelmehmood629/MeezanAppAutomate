package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import io.appium.java_client.android.AndroidDriver;

public class TermsPage extends BasePage {
	
	public TermsPage(AndroidDriver driver) {
	    super(driver);
	    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}
	
	private By clicksidemenu = By.xpath("//android.widget.Image[@resource-id='hamBurger']");
	
	private By termtextclick = By.xpath("//android.widget.TextView[@text='Terms and Conditions']");
	
	private By checkenglishtab = By.xpath("//android.widget.TextView[@text='English']");
	
	private By logouticon = By.xpath("//android.widget.Image[@content-desc='Logout']");
	
	
	public void clicksideMenu() {
		safeClick(clicksidemenu, 250);
	}
	public void clickTerms() {
		safeClick(termtextclick, 250);
	}
	
	public void checkEnglishTab() {
		safeWait(checkenglishtab, 250);
	}
		
	public void clickLogoutIcon() {
		safeClick(logouticon, 250);
	
	
}
}

	
	