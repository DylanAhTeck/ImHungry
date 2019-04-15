Feature: Test the List Management page

Background:

	Given I am on the Login page
	And I login with valid credentials

Scenario: S401 - Test that clicking on an item takes the user to the corresponding restaurant or recipe page

 	Given I am on the Login page
	And I login with valid credentials
  When I perform a search for 'Burgers' and click on the first restaurant result
  And I add the restaurant to the 'Favorites' list
  And I click on the 'Back to Results' button
  And I select the 'Favorites' option from the 'list management' dropdown box
  And I click on the 'Manage List' button
  And I click on the restaurant in the 'Favorites' list
	Then I should be on the Restaurant page for the item

Scenario: S402 - Test that an item may be removed from a list

	Given I am on the Login page
	And I login with valid credentials
	When I perform a search for 'Burgers' and click on the first recipe result
	And I add the recipe to the 'To Explore' list
	And I click on the 'Back to Results' button
	And I select the 'To Explore' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	And I click on the Remove button for the first item
	Then I should not see the item in the 'To Explore' List

Scenario: S403 - Test that an item may be moved to another list

	Given I am on the Login page
	And I login with valid credentials
	When I perform a search for 'Burgers' and click on the first recipe result
	And I add the recipe to the 'To Explore' list
	And I click on the 'Back to Results' button
	And I select the 'To Explore' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	And I select the 'Favorites' option for moving to another list
	And I select the 'Favorites' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should see the item in the 'Favorites' List

Scenario: S404 - Test that the 'Results Page' button takes the user back to the Results Page

	Given I am on the Login page
	And I login with valid credentials
	When I perform a search for 'Burgers'
	And I select the 'Favorites' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	And I click on the 'Results Page' button on the List Management Page
	Then I should be on the Results Page

Scenario: S405 - Test that the 'Return to Search Page' button takes the user back to the Search Page

	Given I am on the Login page
	And I login with valid credentials
	When I perform a search for 'Burgers'
	And I select the 'Favorites' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	And I click on the 'Search Page' button
	Then I should be on the Search Page

Scenario: S406 - Test that the 'list management' dropdown box exists and is empty by default

	Given I am on the List Management page
	Then I should see a 'list management' dropdown box which is empty by default

Scenario: S407 - Test that the 'list management' dropdown box exists and contains selectable options for the predefined lists

	Given I am on the List Management page
	Then The 'list management' dropdown box should contain selectable options for the predefined lists

Scenario: S408 - Test that the 'Manage List' button exists and causes a transition to the 'To Explore' list page when clicked

	Given I am on the List Management page
	When I select the 'To Explore' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should be on the 'To Explore' list management page

Scenario: S409 - Test that the 'Manage List' button causes a transition to the 'Favorites' list page when clicked

	Given I am on the List Management page
	When I select the 'Favorites' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should be on the 'Favorites' list management page

Scenario: S410 - Test that the 'Manage List' button causes a transition to the 'Do Not Show' list page when clicked

	Given I am on the List Management page
	When I select the 'Do Not Show' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should be on the 'Do Not Show' list management page

Scenario: S411 - Test that the 'Manage List' button does not cause a transition to the list management page

	Given I am on the List Management page
	When I click on the 'Manage List' button
	Then I should be on the List Management Page

Scenario: S412 - Test that rows in the lists alternate their background color between two different shades of gray

	Given I am on the Login page
	And I login with valid credentials
	When I perform a search for 'Burgers' and input two search results
	And I click on the first restaurant result
	And I add the restaurant to the 'To Explore' list
	And I click on the 'Results Page' button
	And I click on the second restaurant result
	And I add the restaurant to the 'To Explore' list
	And I click on the 'Results Page' button
	And I select the 'To Explore' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should see rows with alternating shades of gray on the List Management Page

# Scenario: S413 - Test that reordering of lists work properly
#
# 	Given I am on the List Mnaagement page
# 	When I perform a search for 'Burgers' and input two search results
# 	And I select the 'To Explore' option from the 'list management' dropdown box
# 	And I click on the 'Manage List' button
# 	And I click on the Move Down button for the first item
# 	Then the first item should be in second from the top
