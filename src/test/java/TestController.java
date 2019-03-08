import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import hello.Controller;
import hello.Result;

public class TestController {
	
	private static Controller controller;

	@BeforeClass
	public static void setup() {
		controller = new Controller();
	}
	
	//TODO: implement each method
	
	@Test
	public void testRetrieveRestaurants() throws IOException {
		String query = "burger"; // do not change the query
		int numResults = 20;
		
		Result fav = new Result("ChIJLyzMquXHwoAR0RpYK9bAM3M");
		ArrayList<Result> favorite = new ArrayList<Result>();
		favorite.add(fav);
		Result not = new Result("ChIJg6NxEefHwoARgGH0PL8Rx7A");
		ArrayList<Result> doNotShow = new ArrayList<Result>();
		doNotShow.add(not);
		controller.setDoNotShow(doNotShow);
		controller.setFav(favorite);
		controller.retrieveRestaurants("null", numResults);
		controller.retrieveRestaurants(query, 1);
		controller.retrieveRestaurants(query, numResults);
	}
	
//	@Test
//	public void testRetrieveRecipes() {
//		//fail("Not yet implemented");
//	}
//	
	// NEED TO TEST UnsupportedEncodingException
	// ASSERT STATEMENT IS STRANGELY HIT AND MISS CURRENTLY
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
	
	// CAN'T TEST VALID JSON BECAUSE RESPONSE TIME WILL ALWAYS BE DIFFERENT
	// NEED TO TEST IOException
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
		
		// tests MalformedUrlException
		assertEquals("MalformedUrlException", controller.getImagesJson("badUrl"));
		// tests IOException
	}

	// STILL NEED TO TEST org.json.simple.parser.ParseException
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
	}

}
