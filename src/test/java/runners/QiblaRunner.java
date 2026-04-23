package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/13_Qibla.feature",
        glue = "steps",
        tags ="@Qibla",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class QiblaRunner extends AbstractTestNGCucumberTests {
}