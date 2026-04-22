package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
	    features = "src/test/resources/features/01_Login.feature",
	    glue = "steps",
	    tags = "@Login",
	    plugin = {"pretty", "html:target/ft-report.html"},
	    monochrome = true
	)
	public class LoginRunner extends AbstractTestNGCucumberTests {
}