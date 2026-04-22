package utils;

import com.aventstack.extentreports.Status;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class CucumberListener implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {

        publisher.registerHandlerFor(TestStepStarted.class, this::onStepStart);
        publisher.registerHandlerFor(TestStepFinished.class, this::onStepFinish);
    }

    // 🔵 STEP START LOG
    private void onStepStart(TestStepStarted event) {

        if (event.getTestStep() instanceof PickleStepTestStep step) {

            String stepText =
                    step.getStep().getKeyword() + step.getStep().getText();

            ReportManager.getTest().log(Status.INFO, "➡️ " + stepText);
        }
    }

    // 🔴 STEP RESULT LOG
    private void onStepFinish(TestStepFinished event) {

        if (event.getResult().getStatus().name().equals("PASSED")) {

            ReportManager.getTest().log(Status.PASS, "✅ Step Passed");

        } else if (event.getResult().getStatus().name().equals("FAILED")) {

            ReportManager.getTest().log(
                    Status.FAIL,
                    "❌ Step Failed: " + event.getResult().getError()
            );
        }
    }
}

