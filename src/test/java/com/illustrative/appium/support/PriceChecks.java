package com.illustrative.appium.support;

import java.math.BigDecimal;
import java.util.List;

/**
 * Turns price text into numbers and compares a sum with a total.
 * The spreadsheet says the numbers must match. It does not say how a price is written.
 */
public final class PriceChecks {

    private PriceChecks() {
    }

    public static BigDecimal parse(String raw, String rule) {
        if (!"digits-and-dot".equals(rule)) {
            return PlaceholderAction.stop(
                    "PLACEHOLDER_PRICE_PARSE_RULE",
                    "Set price.parse.rule to digits-and-dot only if the app shows prices that way.");
        }
        String cleaned = raw.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty() || ".".equals(cleaned)) {
            throw new IllustrationNotConfiguredException(
                    "PLACEHOLDER_PRICE_PARSE_RULE",
                    "The price text had no digits to add.");
        }
        return new BigDecimal(cleaned);
    }

    public static BigDecimal sum(List<String> priceTexts, String rule) {
        BigDecimal total = BigDecimal.ZERO;
        for (String priceText : priceTexts) {
            total = total.add(parse(priceText, rule));
        }
        return total;
    }
}
