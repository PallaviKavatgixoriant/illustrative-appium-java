# Same scenario on Android and iOS.
# Test_Case.xlsx asks for a Success popup for the section under test.
# On the device that popup line is Step 1: Success, Step 2: Success, or Step 3: Success.
# TAT-71: one selector finds every checkbox in section 1.
# TAT-72: one selector finds the checkboxes before or after the checkbox named in the section title.
# TAT-73: enable checkboxes that have Yes under them.

Feature: Part 4 checkboxes

  Scenario Outline: <testId> enable Part 4 checkboxes and submit
    Given the mobile app is open on "Part 4"
    When the user enables checkboxes using the rule "<rule>"
    And the user submits the current form
    Then the popup text is "<expectedPopup>" for test "<testId>"

    @TAT-71
    Examples: section 1
      | testId | rule                          | expectedPopup   |
      | TAT-71 | all checkboxes in the section | Step 1: Success |

    @TAT-72
    Examples: section 2
      | testId | rule                                                      | expectedPopup   |
      | TAT-72 | preceding or following checkboxes from a random checkbox | Step 2: Success |

    @TAT-73
    Examples: section 3
      | testId | rule                                | expectedPopup   |
      | TAT-73 | checkboxes that have yes under them | Step 3: Success |
