package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/20_OTPpreference.feature",
        glue = "steps",
        tags ="@ChangeOTPPreference",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class ChangeOtpPreferenceRunner extends AbstractTestNGCucumberTests {
}