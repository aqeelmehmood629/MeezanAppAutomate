package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/FundTransfer.feature",
        glue = "steps",
        tags = "@Login or @Dashboard or @Sadqa or @Zakat or @Qibla or @MyProfile or @Notification or @Limit or @ChangeNotificationLanguage",
        plugin = {"pretty", "html:target/ft-report.html"}
)
public class FundTransferRunner extends AbstractTestNGCucumberTests {
}