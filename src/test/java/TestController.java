import static org.junit.Assert.assertEquals;
import java.util.Random; 


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import hello.Controller;
import hello.Recipe;
import hello.Restaurant;
import hello.Result;

public class TestController {
	
	private static Controller controller;

	@BeforeClass
	public static void setup() {
		controller = new Controller();
	}
	
//	@Test 
//	public void testGetResult() {
//		// result doesn't exist
//		assertEquals(null, controller.getResult("123"));
//		
//		// testing getResult from past query
//		controller.handleSearchRequest("burger", 1);
//		Result r = controller.getResult("ChIJk2uXa-PHwoARFOHSKjqYyFo");
//		assertEquals("ChIJk2uXa-PHwoARFOHSKjqYyFo", r.getUniqueId());
//	}
	
	@Test
	public void testRetrieveRestaurants() throws IOException {
		String query = "burger"; // do not change the query
		int numResults = 20;
		
		Result fav = new Result("ChIJLyzMquXHwoAR0RpYK9bAM3M");
		ArrayList<Result> favorite = new ArrayList<Result>();
		favorite.add(fav);
		Result not = new Result("ChIJk2uXa-PHwoARFOHSKjqYyFo");
		ArrayList<Result> doNotShow = new ArrayList<Result>();
		doNotShow.add(not);
		controller.setDoNotShow(doNotShow);
		controller.setFav(favorite);
		controller.retrieveRestaurants("null", numResults);
		controller.retrieveRestaurants(query, 1);
		ArrayList<Restaurant> result = controller.retrieveRestaurants(query, numResults);
		assertEquals(16, result.size());
	}
	
	@Test
	public void testRetrieveRecipes() {
		String query = "burger"; // do not change the query
		int numResults = 20;
		
		Result fav = new Result("449835");
		ArrayList<Result> favorite = new ArrayList<Result>();
		favorite.add(fav);
		Result not = new Result("669071");
		ArrayList<Result> doNotShow = new ArrayList<Result>();
		doNotShow.add(not);
		controller.setDoNotShow(doNotShow);
		controller.setFav(favorite);
		controller.retrieveRecipes("null", numResults);
		controller.retrieveRecipes(query, 1);
		controller.retrieveRecipes(query, numResults);
	}
	
	@Test 
	public void testHandleGetResult() {
		// test for invalid parameters 
		assertEquals("uniqueId == null", controller.handleGetResult(null));
		assertEquals("uniqueId is empty", controller.handleGetResult(""));
		
		controller.handleSearchRequest("burger", 1);
		// tests valid handleGetResult() call
		String expectedJson = "{\"uniqueId\":\"449835\",\"name\":\"Kickin' Turkey Burger with Caramelized Onions and Spicy Sweet Mayo\",\"rating\":81.0,\"prepTime\":15.0,\"cookTime\":20.0,\"ingredients\":[\"2 tablespoons barbeque sauce\",\"1 teaspoon ground cayenne pepper\",\"1 1/4 pounds ground turkey breast\",\"5 hamburger buns, split\",\"1/4 cup honey\",\"1 tablespoon prepared horseradish\",\"1 jalapeno pepper, seeded and minced\",\"1 cup light mayonnaise\",\"1/4 teaspoon liquid smoke flavoring\",\"Spicy Sweet Mayo\",\"1/4 cup coarse-grain mustard\",\"1 tablespoon olive oil\",\"1/2 large onion, sliced\",\"hot pepper sauce (e.g. ) to taste\",\"1 teaspoon dry mesquite flavored seasoning mix\",\"1 tablespoon steak seasoning\",\"Burgers\",\"2 tablespoons Worcestershire sauce\"],\"instructions\":[\"Combine mayonnaise, mustard, honey, horseradish, hot pepper sauce, and cayenne pepper in a bowl. Cover and refrigerate.\",\"Mix ground turkey, grated onion, jalapeno, barbeque sauce, Worcestershire sauce, liquid smoke, steak seasoning, and mesquite seasoning in a large bowl. Form into 5 patties.\",\"Heat the olive oil in a skillet over medium heat. Stir in the onion; cook and stir until the onion has softened and turned translucent, about 5 minutes. Reduce heat to medium-low, and continue cooking and stirring until the onion is very tender and dark brown, 15 to 20 minutes more.\",\"Cook the patties in a medium skillet over medium heat, turning once, to an internal temperature of 180 degrees F (85 degrees C), about 6 minutes per side.\",\"Serve on buns topped with spicy sweet mayo and caramelized onions.\"],\"sourceURL\":\"http://allrecipes.com/recipe/kickin-turkey-burger-with-caramelized-onions-and-spicy-sweet-mayo/detail.aspx\",\"imageURL\":\"https://spoonacular.com/recipeImages/Kickin-Turkey-Burger-with-Caramelized-Onions-and-Spicy-Sweet-Mayo-449835.jpg\",\"type\":\"Recipe\",\"isFavorite\":false}";
		assertEquals(expectedJson, controller.handleGetResult("449835"));
	}
	
	@Test 
	public void testHandleSearchRequest() {
		// valid input 
		String jsonResponse = controller.handleSearchRequest("burger", 1);
		System.out.println(jsonResponse);
		// if no input is provided 
		assertEquals("Thanks for searching!", controller.handleSearchRequest(null, 1));
	}
	
	// CAN'T CAUSE UNSUPPORTED ENCODING EXCEPTION BECAUSE PARAM WILL ALWAYS BE UTF-8
	@Test
	public void testCreateCollage() throws IOException {
		ArrayList<String> thumbnailLinks = controller.createCollage("burger");
		ArrayList<String> expectedLinks = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader("ExpectedLinks.txt"));
		String line = reader.readLine();
		while(line != null) {
			expectedLinks.add(line);
			line = reader.readLine();
		}
	}
	
	// NEED TO WRITE CODE FOR EXCEPTIONS 
	@Test
	public void testGetList() {
		controller.handleSearchRequest("burger", 1);
		// list is null
		assertEquals("Invalid list name", controller.getList(null));
		// test retrieving lists 
		assertEquals("{\"favorites\":[]}", controller.getList("favorites"));
		assertEquals("{\"toExplore\":[]}", controller.getList("toExplore"));
		assertEquals("{\"doNotShow\":[]}", controller.getList("doNotShow"));
		// test invalid list 
		assertEquals("Invalid list name", controller.getList("invalidList"));	
	}
	
	@Test
	public void testHandleAddToList() {
		// tests null id
		assertEquals("uniqueId == null", controller.handleAddToList(null, "favorites"));
		// tests empty id 
		assertEquals("No uniqueId provided", controller.handleAddToList("", "favorites"));
		// tests null listname 
		assertEquals("targetListName == null", controller.handleAddToList("123456", null));
		// tests empty listname
		assertEquals("No targetListName provided", controller.handleAddToList("123456", ""));
	
		String json = controller.handleSearchRequest("burger", 1);
		// tests add to list from recipes 
		assertEquals("Added item: 449835 to list: favorites", controller.handleAddToList("449835", "favorites"));
		// tests add to list from restaurants
		assertEquals("Added item: ChIJk2uXa-PHwoARFOHSKjqYyFo to list: favorites", controller.handleAddToList("ChIJk2uXa-PHwoARFOHSKjqYyFo", "favorites"));
		// tests nonexistent id 
		assertEquals("Couldn't find uniqueId", controller.handleAddToList("123456", "favorites"));
	}
	
	@Test 
	public void testHandleRemoveFromList() {
		// tests for invalid parameters 
		assertEquals("itemToRemoveId == null", controller.handleRemoveFromList(null, "favorites"));
		assertEquals("itemToRemoveId is empty", controller.handleRemoveFromList("", "favorites"));
		assertEquals("originListName == null", controller.handleRemoveFromList("123456", null));
		assertEquals("originListName is empty", controller.handleRemoveFromList("123456", ""));
		
		controller.handleSearchRequest("burger", 1);
		controller.handleAddToList("449835", "favorites");
		// tests valid removal 
		assertEquals("Removed item: 449835 from list: favorites", controller.handleRemoveFromList("449835", "favorites"));
		//assertEquals(0, controller.getList("favorites"));
		System.out.println(controller.getList("favorites"));
	}
	
	@Test 
	public void testHandleMoveBetweenLists() {
		// tests for invalid parameters 
		assertEquals("itemToMoveId == null", controller.handleMoveLists(null, "favorites", "toExplore"));
		assertEquals("itemToMoveId is empty", controller.handleMoveLists("", "favorites", "toExplore"));
		assertEquals("originListName == null", controller.handleMoveLists("123456", null, "toExplore"));
		assertEquals("originListName is empty", controller.handleMoveLists("123456", "", "toExplore"));
		assertEquals("targetListName == null", controller.handleMoveLists("123456", "toExplore", null));
		assertEquals("targetListName is empty", controller.handleMoveLists("123456", "toExplore", ""));
		
		// populates lists
		controller.handleSearchRequest("burger", 1);
		controller.handleAddToList("449835", "favorites");
		// tests move 
		assertEquals("Moved item: 449835 from list: favorites to list: toExplore", controller.handleMoveLists("449835", "favorites", "toExplore"));
	}
	
	// CAN'T TEST VALID JSON BECAUSE RESPONSE TIME WILL ALWAYS BE DIFFERENT
	@Test
	public void testGetImagesJson() throws IOException, ParseException {
		// tests IOException
		assertEquals("IOException", controller.getImagesJson("badUrl"));
		
		// tests an invalid GET request
		String invalidRequestUrl = controller.constructRequest(controller.GET_URL, "burger", controller.cx, controller.searchType, "invalidKey");
		String invalidFunctionJson = controller.getImagesJson(invalidRequestUrl);
		assertEquals("GET request not worked", invalidFunctionJson);

		// tests a valid GET request
		String validRequestUrl = controller.constructRequest(controller.GET_URL, "burger", controller.cx, controller.searchType, controller.key);
		System.out.println(controller.GET_URL);
		System.out.println("burger");
		System.out.println(controller.searchType);
		System.out.println(controller.key);
		
		String functionJson = controller.getImagesJson(validRequestUrl);
		// used to parse JSON 
		JSONParser parser = new JSONParser();
//		Object obj = parser.parse(functionJson);
//		org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
//		// confirms that test returned correct result
//		org.json.simple.JSONObject queries = (org.json.simple.JSONObject) jsonObject.get("queries");
//		org.json.simple.JSONArray request = (org.json.simple.JSONArray) queries.get("request");
//		org.json.simple.JSONObject title = (org.json.simple.JSONObject) request.get(0);
//		String titleString = (String) title.get("title");
//		// checks that the search made matches the request 
//		assertEquals("Google Custom Search - burger", titleString);
	}

	@Test 
	public void testGetThumbnailLinks() throws IOException {
		// tests getThumbnailLinks with no results
		String noResultsJson = new String(Files.readAllBytes(Paths.get("NoResultsQuery.txt"))); 
		assertEquals("Search returned no results", controller.getThumbnailLinks(noResultsJson).get(0));
		
		// tests "GET request not worked"
		assertEquals(null, controller.getThumbnailLinks("GET request not worked"));
			
		// tests getThumbnailLinks with results
		String resultsJson = new String(Files.readAllBytes(Paths.get("ExampleQuery.txt")));
		ArrayList<String> functionLinks = controller.getThumbnailLinks(resultsJson);
		BufferedReader expectedLinks = new BufferedReader(new FileReader("ExpectedLinks.txt"));
		// for each link, make sure results match expected
		String functionLink, expectedLink;
		for(int i=0; i<10; i++) {
			functionLink = functionLinks.get(i);
			expectedLink = expectedLinks.readLine();
			// does the link produced by the function match the expected link?
			assertEquals(expectedLink, functionLink);
		}
		expectedLinks.close();
		
		ArrayList<String> temp = new ArrayList<String>();
		// tests org.json.simple.parser.ParseException
		assertEquals(temp, controller.getThumbnailLinks("(Invalid JSON)"));
	}


	// test to ensure successful login of user at to-be-written "loginUser" endpoint
	@Test
	public void testLoginUser() throws IOException {
		// String registerJSON = new String Files.readAllBytes(Paths.get("testLoginCredentials.json"));
		String userId = "kVsDRFOWJxU8Xdw5aDATPwTSkuY2";
		assertEquals("success", controller.loginUser(userId));
		userId = "doesnotexist";
		assertEquals("failed", controller.loginUser(userId));
	}

	// test to ensure successful login of user at to-be-written "registerUser" endpoint
	@Test
	public void testRegisterUser() throws IOException {
	        Random rand = new Random(); 
            int randInt = rand.nextInt(1000);
            String userEmail = "test" + randInt + "@test.com";
            String password = "test" + randInt;
            assertEquals("success", controller.registerUser(userEmail, password)); 
	}
	//test to ensure successful signout
	@Test
	public void testSignUserOut() throws IOException {
		assertEquals("success",controller.signUserOut());
	}
	
	//Test to ensure successful add of result to the database for to-be-written function addToDB
	@Test
	public void testAddToDB() throws IOException {
		 Random rand = new Random(); 
		//Test valid add but user not logged in
		controller.signUserOut();
		String listName = "favorites";
		Recipe result = new Recipe("randomID");
		assertEquals(false, controller.addToDB(listName, result));
		//Test valid recipe and restaurant add with user logged in
		String login = controller.loginUser("kVsDRFOWJxU8Xdw5aDATPwTSkuY2");
		listName = "favorites";
		int randInt = rand.nextInt(1000);
		Recipe recipe = new Recipe(Integer.toString(randInt));
		randInt = rand.nextInt(1000);
		
		
	}

	//Test to ensure successful add of result to the database for to-be-written function addToDB
	@Test
	public void tesRemoveFromDB() throws IOException {
		//Test remove but user not logged in
		controller.signUserOut();
		String listName = "favorites";
		Recipe result = new Recipe("randomID");
		assertEquals(false, controller.removeFromDB(listName, result));
		//Test valid remove with user logged in
		String login = controller.loginUser("kVsDRFOWJxU8Xdw5aDATPwTSkuY2");
		listName = "favorites";
		result = new Recipe("randomID");
		controller.addToDB(listName, result);
		assertEquals(true, controller.removeFromDB(listName, result));
	}
	
	//Test for adding prior searches to database
	@Test
	public void testAddSearchToDB() throws IOException {
		//Test remove but user not logged in
		controller.signUserOut();
		String listName = "priorSearchQueries";
		PriorSearch search = new PriorSearch("bugers", 5, 500);
		assertEquals(false, controller.addSearchToDB(listName, search));
		//Test valid remove with user logged in
		String login = controller.loginUser("kVsDRFOWJxU8Xdw5aDATPwTSkuY2");
		listName = "favorites";
		search = new PriorSearch("pizza", 2, 5000);
		assertEquals(true, controller.addSearchToDB(listName, search));
	}









}
