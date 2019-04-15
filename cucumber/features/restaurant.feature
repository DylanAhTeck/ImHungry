Feature: Test the Restaurant page

Background:
  Given I am on the Login page
And I login with valid credentials

Scenario: S201 - Test that clicking on a result redirects to the restaurant page

  Given I am on the Login page
And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first restaurant result for 'Burgers'
  Then I am on the Restaurant page

Scenario: S202 -  Test that restaurant name is displayed

  Given I am on the Login page
And I login with valid credentials
When I perform a search for 'Burgers'
And I click on the first restaurant result for 'Burgers'
  Then I should see the restaurant name

Scenario: S203 -  Test that restaurant address is displayed

  Given I am on the Login page
And I login with valid credentials
When I perform a search for 'Burgers'
And I click on the first restaurant result for 'Burgers'
  Then I should see the restaurant address

Scenario: S204 -  Test that restaurant phone number is displayed

  Given I am on the Login page
And I login with valid credentials
When I perform a search for 'Burgers'
And I click on the first restaurant result for 'Burgers'
  Then I should see the restaurant phone number

Scenario: S205 -  Test that restaurant website link is displayed

  Given I am on the Login page
And I login with valid credentials
When I perform a search for 'Burgers'
And I click on the first restaurant result for 'Burgers'
  Then I should see the restaurant website link

Scenario: S206 - test that the restaurant information is correctly rendered

  Given I am on the Login page
And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first restaurant result for 'Burgers'
  Then I should be presented with the correct information for 'The Habit Burger Grill'

Scenario: S207 - Test that the 'Printable Version' button exists

  Given I am on the Login page
And I login with valid credentials
When I perform a search for 'Burgers'
And I click on the first restaurant result for 'Burgers'
  Then 'Printable Version' button should exist

Scenario: S208 - Test that the 'Back to Results' exists and redirects to Results page generated for F on click

  Given I am on the Login page
And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first restaurant result for 'Burgers'
  And I click on the 'Back to Results' button
  Then I am on the Results page with Results generated for F

Scenario: S209 - Test that the 'list select' dropdown box exists and is empty by default

  Given I am on the Login page
And I login with valid credentials
When I perform a search for 'Burgers'
And I click on the first restaurant result for 'Burgers'
  Then I should see a 'list select' dropdown box which is empty by default

Scenario: S210 - Test that clicking on the address redirects to Google Maps directions page with the destination prefilled with the address of the restaurant and starting point as USC (Tommy Trojan)
  Given I am on the Login page
And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first restaurant result for 'Burgers'
  And I click on the address for 'The Habit Burger Grill'
  Then I should be redirected to Google Maps directions page with destination prefilled and starting point set to Tommy Trojan

Scenario: S211 - Test that clicking on the website link takes you to the restaurant’s home page, if it exists
  Given I am on the Login page
And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first restaurant result for 'Burgers'
  And I click on the website for 'The Habit Burger Grill'
  Then I should be redirected to the restaurant home page
