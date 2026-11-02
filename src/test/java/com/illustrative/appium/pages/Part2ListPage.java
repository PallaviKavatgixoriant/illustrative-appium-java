package com.illustrative.appium.pages;

import com.illustrative.appium.locators.LocatorPlaceholders;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Part 2 screen: a scrolling list of category letters and car manufacturers.
 */
public final class Part2ListPage extends BasePage {

    private final List<String> storedRows = new ArrayList<>();

    public Part2ListPage(TestContext context) {
        super(context);
    }

    public void open() {
        // Step: open Part 2 from the app's navigation.
        ScenarioLogger.info("Open Part 2 with accessibility id " + LocatorPlaceholders.OPEN_PART_2 + ".");
        waitUntilVisible(LocatorPlaceholders.OPEN_PART_2).click();
    }

    public void scrollAndStoreRows() {
        // Step: scroll the whole list and remember every row once.
        ScenarioLogger.info("Scroll Part 2 and store each row.");
        storedRows.clear();
        storedRows.addAll(scrollAndCollectVisibleText(LocatorPlaceholders.PART2_LIST));
    }

    public List<String> storedItemNames() {
        // Step: hand the manufacturer names to the alphabetical check. Single-letter headers stay out of this list.
        List<String> names = new ArrayList<>();
        for (String row : storedRows) {
            if (isManufacturer(row)) {
                names.add(row);
            }
        }
        return names;
    }

    public Map<String, List<String>> manufacturersByCategory() {
        // Step: a row such as ---A is the category letter. The manufacturer names after it belong to that letter.
        Map<String, List<String>> grouped = new LinkedHashMap<>();
        String current = null;
        for (String row : storedRows) {
            if (row.matches("---[A-Z]")) {
                current = row.substring(3);
                grouped.computeIfAbsent(current, letter -> new ArrayList<>());
            } else if (current != null && isManufacturer(row)) {
                grouped.get(current).add(row);
            }
        }
        return grouped;
    }

    private boolean isManufacturer(String row) {
        return !row.matches("---[A-Z]")
                && !row.equals("Part 2")
                && !row.equals("AppForTestAutomation")
                && !row.equals("Home Screen");
    }
}
