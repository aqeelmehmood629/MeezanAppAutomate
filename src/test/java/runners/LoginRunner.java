package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
	    features = "src/test/resources/features/",
	    glue = "steps",
	    tags = "@Login or @Dashboard or @FT",
	    plugin = {"pretty", "html:target/ft-report.html"},
	    monochrome = true
	)
	public class LoginRunner extends AbstractTestNGCucumberTests {
}