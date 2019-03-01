package hello;

import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
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

	@RequestMapping("/search")
	// TODO: Once the internal function calls exist, we'll need to put in the appropriate sequential calls here.
	public String handleSearchRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults) {

		return "Thanks for searching!";

	}

	// NOTE: this is a test endpoint that you can hit to make sure that you're actually adding a random item
	// to the end of the favorites list.
	@RequestMapping("/addItemToFavorites") 
	public String addItemToFavorites() {
		Result tempResult = new Result(String.valueOf(counter.incrementAndGet()), "{woo}");
		listManager.addToList(tempResult, "favorites");
		String favoritesString = listManager.getFavorites().toString();
		return "favorites: " + favoritesString;
	}


	// TODO: Need to write this.
	@RequestMapping("/addToList")
	public String handleAddToList(@RequestParam String itemToAdd, @RequestParam String targetListName) {
		Result temp = new Result("1", "temp");
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

	public Result getResult(String uniqueId) {
		// TOOD: iterate over the items in the most recently generated results and return it if there's a matching one.
		return new Result("placholder");
	}
	
	// TOOD: Need to write this. 
	public ArrayList<Result> retrieveRestaurants(String searchQuery, Integer numResults) {
		// TODO: Pull restaurants from external API and grab relevant information.
		return new ArrayList<Result>();
	}

	// TOOD: Need to write this.
	public ArrayList<Result> retrieveRecipes(String searchQuery, Integer numResults) {
		// TODO: Pull recipes from external API and grab relevant information.
		return new ArrayList<Result>();
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