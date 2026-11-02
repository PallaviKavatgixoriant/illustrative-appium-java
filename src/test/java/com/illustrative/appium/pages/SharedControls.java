package com.illustrative.appium.pages;

import com.illustrative.appium.locators.LocatorPlaceholders;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Actions shared by more than one part: submit, and reading the popup.
 */
public final class SharedControls extends BasePage {

    public SharedControls(TestContext context) {
        super(context);
    }

    public void submit() {
        // Step: on Part 1 the form also has an email field. Fill it, then click Submit.
        // Part 4's Submit control has the visible text Submit and may sit below the checkboxes.
        ScenarioLogger.info("Submit the form that is open on " + context.currentPart() + ".");
        if ("Part 1".equals(context.currentPart())) {
            try {
                context.driver().executeScript("mobile: hideKeyboard");
            } catch (RuntimeException ignored) {
                ScenarioLogger.info("The keyboard was already closed.");
            }
        }
        var submit = AppiumBy.xpath("//android.widget.TextView[@text='Submit']/parent::*");
        for (int attempt = 0; attempt < 6; attempt++) {
            if (!context.driver().findElements(submit).isEmpty()) {
                context.driver().findElement(submit).click();
                return;
            }
            scrollDownOnce();
        }
        waitUntilVisible(LocatorPlaceholders.SUBMIT).click();
    }

    public String popupText() {
        // Step: read every line inside the popup.
        // Part 1 is Success, or Incorrect username! followed by the expected pattern.
        // Part 4 is Step 1: Success, Step 2: Success, or Step 3: Success.
        ScenarioLogger.info("Read the popup text.");
        WebDriverWait wait = new WebDriverWait(
                context.driver(), Duration.ofSeconds(context.explicitWaitSeconds()));
        return wait.until(driver -> {
            List<WebElement> popups = driver.findElements(AppiumBy.accessibilityId("popup"));
            if (popups.isEmpty()) {
                return null;
            }
            StringBuilder text = new StringBuilder();
            for (WebElement line : popups.get(0).findElements(AppiumBy.className("android.widget.TextView"))) {
                String value = line.getText();
                if (value == null || value.isBlank() || "Ok".equals(value.trim())) {
                    continue;
                }
                if (text.length() > 0) {
                    text.append('\n');
                }
                text.append(value.trim());
            }
            return text.length() == 0 ? null : text.toString();
        });
    }
}
