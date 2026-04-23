package steps;

import io.cucumber.java.en.*;
import pages.FeedbackPage;
import utils.CSVUtils;

import java.util.List;
import java.util.Map;

public class FeedbackSteps {

    FeedbackPage page = new FeedbackPage();

    private List<Map<String, String>> csvData;
    private Map<String, String> data;

    @Given("user opens feedback screen")
    public void open_feedback() {

        if (csvData == null) {
            csvData = CSVUtils.getAllData("src/test/resources/FeedbackData.csv");
        }

        data = csvData.get(0);

        page.openFeedback();
    }

    @And("user gives rating for feedback")
    public void give_rating() {
        page.giveRating(data.get("rating"));
    }

    @And("user enters feedback text from csv")
    public void enter_text() {
        page.enterFeedbackText(data.get("feedbackText"));
    }

    @And("user clicks submit feedback button")
    public void submit() {
        page.clickSubmit();
    }
    @Then("user clicks home icon after feedback")
    public void feedbackHomeIcon() {
    	page.feedbackHomeClick();
    }
}