package runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/22_RegisterDigitalBanking.feature",
        		glue = "steps",
                tags ="@RegisterDigitalBanking",
                plugin = {"pretty", "html:target/ft-report.html"}
)

public class ManageRegisterDevicesRunner extends AbstractTestNGCucumberTests {

}
