package com.illustrative.appium.pages;

import com.illustrative.appium.support.PlaceholderAction;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared page behaviour. Pages call these helpers instead of copying waits and scrolls.
 * A hardcoded sleep is not used. An explicit wait is the method to fill in later.
 */
public abstract class BasePage {

    protected final TestContext context;

    protected BasePage(TestContext context) {
        this.context = context;
    }

    /**
     * Step: wait until one element is visible, then return it.
     * Pass a real accessibility id. A name that still starts with PLACEHOLDER_ stops here.
     * Thread.sleep is not used.
     */
    protected WebElement waitUntilVisible(String selectorPlaceholder) {
        ScenarioLogger.info(
                "Explicit wait for " + selectorPlaceholder
                        + ". Timeout seconds key is explicit.wait.seconds. Thread.sleep is not used.");
        if (selectorPlaceholder.contains("PLACEHOLDER_")) {
            return PlaceholderAction.stop(
                    "PLACEHOLDER_ACTION_EXPLICIT_WAIT",
                    "Replace " + selectorPlaceholder + " with a real accessibility id before waiting.");
        }
        if (context.explicitWaitSeconds() <= 0) {
            return PlaceholderAction.stop(
                    "PLACEHOLDER_EXPLICIT_WAIT_SECONDS",
                    "Set explicit.wait.seconds in config/environment.yaml to a whole number greater than 0.");
        }
        WebDriverWait wait = new WebDriverWait(
                context.driver(), Duration.ofSeconds(context.explicitWaitSeconds()));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.accessibilityId(selectorPlaceholder)));
    }

    /**
     * Step: move down the list and keep rows in the order they appear, including repeated prices.
     */
    protected List<String> scrollAndCollectVisibleText(String listName) {
        ScenarioLogger.info("Scroll " + listName + " and store each visible text row.");
        List<String> collected = new ArrayList<>();
        int unchangedPasses = 0;
        for (int attempt = 0; attempt < 30 && unchangedPasses < 2; attempt++) {
            int added = appendNewlyVisible(collected, visibleTextRows());
            unchangedPasses = added == 0 ? unchangedPasses + 1 : 0;
            Map<String, Object> params = new HashMap<>();
            params.put("left", 100);
            params.put("top", 500);
            params.put("width", 800);
            params.put("height", 1400);
            params.put("direction", "down");
            params.put("percent", 0.45);
            ((JavascriptExecutor) context.driver()).executeScript("mobile: scrollGesture", params);
        }
        return collected;
    }

    private List<String> visibleTextRows() {
        List<String> visible = new ArrayList<>();
        for (WebElement element : context.driver().findElements(AppiumBy.xpath("//android.widget.TextView"))) {
            String text = element.getText();
            if (text == null || text.isBlank() || isChrome(text.trim())) {
                continue;
            }
            visible.add(text.trim());
        }
        return visible;
    }

    private boolean isChrome(String text) {
        // The app title stays on screen while the list moves, so it must not be stored as a row.
        return "AppForTestAutomation".equals(text)
                || "Home Screen".equals(text)
                || "Part 1".equals(text)
                || "Part 2".equals(text)
                || "Part 3".equals(text)
                || "Part 4".equals(text);
    }

    private int appendNewlyVisible(List<String> collected, List<String> visible) {
        if (collected.isEmpty()) {
            collected.addAll(visible);
            return visible.size();
        }
        int overlap = 0;
        int max = Math.min(collected.size(), visible.size());
        for (int size = max; size > 0; size--) {
            boolean matches = true;
            for (int index = 0; index < size; index++) {
                if (!collected.get(collected.size() - size + index).equals(visible.get(index))) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                overlap = size;
                break;
            }
        }
        for (int index = overlap; index < visible.size(); index++) {
            collected.add(visible.get(index));
        }
        return visible.size() - overlap;
    }

    protected void scrollDownOnce() {
        Map<String, Object> params = new HashMap<>();
        params.put("left", 100);
        params.put("top", 500);
        params.put("width", 800);
        params.put("height", 1400);
        params.put("direction", "down");
        params.put("percent", 0.6);
        ((JavascriptExecutor) context.driver()).executeScript("mobile: scrollGesture", params);
    }
}
