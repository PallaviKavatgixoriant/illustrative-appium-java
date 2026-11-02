# Same scenario on Android and iOS.
# The expected letters differ by platform and live in config/testdata.yaml.
# TAT-64 checks alphabetical order.
# TAT-66 checks the empty category letters from Test_Case.xlsx.
# TAT-67 checks the letters that must have 3 or more manufacturers.

Feature: Part 2 category list

  Scenario Outline: <testId> collect the Part 2 list and check it
    Given the mobile app is open on "Part 2"
    When the user scrolls through the list and stores every row
    Then the Part 2 check "<check>" passes for test "<testId>"

    @TAT-64
    Examples: alphabetical order
      | testId | check              |
      | TAT-64 | alphabetical order |

    @TAT-66
    Examples: empty categories
      | testId | check            |
      | TAT-66 | empty categories |

    @TAT-67
    Examples: categories with three or more items
      | testId | check                               |
      | TAT-67 | categories with 3 or more items     |
