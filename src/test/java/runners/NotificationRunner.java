package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/Notification.feature",
        glue = "steps",
        tags ="@Notification",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class NotificationRunner extends AbstractTestNGCucumberTests {
}