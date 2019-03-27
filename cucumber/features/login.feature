Feature: Test the Login Page

Background:

  Given I am on the Login page

Scenario: S501 - Test that the background color of the page is white smoke

  Then The background color of the page should be white smoke

Scenario: S502 - Test that a email input field exists on the login page

  Then I should see a email input field

Scenario: S503 - Test that a password input field exists on the login page

  Then I should see a password input field

Scenario: S504 - Test that a login button exists on the login page

  Then I should see a login button

Scenario: S505 - Test that a register button exists on the login page

  Then I should see a register button

Scenario: S506 - Test that clicking on the register button redirects to the register page

  When I click on the register button
  Then I should be on the Register page
