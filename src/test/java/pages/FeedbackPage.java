package pages;

import driver.DriverFactory;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class FeedbackPage extends BasePage {

    public FeedbackPage() {
        super(DriverFactory.getDriver());
    }

    private By submitBtn = By.xpath("//android.widget.Button[@text=\"Submit\"]");
    private By feedbackTextField = By.xpath("//android.widget.EditText[@resource-id=\"commentBox|input\"]");
    private By feedbackHomeClickBtn = By.xpath("//android.widget.Image[@resource-id='home-icon']");

    public void openFeedback() {
    	By feedbackBtn = By.xpath("//android.widget.Button[@text='Feedback']");
        driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().text(\"Feedback\"))"
        ));

        safeClick(feedbackBtn, 250);

        System.out.println("✅ Feedback button clicked after scroll");
    }

    public void giveRating(String rating) {

        int r = Integer.parseInt(rating);

        By stars = By.xpath("//android.widget.Image[@content-desc='mzn-image']");

        List<WebElement> starList = new WebDriverWait(driver, Duration.ofSeconds(250)).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(stars)
        );

        if (r < 1 || r > starList.size()) {
            throw new IllegalArgumentException("Invalid rating: " + rating);
        }

        starList.get(r - 1).click();

        System.out.println("⭐ Rating selected from CSV: " + rating);
    }

    public void enterFeedbackText(String text) {
        safeSendKeys(feedbackTextField, text, 250);
        System.out.println("📝 Feedback entered: " + text);
    }

    public void clickSubmit() {
        safeClick(submitBtn, 250);
    }
    public void feedbackHomeClick() {
    	
    	safeClick(feedbackHomeClickBtn, 250);
    	
    }
}