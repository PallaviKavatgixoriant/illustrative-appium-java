package com.illustrative.appium.support;

/**
 * Stops a screen action that has not been filled in yet.
 * The name argument is the label you search for when you replace that action.
 */
public final class PlaceholderAction {

    private PlaceholderAction() {
    }

    public static <T> T stop(String placeholderName, String whatToConfigure) {
        ScenarioLogger.error(placeholderName + " — " + whatToConfigure);
        throw new IllustrationNotConfiguredException(placeholderName, whatToConfigure);
    }
}
