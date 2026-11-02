package com.illustrative.appium.support;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.ios.options.wda.XcodeCertificate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

/**
 * Starts and stops the Appium session.
 * The delivered configuration still contains placeholders, so start() stops
 * before it opens a session.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static AppiumDriver start(String platform, FlatConfig environment) {
        // Step: create the mobile session for the platform chosen on the command line.
        ScenarioLogger.info("Preparing an Appium session for platform " + platform + ".");
        if ("android".equalsIgnoreCase(platform)) {
            requireReady(environment,
                    "appium.server.url",
                    "explicit.wait.seconds",
                    "android.app.path",
                    "android.app.package",
                    "android.app.activity",
                    "android.device.name",
                    "android.udid",
                    "android.platform.version");
            return startAndroid(environment);
        }
        if ("ios".equalsIgnoreCase(platform)) {
            requireReady(environment,
                    "appium.server.url",
                    "explicit.wait.seconds",
                    "ios.app.path",
                    "ios.bundle.id",
                    "ios.device.name",
                    "ios.udid",
                    "ios.platform.version",
                    "ios.xcode.org.id",
                    "ios.xcode.signing.id");
            return startIos(environment);
        }
        return PlaceholderAction.stop(
                "PLACEHOLDER_PLATFORM",
                "Run with -Dtarget.platform=android or -Dtarget.platform=ios.");
    }

    public static void stop(AppiumDriver driver) {
        // Step: close the session after the scenario, including when the scenario failed.
        if (driver == null) {
            ScenarioLogger.info("PLACEHOLDER_ACTION_STOP_SESSION — no session was opened.");
            return;
        }
        ScenarioLogger.info("Stopping the Appium session.");
        driver.quit();
    }

    private static AndroidDriver startAndroid(FlatConfig environment) {
        // UiAutomator2Options is the Android automation choice. It is not an app selector.
        UiAutomator2Options options = new UiAutomator2Options()
                .setUdid(environment.required("android.udid"))
                .setDeviceName(environment.required("android.device.name"))
                .setPlatformVersion(environment.required("android.platform.version"))
                .setApp(appFile(environment.required("android.app.path")))
                .setAppPackage(environment.required("android.app.package"))
                .setAppActivity(environment.required("android.app.activity"));
        return new AndroidDriver(serverUrl(environment), options);
    }

    private static IOSDriver startIos(FlatConfig environment) {
        // XCUITestOptions is the iOS automation choice. It is not an app selector.
        // Signing uses one certificate object: team id, then signing id.
        XCUITestOptions options = new XCUITestOptions()
                .setUdid(environment.required("ios.udid"))
                .setDeviceName(environment.required("ios.device.name"))
                .setPlatformVersion(environment.required("ios.platform.version"))
                .setApp(appFile(environment.required("ios.app.path")))
                .setBundleId(environment.required("ios.bundle.id"));
        options.setXcodeCertificate(new XcodeCertificate(
                environment.required("ios.xcode.org.id"),
                environment.required("ios.xcode.signing.id")));
        return new IOSDriver(serverUrl(environment), options);
    }

    private static String appFile(String configuredPath) {
        // A path inside the project, such as apk/AppForTestAutomation.apk, is turned into a full path.
        // Appium installs that file. An absolute path is left unchanged.
        Path path = Path.of(configuredPath);
        if (!path.isAbsolute()) {
            path = Path.of(System.getProperty("user.dir")).resolve(path).normalize();
        }
        return path.toString();
    }

    private static URL serverUrl(FlatConfig environment) {
        String raw = environment.required("appium.server.url");
        try {
            return URI.create(raw).toURL();
        } catch (MalformedURLException | IllegalArgumentException ex) {
            throw new IllustrationNotConfiguredException(
                    "PLACEHOLDER_APPIUM_SERVER_URL",
                    "appium.server.url could not be read as a URL.");
        }
    }

    private static void requireReady(FlatConfig environment, String... keys) {
        for (String key : keys) {
            if (environment.isPlaceholder(key)) {
                PlaceholderAction.stop(
                        environment.required(key),
                        "Replace " + key + " in config/environment.yaml.");
            }
        }
    }
}
