package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/DealsDiscounts.feature",
        glue = {"steps"},
        plugin = {"pretty", "html:target/ft-report.html"},
        tags = "@DealsDiscounts",
        monochrome = true
)
public class DealsDiscountRunner extends AbstractTestNGCucumberTests {
}