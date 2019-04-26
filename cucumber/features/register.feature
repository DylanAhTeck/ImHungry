Feature: Test the Register page

Background:

  Given I am on the Register page

Scenario: S601 - Test that the background color of the page is 466D9F

  Then The background color of the page should be 466D9F

Scenario: S602 - Test that a email input field exists on the login page

  Then I should see a email input field

Scenario: S603 - Test that a password input field exists on the login page

  Then I should see a password input field

Scenario: S604 - Test that a register button exists on the login page

  Then I should see a register button
