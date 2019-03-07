import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import hello.Controller;
import hello.ListManager;
import hello.Result;

public class TestController {
	
	private static Controller controller;
	private static ListManager listManager;

	@BeforeClass
	public static void setup() {
		controller = new Controller();
		listManager = new ListManager();
	}
	
	//TODO: implement each method
	
	@Test
	public void testRetrieveRestaurants() {
		String query = "fries";
		int numResults = 2;
		controller.handleSearchRequest("null", numResults);
		controller.handleSearchRequest(query, numResults);
		Result fav = new Result("ChIJIW3OiuHHwoAR6paZISsjQ7Q");
		listManager.addToList(fav, "favorites");
		Result not = new Result("123456");
		listManager.addToList(not, "ChIJg6NxEefHwoARgGH0PL8Rx7A");
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
