package steps;

import io.cucumber.java.en.*;
import pages.FeedbackPage;
import utils.CSVUtils;

import java.util.List;
import java.util.Map;

public class FeedbackSteps {

    FeedbackPage page;

    private void init() {
        if (page == null) {
            page = new FeedbackPage();
        }
    }

    private List<Map<String, String>> csvData;
    private Map<String, String> data;

    @Given("user opens feedback screen")
    public void open_feedback() {
        init();

        if (csvData == null) {
            csvData = CSVUtils.getAllData("src/test/resources/FeedbackData.csv");
        }

        data = csvData.get(0);

        page.openFeedback();
    }

    @And("user gives rating for feedback")
    public void give_rating() {
        init();
        page.giveRating(data.get("rating"));
    }

    @And("user enters feedback text from csv")
    public void enter_text() {
        init();
        page.enterFeedbackText(data.get("feedbackText"));
    }

    @And("user clicks submit feedback button")
    public void submit() {
        init();
        page.clickSubmit();
    }
    @Then("user clicks home icon after feedback")
    public void feedbackHomeIcon() {
        init();
    	page.feedbackHomeClick();
    }
}