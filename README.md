# Illustrative Appium tests (Java)

This project is a teaching skeleton for the scenarios in `Test_Case.xlsx`. It uses Cucumber (Gherkin), a page object per screen, and Appium Java client 11.

The scenario text follows the spreadsheet. Selectors, credentials, server address, and screen actions are named placeholders such as `PLACEHOLDER_SELECTOR_PART1_USERNAME` and `PLACEHOLDER_ACTION_SUBMIT`. The APK was not unpacked, and no selector was taken from it.

These tests have not been compiled or executed. Nothing in this project is a passing run.

`TAT-65` is not in the spreadsheet, so there is no scenario for it.

## What each test id does

| Test | Scenario |
| --- | --- |
| TAT-62 | Part 1, valid username and password, popup `Success` |
| TAT-63 | Part 1, invalid username and correct password, popup `Incorrect username!` |
| TAT-64 | Part 2 list is alphabetical |
| TAT-66 | Part 2 letters with no manufacturers |
| TAT-67 | Part 2 letters with 3 or more manufacturers |
| TAT-68 | Part 3 Total Sum equals the sum of the item prices |
| TAT-69 | Part 3 one brand total equals that brand's prices |
| TAT-70 | Part 3 image names and the FAKEIMAGE pool |
| TAT-71 | Part 4 section 1, all checkboxes, popup `Success` |
| TAT-72 | Part 4 section 2, checkboxes before or after one checkbox, popup `Success` |
| TAT-73 | Part 4 section 3, checkboxes with `yes` under them, popup `Success` |

The same Gherkin scenario is used for Android and iOS. The platform comes from `-Dtarget.platform`. Expected letters that differ by platform live in `config/testdata.yaml`.

## Assertions from the spreadsheet, and checks that need the app

`AC_SUPPORTED` means the spreadsheet already states the expected result. `REQUIRES_APPLICATION_INFO` means the check still needs something the spreadsheet does not describe. Both notes are also written in the step classes and logged with the prefixes `ASSERTION AC_SUPPORTED` and `ASSERTION REQUIRES_APPLICATION_INFO`.

| Test | Directly supported by the spreadsheet | Needs the application, or a decision the spreadsheet does not make |
| --- | --- | --- |
| TAT-62 | Popup text is `Success` | Username, password, and every selector, including the popup |
| TAT-63 | Popup text is `Incorrect username!` | The invalid username, the correct password, and every selector |
| TAT-64 | The collected list is in alphabetical order | Case sensitivity, alphabet, and whether category headers are part of that list (`PLACEHOLDER_SORT_RULE`) |
| TAT-66 | Android empty letters: E, G, Q, U, W, X, Y, Z. iOS empty letters: E, Q, U, W, X, Y | How a row becomes a letter and a manufacturer. An empty letter may be hidden instead of shown as a blank header |
| TAT-67 | Android letters with 3 or more: A, B, C, D, F, H, L, M, R, S. iOS: A, B, C, L, M, P, R, S | How a manufacturer is counted. Other letters are not required to have fewer than 3 |
| TAT-68 | Displayed Total Sum equals the sum of the item prices | Currency format (`PLACEHOLDER_PRICE_PARSE_RULE`) and the price and total controls |
| TAT-69 | That brand total equals the sum of that brand's prices. iOS brands are `nike`, `puma`, and `adidas` | Which total is the "random" one. Android brand names change every launch and must be read from the screen |
| TAT-70 | iOS names are `FAKEIMAGE-car`, `FAKEIMAGE-macbook`, `FAKEIMAGE-phone`, `FAKEIMAGE-watch`, `FAKEIMAGE-person`, `FAKEIMAGE-cactus`, `FAKEIMAGE-book` | Android names are not listed (`PLACEHOLDER_ANDROID_FAKEIMAGE_POOL`). "Match at least one" can mean every name is in the pool, or only one name is (`PLACEHOLDER_IMAGE_MATCH_RULE`). The name's place on the screen is not described |
| TAT-71 | One selector enables every section 1 checkbox, then the popup is `Success` | The checkbox names and the selector |
| TAT-72 | Checkboxes before or after one chosen checkbox, then the popup is `Success` | The summary says "required" but does not name them. Direction is `PLACEHOLDER_SECTION2_AXIS` (`preceding` or `following`) |
| TAT-73 | Checkboxes with `yes` under them, then the popup is `Success` | The view type of that label and how far under the checkbox it sits |

## Layout

```
config/environment.yaml          server, device, app path, wait time
config/testdata.yaml             credentials and expected lists
src/test/resources/features/     one Scenario Outline per part
src/test/java/.../steps/         Gherkin steps
src/test/java/.../pages/         page objects
src/test/java/.../locators/      selector placeholder names
src/test/java/.../hooks/         session start, screenshot hook, session stop
src/test/java/.../runners/       RunCucumberTest
src/test/java/.../support/       config reader, logger, driver, assertion notes
```

A step calls a page. The page stops on a placeholder instead of calling a guessed selector. `BasePage` shows the later explicit-wait shape and does not call `Thread.sleep`. Logs use a timestamp, a level, the thread name, and a message (`ScenarioLogger`).

## What to configure before a run

1. Install JDK 17 or newer, and Maven 3.9 or newer.
2. Confirm the versions in `pom.xml` on Maven Central before the first download. The pins used here are Cucumber 7.34.9, JUnit 5.14.2, Appium Java client 11.0.0, Allure 3.0.1, and Surefire 3.5.2. Java client 11 expects Appium Server 3.
3. Install Appium Server 3 and the UiAutomator2 driver for Android. iOS also needs the XCUITest driver, Xcode, and a Mac. This Windows machine can run the Android scenarios. It cannot host an iOS simulator.
4. Start your Appium server and copy the address it prints into `appium.server.url` in `config/environment.yaml`.
5. Replace every remaining `PLACEHOLDER_*` value in `config/environment.yaml`.
   - `android.app.path` is already `apk/AppForTestAutomation.apk` in this project. Maven turns that into a full path when the session starts.
   - `android.app.package` and `android.app.activity` are not in the spreadsheet. Supply them from the app.
   - There is no iOS app file in this workspace. `ios.app.path` and `ios.bundle.id` stay empty until you have that app.
6. Replace these values in `config/testdata.yaml` after you know them: `valid.username`, `valid.password`, `invalid.username`, `sort.rule`, `price.parse.rule`, `image.match.rule`, `android.fakeimage.pool`, `section2.relative.axis`.
7. In the page classes, replace each `PlaceholderAction.stop(...)` call with the action described in the comment above it. Use an accessibility id or a resource id. Keep the explicit wait from `BasePage.waitUntilVisible`. Leave XPath for the case where neither id exists.
8. In `TestContext.captureFailureScreenshot`, uncomment the two screenshot lines so a failed scenario attaches a PNG. Allure then shows that picture with the step list.
9. Set `explicit.wait.seconds` to a whole number. The suite still must not use a fixed sleep.

Until those placeholders are replaced, a run stops with `IllustrationNotConfiguredException`. That exception is the signal that the skeleton is still incomplete. It is not a product failure and it is not a pass.

## How to execute it later

Run these commands yourself from `illustrative-appium-java`. They were not run while this project was written.

1. Open a terminal in `D:\APM\illustrative-appium-java`.
2. Start the Appium server.
3. Boot the Android emulator or connect the device whose name and udid you put in `config/environment.yaml`.
4. Run every Android scenario:

```powershell
mvn test "-Dtarget.platform=android"
```

5. Run one scenario, for example TAT-62:

```powershell
mvn test "-Dtarget.platform=android" "-Dcucumber.filter.tags=@TAT-62"
```

The tags are `@TAT-62`, `@TAT-63`, `@TAT-64`, `@TAT-66`, `@TAT-67`, `@TAT-68`, `@TAT-69`, `@TAT-70`, `@TAT-71`, `@TAT-72`, and `@TAT-73`.

6. On a Mac, with an iOS device and the iOS placeholders filled, run:

```powershell
mvn test "-Dtarget.platform=ios"
```

7. Read the logs in the console. Each line has a timestamp, a level, a thread name, and a message.
8. Open the Cucumber HTML report at `target/cucumber.html`.
9. Install the Allure 3 command-line tool from the Allure documentation, then generate the report:

```powershell
allure serve target/allure-results
```

The report is meant to show the scenario result, the Gherkin steps, a screenshot after you enable the two lines in the failure hook, and the device name written to `target/allure-results/environment.properties`.

## Android and iOS at the same time

Each process owns one device. Two processes must not share one `target` folder.

1. Copy this project folder twice, for example `illustrative-appium-java-android` and `illustrative-appium-java-ios`.
2. Fill `config/environment.yaml` in each copy for that platform.
3. Start both devices and one Appium server that can see both, or one server per machine.
4. In the Android copy, run `mvn test "-Dtarget.platform=android"`.
5. At the same time, in the iOS copy on a Mac, run `mvn test "-Dtarget.platform=ios"`.
6. Serve each copy's `target/allure-results` folder separately.

`src/test/resources/junit-platform.properties` keeps scenarios on one device sequential (`cucumber.execution.parallel.enabled=false`). The file's comments show the fixed-parallel settings to turn on only after each thread has its own device. Sharing one device across scenarios makes the run flaky.

The driver is stored in a `ThreadLocal`, so a later parallel run does not hand one session to two threads.

## First failure you should expect today

With the files as they are, `mvn test "-Dtarget.platform=android"` fails in the before-hook on `PLACEHOLDER_APPIUM_SERVER_URL` (or on `PLACEHOLDER_PLATFORM` if `-Dtarget.platform` is omitted). The Gherkin steps start only after `config/environment.yaml` is filled. The next stop is `PLACEHOLDER_ACTION_OPEN_PART`, then the placeholder for that step's selector or test data.
