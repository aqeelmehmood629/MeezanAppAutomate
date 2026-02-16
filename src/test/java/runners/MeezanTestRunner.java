package runners;	

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;

@CucumberOptions(
		features = {
	            "src/test/resources/features/alogin.feature",
	            "src/test/resources/features/dashboard.feature",
	            "src/test/resources/features/sendmoney.feature"
	        },
        glue = {"steps", "base"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html"
        },
        monochrome = true
)
public class MeezanTestRunner extends AbstractTestNGCucumberTests {
	
	
}

