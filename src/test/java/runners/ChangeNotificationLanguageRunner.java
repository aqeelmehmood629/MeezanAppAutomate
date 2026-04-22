package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/05_ChangeNotificationLanguage.feature",
        glue = "steps",
        tags ="@ChangeNotificationLanguage",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class ChangeNotificationLanguageRunner extends AbstractTestNGCucumberTests {
}