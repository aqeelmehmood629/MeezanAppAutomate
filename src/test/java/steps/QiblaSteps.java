package steps;

import io.cucumber.java.en.*;
import pages.QiblaPage;

public class QiblaSteps {

    QiblaPage qiblaPage = new QiblaPage();

    @Given("user is on dashboard screen for qibla")
    public void user_is_on_dashboard_screen() {
        // optional validation
    }

    @When("user clicks on side menu for qibla")
    public void user_clicks_on_side_menu() {
        qiblaPage.clickSideMenu();
    }

    @And("user clicks on Qibla option")
    public void user_clicks_on_qibla_option() {
        qiblaPage.clickQibla();
    }

    @And("user should see Qibla screen")
    public void user_should_see_qibla_screen() {
        qiblaPage.verifyQiblaScreen();
    }
    @Then("user click home icon after qibla view")
    public void clickhomeqibla() {
    	qiblaPage.clickHomeIconQibla();
    }
}