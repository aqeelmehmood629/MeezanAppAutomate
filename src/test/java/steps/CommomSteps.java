package steps;

import driver.DriverFactory;
import io.cucumber.java.After;
import pages.DashboardPage;

public class CommomSteps {
	
	@After("(@FT or @Sadqa or @Zakat or @FTOwn or @Raast)")
    public void navigateToDashboard() {
        DashboardPage dashboardPage = new DashboardPage(DriverFactory.getDriver());
        dashboardPage.goToDashboard();
    }

}
