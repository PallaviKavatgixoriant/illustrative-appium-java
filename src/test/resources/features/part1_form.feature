# Same scenario on Android and iOS. The platform comes from -Dtarget.platform.
# Selectors, usernames, and passwords are placeholders in config/testdata.yaml.
# TAT-62 from Test_Case.xlsx: correct username, correct password, Submit, popup Success.
# The Part 1 form also has an email field. TAT-62 uses the correct email shown on that screen.
# TAT-63 from Test_Case.xlsx: incorrect username, incorrect password, incorrect email,
# Submit, popup Incorrect username!

Feature: Part 1 form submission

  Scenario Outline: <testId> submit Part 1 and check the popup
    Given the mobile app is open on "Part 1"
    When the user types the username stored under "<usernameKey>"
    And the user types the email stored under "<emailKey>"
    And the user types the password stored under "<passwordKey>"
    And the user submits the current form
    Then the popup text is "<expectedPopup>" for test "<testId>"

    @TAT-62
    Examples: valid credentials
      | testId | usernameKey    | emailKey   | passwordKey    | expectedPopup |
      | TAT-62 | valid.username | valid.email | valid.password | Success       |

    @TAT-63
    Examples: incorrect username, password, and email
      | testId | usernameKey      | emailKey      | passwordKey      | expectedPopup        |
      | TAT-63 | invalid.username | invalid.email | invalid.password | Incorrect username! |
