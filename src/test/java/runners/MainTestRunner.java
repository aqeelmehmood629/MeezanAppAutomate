package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
    features = "src/test/resources/features/",
    glue = "steps",
    tags = "@Smoke",
      plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/CucumberTestReport.json",
        "rerun:target/cucumber-reports/rerun.txt",
        "utils.CucumberListener"   // ✅ Step-level Extent Report logging
    },
    monochrome = true
)
public class MainTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    // ✅ Report flush + driver quit are handled in Hooks.@AfterAll
    // No duplicate @AfterSuite needed here
}
