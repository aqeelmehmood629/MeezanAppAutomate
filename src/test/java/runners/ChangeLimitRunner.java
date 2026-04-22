package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = "src/test/resources/features/LimitManagement.feature",
        glue = {"steps","utils"},
        tags ="@Limit",
        plugin = {
        		"pretty", "html:target/ft-report.html"
        		
        		
        }
)



public class ChangeLimitRunner extends AbstractTestNGCucumberTests {

}
