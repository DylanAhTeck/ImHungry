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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import hello.Controller;
import hello.ListManager;
import hello.PriorSearch;
import hello.Recipe;
import hello.Restaurant;
import hello.Result;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
		controller.retrieveRestaurants("null", numResults, 5);
		controller.retrieveRestaurants(query, 1, 5);
		ArrayList<Restaurant> result = controller.retrieveRestaurants(query, numResults, 5);
		assertEquals(18, result.size());
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
		ListManager lm = controller.getListManager();
		lm.setDoNotShow(new ArrayList<Result>());
		lm.setFavorites(new ArrayList<Result>());
		lm.setToExplore(new ArrayList<Result>());

		controller.handleSearchRequest("burger", 1, 5);
		// tests valid handleGetResult() call
	
		String actualJson = controller.handleGetResult("219957");
		System.out.println(expectedJson);
		System.out.println(actualJson);
		assertEquals(expectedJson, actualJson);
	}


	@Test
	public void testHandleSearchRadius() {
		// test for invalid parameters
		assertEquals("uniqueId == null", controller.handleGetResult(null));
		assertEquals("uniqueId is empty", controller.handleGetResult(""));
		ListManager lm = controller.getListManager();
		lm.setDoNotShow(new ArrayList<Result>());
		lm.setFavorites(new ArrayList<Result>());
		lm.setToExplore(new ArrayList<Result>());

		String resultFiveFiveFive = controller.handleSearchRequest("burger", 5, 5, 5);
		BufferedReader reader = new BufferedReader(new FileReader("burger-5-5-5-json.txt"));
		String line = reader.readLine();
		assertEquals(line, resultFiveFiveFive);


		String resultFiveFiveOne = controller.handleSearchRequest("burger", 5, 5, 1);
		BufferedReader reader = new BufferedReader(new FileReader("burger-5-5-1-json.txt"));
		String line = reader.readLine();
		assertEquals(line, resultFiveFiveOne);
	}


	@Test
	public void testHandleSearchRequest() {
		// valid input
		String jsonResponse = controller.handleSearchRequest("burger", 1, 5000);
		System.out.println(jsonResponse);
		// if no input is provided
		assertEquals("Thanks for searching!", controller.handleSearchRequest(null, 1, 5000));
	}

	// CAN'T CAUSE UNSUPPORTED ENCODING EXCEPTION BECAUSE PARAM WILL ALWAYS BE UTF-8
	@Test
	public void testFetchImageURLs() throws IOException {
		ArrayList<String> thumbnailLinks = controller.fetchImageURLs("burger");
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
		controller.handleSearchRequest("burger", 1, 5000);
		controller.loginUser("hCHdoXQkudPGxBP4E4ur0eSKbNN2");
		ListManager lm = controller.getListManager();
		lm.setDoNotShow(new ArrayList<Result>());
		lm.setFavorites(new ArrayList<Result>());
		lm.setToExplore(new ArrayList<Result>());
		// list is null
		assertEquals("Invalid list name", controller.getList(null));
		// test retrieving lists
		assertEquals("[]", controller.getList("favorites"));
		assertEquals("[]", controller.getList("toExplore"));
		assertEquals("[]", controller.getList("doNotShow"));
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

		String json = controller.handleSearchRequest("burger", 1, 5);
		// tests add to list from recipes

		assertEquals("Added item: 219957 to list: favorites", controller.handleAddToList("219957", "favorites"));
		// tests add to list from restaurants
		assertEquals("Added item: ChIJJewhXDol6IARKGxyEnT4sIA to list: favorites", controller.handleAddToList("ChIJJewhXDol6IARKGxyEnT4sIA", "favorites"));

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

		controller.handleSearchRequest("burger", 1, 5);
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
		controller.handleSearchRequest("burger", 1, 5000);
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
            String password = "password" + randInt;
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
		Restaurant restaurant = new Restaurant(Integer.toString(randInt+10));
		randInt = rand.nextInt(1000);

		controller.loginUser("kVsDRFOWJxU8Xdw5aDATPwTSkuY2");
		listName = "doNotShow";
		randInt = rand.nextInt(1000);
		recipe = new Recipe(Integer.toString(randInt));
		restaurant = new Restaurant(Integer.toString(randInt+10));
		randInt = rand.nextInt(1000);
		assertEquals(true, controller.addToDB(listName, recipe));
		assertEquals(true, controller.addToDB(listName, restaurant));
		listName = "toExplore";
		randInt = rand.nextInt(1000);
		recipe = new Recipe(Integer.toString(randInt));
		restaurant = new Restaurant(Integer.toString(randInt+10));
		randInt = rand.nextInt(1000);
		assertEquals(true, controller.addToDB(listName, recipe));
		assertEquals(true, controller.addToDB(listName, restaurant));
		controller.loginUser("kVsDRFOWJxU8Xdw5aDATPwTSkuY2");
		listName = "toExplore";
		randInt = rand.nextInt(1000);
		recipe = new Recipe(Integer.toString(randInt));
		restaurant = new Restaurant(Integer.toString(randInt+10));
		randInt = rand.nextInt(1000);
		assertEquals(true, controller.addToDB(listName, recipe));
		assertEquals(true, controller.addToDB(listName, restaurant));
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
		search = new PriorSearch("pizza", 2, 5000);
		assertEquals(true, controller.addSearchToDB(listName, search));
	}

	//Test for moving a result up one in list
	@Test
	public void testMoveUpOne() throws IOException {
		Random rand = new Random();
        int randInt = rand.nextInt(1000);
		controller.registerUser("test"+randInt+"@test.com", "password");

		ListManager listManager = controller.getListManager();
		listManager.setDoNotShow(new ArrayList<Result>());
		listManager.setFavorites(new ArrayList<Result>());
		listManager.setToExplore(new ArrayList<Result>());

		Result r1 = new Result("id1");
		Result r2 = new Result("id2");
		Result r3 = new Result("id3");
		Result r4 = new Result("id4");
		Result r5 = new Result("id5");
		Result r6 = new Result("id6");

		listManager.addToList(r1, "favorites");
		listManager.addToList(r2, "favorites");
		listManager.addToList(r3, "toExplore");
		listManager.addToList(r4, "toExplore");
		listManager.addToList(r5, "doNotShow");
		listManager.addToList(r6, "doNotShow");

		ArrayList<Result> favorites = listManager.getFavorites();
		ArrayList<Result> toExplore = listManager.getToExplore();
		ArrayList<Result> doNotShow = listManager.getdoNotShow();

		controller.addToDB("favorites", r1);
		controller.addToDB("favorites", r2);
		controller.addToDB("toExplore", r3);
		controller.addToDB("toExplore", r4);
		controller.addToDB("doNotShow", r5);
		controller.addToDB("doNotShow", r6);

		assertEquals(true, favorites.get(0).getUniqueId().equals("id1"));
		assertEquals(true, favorites.get(1).getUniqueId().equals("id2"));
		assertEquals(true, toExplore.get(0).getUniqueId().equals("id3"));
		assertEquals(true, toExplore.get(1).getUniqueId().equals("id4"));
		assertEquals(true, doNotShow.get(0).getUniqueId().equals("id5"));
		assertEquals(true, doNotShow.get(1).getUniqueId().equals("id6"));

		controller.moveUpOne("id2", "favorites");
		controller.moveUpOne("id4", "toExplore");


		assertEquals(false, controller.moveUpOne("id2", "advasdf"));
		assertEquals(true, favorites.get(1).getUniqueId().equals("id1"));
		assertEquals(true, favorites.get(0).getUniqueId().equals("id2"));

		//Testing moving up one index at index 0
		controller.moveUpOne("id5", "doNotShow");
		assertEquals(true, doNotShow.get(0).getUniqueId().equals("id5"));
	}

	//Test for moving a result down one in list
	@Test
	public void testMoveDownOne() throws IOException {
		Random rand = new Random();
        int randInt = rand.nextInt(1000);
		controller.registerUser("test"+randInt+"@test.com", "password");

		ListManager listManager = controller.getListManager();
		listManager.setDoNotShow(new ArrayList<Result>());
		listManager.setFavorites(new ArrayList<Result>());
		listManager.setToExplore(new ArrayList<Result>());

		Result r1 = new Result("id1");
		Result r2 = new Result("id2");
		Result r3 = new Result("id3");
		Result r4 = new Result("id4");
		Result r5 = new Result("id5");
		Result r6 = new Result("id6");

		listManager.addToList(r1, "favorites");
		listManager.addToList(r2, "favorites");
		listManager.addToList(r3, "toExplore");
		listManager.addToList(r4, "toExplore");
		listManager.addToList(r5, "doNotShow");
		listManager.addToList(r6, "doNotShow");

		ArrayList<Result> favorites = listManager.getFavorites();
		ArrayList<Result> toExplore = listManager.getToExplore();
		ArrayList<Result> doNotShow = listManager.getdoNotShow();

		controller.addToDB("favorites", r1);
		controller.addToDB("favorites", r2);
		controller.addToDB("toExplore", r3);
		controller.addToDB("toExplore", r4);
		controller.addToDB("doNotShow", r5);
		controller.addToDB("doNotShow", r6);

		assertEquals(true, favorites.get(0).getUniqueId().equals("id1"));
		assertEquals(true, favorites.get(1).getUniqueId().equals("id2"));
		assertEquals(true, toExplore.get(0).getUniqueId().equals("id3"));
		assertEquals(true, toExplore.get(1).getUniqueId().equals("id4"));
		assertEquals(true, doNotShow.get(0).getUniqueId().equals("id5"));
		assertEquals(true, doNotShow.get(1).getUniqueId().equals("id6"));

		controller.moveDownOne("id1", "favorites");
		controller.moveDownOne("id3", "toExplore");


		assertEquals(false, controller.moveDownOne("id2", "advasdf"));
		assertEquals(true, favorites.get(1).getUniqueId().equals("id1"));
		assertEquals(true, favorites.get(0).getUniqueId().equals("id2"));

		controller.moveDownOne("id2", "favorites");

		assertEquals(true, favorites.get(0).getUniqueId().equals("id1"));
		assertEquals(true, favorites.get(1).getUniqueId().equals("id2"));

		//Testing moving down on last index
		controller.moveDownOne("id6", "doNotShow");
		assertEquals(true, doNotShow.get(1).getUniqueId().equals("id6"));
	}

	//Test for conversion to meters
	@Test
	public void testToMeters() throws IOException {
		assertEquals(controller.toMeters(1) == 1609.34, true);
		assertEquals(controller.toMeters(2) == 3218.68, true);
	}


	@Test
	public void testAddIngredient() throws IOException {
		//Test remove but user not logged in
		String login = controller.loginUser("kVsDRFOWJxU8Xdw5aDATPwTSkuY2");
		assertEquals(controller.addIngredient(""), false);
		assertEquals(controller.addIngredient("apple"), true);
	}

	@Test
	public void testRemoveIngredient() throws IOException {
		//Test remove but user not logged in
		String login = controller.loginUser("kVsDRFOWJxU8Xdw5aDATPwTSkuY2");
		controller.addIngredient("banana");
		assertEquals(controller.removeIngredient(""), false);
		assertEquals(controller.removeIngredient("banana"), true);
	}





}
