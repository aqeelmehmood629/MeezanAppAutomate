package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/SadqaZakat.feature",
        glue = {"steps"},
        tags = "@Zakat or @Sadqa",
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class SadqaZakatRunner extends AbstractTestNGCucumberTests {
}