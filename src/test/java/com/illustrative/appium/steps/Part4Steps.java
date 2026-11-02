package com.illustrative.appium.steps;

import com.illustrative.appium.support.AssertionNotes;
import com.illustrative.appium.support.PlaceholderAction;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import io.cucumber.java.en.When;

/**
 * Part 4 steps for TAT-71, TAT-72, and TAT-73.
 * The Success popup check is the shared step in CommonSteps.
 */
public final class Part4Steps {

    @When("the user enables checkboxes using the rule {string}")
    public void enableCheckboxes(String rule) {
        // Step: enable the checkboxes described by this rule, then the shared submit step runs.
        ScenarioLogger.info("Step: enable checkboxes using the rule [" + rule + "].");
        switch (rule) {
            case "all checkboxes in the section" -> enableSection1();
            case "preceding or following checkboxes from a random checkbox" -> enableSection2();
            case "checkboxes that have yes under them" -> enableSection3();
            default -> PlaceholderAction.stop(
                    "PLACEHOLDER_ACTION_ENABLE_ALL_MATCHING",
                    "Unknown checkbox rule: " + rule);
        }
    }

    private void enableSection1() {
        // AC_SUPPORTED: submitting section 1 shows Success, and one selector enables every checkbox.
        // REQUIRES_APPLICATION_INFO: the checkbox names and the selector are not in the spreadsheet.
        AssertionNotes.acSupported(
                "TAT-71",
                "section 1 submit shows Success, found by one selector",
                "TAT-71 says one selector dynamically finds and enables all section 1 checkboxes.");
        AssertionNotes.needsApplication(
                "TAT-71",
                "which controls are section 1",
                "The spreadsheet does not name the checkboxes.");
        TestContext.current().part4().enableAllInSection1();
    }

    private void enableSection2() {
        // AC_SUPPORTED: the checkboxes are the ones before or after a chosen checkbox, then Success.
        // REQUIRES_APPLICATION_INFO: "required" is not a list of names, and preceding versus following is not chosen.
        AssertionNotes.acSupported(
                "TAT-72",
                "section 2 uses checkboxes relative to one chosen checkbox",
                "TAT-72 says preceding or following checkboxes from a randomly selected checkbox, then a Success popup.");
        AssertionNotes.needsApplication(
                "TAT-72",
                "which checkbox is required, and whether to look preceding or following",
                "The summary says required checkboxes, but the steps do not name them or choose a direction.");
        TestContext.current().part4().enableRelativeToRandom();
    }

    private void enableSection3() {
        // AC_SUPPORTED: enable checkboxes with yes under them, then the popup says Success.
        // REQUIRES_APPLICATION_INFO: the distance and the view type of that yes label are not described.
        AssertionNotes.acSupported(
                "TAT-73",
                "only checkboxes with yes underneath are enabled",
                "TAT-73 says to enable checkboxes that have yes under them and then validate Success.");
        AssertionNotes.needsApplication(
                "TAT-73",
                "how under is located",
                "The spreadsheet does not say the label's view type or how far under the checkbox it sits.");
        TestContext.current().part4().enableWhereLabelUnderneathIsYes();
    }
}
