package com.illustrative.appium.steps;

import com.illustrative.appium.support.PlaceholderAction;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import io.cucumber.java.en.When;

/**
 * Part 1 steps for TAT-62 and TAT-63.
 * The expected popup check is the shared step in CommonSteps.
 */
public final class Part1Steps {

    @When("the user types the username stored under {string}")
    public void typeUsername(String dataKey) {
        // Step: type the correct username for TAT-62, or the incorrect username for TAT-63.
        String username = TestContext.current().testData().required(dataKey);
        ScenarioLogger.info("Step: type the username stored under " + dataKey + ".");
        if (username.contains("PLACEHOLDER_")) {
            PlaceholderAction.stop(
                    username,
                    "Replace " + dataKey + " in config/testdata.yaml. The spreadsheet does not give a username.");
        }
        TestContext.current().part1().typeUsername(username);
    }

    @When("the user types the email stored under {string}")
    public void typeEmail(String dataKey) {
        // Step: TAT-62 uses the correct email. TAT-63 uses an incorrect email, as Test_Case.xlsx says.
        String email = TestContext.current().testData().required(dataKey);
        ScenarioLogger.info("Step: type the email stored under " + dataKey + ".");
        if (email.contains("PLACEHOLDER_")) {
            PlaceholderAction.stop(
                    email,
                    "Replace " + dataKey + " in config/testdata.yaml. The spreadsheet does not give an email.");
        }
        TestContext.current().part1().typeEmail(email);
    }

    @When("the user types the password stored under {string}")
    public void typePassword(String dataKey) {
        // Step: TAT-62 uses the correct password. TAT-63 uses an incorrect password, as Test_Case.xlsx says.
        String password = TestContext.current().testData().required(dataKey);
        ScenarioLogger.info("Step: type the password stored under " + dataKey + ". The password is not logged.");
        if (password.contains("PLACEHOLDER_")) {
            PlaceholderAction.stop(
                    password,
                    "Replace " + dataKey + " in config/testdata.yaml. The spreadsheet does not give a password.");
        }
        TestContext.current().part1().typePassword(password);
    }
}
