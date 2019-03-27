Feature: Test the Search page

Background:

	Given I am on the Search page

Scenario: S1 - Test that the color of the search page is white smoke

	Then The background color of the page should be white smoke

Scenario: S2 - Test that the color of the results page is white smoke

	Given I am on the Results page
	Then The background color of the page should be white smoke

Scenario: S3 - Test that the color of the recipe page is white smoke

	Given I am on the Recipe page
	Then The background color of the page should be white smoke

Scenario: S4 - Test that the color of the restaurant page is white smoke

	Given I am on the Restaurant page
	Then The background color of the page should be white smoke

Scenario: S5 - Test that the color of the list management page is white smoke

	Given I am on the List Management page
	Then The background color of the page should be white smoke

Scenario: S6 - Test that a text box with a prompt of 'Enter Food' exists

	Given I am on the Search page
	Then There should be a text box with a prompt of 'Enter Food'

Scenario: S7 - Test that a text box with hover-over text 'Number of items to show in results' exists

	Given I am on the Search page
	Then There should be a text box for entering the number of search results with hover text that says 'Number of items to show in results'

Scenario: S8 - Test that the text box for entering the number of search results has a default value of 5.

	Given I am on the Search page
	Then The 'number of search results' text box should have a default value of 5

Scenario: S9 - Test that the 'number of search results' text box only takes integer values greater than or equal to 1

	Given I am on the Search page
	Then The 'number of search results' text box should only take integer values greater than or equal to 1

Scenario: S10 - Test that a button with the label 'Feed Me!' exists

	Given I am on the Search page
	Then There should be a button with the label 'Feed Me!'

Scenario: S11 - Test that a transition from the search page to the Results Page occurs if the 'Feed Me!' button is pressed and there is text entered into the 'Enter Food' text box

	Given I am on the Search page
	When I enter text into the 'Enter Food' text box
	And I click on the 'Feed Me!' button
	Then I should be on the Results Page

Scenario: S12 - Test that a transition from the search page to the Results Page does not occur if the 'Feed Me!' button is pressed and there is no text entered into the 'Enter Food' text box

	Given I am on the Search page
	When I click on the 'Feed Me!' button
	Then I should be on the Search Page

Scenario: S13 - Test that the 'Feed Me!' button is an image with non-black text

	Given I am on the Search page
	Then The 'Feed Me!' button should be an image with non-black text

Scenario: S14 - Test that the grumpy emoji face changes to a smiling emoji face when clicked

	Given I am on the Search page
	When I click on the grumpy emoji face
	Then I should see a smiling emoji face
