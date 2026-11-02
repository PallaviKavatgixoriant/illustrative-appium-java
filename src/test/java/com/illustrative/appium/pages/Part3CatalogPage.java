package com.illustrative.appium.pages;

import com.illustrative.appium.locators.LocatorPlaceholders;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Part 3 screen: item price, brand, image name, and the totals.
 */
public final class Part3CatalogPage extends BasePage {

    private final List<String> storedRows = new ArrayList<>();

    public Part3CatalogPage(TestContext context) {
        super(context);
    }

    public void open() {
        // Step: open Part 3 from the app's navigation.
        ScenarioLogger.info("Open Part 3 with accessibility id " + LocatorPlaceholders.OPEN_PART_3 + ".");
        waitUntilVisible(LocatorPlaceholders.OPEN_PART_3).click();
    }

    public void scrollAndStoreRows() {
        // Step: scroll the whole catalog and remember every text row once.
        ScenarioLogger.info("Scroll Part 3 and store each row.");
        storedRows.clear();
        storedRows.addAll(scrollAndCollectVisibleText(LocatorPlaceholders.PART3_LIST));
    }

    public List<String> priceTexts() {
        // Step: item prices are the rows that start with Price.
        List<String> prices = new ArrayList<>();
        for (String row : storedRows) {
            if (row.startsWith("Price:")) {
                prices.add(row);
            }
        }
        return prices;
    }

    public String totalSumText() {
        // Step: the overall total is the row that starts with Total:, not Selected total or a brand total.
        for (String row : storedRows) {
            if (row.startsWith("Total:")) {
                return row;
            }
        }
        return "";
    }

    public String chosenBrand() {
        // Step: a brand total looks like Total (Landwind): 1012.5$. Use the brand inside the parentheses.
        for (String row : storedRows) {
            if (row.startsWith("Total (")) {
                int end = row.indexOf(')');
                if (end > 7) {
                    return row.substring(7, end);
                }
            }
        }
        return "";
    }

    public List<String> pricesForBrand(String brand) {
        // Step: a Price row belongs to the Brand row that came before it.
        List<String> prices = new ArrayList<>();
        String currentBrand = "";
        for (String row : storedRows) {
            if (row.startsWith("Brand: ")) {
                currentBrand = row.substring("Brand: ".length()).trim();
            } else if (row.startsWith("Price:") && brand.equals(currentBrand)) {
                prices.add(row);
            }
        }
        return prices;
    }

    public String brandTotalText(String brand) {
        String prefix = "Total (" + brand + "):";
        for (String row : storedRows) {
            if (row.startsWith(prefix)) {
                return row;
            }
        }
        return "";
    }

    public List<String> imageNames() {
        // Step: image names are the rows that start with FAKEIMAGE-.
        ScenarioLogger.info("Read Part 3 image names.");
        List<String> names = new ArrayList<>();
        for (String row : storedRows) {
            if (row.startsWith("FAKEIMAGE-")) {
                names.add(row);
            }
        }
        return names;
    }
}
