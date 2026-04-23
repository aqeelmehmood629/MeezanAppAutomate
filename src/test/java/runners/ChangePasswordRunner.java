package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/16_ChangePassword.feature",
        glue = "steps",
        tags ="@ChangePassword",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class ChangePasswordRunner extends AbstractTestNGCucumberTests {
}