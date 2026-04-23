package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
	    features = "src/test/resources/features/",
	    glue = "steps",
	    tags = "@Login or @Dashboard or @FT or @FTOwn or @Raast or @Sadqa or @Zakat or @Limit or @ChangeNotificationLanguage or @ChangePassword or @DealsDiscounts or @Accountdeselection or @EnableQuickViewBalance or @FAQs or @Feedback or @ForgotPassword or @Notification or @MyProfile or @Qibla or @MyAccounts or @Logout"
	    		,
	    plugin = {"pretty", "html:target/ft-report.html"},
	    monochrome = true
	)
	public class LoginRunner extends AbstractTestNGCucumberTests {
}