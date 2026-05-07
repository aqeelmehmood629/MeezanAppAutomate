package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/19_ViewBal.feature",
        glue = "steps",
        tags ="@ViewBal",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class QuickViewBalRunner extends AbstractTestNGCucumberTests {
}