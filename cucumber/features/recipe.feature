Feature: Test the Recipe page

Background:
  Given I am on the Login page
  And I login with valid credentials

Scenario: S301 - Test that page displays the title of the recipe

  Given I am on the Login page
  And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first recipe result for 'Burgers'
  Then I should see the recipe name

Scenario: S302 - Test that page displays a picture of the dish

  Given I am on the Login page
  And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first recipe result for 'Burgers'
  Then I should see the recipe image

Scenario: S303 - Test that page displays prep and cook times of the recipe

  Given I am on the Login page
  And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first recipe result for 'Burgers'
  Then I should see the recipe prep and cook times

Scenario: S304 - Test that page lists all ingredients

  Given I am on the Login page
  And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first recipe result for 'Burgers'
  Then I should see the recipe ingredients

Scenario: S305 - Test that page lists step by step instructions

  Given I am on the Login page
  And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first recipe result for 'Burgers'
  Then I should see the recipe instructions

Scenario: S306 - Test that the 'Printable Version' button exists
  Given I am on the Login page
And I login with valid credentials
When I perform a search for 'Burgers'
And I click on the first recipe result for 'Burgers'
  Then 'Printable Version' button should exist

Scenario: S307 - Test that the 'Back to Results' exists and redirects to Results page generated for F on click

  Given I am on the Login page
  And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first recipe result for 'Burgers'
  And I click on the 'Back to Results' button
  Then I am on the Results page with Results generated for F

Scenario: S308 - Test that the 'list select' dropdown box exists and is empty by default

  Given I am on the Login page
  And I login with valid credentials
  When I perform a search for 'Burgers'
  And I click on the first recipe result for 'Burgers'
  Then I should see a 'list select' dropdown box which is empty by default
