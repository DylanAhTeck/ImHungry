import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import hello.Controller;
import hello.ListManager;

public class TestController {
	
	private static Controller controller;
	private static ListManager listManager;

	@BeforeClass
	public static void setup() {
		controller = new Controller();
		listManager = new ListManager();
	}
	
	//TODO: implement each method
	
//	@Test
//	public void testRetrieveRestaurants() {
//		String query = "burger"; // do not change the query
//		int numResults = 20;
//		
//		Result fav = new Result("ChIJLyzMquXHwoAR0RpYK9bAM3M");
//		ArrayList<Result> favorite = new ArrayList<Result>();
//		favorite.add(fav);
//		Result not = new Result("ChIJg6NxEefHwoARgGH0PL8Rx7A");
//		ArrayList<Result> doNotShow = new ArrayList<Result>();
//		doNotShow.add(not);
//		controller.setDoNotShow(doNotShow);
//		controller.setFav(favorite);
//		controller.handleSearchRequest("null", numResults);
//		controller.handleSearchRequest(query, 1);
//		controller.handleSearchRequest(query, numResults);
//	}
	
//	@Test
//	public void testRetrieveRecipes() {
//		//fail("Not yet implemented");
//	}
//	
	
	@Test 
	public void testHandleSearchRequest() {
		// if no input is provided 
		assertEquals("Thanks for searching!", controller.handleSearchRequest(null, 1));
		// 
		String jsonResponse = controller.handleSearchRequest("burger", 1);
		JSONParser parser = new JSONParser();
		try {
			// obtains JSON to be parsed
			Object obj = parser.parse(jsonResponse);
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
			// extracts results set from query JSON
			org.json.simple.JSONArray results = (org.json.simple.JSONArray) jsonObject.get("items");
			org.json.simple.JSONObject result = (org.json.simple.JSONObject) results.get(1);
			assertEquals("449835", result.get("uniqeId"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// CAN'T CAUSE UNSUPPORTED ENCODING EXCEPTION BECAUSE PARAM WILL ALWAYS BE UTF-8
	@Test
	public void testCreateCollage() throws IOException {
		ArrayList<String> thumbnailLinks = controller.createCollage("burger");
		String actualLinks = "";
		for (int i=0; i<thumbnailLinks.size(); i++) {
			if (i == thumbnailLinks.size()-1) {
				actualLinks += thumbnailLinks.get(i);
			} else {
				actualLinks += thumbnailLinks.get(i) + "\r\n";
			}
		}
		String expectedLinks = new String(Files.readAllBytes(Paths.get("ExpectedLinks.txt")));
		assertEquals(expectedLinks, actualLinks);
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
		// tests a valid GET request
		String validRequestUrl = controller.constructRequest(controller.GET_URL, "burger", controller.cx, controller.searchType, controller.key);
		String functionJson = controller.getImagesJson(validRequestUrl);
		// used to parse JSON 
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(functionJson);
		org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
		// confirms that test returned correct result
		org.json.simple.JSONObject queries = (org.json.simple.JSONObject) jsonObject.get("queries");
		org.json.simple.JSONArray request = (org.json.simple.JSONArray) queries.get("request");
		org.json.simple.JSONObject title = (org.json.simple.JSONObject) request.get(0);
		String titleString = (String) title.get("title");
		// checks that the search made matches the request 
		assertEquals("Google Custom Search - burger", titleString);
		
		// tests an invalid GET request
		String invalidRequestUrl = controller.constructRequest(controller.GET_URL, "burger", controller.cx, controller.searchType, "invalidKey");
		String invalidFunctionJson = controller.getImagesJson(invalidRequestUrl);
		assertEquals("GET request did not work", invalidFunctionJson);
		
		// tests IOException
		assertEquals("IOException", controller.getImagesJson("badUrl"));
	}

	@Test 
	public void testGetThumbnailLinks() throws IOException {
		// tests getThumbnailLinks with no results
		String noResultsJson = new String(Files.readAllBytes(Paths.get("NoResultsQuery.txt"))); 
		assertEquals("Search returned no results", controller.getThumbnailLinks(noResultsJson).get(0));
			
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
		
		// tests org.json.simple.parser.ParseException
		assertEquals(null, controller.getThumbnailLinks("(Invalid JSON)"));
	}

}
