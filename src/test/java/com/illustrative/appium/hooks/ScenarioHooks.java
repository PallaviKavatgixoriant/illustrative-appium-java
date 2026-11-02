package com.illustrative.appium.hooks;

import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Opens the Appium session before each scenario and closes it afterwards.
 * A failed scenario asks for a screenshot. That action stays a placeholder until a session exists.
 */
public final class ScenarioHooks {

    @Before(order = 0)
    public void startSession(Scenario scenario) {
        // Step: prepare config, logs, and the driver before the first Gherkin step.
        TestContext.open(scenario);
        TestContext.current().startDriver();
    }

    @After(order = 1)
    public void screenshotOnFailure(Scenario scenario) {
        // Step: if the scenario failed, attach a screen picture for the Allure report.
        // This runs before the session is closed because a higher After order runs first.
        if (!scenario.isFailed()) {
            return;
        }
        ScenarioLogger.warn("Scenario failed: " + scenario.getName() + ".");
        TestContext.peek().ifPresent(context -> context.captureFailureScreenshot(scenario));
    }

    @After(order = 0)
    public void stopSession() {
        // Step: quit the driver so the next scenario starts from a clean session.
        TestContext.close();
    }
}
