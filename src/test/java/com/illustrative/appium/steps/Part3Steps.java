package com.illustrative.appium.steps;

import com.illustrative.appium.pages.Part3CatalogPage;
import com.illustrative.appium.support.AssertionNotes;
import com.illustrative.appium.support.PlaceholderAction;
import com.illustrative.appium.support.PriceChecks;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * Part 3 checks for TAT-68, TAT-69, and TAT-70.
 */
public final class Part3Steps {

    @Then("the Part 3 check {string} passes for test {string}")
    public void checkPart3(String check, String testId) {
        ScenarioLogger.info("Step: validate Part 3 check [" + check + "] for " + testId + ".");
        switch (check) {
            case "total sum" -> assertTotalSum(testId);
            case "brand sum" -> assertBrandSum(testId);
            case "fake image names" -> assertImageNames(testId);
            default -> PlaceholderAction.stop(
                    "PLACEHOLDER_ACTION_READ_PRICES",
                    "Unknown Part 3 check: " + check);
        }
    }

    private void assertTotalSum(String testId) {
        // AC_SUPPORTED: the Total Sum equals the sum of the item prices.
        // REQUIRES_APPLICATION_INFO: the price text format and the Total Sum control are not described.
        AssertionNotes.acSupported(
                testId,
                "Total Sum equals the sum of item prices",
                "TAT-68 says the Total Sum matches all item price sums.");
        AssertionNotes.needsApplication(
                testId,
                "price text and the Total Sum control",
                "The spreadsheet does not give the currency format or a selector.");
        TestContext context = TestContext.current();
        String rule = context.testData().required("price.parse.rule");
        Part3CatalogPage page = context.part3();
        BigDecimal actualSum = PriceChecks.sum(page.priceTexts(), rule);
        BigDecimal displayedTotal = PriceChecks.parse(page.totalSumText(), rule);
        Assertions.assertEquals(0, actualSum.compareTo(displayedTotal), testId + " Total Sum should match the item prices");
    }

    private void assertBrandSum(String testId) {
        // AC_SUPPORTED: one brand total equals the sum of that brand's prices.
        // iOS brand names nike, puma, and adidas are in the spreadsheet.
        // Android brand names change on every launch, so they are read from the screen.
        AssertionNotes.acSupported(
                testId,
                "a brand total equals that brand's prices",
                "TAT-69 says the brand Total Sum matches that brand's price sum.");
        AssertionNotes.acSupported(
                testId,
                "iOS brands are nike, puma, or adidas",
                "TAT-69 lists those three iOS brand names.");
        AssertionNotes.needsApplication(
                testId,
                "which brand total is on screen, and the Android brand names",
                "Android names change every run. The spreadsheet does not say which control is the random total.");
        TestContext context = TestContext.current();
        String rule = context.testData().required("price.parse.rule");
        Part3CatalogPage page = context.part3();
        String brand = page.chosenBrand();
        if ("ios".equals(context.platform().toLowerCase(Locale.ROOT))) {
            List<String> allowed = context.testData().list("ios.brand.pool");
            Assertions.assertTrue(
                    allowed.contains(brand),
                    testId + " iOS brand " + brand + " should be one of " + allowed);
        }
        BigDecimal actualSum = PriceChecks.sum(page.pricesForBrand(brand), rule);
        BigDecimal displayedTotal = PriceChecks.parse(page.brandTotalText(brand), rule);
        Assertions.assertEquals(0, actualSum.compareTo(displayedTotal), testId + " brand total for " + brand);
    }

    private void assertImageNames(String testId) {
        // AC_SUPPORTED: iOS image names must come from the FAKEIMAGE pool in the spreadsheet.
        // The sentence "match at least one" can mean every name is in the pool, or only one name is.
        // REQUIRES_APPLICATION_INFO: the Android pool is not listed, and the image name's place on screen is not listed.
        AssertionNotes.acSupported(
                testId,
                "iOS image names belong to the published FAKEIMAGE pool",
                "TAT-70 lists FAKEIMAGE-car, macbook, phone, watch, person, cactus, and book.");
        AssertionNotes.needsApplication(
                testId,
                "Android image-name pool and where the name is shown",
                "TAT-70 does not list Android names. It also does not say if the name is visible text or a content description.");
        AssertionNotes.needsApplication(
                testId,
                "every name must match, or only one name must match",
                "The words 'match at least one' do not choose between those two readings.");
        TestContext context = TestContext.current();
        List<String> actualNames = context.part3().imageNames();
        List<String> pool = context.testData().list(context.dataKey("fakeimage.pool"));
        if (pool.stream().anyMatch(name -> name.contains("PLACEHOLDER_"))) {
            for (String name : actualNames) {
                Assertions.assertTrue(
                        name.startsWith("FAKEIMAGE-"),
                        testId + " image name " + name + " should start with FAKEIMAGE-");
            }
            return;
        }
        String matchRule = context.testData().required("image.match.rule");
        if (matchRule.contains("PLACEHOLDER_")) {
            PlaceholderAction.stop(
                    "PLACEHOLDER_IMAGE_MATCH_RULE",
                    "Set image.match.rule to every-name-in-pool or at-least-one-name-in-pool.");
        }
        if ("every-name-in-pool".equals(matchRule)) {
            for (String name : actualNames) {
                Assertions.assertTrue(pool.contains(name), testId + " image name " + name + " should be in " + pool);
            }
            return;
        }
        if ("at-least-one-name-in-pool".equals(matchRule)) {
            Assertions.assertTrue(
                    actualNames.stream().anyMatch(pool::contains),
                    testId + " at least one image name should be in " + pool);
            return;
        }
        PlaceholderAction.stop(
                "PLACEHOLDER_IMAGE_MATCH_RULE",
                "Use every-name-in-pool or at-least-one-name-in-pool. Found: " + matchRule);
    }
}
