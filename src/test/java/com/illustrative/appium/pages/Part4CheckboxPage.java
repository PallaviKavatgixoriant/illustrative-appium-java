package com.illustrative.appium.pages;

import com.illustrative.appium.locators.LocatorPlaceholders;
import com.illustrative.appium.support.ScenarioLogger;
import com.illustrative.appium.support.TestContext;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Part 4 screen: three checkbox sections and a submit button.
 */
public final class Part4CheckboxPage extends BasePage {

    public Part4CheckboxPage(TestContext context) {
        super(context);
    }

    public void open() {
        // Step: open Part 4 from the app's navigation.
        ScenarioLogger.info("Open Part 4 with accessibility id " + LocatorPlaceholders.OPEN_PART_4 + ".");
        waitUntilVisible(LocatorPlaceholders.OPEN_PART_4).click();
    }

    public void enableAllInSection1() {
        // Step: enable every checkbox that belongs to Section 1 only.
        ScenarioLogger.info("Enable every checkbox in Section 1.");
        WebElement section = waitUntilVisible("Section 1");
        for (WebElement box : section.findElements(AppiumBy.xpath(".//*[@content-desc='checkbox']"))) {
            if (!isChecked(box)) {
                box.click();
            }
        }
    }

    public void enableRelativeToRandom() {
        // Step: the section title says whether to click the items before or after one item.
        // Example: Click all following siblings of Item4.
        ScenarioLogger.info("Enable the section 2 checkboxes named by the section title.");
        WebElement section = waitUntilVisible("Section 2");
        String title = section.findElement(AppiumBy.accessibilityId("Section Title")).getText();
        Matcher matcher = Pattern.compile("Click all (following|preceding) siblings of (Item\\d+)").matcher(title);
        if (!matcher.find()) {
            throw new IllegalStateException("Section 2 title was not understood: " + title);
        }
        String axis = matcher.group(1);
        String anchor = matcher.group(2);
        List<WebElement> items = section.findElements(AppiumBy.xpath(".//*[starts-with(@content-desc,'Item')]"));
        int anchorIndex = -1;
        for (int index = 0; index < items.size(); index++) {
            if (anchor.equals(items.get(index).getAttribute("content-desc"))) {
                anchorIndex = index;
                break;
            }
        }
        for (int index = 0; index < items.size(); index++) {
            boolean select = "following".equals(axis) ? index > anchorIndex : index < anchorIndex;
            if (select && anchorIndex >= 0) {
                WebElement box = items.get(index).findElement(AppiumBy.accessibilityId("checkbox"));
                if (!isChecked(box)) {
                    box.click();
                }
            }
        }
    }

    public void enableWhereLabelUnderneathIsYes() {
        // Step: inside Section 3, click a checkbox only when the text under it says yes.
        String yesLabel = context.testData().required("section3.yes.label");
        ScenarioLogger.info("Enable section 3 checkboxes that have [" + yesLabel + "] under them.");
        Set<String> seen = new HashSet<>();
        for (int attempt = 0; attempt < 8; attempt++) {
            List<WebElement> sections = context.driver().findElements(AppiumBy.accessibilityId("Section 3"));
            if (!sections.isEmpty()) {
                List<WebElement> items = sections.get(0).findElements(
                        AppiumBy.xpath(".//*[starts-with(@content-desc,'Item')]"));
                for (WebElement item : items) {
                    String name = item.getAttribute("content-desc");
                    if (name == null || seen.contains(name)) {
                        continue;
                    }
                    // Yes or No is the text under the item, not a child of the item.
                    List<WebElement> under = item.findElements(
                            AppiumBy.xpath("following-sibling::android.widget.TextView[1]"));
                    if (under.isEmpty()) {
                        continue;
                    }
                    seen.add(name);
                    if (yesLabel.equalsIgnoreCase(under.get(0).getText())) {
                        WebElement box = item.findElement(AppiumBy.accessibilityId("checkbox"));
                        if (!isChecked(box)) {
                            box.click();
                        }
                    }
                }
            }
            if (!context.driver().findElements(AppiumBy.xpath("//android.widget.TextView[@text='Submit']")).isEmpty()) {
                break;
            }
            scrollDownOnce();
        }
    }

    private boolean isChecked(WebElement box) {
        return box.isSelected() || "true".equals(box.getAttribute("checked"));
    }
}
