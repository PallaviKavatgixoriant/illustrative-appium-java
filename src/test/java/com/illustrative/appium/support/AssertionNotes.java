package com.illustrative.appium.support;

/**
 * Writes down why a check is allowed by the spreadsheet, or why it still needs the app.
 * These lines only explain the check. They do not mark a test as passed.
 */
public final class AssertionNotes {

    private AssertionNotes() {
    }

    public static void acSupported(String testId, String check, String reason) {
        ScenarioLogger.info("ASSERTION AC_SUPPORTED " + testId + " — " + check + " — " + reason);
    }

    public static void needsApplication(String testId, String check, String reason) {
        ScenarioLogger.info("ASSERTION REQUIRES_APPLICATION_INFO " + testId + " — " + check + " — " + reason);
    }
}
