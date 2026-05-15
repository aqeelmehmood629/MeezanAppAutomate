package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import driver.DriverFactory;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.Base64;

/**
 * ✅ CucumberListener — wired via @CucumberOptions plugin list in MainTestRunner.
 *
 * Captures every Gherkin step (Given/When/Then/And/But) and logs it to the
 * Extent Report with keyword, text, status, and on failure: error + screenshot.
 *
 * IMPORTANT: This listener runs in the Cucumber execution thread.
 * ReportManager uses ThreadLocal<ExtentTest> so it is thread-safe.
 */
public class CucumberListener implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::onStepFinish);
    }

    /**
     * Called after each step completes. Logs keyword + text + status as a
     * single row in the Extent Report. On failure, attaches screenshot inline.
     */
    private void onStepFinish(TestStepFinished event) {
        // Ignore Before/After hook results — only log Gherkin steps
        if (!(event.getTestStep() instanceof PickleStepTestStep)) return;

        PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
        String keyword  = step.getStep().getKeyword().trim();   // "Given", "When", "And", etc.
        String stepText = step.getStep().getText().trim();
        String fullStep = "<b>" + keyword + "</b> " + stepText;

        ExtentTest test = ReportManager.getTest();
        if (test == null) return;

        String resultStatus = event.getResult().getStatus().name(); // PASSED, FAILED, SKIPPED, UNDEFINED

        switch (resultStatus) {
            case "PASSED":
                test.log(Status.PASS,
                        MarkupHelper.createLabel("✅ " + fullStep, ExtentColor.GREEN));
                break;

            case "SKIPPED":
            case "PENDING":
                test.log(Status.SKIP,
                        MarkupHelper.createLabel("⏭️ " + fullStep, ExtentColor.YELLOW));
                break;

            case "FAILED":
            case "AMBIGUOUS":
            case "UNDEFINED":
                // Log the step as failed with the error message
                Throwable error = event.getResult().getError();
                String errorMsg = error != null ? error.getMessage() : "Unknown error";
                String stackTrace = error != null ? getStackTrace(error) : "";

                test.log(Status.FAIL,
                        MarkupHelper.createLabel("❌ " + fullStep, ExtentColor.RED));
                test.log(Status.FAIL,
                        "<b>Error:</b> " + escapeHtml(errorMsg));

                if (!stackTrace.isEmpty()) {
                    test.log(Status.FAIL,
                            "<details><summary>📋 Stack Trace</summary><pre>" 
                            + escapeHtml(stackTrace) + "</pre></details>");
                }

                // Attach screenshot using ScreenshotUtil if driver is alive
                attachScreenshotWithUtil(test, stepText);
                break;

            default:
                test.log(Status.WARNING, "⚠️ " + fullStep + " [" + resultStatus + "]");
                break;
        }
    }

    /**
     * Captures screenshot via existing ScreenshotUtil and attaches it to the Extent Report.
     * Uses relative path handling so the report works correctly in any environment.
     */
    private void attachScreenshotWithUtil(ExtentTest test, String stepName) {
        try {
            if (!DriverFactory.isDriverResponsive()) return;

            // 1. Capture screenshot using existing utility
            String absolutePath = ScreenshotUtil.captureScreenshot(stepName, "FAIL");

            if (absolutePath != null) {
                // 2. Attach screenshot directly using the absolute path.
                // Using addScreenCaptureFromPath directly is the most stable way for ExtentReports 
                // to handle local file system images (it automatically adds the file:/// protocol) 
                // and resolves the broken image icon issue.
                test.addScreenCaptureFromPath(absolutePath, "Screenshot of failure");
            }
        } catch (Exception e) {
            test.log(Status.WARNING, "⚠️ Screenshot attachment failed: " + e.getMessage());
        }
    }

    /** Converts a Throwable's stack trace to a readable string */
    private String getStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.toString()).append("\n");
        for (StackTraceElement element : t.getStackTrace()) {
            sb.append("  at ").append(element.toString()).append("\n");
            // Limit to first 15 frames to keep report readable
            if (sb.toString().split("\n").length > 16) {
                sb.append("  ... (truncated)");
                break;
            }
        }
        return sb.toString();
    }

    /** Escapes HTML special chars so stack traces render cleanly inside HTML */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
