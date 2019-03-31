Given(/^I am on the Search page$/) do
	visit "#{Constants.file_path}search.html"
end

Given(/^I am on the Results page$/) do
	visit "#{Constants.file_path}results.html"
end

Given(/^I am on the Recipe page$/) do
	visit "#{Constants.file_path}recipe.html"
end

Given(/^I am on the Restaurant page$/) do
	visit "#{Constants.file_path}restaurant.html"
end

Given(/^I am on the List Management page$/) do
	visit "#{Constants.file_path}manage.html"
end

Given(/^I am on the Login page$/) do
	visit "#{Constants.file_path}login.html"
end

Given(/^I am on the Register page$/) do
	visit "#{Constants.file_path}login.html"
end

Then(/^The background color of the page should be white smoke$/) do
	color = find('body').native.css_value('background-color')
   	expect(color).to eq('rgba(245, 245, 245, 1)')
end

Then(/^There should be a text box with a prompt of 'Enter Food'$/) do
	expect(page).to have_field('query')
	expect(find('#query')['placeholder']).to eq 'Enter Food'
end

Then(/^There should be a text box for entering the number of search results with hover text that says 'Number of items to show in results'$/) do
	expect(page).to have_field('num-results')
	find('#num-results').hover
	expect(page).to have_content('Number of items to show in results')
end

Then(/^The 'number of search results' text box should have a default value of 5$/) do
	expect(find_field('num-results').value).to eq '5'
end

Then(/^The 'number of search results' text box should only take integer values greater than or equal to 1$/) do
	fill_in('query', with: "Burgers")
	fill_in('num-results', with: "-1")
	find('#feedMeButton').click
	expect(page).to have_current_path("#{Constants.file_path}search.html")
	fill_in('query', with: "Burgers")
	fill_in('num-results', with: "2.5")
	find('#feedMeButton').click
	expect(page).to have_current_path("#{Constants.file_path}search.html")
	fill_in('query', with: "Burgers")
	fill_in('num-results', with: "1")
	find('#feedMeButton').click
	expect(page).to have_current_path("#{Constants.file_path}results.html")
end

Then(/^There should be a button with the label 'Feed Me!'$/) do
	expect(page).to have_content 'Feed Me!'
end

When(/^I enter text into the 'Enter Food' text box$/) do
	fill_in('query', with: "Burgers")
end

When(/^I click on the 'Feed Me!' button$/) do
	find('#feedMeButton').click
end

Then(/^I should be on the Results Page$/) do
	expect(page).to have_current_path("#{Constants.file_path}results.html")
end

Then(/^I should be on the Search Page$/) do
	expect(page).to have_current_path("#{Constants.file_path}search.html")
end

Then(/^The 'Feed Me!' button should be an image with non-black text$/) do
	expect(find('#frownIcon')['class']).to have_content 'fas fa-frown'
	color = find('#feedMeText').native.css_value('color')
   	expect(color).not_to eq('rgba(0, 0, 0, 1)')
end

When(/^I click on the grumpy emoji face$/) do
	fill_in('query', with: "Burgers")
	fill_in('num-results', with: "1")
	find('#feedMeButton').click
end

Then(/^I should see a smiling emoji face$/) do
	expect(find('#smileIcon').native.css_value('opacity')).not_to eq '0'
end

When(/^I perform a search for 'Burgers'$/) do
	fill_in('query', with: "Burgers")
	fill_in('num-results', with: "1")
	find('#feedMeButton').click
end

Then(/^I should see a collage of photos of 'Burgers'$/) do
	expect(page).to have_css("img")
end

Then(/^I should see a title of 'Results for Burgers'$/) do
	expect(page).to have_content('Results for Burgers')
end

Then(/^I should see a 'list management' dropdown box which is empty by default$/) do
	expect(page).to have_field('list-select')
	expect(find_field('list-select').value).to eq ''
end

Then(/^The 'list management' dropdown box should contain selectable options for the predefined lists$/) do
	select('To Explore', from: 'list-select')
	select('Favorites', from: 'list-select')
	select('Do Not Show', from: 'list-select')
end

When(/^I select the 'To Explore' option from the 'list management' dropdown box$/) do
	select('To Explore', from: 'list-select')
end

When(/^I click on the 'Manage List' button$/) do
	find('#manage-list').click
end

Then(/^I should be on the 'To Explore' list management page$/) do
	expect(page).to have_current_path("#{Constants.file_path}manage.html")
	expect(page).to have_content('To Explore')
end

When(/^I select the 'Favorites' option from the 'list management' dropdown box$/) do
	select('Favorites', from: 'list-select')
end

Then(/^I should be on the 'Favorites' list management page$/) do
	expect(page).to have_current_path("#{Constants.file_path}manage.html")
	expect(page).to have_content('Favorites')
end

When(/^I select the 'Do Not Show' option from the 'list management' dropdown box$/) do
	select('Do Not Show', from: 'list-select')
end

Then(/^I should be on the 'Do Not Show' list management page$/) do
	expect(page).to have_current_path("#{Constants.file_path}manage.html")
	expect(page).to have_content('Do Not Show')
end

Then(/^I should see a column of results with the title 'Restaurants'$/) do
	expect(page).to have_content('Restaurants')
end

When(/^I perform a search for 'Burgers' and input two search results$/) do
	fill_in('query', with: "Burgers")
	fill_in('num-results', with: "2")
	find('#feedMeButton').click
end

Then(/^I should see two restaurant search results on the Results page$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	expect(find('#restaurants')['childElementCount']).to eq '3'
end

Then(/^I should see the name, address, star rating, minutes of driving, and price range of each restaurant listed$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	page.all(:css, '.restaurant-title').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
	page.all(:css, '.restaurant-address').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
	page.all(:css, '.restaurant-rating').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
	page.all(:css, '.restaurant-distance').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
	page.all(:css, '.restaurant-price').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
end

When(/^I perform a search for 'Burgers' and click on the first restaurant result$/) do
	fill_in('query', with: "Burgers")
	fill_in('num-results', with: "1")
	find('#feedMeButton').click
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	find('#restaurant_0').click
end

Then(/^I should be on the Restaurant Page$/) do
	expect(page).to have_current_path("#{Constants.file_path}restaurant.html")
end

Then(/^I should see a column of results with the title 'Recipes'$/) do
	expect(page).to have_content('Recipes')
end

Then(/^I should see two recipe search results on the Results page$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	expect(find('#recipes')['childElementCount']).to eq '3'
end

Then(/^I should see the name, star rating, prep time, and cook time of each recipe listed$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	page.all(:css, '.recipe-title').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
	page.all(:css, '.recipe-rating').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
	page.all(:css, '.recipe-prep-time').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
	page.all(:css, '.recipe-cook-time').each do |el|
		expect(el['innerHTML']).not_to eq ''
	end
end

When(/^I perform a search for 'Burgers' and click on the first recipe result$/) do
	fill_in('query', with: "Burgers")
	fill_in('num-results', with: "1")
	find('#feedMeButton').click
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	find('#recipe_0').click
end

Then(/^I should be on the Recipe Page$/) do
	expect(page).to have_current_path("#{Constants.file_path}recipe.html")
end

When(/^I add the recipe to the 'To Explore' list$/) do
	select('To Explore', from: 'listDropdown')
	find('#addToListButton').click
	$currentItemTitle = find('#recipeTitle')['innerHTML']
end

Then(/^I should see the item in the 'To Explore' List$/) do
	expect(page).to have_content $currentItemTitle
end

When(/^I add the restaurant to the 'To Explore' list$/) do
	select('To Explore', from: 'listDropdown')
	find('#addToListButton').click
	$currentItemTitle = find('#restaurantName')['innerHTML']
end

When(/^I add the recipe to the 'Favorites' list$/) do
	select('Favorites', from: 'listDropdown')
	find('#addToListButton').click
	$currentItemTitle = find('#recipeTitle')['innerHTML']
end

Then(/^I should see the item in the 'Favorites' List$/) do
	expect(page).to have_content $currentItemTitle
end

When(/^I add the restaurant to the 'Favorites' list$/) do
	select('Favorites', from: 'listDropdown')
	find('#addToListButton').click
	$currentItemTitle = find('#restaurantName')['innerHTML']
end

When(/^I add the recipe to the 'Do Not Show' list$/) do
	select('Do Not Show', from: 'listDropdown')
	find('#addToListButton').click
	$currentItemTitle = find('#recipeTitle')['innerHTML']
end

Then(/^I should see the item in the 'Do Not Show' List$/) do
	expect(page).to have_content $currentItemTitle
end

When(/^I add the restaurant to the 'Do Not Show' list$/) do
	select('Do Not Show', from: 'listDropdown')
	find('#addToListButton').click
	$currentItemTitle = find('#restaurantName')['innerHTML']
end

When(/^I add the second search result to the 'Favorites' list$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	find('#restaurant_1').click
	select('Favorites', from: 'listDropdown')
	find('#addToListButton').click
	$secondResult = find('#restaurantName')['innerHTML']
end

Then(/^I should see the order of the two results has switched$/) do
	find('#restaurant_0').click
	expect(find('#restaurantName')['innerHTML']).to eq $secondResult
end

When(/^I click on the first restaurant$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	find('#restaurant_0').click
end

Then(/^I should not see the prior first result in the results list$/) do
	expect(page).not_to have_content $currentItemTitle
end

When(/^I click on the 'Return to Search Page' button$/) do
	expect(page).to have_content 'Return to Search Page'
	find('#backToSearchButton').click
end

When(/^I click on the first restaurant result for 'Burgers'$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	find('#restaurants .card').click
end

Then(/^I should see the restaurant name$/) do
	expect(page).to have_selector('#restaurantName')
end

Then(/^I should see the restaurant address$/) do
	expect(page).to have_selector('#address')
end

Then(/^I should see the restaurant phone number$/) do
	expect(page).to have_selector('#phoneNumber')
end

Then(/^I should see the restaurant website link$/) do
	expect(page).to have_selector('#website')
end

Then(/^I should be presented with the correct information for 'The Habit Burger Grill'$/) do
	expect(page).to have_current_path("#{Constants.file_path}restaurant.html")
	expect(find('#restaurantName')['innerHTML']).to eq 'The Habit Burger Grill'
	expect(find('#address')['innerHTML']).to eq '3607 Trousdale Pkwy, Los Angeles, CA 90089, USA'
	expect(find('#phoneNumber')['innerHTML']).to eq '(213) 740-2311'
	expect(find('#website')['innerHTML']).to eq 'https://www.habitburger.com/locations/echo-park/'
end

Then(/^'Printable Version' button should exist$/) do
	expect(page).to have_selector('#printable-version')
end

When(/^I click on the 'Back to Results' button$/) do
	expect(page).to have_selector('#back-to-results')
	click_on('Back to Results')
end

Then(/^I am on the Results page with Results generated for F$/) do
	expect(page).to have_current_path("#{Constants.file_path}results.html")
	expect(find('#query')['textContent']).to eq 'Burgers'
end

Then(/^I should see a 'list select' dropdown box which is empty by default$/) do
 	expect(page).to have_selector('#listDropdown')
	expect(find_field('listDropdown').value).to eq ''
end

When(/^I click on the address for 'The Habit Burger Grill'$/) do
	find('#address').click
end

Then(/^I should be redirected to Google Maps directions page with destination prefilled and starting point set to Tommy Trojan$/) do
	expect(page).to have_current_path('https://www.google.com/maps/dir/Tommy+Trojan/3607+Trousdale+Pkwy,+Los+Angeles,+CA+90089,+USA')
end

When(/^I click on the website for 'The Habit Burger Grill'$/) do
	find('#website').click
end

Then(/^I should be redirected to the restaurant home page$/) do
	expect(page).to have_current_path('https://www.habitburger.com/locations/echo-park/')
end

Then(/^I should see the recipe name$/) do
	expect(page).to have_selector('#recipeTitle')
end

Then(/^I should see the recipe image$/) do
	expect(page).to have_selector('#recipe-image')
end

Then(/^I should see the recipe prep and cook times$/) do
	expect(page).to have_selector('#prepTime')
	expect(page).to have_selector('#cookTime')
end

Then(/^I should see the recipe ingredients$/) do
	expect(page).to have_content('Ingredients')
	expect(page).to have_selector('#firstIngredientsList')
	expect(page).to have_selector('#secondIngredientsList')
end

Then(/^I should see the recipe instructions$/) do
	expect(page).to have_content('Instructions')
	expect(page).to have_selector('#instructionsList')
end

When(/^I click on the first recipe result for 'Burgers'$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	find('#recipes .card').click
end

When(/^I click on the Remove button for the first item$/) do
	find('.remove_0').click
end

Then(/^I should not see the item in the 'To Explore' List$/) do
	expect(page).not_to have_content $currentItemTitle
end

When(/^I select the 'Favorites' option for moving to another list$/) do
	select('Favorites', from: find('.move_select_0')['id'])
	find('.move_0').click
end

When(/^I click on the 'Results Page' button$/) do
	find('#back-to-results').click
end

When(/^I click on the 'Search Page' button$/) do
	find('#backToSearchButton').click
end

Then(/^I should be on the List Management Page$/) do
	expect(page).to have_current_path("#{Constants.file_path}manage.html")
	expect(page).to have_content('Favorites')
end

When(/^I click on the restaurant in the 'Favorites' list$/) do
    $itemName = find('.restaurant-name')['innerHTML'];
    Capybara.match = :first
    find('.card').click
end

Then(/^I should be on the Restaurant page for the item$/) do
    expect(page).to have_current_path("#{Constants.file_path}restaurant.html")
    expect(find('#restaurantName')['innerHTML']).to eq $itemName
end

Then(/^I should see rows with alternating shades of gray on the Search Page$/) do
    Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	expect(find('#restaurant_0')['class']).to have_text('bg-secondary')
	expect(find('#restaurant_1')['class']).to have_text('bg-dark')
end

When(/^I click on the first restaurant result$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	find('#restaurant_0').click
end

When(/^I click on the second restaurant result$/) do
	Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	find('#restaurant_1').click
end

Then(/^I should see rows with alternating shades of gray on the List Management Page$/) do
  Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')
	expect(find('.card_0')['class']).to have_text('bg-secondary')
	expect(find('.card_1')['class']).to have_text('bg-dark')
end

Then(/^I should see the collage$/) do
    Capybara.default_max_wait_time = 10
	expect(page).to have_css('.card')

	expect(page).to have_css("#collage")
	expect(page).to have_css(".dynamicImage")
end

When(/^I click on the 'Results Page' button on the List Management Page$/) do
    find('#backToResultsButton').click
end

Then(/^I should see a email input field$/) do
	expect(page).to have_css('#email')
end

Then(/^I should see a password input field$/) do
	expect(page).to have_css('#password')
end

Then(/^I should see a login button$/) do
	expect(page).to have_css('#login-btn')
end

Then(/^I should see a register button$/) do
	expect(page).to have_css('#register-btn')
end

When(/^I click on the register button$/) do
	find('#register-btn').click
end

Then(/^I should be on the Register page$/) do
	expect(page).to have_current_path("#{Constants.file_path}register.html")
end

Then(/^I should be on the Login page$/) do
	expect(page).to have_current_path("#{Constants.file_path}login.html")
end

When(/^I type in a correct email$/) do
	fill_in('email', with: "test_wayne@test.com")
end

When(/^I type in a wrong password$/) do
	fill_in('password', with: "wrong_password")
end

When(/^I type in a correct password$/) do
	fill_in('password', with: "password")
end

When(/^I click on the login button$/) do
	find('#login-btn').click
end

Then(/^I should see an error message displayed$/) do
	expect(page).to have_content("The password is invalid or the user does not have a password.")
end

When(/^I click on the logout button$/) do
	find('#logout-btn').click
end
