# Same scenario on Android and iOS.
# TAT-68 compares the Total Sum with the sum of the item prices.
# TAT-69 compares one brand total with that brand's prices.
#   iOS brand names in the spreadsheet: nike, puma, adidas.
#   Android brand names change every launch and must be read from the screen.
# TAT-70 checks FAKEIMAGE names. The iOS pool is in config/testdata.yaml.
#   The Android pool is not in the spreadsheet.

Feature: Part 3 prices and images

  Scenario Outline: <testId> collect Part 3 rows and check them
    Given the mobile app is open on "Part 3"
    When the user scrolls through the list and stores every row
    Then the Part 3 check "<check>" passes for test "<testId>"

    @TAT-68
    Examples: total sum
      | testId | check     |
      | TAT-68 | total sum |

    @TAT-69
    Examples: one brand sum
      | testId | check     |
      | TAT-69 | brand sum |

    @TAT-70
    Examples: image names
      | testId | check            |
      | TAT-70 | fake image names |
