package com.illustrative.appium.pages;

import com.illustrative.appium.locators.LocatorPlaceholders;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import org.openqa.selenium.WebElement;

/**
 * Part 1 screen: username, password, submit, and the result popup.
 */
public final class Part1FormPage extends BasePage {

    public Part1FormPage(TestContext context) {
        super(context);
    }

    public void open() {
        // Step: open Part 1 from the app's navigation.
        ScenarioLogger.info("Open Part 1 with accessibility id " + LocatorPlaceholders.OPEN_PART_1 + ".");
        waitUntilVisible(LocatorPlaceholders.OPEN_PART_1).click();
    }

    public void typeUsername(String username) {
        // Step: type the username into the username field.
        ScenarioLogger.info("Type the username into " + LocatorPlaceholders.PART1_USERNAME + ".");
        WebElement field = waitUntilVisible(LocatorPlaceholders.PART1_USERNAME);
        field.clear();
        field.sendKeys(username);
    }

    public void typeEmail(String email) {
        // Step: type the email. TAT-63 uses an incorrect email. TAT-62 uses the correct one.
        ScenarioLogger.info("Type the email into " + LocatorPlaceholders.PART1_EMAIL + ".");
        WebElement field = waitUntilVisible(LocatorPlaceholders.PART1_EMAIL);
        field.clear();
        field.sendKeys(email);
    }

    public void typePassword(String password) {
        // Step: type the password. The password is not copied into the log.
        ScenarioLogger.info("Type the password into " + LocatorPlaceholders.PART1_PASSWORD + ".");
        WebElement field = waitUntilVisible(LocatorPlaceholders.PART1_PASSWORD);
        field.clear();
        field.sendKeys(password);
    }
}
