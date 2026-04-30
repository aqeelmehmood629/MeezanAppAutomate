package steps;

import io.cucumber.java.en.*;
import pages.ChangeLimitPage;
import utils.CSVUtils;

import org.testng.Assert;
import driver.DriverFactory;

import java.util.List;
import java.util.Map;

public class ChangeLimitSteps {

    ChangeLimitPage page;

    private void init() {
        if (page == null) {
            page = new ChangeLimitPage(DriverFactory.getDriver());
        }
    }

    List<Map<String, String>> data;
    int index = 0;

    @When("user clicks Limit Management")
    public void click_limit_management() {
        init();
        page.clickLimitManagement();

        // Load CSV once
        data = CSVUtils.getAllData("src/test/resources/Limit.csv");
    }

    @And("user selects {string}")
    public void select_limit_type(String type) {
        init();
        page.selectLimitType(type);
    }

    @And("user selects amount using CSV")
    public void select_amount() {
        init();
        String amount = data.get(index).get("Amount Limit");
        page.selectAmount(amount);      // 🔥 correct usage
    }

    @And("user clicks Apply Button")
    public void click_apply() {
        init();
        page.clickApply();
        index++; // next row for next scenario
    }

    @And("limit should be updated successfully")
    public void verify_limit_update() {
        init();
        Assert.assertTrue(page.clickProceed(),
                "Limit update failed");
    }
    @Then("user click home icon after limit updated")
    public void clickHomeIcon() {
        init();
    	page.clickHomeIconLimit();
    }
    
}