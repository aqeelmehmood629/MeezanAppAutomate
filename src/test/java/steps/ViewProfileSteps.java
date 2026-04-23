package steps;

import io.cucumber.java.en.*;
import pages.ViewProfilePage;

public class ViewProfileSteps {

    ViewProfilePage page = new ViewProfilePage();

    @Given("user clicks on side bar menu")
    public void open_sidebar() {
        page.openSideMenu();
    }

    @And("user clicks on My Profile menu")
    public void click_my_profile() {
        page.clickMyProfile();
    }

    @And("My Profile screen should be displayed")
    public void verify_profile_screen() {

        if (!page.isMyProfileDisplayed()) {
            throw new AssertionError("❌ My Profile screen not displayed");
        }

        System.out.println("✅ My Profile screen displayed");
    }
    @Then("user clicks home icon after profile view")
    public void clickHomeIconProfile() {
    	page.clickHomeIconAfterProfile();
    }
}