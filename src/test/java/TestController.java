import java.util.ArrayList;

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
		controller.handleSearchRequest("null", numResults);
		controller.handleSearchRequest(query, 1);
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
