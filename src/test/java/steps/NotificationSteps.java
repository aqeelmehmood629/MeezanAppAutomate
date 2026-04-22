package steps;

import io.cucumber.java.en.*;
import pages.NotificationPage;

public class NotificationSteps {

    NotificationPage notificationPage = new NotificationPage();

    @Given("user is on dashboard screen")
    public void user_is_on_dashboard_screen() {
        // optional validation
    }

    @When("user clicks on sidebar menu")
    public void user_clicks_on_sidebar_menu() {
        notificationPage.openSideMenuNotification();
    }

    @When("user clicks on notification icon")
    public void user_clicks_on_notification_icon() {
        notificationPage.clickNotification();
    }

    @Then("user should be able to view notifications")
    public void user_should_be_able_to_view_notifications() {
        notificationPage.verifyShownotification();
    }
}