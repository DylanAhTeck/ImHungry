package hello;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
// import com.mashape.unirest.http.JsonNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong(0);
	private ListManager listManager = new ListManager();

	// NOTE: We'll use this to track our most recent results prior to returning to Wayne
	private ArrayList<Result> mostRecentResults = new ArrayList<Result>();



	///////////////////////////////////////////////////
	// 												 //
	// 			OUR ENDPOINTS AND ROUTES   		     //
	// 												 //
	///////////////////////////////////////////////////

	@RequestMapping("/test")
	public String handleTestRequest() {
		return "Look's like you're up and running!";
	}

	@RequestMapping("/testRecipe")
	public String handleTestRecipeRequest() {
		return getTestRecipeString();
	}

	@RequestMapping("/testSearchRecipe")
	public String handleTestRecipeRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults) {
		return retrieveRecipes(searchQuery, numResults);
	}


	@RequestMapping("/search")
	// TODO: Once the internal function calls exist, we'll need to put in the appropriate sequential calls here.
	public String handleSearchRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults) {

		return "Thanks for searching!";

	}

	// NOTE: this is a test endpoint that you can hit to make sure that you're actually adding a random item
	// to the end of the favorites list.
	@RequestMapping("/addItemToFavorites")
	public String addItemToFavorites() {
		Result tempResult = new Result(String.valueOf(counter.incrementAndGet()));
		listManager.addToList(tempResult, "favorites");
		String favoritesString = listManager.getFavorites().toString();
		return "favorites: " + favoritesString;
	}


	// TODO: Need to write this.
	@RequestMapping("/addToList")
	public String handleAddToList(@RequestParam String itemToAdd, @RequestParam String targetListName) {
		Result temp = new Result("1");
		listManager.addToList(temp, targetListName);
		return "Added item:" + temp.getUniqueId() + " to list: " + targetListName;
	}

	// TODO: Need to write this.
	@RequestMapping("/removeFromList")
	public String handleRemoveFromList(@RequestParam String itemToRemoveId, @RequestParam String originListName) {
		listManager.removeFromList(itemToRemoveId, originListName);
		return "Removed item:" + itemToRemoveId + " from list: " + originListName;
	}

	// TODO: Need to write this.
	@RequestMapping("/moveBetweenLists")
	public String handleMoveLists(@RequestParam String itemToMoveId, @RequestParam String originListName, @RequestParam String targetListName) {
		listManager.moveBetweenLists(itemToMoveId, originListName, targetListName);
		return "Moved item:" + itemToMoveId + " from list: " + originListName + " to list: " + targetListName;
	}



	///////////////////////////////////////////////////
	// 												 //
	// 	EXTERNAL API INTERACTION AND PROCESSING      //
	// NOTE: This is where the actual work happens.  //
	// 												 //
	///////////////////////////////////////////////////

	public String getTestRecipeString() {

		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("1 oz ham");
		ingredients.add("2oz cheese");
		ingredients.add("2 slices bread");

		ArrayList<String> instructions = new ArrayList<String>();
		instructions.add("1. do the thing");
		instructions.add("2. finish the thing");

		Recipe r = new Recipe("1");
		r.setIngredients(ingredients);
		r.setName("best recipe");
		r.setSourceURL("http://localhost:1000");
		r.setPrepTime(40);
		r.setInstructions(instructions);
		r.setNumStars(2);

		r.setCookTime(20);

		return r.writeToJSON();

	}

	public Result getResult(String uniqueId) {
		// TODO: iterate over the items in the most recently generated results and return it if there's a matching one.
		return new Result("placholder");
	}

	// TODO: Need to write this.

	public ArrayList<Result> retrieveRestaurants(String searchQuery, Integer numResults) {
		// TODO: Pull restaurants from external API and grab relevant information.
		return new ArrayList<Result>();
	}

	// TODO: Need to write this.
	// TODO: this will need to return an ArrayList<Result> when it's done
	public String retrieveRecipes(String searchQuery, Integer numResults) {
		// TODO: Pull recipes from external API and grab relevant information.


		/* need these pieces of information

			name
			rating
			image
			prepTime
			cookTime
			ingredients
			instructions

		*/

		ArrayList<Recipe> recipes = new ArrayList<Recipe>();


		try {
			HttpResponse<com.mashape.unirest.http.JsonNode> response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search")
					.header("X-RapidAPI-Key", "ebff0f5311msh75407f578a41008p14174ejsnf16b8bcf5559")
					.queryString("query", searchQuery)
					.queryString("maxResults", numResults)
					.asJson();

			
			String allDataString = response.getBody().toString();

			// convert to a usable jackson JSONNode
			ObjectMapper mapper = new ObjectMapper();
    		JsonNode root = mapper.readTree(allDataString);
    		
    		JsonNode resultsNode = root.path("results");




    		for (JsonNode result : resultsNode) {
    			// identify the sourceURL, use it to construct the recipes and set the unique id the uniqueID


    		}
    		
    		return actualObj.toString();


		} catch (UnirestException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		return "failure";


	}

	// should take the searchQuery as a parameter and return a path to the collage.
	// NOTE: will likely need submethods here...
	public String createCollage(String searchQuery) {
		// TODO: Pull restaurants from external API and grab relevant information.
		return "placeholder";
	}


	public String packageResponseString(ArrayList<Result> restaurants, ArrayList<Result> recipes, String collagePath) {
		return "placeholder";
	}
}