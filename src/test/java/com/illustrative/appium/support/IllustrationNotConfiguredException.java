package com.illustrative.appium.support;

/**
 * Raised when a placeholder is still in place.
 * A real run stops here instead of guessing a selector, a URL, or a screen action.
 */
public final class IllustrationNotConfiguredException extends RuntimeException {

    private final String placeholderName;

    public IllustrationNotConfiguredException(String placeholderName, String whatToConfigure) {
        super(placeholderName + " — " + whatToConfigure);
        this.placeholderName = placeholderName;
    }

    public String placeholderName() {
        return placeholderName;
    }
}
