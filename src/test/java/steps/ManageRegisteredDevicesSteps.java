package steps;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ManageRegisteredDevicesPage;
import utils.HybridAppStabilizer;

public class ManageRegisteredDevicesSteps {
    private AndroidDriver driver;
    private ManageRegisteredDevicesPage manageDevicesPage;

    private void init() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }
        if (manageDevicesPage == null && driver != null) {
            manageDevicesPage = new ManageRegisteredDevicesPage(driver);
        }
    }

    @When("user clicks on Side Menu for remove device")
    public void user_clicks_on_side_menu() {
        init();
        manageDevicesPage.clickSideMenu();
    }

    @And("user clicks on Setting for remove device")
    public void user_clicks_on_setting() {
        init();
        manageDevicesPage.clickSetting();
    }

    @And("user clicks on Manage Registered Devices")
    public void user_clicks_on_manage_registered_devices() {
        init();
        manageDevicesPage.clickManageRegisteredDevices();
    }

    @And("user clicks on Remove button for remove device")
    public void user_clicks_on_remove_button() {
        init();
        HybridAppStabilizer.ensureWebView(driver);
        manageDevicesPage.clickAllRemoveButtons();
    }

    @Then("registered device should be removed successfully")
    public void registered_device_should_be_removed_successfully() {
        init();
        manageDevicesPage.verifyDeviceRemoved();
    }
}
