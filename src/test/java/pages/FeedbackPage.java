package pages;

import driver.DriverFactory;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class FeedbackPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public FeedbackPage() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(250));
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

        wait.until(ExpectedConditions.elementToBeClickable(feedbackBtn)).click();

        System.out.println("✅ Feedback button clicked after scroll");
    }

    public void giveRating(String rating) {

        int r = Integer.parseInt(rating);

        By stars = By.xpath("//android.widget.Image[@content-desc='mzn-image']");

        List<WebElement> starList = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(stars)
        );

        if (r < 1 || r > starList.size()) {
            throw new IllegalArgumentException("Invalid rating: " + rating);
        }

        starList.get(r - 1).click();

        System.out.println("⭐ Rating selected from CSV: " + rating);
    }

    public void enterFeedbackText(String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(feedbackTextField));
        el.click();
        el.clear();
        el.sendKeys(text);
        System.out.println("📝 Feedback entered: " + text);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
    }
    public void feedbackHomeClick() {
    	
    	wait.until(ExpectedConditions.elementToBeClickable(feedbackHomeClickBtn)).click();
    	
    }
}