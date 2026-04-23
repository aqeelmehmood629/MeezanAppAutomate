package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/10_Feedback.feature",
        glue = "steps",
        tags ="@Feedback",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class SubmitfeedbackRunner extends AbstractTestNGCucumberTests {
}