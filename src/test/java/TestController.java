import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import hello.Controller;

public class TestController {
	
	private static Controller controller;

	@BeforeClass
	public static void setup() {
		controller = new Controller();
	}
	
	//TODO: implement each method
	
	@Test
	public void testRetrieveRestaurants() {
		String query = "burger";
		int numResults = 2;
		controller.handleSearchRequest("null", numResults);
		controller.handleSearchRequest(query, numResults);
	}
	
	@Test
	public void testRetrieveRecipes() {
		//fail("Not yet implemented");
	}
	
	@Test
	public void testCreateCollage() {
		//fail("Not yet implemented");
	}

}
