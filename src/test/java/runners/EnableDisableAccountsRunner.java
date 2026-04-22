package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/EnableDisableAccounts.feature",
        glue = "steps",
        tags ="@Accountdeselection",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class EnableDisableAccountsRunner extends AbstractTestNGCucumberTests {
}