package com.illustrative.appium.steps;

import com.illustrative.appium.support.AssertionNotes;
import com.illustrative.appium.support.PlaceholderAction;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Steps that more than one part uses: open a part, submit, scroll, and read the popup.
 */
public final class CommonSteps {

    @Given("the mobile app is open on {string}")
    public void openPart(String partName) {
        // Step: go to the named part. The same sentence is used by every test id.
        TestContext context = TestContext.current();
        context.setCurrentPart(partName);
        ScenarioLogger.info("Step: open " + partName + ".");
        switch (partName) {
            case "Part 1" -> context.part1().open();
            case "Part 2" -> context.part2().open();
            case "Part 3" -> context.part3().open();
            case "Part 4" -> context.part4().open();
            default -> PlaceholderAction.stop(
                    "PLACEHOLDER_ACTION_OPEN_PART",
                    "The feature file asked for an unknown part: " + partName);
        }
    }

    @When("the user scrolls through the list and stores every row")
    public void scrollAndStore() {
        // Step: scroll until the list ends and keep each row once.
        TestContext context = TestContext.current();
        ScenarioLogger.info("Step: scroll and store rows on " + context.currentPart() + ".");
        switch (context.currentPart()) {
            case "Part 2" -> context.part2().scrollAndStoreRows();
            case "Part 3" -> context.part3().scrollAndStoreRows();
            default -> PlaceholderAction.stop(
                    "PLACEHOLDER_ACTION_SCROLL_LIST",
                    "Scrolling is defined for Part 2 and Part 3. Current part is " + context.currentPart() + ".");
        }
    }

    @When("the user submits the current form")
    public void submit() {
        // Step: click Submit. Part 1 and Part 4 both end with this action.
        ScenarioLogger.info("Step: submit the current form.");
        TestContext.current().shared().submit();
    }

    @Then("the popup text is {string} for test {string}")
    public void popupTextIs(String expected, String testId) {
        // Step: compare the popup with the text written in Test_Case.xlsx.
        if (expected.contains("Success")) {
            AssertionNotes.acSupported(
                    testId,
                    "popup text contains " + expected,
                    "Test_Case.xlsx says to validate a Success popup. Part 4 shows that as Step 1, Step 2, or Step 3.");
        } else if (expected.contains("Incorrect username!")) {
            AssertionNotes.acSupported(
                    testId,
                    "popup text contains Incorrect username!",
                    "TAT-63 says to validate the Incorrect username! popup.");
        } else {
            AssertionNotes.needsApplication(
                    testId,
                    "unexpected popup text in the feature file",
                    "This text is not one of the popup sentences in Test_Case.xlsx.");
        }
        AssertionNotes.needsApplication(
                testId,
                "where the popup is on the screen",
                "The spreadsheet does not give a resource id, accessibility id, or view name for the popup.");
        String actual = TestContext.current().shared().popupText();
        Assertions.assertTrue(
                actual.contains(expected),
                testId + " popup text should contain [" + expected + "] but was [" + actual + "]");
    }
}
