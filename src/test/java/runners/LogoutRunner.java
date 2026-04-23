package runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/17_Logout.feature",
        		glue = "steps",
                tags ="@Logout",
                plugin = {"pretty", "html:target/ft-report.html"}
)

public class LogoutRunner extends AbstractTestNGCucumberTests {

}
