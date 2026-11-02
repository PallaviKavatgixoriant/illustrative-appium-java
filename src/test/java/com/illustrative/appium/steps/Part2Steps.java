package com.illustrative.appium.steps;

import com.illustrative.appium.pages.Part2ListPage;
import com.illustrative.appium.support.AssertionNotes;
import com.illustrative.appium.support.PlaceholderAction;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Part 2 checks for TAT-64, TAT-66, and TAT-67.
 */
public final class Part2Steps {

    @Then("the Part 2 check {string} passes for test {string}")
    public void checkPart2(String check, String testId) {
        ScenarioLogger.info("Step: validate Part 2 check [" + check + "] for " + testId + ".");
        switch (check) {
            case "alphabetical order" -> assertAlphabetical(testId);
            case "empty categories" -> assertEmptyCategories(testId);
            case "categories with 3 or more items" -> assertCategoriesWithAtLeastThree(testId);
            default -> PlaceholderAction.stop(
                    "PLACEHOLDER_ACTION_PARSE_CATEGORY_ROWS",
                    "Unknown Part 2 check: " + check);
        }
    }

    private void assertAlphabetical(String testId) {
        // AC_SUPPORTED: TAT-64 says the list is in alphabetical order.
        // REQUIRES_APPLICATION_INFO: case, locale, and whether headers are included are not in the spreadsheet.
        AssertionNotes.acSupported(testId, "the stored list is alphabetical", "TAT-64 says to validate alphabetical order.");
        AssertionNotes.needsApplication(
                testId,
                "sort case, locale, and which rows count",
                "The spreadsheet does not say case-sensitive, which alphabet, or whether category headers are in the list.");
        String rule = TestContext.current().testData().required("sort.rule");
        if (rule.contains("PLACEHOLDER_")) {
            PlaceholderAction.stop(
                    "PLACEHOLDER_SORT_RULE",
                    "Set sort.rule to case-sensitive or case-insensitive after checking how the app sorts.");
        }
        List<String> actual = new ArrayList<>(TestContext.current().part2().storedItemNames());
        List<String> sorted = new ArrayList<>(actual);
        if ("case-insensitive".equals(rule)) {
            sorted.sort(String.CASE_INSENSITIVE_ORDER);
        } else if ("case-sensitive".equals(rule)) {
            sorted.sort(Comparator.naturalOrder());
        } else {
            PlaceholderAction.stop("PLACEHOLDER_SORT_RULE", "Use case-sensitive or case-insensitive. Found: " + rule);
        }
        Assertions.assertEquals(sorted, actual, testId + " list should already be in alphabetical order");
    }

    private void assertEmptyCategories(String testId) {
        // AC_SUPPORTED: the letters that must have no manufacturers are listed in the spreadsheet.
        // REQUIRES_APPLICATION_INFO: an empty letter may be a blank header or may be hidden.
        AssertionNotes.acSupported(
                testId,
                "named letters have no manufacturers",
                "TAT-66 lists the empty letters for Android and the empty letters for iOS.");
        AssertionNotes.needsApplication(
                testId,
                "how an empty category is drawn",
                "The spreadsheet does not say whether a hidden letter still counts as an empty category.");
        List<String> expectedEmpty = TestContext.current().testData().list(
                TestContext.current().dataKey("empty.categories"));
        Map<String, List<String>> grouped = TestContext.current().part2().manufacturersByCategory();
        for (String letter : expectedEmpty) {
            int count = grouped.getOrDefault(letter, List.of()).size();
            Assertions.assertEquals(0, count, testId + " category " + letter + " should have no manufacturers");
        }
    }

    private void assertCategoriesWithAtLeastThree(String testId) {
        // AC_SUPPORTED: the named letters must have 3 or more manufacturers.
        // The spreadsheet does not say that every other letter has fewer than 3.
        AssertionNotes.acSupported(
                testId,
                "named letters have 3 or more manufacturers",
                "TAT-67 lists those letters separately for Android and iOS.");
        AssertionNotes.needsApplication(
                testId,
                "how a manufacturer row is counted",
                "The spreadsheet does not describe duplicate rows, nested rows, or section headers.");
        List<String> expectedLetters = TestContext.current().testData().list(
                TestContext.current().dataKey("categories.with.three.or.more"));
        Part2ListPage page = TestContext.current().part2();
        Map<String, List<String>> grouped = page.manufacturersByCategory();
        for (String letter : expectedLetters) {
            int count = grouped.getOrDefault(letter, List.of()).size();
            Assertions.assertTrue(count >= 3, testId + " category " + letter + " should have 3 or more manufacturers");
        }
    }
}
