package steps;

import driver.DriverFactory;
import io.cucumber.java.After;
import pages.DashboardPage;

public class CommomSteps {
	
	@After("(@FT or @Sadqa or @Zakat or @FTOwn or @Raast or @Limit or @Accountdeselection or @Qibla or @ChangeNotificationLanguage or @ChangePassword or @DealsDiscounts or @EnableQuickViewBalance or @FAQs or @Feedback or @Notification or @MyProfile or @MyAccounts)")
    public void navigateToDashboard() {
        DashboardPage dashboardPage = new DashboardPage(DriverFactory.getDriver());
        dashboardPage.goToDashboard();
    }

}
