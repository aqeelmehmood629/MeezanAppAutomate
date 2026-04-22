package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/Profile.feature",
        glue = "steps",
        tags ="@MyProfile",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class ViewProfileRunner extends AbstractTestNGCucumberTests {
}