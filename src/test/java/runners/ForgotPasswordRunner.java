package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/ForgotPassword.feature",
        glue = "steps",
        tags ="@ForgotPassword",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class ForgotPasswordRunner extends AbstractTestNGCucumberTests {
}