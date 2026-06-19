package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;

    public synchronized static ExtentReports getExtentReports() {

        if (extent == null) {

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = "reports/Regression_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

            // ✅ Report configuration
            spark.config().setDocumentTitle("Meezan Bank — Automation Report");
            spark.config().setReportName("Appium + Cucumber Test Results");
            spark.config().setTheme(Theme.DARK);
            spark.config().setTimeStampFormat("dd MMM yyyy, hh:mm:ss a");
            spark.config().setEncoding("utf-8");

            // ✅ Custom professional CSS for report styling (logo will be handled manually later)
            spark.config().setCss(buildCustomCss());

            extent = new ExtentReports();
            extent.attachReporter(spark);

            // ✅ System / environment info panel
            String execDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            extent.setSystemInfo("Project",        "Meezan Bank Mobile Automation");
            extent.setSystemInfo("Tester",         "Aqeel Mehmood");
            extent.setSystemInfo("Platform",       "Android 13");
            extent.setSystemInfo("Device",         "Infinix X670 (UDID: 083672525V005614)");
            extent.setSystemInfo("App Package",    "com.ofss.tx.meezan");
            extent.setSystemInfo("Automation",     "Appium + Cucumber (Java)");
            extent.setSystemInfo("Appium Server",  "http://127.0.0.1:4723");
            extent.setSystemInfo("Execution Date", execDate);
        }

        return extent;
    }


    /**
     * Custom CSS: styling for step labels, header accent, and logo container.
     */
    private static String buildCustomCss() {
        return
            // Accent bar on the top of the report header
            ".report-header { border-top: 4px solid #1a7a4a !important; }" +

            // Colour-coded step labels inside test details
            ".badge-success { background:#1a7a4a !important; color:#fff !important; " +
            "  font-size:12px; padding:2px 8px; border-radius:3px; }" +
            ".badge-danger  { background:#c0392b !important; color:#fff !important; " +
            "  font-size:12px; padding:2px 8px; border-radius:3px; }" +
            ".badge-warning { background:#d68910 !important; color:#fff !important; " +
            "  font-size:12px; padding:2px 8px; border-radius:3px; }" +

            // Stack-trace collapse styling
            "details summary { cursor:pointer; color:#f0a500; font-weight:600; }" +
            "details pre { background:#1e1e1e; color:#e8e8e8; padding:10px; " +
            "  border-radius:4px; font-size:11px; overflow-x:auto; white-space:pre-wrap; }" +

            // Child node (data-driven row) styling — left border accent
            ".node .node-content { border-left: 3px solid #2980b9; " +
            "  margin-left: 12px; padding-left: 10px; }" +

            // Execution summary node styling
            ".test-content b { font-size: 13px; }";
    }


}