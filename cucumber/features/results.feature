Feature: Test the Results page

Background:

	Given I am on the Search page

Scenario: S101 - Test that the collage is displayed

	Given I am on the Search page
	When I perform a search for 'Burgers'
	Then I should see a collage of photos of 'Burgers'

Scenario: S102 - Test that a title of the form 'Results for F' is displayed

	Given I am on the Search page
	When I perform a search for 'Burgers'
	Then I should see a title of 'Results for Burgers'

Scenario: S103 - Test that the 'list management' dropdown box exists and is empty by default

	Given I am on the Results page
	Then I should see a 'list management' dropdown box which is empty by default

Scenario: S104 - Test that the 'list management' dropdown box exists and contains selectable options for the predefined lists

	Given I am on the Results page
	Then The 'list management' dropdown box should contain selectable options for the predefined lists

Scenario: S105 - Test that the 'Manage List' button exists and causes a transition to the 'To Explore' list page when clicked

	Given I am on the Results page
	When I select the 'To Explore' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should be on the 'To Explore' list management page

Scenario: S106 - Test that the 'Manage List' button causes a transition to the 'Favorites' list page when clicked

	Given I am on the Results page
	When I select the 'Favorites' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should be on the 'Favorites' list management page

Scenario: S107 - Test that the 'Manage List' button causes a transition to the 'Do Not Show' list page when clicked

	Given I am on the Results page
	When I select the 'Do Not Show' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should be on the 'Do Not Show' list management page

Scenario: S108 - Test that the 'Manage List' button does not cause a transition to the list management page

	Given I am on the Results page
	When I click on the 'Manage List' button
	Then I should be on the Results Page

Scenario: S109 - Test that there is a column of results with the title 'Restaurants'

	Given I am on the Results page
	Then I should see a column of results with the title 'Restaurants'

Scenario: S110 - Test that X restaurants are shown in the results

	Given I am on the Search page
	When I perform a search for 'Burgers' and input two search results
	Then I should see two restaurant search results on the Results page

Scenario: S111 - Test that each restaurant listed includes the name, address, star rating, minutes of driving, and price range of the restaurant

	Given I am on the Search page
	When I perform a search for 'Burgers' and input two search results
	Then I should see the name, address, star rating, minutes of driving, and price range of each restaurant listed

Scenario: S112 - Test that clicking on a restaurant will take the user to the Restaurant Page

	Given I am on the Search page
	When I perform a search for 'Burgers' and click on the first restaurant result
	Then I should be on the Restaurant Page

Scenario: S113 - Test that there is a column of results with the title 'Recipes'

	Given I am on the Results page
	Then I should see a column of results with the title 'Recipes'

Scenario: S114 - Test that X recipes are shown in the results

	Given I am on the Search page
	When I perform a search for 'Burgers' and input two search results
	Then I should see two recipe search results on the Results page

Scenario: S115 - Test that each recipe listed includes the name, star rating, prep time, and cook time of the recipe

	Given I am on the Search page
	When I perform a search for 'Burgers' and input two search results
	Then I should see the name, star rating, prep time, and cook time of each recipe listed

Scenario: S116 - Test that clicking on a recipe will take the user to the Recipe Page

	Given I am on the Search page
	When I perform a search for 'Burgers' and click on the first recipe result
	Then I should be on the Recipe Page

Scenario: S117 - Test that an item in the recipe list may be added to the 'To Explore' List

	Given I am on the Search page
	When I perform a search for 'Burgers' and click on the first recipe result
	And I add the recipe to the 'To Explore' list
	And I click on the 'Back to Results' button
	And I select the 'To Explore' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should see the item in the 'To Explore' List

Scenario: S118 - Test that an item in the restaurant list may be added to the 'To Explore' List

	Given I am on the Search page
	When I perform a search for 'Burgers' and click on the first restaurant result
	And I add the restaurant to the 'To Explore' list
	And I click on the 'Back to Results' button
	And I select the 'To Explore' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should see the item in the 'To Explore' List

Scenario: S119 - Test that an item in the recipe list may be added to the 'Favorites' List

	Given I am on the Search page
	When I perform a search for 'Burgers' and click on the first recipe result
	And I add the recipe to the 'Favorites' list
	And I click on the 'Back to Results' button
	And I select the 'Favorites' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should see the item in the 'Favorites' List

Scenario: S120 - Test that an item in the restaurant list may be added to the 'Favorites' List

	Given I am on the Search page
	When I perform a search for 'Burgers' and click on the first restaurant result
	And I add the restaurant to the 'Favorites' list
	And I click on the 'Back to Results' button
	And I select the 'Favorites' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should see the item in the 'Favorites' List

Scenario: S121 - Test that an item in the recipe list may be added to the 'Do Not Show' List

	Given I am on the Search page
	When I perform a search for 'Burgers' and click on the first recipe result
	And I add the recipe to the 'Do Not Show' list
	And I click on the 'Back to Results' button
	And I select the 'Do Not Show' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should see the item in the 'Do Not Show' List

Scenario: S122 - Test that an item in the restaurant list may be added to the 'Do Not Show' List

	Given I am on the Search page
	When I perform a search for 'Burgers' and click on the first restaurant result
	And I add the restaurant to the 'Do Not Show' list
	And I click on the 'Back to Results' button
	And I select the 'Do Not Show' option from the 'list management' dropdown box
	And I click on the 'Manage List' button
	Then I should see the item in the 'Do Not Show' List

Scenario: S123 - Test that an item in the 'Favorites' list is ranked ahead of items not in the favorite list

	Given I am on the Search page
	When I perform a search for 'Burgers' and input two search results
	And I add the second search result to the 'Favorites' list
	And I click on the 'Back to Results' button
	Then I should see the order of the two results has switched

Scenario: S124 - Test that an item in the 'Do Not Show' list does not appear in the results list

	Given I am on the Search page
	When I perform a search for 'Burgers'
	And I click on the first restaurant
	And I add the restaurant to the 'Do Not Show' list
	And I click on the 'Back to Results' button
	Then I should not see the prior first result in the results list

Scenario: S125 - Test that there exists a 'Return to Search Page' button which transitions the user to the Search Page when clicked

	Given I am on the Results page
	When I click on the 'Return to Search Page' button
	Then I should be on the Search Page

Scenario: S126 - Test that rows in the lists alternate their background color between two different shades of gray

	Given I am on the Search page
	When I perform a search for 'Burgers' and input two search results
	Then I should see rows with alternating shades of gray on the Search Page

Scenario: S127 - Test that the page displays a collage of photos of the query

	Given I am on the Search page
	When I perform a search for 'Burgers' and input two search results
	Then I should see the collage

Scenario: S128 - Test that there exists a pagination section below results

	Given I am on the Search page
	When I perform a search for 'Curry' and input 10 search results
	Then I should see a pagination section

Scenario: S129 - Test that clicking on the button for page 2 correctly loads new results

	Given I am on the Search page
	When I perform a search for 'Curry' and input 10 search results
	And I click on the 'page 2' button
	Then I should see new results loaded

Scenario: S130 - Test that clicking on the Prev button correctly shows the previous results

	When I click on the 'Prev' button
	Then I should see previous results loaded

Scenario: S131 - Test that clicking on the Next button correctly shows the previous results

	When I click on the 'Next' button
	Then I should see next results loaded
