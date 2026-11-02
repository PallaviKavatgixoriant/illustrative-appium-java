package com.illustrative.appium.support;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Small logger used by every step. Each line has a timestamp, a level, and a message.
 */
public final class ScenarioLogger {

    private static final Logger LOGGER = Logger.getLogger("com.illustrative.appium");
    private static boolean installed;

    private ScenarioLogger() {
    }

    public static void install() {
        if (installed) {
            return;
        }
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.INFO);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        handler.setFormatter(new TimestampFormatter());
        LOGGER.addHandler(handler);
        installed = true;
    }

    public static void info(String message) {
        install();
        LOGGER.info(message);
    }

    public static void warn(String message) {
        install();
        LOGGER.warning(message);
    }

    public static void error(String message) {
        install();
        LOGGER.severe(message);
    }
}
