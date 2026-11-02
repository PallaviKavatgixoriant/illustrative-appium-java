package com.illustrative.appium.support;

import com.illustrative.appium.pages.Part1FormPage;
import com.illustrative.appium.pages.Part2ListPage;
import com.illustrative.appium.pages.Part3CatalogPage;
import com.illustrative.appium.pages.Part4CheckboxPage;
import com.illustrative.appium.pages.SharedControls;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.Scenario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

/**
 * Holds the driver and the page objects for the scenario that is running on this thread.
 * Each thread gets its own copy, which is what a later parallel run needs.
 */
public final class TestContext {

    private static final ThreadLocal<TestContext> CURRENT = new ThreadLocal<>();

    private final String platform;
    private final FlatConfig environment;
    private final FlatConfig testData;
    private final long explicitWaitSeconds;
    private final Part1FormPage part1;
    private final Part2ListPage part2;
    private final Part3CatalogPage part3;
    private final Part4CheckboxPage part4;
    private final SharedControls shared;
    private AppiumDriver driver;
    private String currentPart = "";

    private TestContext(String platform, FlatConfig environment, FlatConfig testData) {
        this.platform = platform;
        this.environment = environment;
        this.testData = testData;
        this.explicitWaitSeconds = readWaitSeconds(environment);
        this.part1 = new Part1FormPage(this);
        this.part2 = new Part2ListPage(this);
        this.part3 = new Part3CatalogPage(this);
        this.part4 = new Part4CheckboxPage(this);
        this.shared = new SharedControls(this);
    }

    public static void open(Scenario scenario) {
        ScenarioLogger.install();
        String platform = System.getProperty("target.platform", "PLACEHOLDER_PLATFORM");
        FlatConfig environment = FlatConfig.load("config/environment.yaml");
        FlatConfig testData = FlatConfig.load("config/testdata.yaml");
        TestContext context = new TestContext(platform, environment, testData);
        CURRENT.set(context);
        context.writeAllureEnvironment(scenario);
        ScenarioLogger.info("Scenario started: " + scenario.getName() + " on platform " + platform + ".");
    }

    public static Optional<TestContext> peek() {
        return Optional.ofNullable(CURRENT.get());
    }

    public static TestContext current() {
        TestContext context = CURRENT.get();
        if (context == null) {
            throw new IllustrationNotConfiguredException(
                    "PLACEHOLDER_ACTION_START_SESSION",
                    "The before-hook did not prepare a scenario context.");
        }
        return context;
    }

    public static void close() {
        TestContext context = CURRENT.get();
        if (context != null) {
            DriverFactory.stop(context.driver);
        }
        CURRENT.remove();
    }

    public void startDriver() {
        // Step: ask Appium to launch the app. Placeholders stop this before a session opens.
        this.driver = DriverFactory.start(platform, environment);
    }

    public void captureFailureScreenshot(Scenario scenario) {
        // Step: on a failure, attach a picture of the screen to the report.
        // The picture is intentionally not taken while the session is still a placeholder.
        ScenarioLogger.warn(
                "PLACEHOLDER_ACTION_SCREENSHOT_ON_FAILURE — attach a PNG after a real session exists.");
        if (driver == null || scenario == null) {
            return;
        }
        // Intended call after the session is real:
        // byte[] png = driver.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
        // scenario.attach(png, "image/png", "failure");
    }

    public String platform() {
        return platform;
    }

    /** Builds keys such as android.empty.categories from the platform on this thread. */
    public String dataKey(String suffix) {
        if (platform.contains("PLACEHOLDER_")) {
            return PlaceholderAction.stop(
                    "PLACEHOLDER_PLATFORM",
                    "Pass -Dtarget.platform=android or -Dtarget.platform=ios.");
        }
        return platform.toLowerCase(Locale.ROOT) + "." + suffix;
    }

    public FlatConfig testData() {
        return testData;
    }

    public long explicitWaitSeconds() {
        return explicitWaitSeconds;
    }

    public AppiumDriver driver() {
        if (driver == null) {
            return PlaceholderAction.stop(
                    "PLACEHOLDER_ACTION_START_SESSION",
                    "Start a session before looking up elements.");
        }
        return driver;
    }

    public void setCurrentPart(String partName) {
        this.currentPart = partName;
    }

    public String currentPart() {
        return currentPart;
    }

    public Part1FormPage part1() {
        return part1;
    }

    public Part2ListPage part2() {
        return part2;
    }

    public Part3CatalogPage part3() {
        return part3;
    }

    public Part4CheckboxPage part4() {
        return part4;
    }

    public SharedControls shared() {
        return shared;
    }

    private static long readWaitSeconds(FlatConfig environment) {
        String raw = environment.required("explicit.wait.seconds");
        if (raw.contains("PLACEHOLDER_")) {
            // The session start checks this again. Zero here only lets the context object exist.
            return 0L;
        }
        return Long.parseLong(raw);
    }

    private void writeAllureEnvironment(Scenario scenario) {
        // Step: store the device and platform next to the Allure results.
        // Allure shows this file as environment details. The values are still placeholders.
        String folder = System.getProperty("allure.results.directory", "target/allure-results");
        Path file = Path.of(folder, "environment.properties");
        Properties properties = new Properties();
        properties.setProperty("platform", platform);
        properties.setProperty("scenario", scenario.getName());
        properties.setProperty("android.device.name", environment.required("android.device.name"));
        properties.setProperty("ios.device.name", environment.required("ios.device.name"));
        properties.setProperty("appium.server.url", environment.required("appium.server.url"));
        try {
            Files.createDirectories(file.getParent());
            try (var output = Files.newOutputStream(file)) {
                properties.store(output, "Filled from config/environment.yaml");
            }
        } catch (IOException ex) {
            ScenarioLogger.warn("Could not write Allure environment file. " + ex.getMessage());
        }
    }
}
