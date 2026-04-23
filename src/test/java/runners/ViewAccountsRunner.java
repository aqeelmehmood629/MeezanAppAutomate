package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/15_ViewAccounts.feature",
        glue = "steps",
        tags ="@MyAccounts",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class ViewAccountsRunner extends AbstractTestNGCucumberTests {
}