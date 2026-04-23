package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/09_FAQs.feature",
        glue = "steps",
        tags ="@FAQs",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class FAQsRunner extends AbstractTestNGCucumberTests {
}