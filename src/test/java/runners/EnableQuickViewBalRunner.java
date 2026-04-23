package runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/08_EnableQuickViewBal.feature",
        		glue = "steps",
                tags ="@EnableQuickViewBalance",
                plugin = {"pretty", "html:target/ft-report.html"}
)

public class EnableQuickViewBalRunner extends AbstractTestNGCucumberTests {

}
