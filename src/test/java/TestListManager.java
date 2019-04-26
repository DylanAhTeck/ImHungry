import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import hello.ListManager;
import hello.Result;

public class TestListManager {
	private static ListManager listManager;
	private static Result result;
	private static Result result2;
	
	@Before
	public void setup() {
		listManager = new ListManager();
		result = new Result("123456");
		result2 = new Result("654321");
	}
	
	@Test
	public void testAddToList() {
		// test adding to favorites 
		assertEquals(true, listManager.addToList(result, "favorites"));
		assertEquals(result, listManager.getFavorites().get(0));
		// test duplicate add
		assertEquals(false, listManager.addToList(result, "favorites"));
		
		// test adding to toExplore
		assertEquals(true, listManager.addToList(result, "toExplore"));
		assertEquals(result, listManager.getToExplore().get(0));
		// test duplicate add
		assertEquals(false, listManager.addToList(result, "toExplore"));
		
		// test adding to doNotShow
		assertEquals(true, listManager.addToList(result, "doNotShow"));
		assertEquals(result, listManager.getdoNotShow().get(0));
		// test duplicate add
		assertEquals(false, listManager.addToList(result, "doNotShow"));
		
		// test null result 
		assertEquals(false, listManager.addToList(null, "favorites"));
		
		// test invalid list input
		assertEquals(false, listManager.addToList(result, "randomList"));
	}
	
	@Test
	public void testMoveBetweenLists() {
		listManager.addToList(result, "favorites");
		
		assertEquals(true, listManager.moveBetweenLists("123456", "favorites", "toExplore").getUniqueId().equals("123456"));
		assertEquals(true, listManager.moveBetweenLists("123456", "toExplore", "doNotShow").getUniqueId().equals("123456"));
		assertEquals(true, listManager.moveBetweenLists("123456", "doNotShow", "favorites").getUniqueId().equals("123456"));
		assertEquals(null, listManager.moveBetweenLists("fakeId", "favorites", "doNotShow"));
		assertEquals(null, listManager.moveBetweenLists("123456", "favorites", "fakeList"));
	}

	@Test
	public void testRemoveFromList() {
		listManager.addToList(result, "favorites");
		listManager.addToList(result, "toExplore");
		listManager.addToList(result, "doNotShow");
		
		// remove from favorites 
		assertEquals(null, listManager.removeFromList("", "favorites"));
		assertEquals(result, listManager.removeFromList("123456", "favorites"));
		assertEquals(0, listManager.getFavorites().size());
		
		// remove from toExplore 
		assertEquals(null, listManager.removeFromList("", "toExplore"));
		assertEquals(result, listManager.removeFromList("123456", "toExplore"));
		assertEquals(0, listManager.getToExplore().size());
		
		// remove from doNotShow 
		assertEquals(null, listManager.removeFromList("", "doNotShow"));
		assertEquals(result, listManager.removeFromList("123456", "doNotShow"));
		assertEquals(0, listManager.getdoNotShow().size());
		
		// remove from invalid list 
		assertEquals(null, listManager.removeFromList("", "fakeList"));
		
		// invalid inputs
		assertEquals(null, listManager.removeFromList(null, "placeholder"));
		assertEquals(null, listManager.removeFromList("placeholder", null));
	}
	
	@Test
	public void testItemInList() {
		assertEquals(false, listManager.itemInList(result, "favorites"));
		assertEquals(false, listManager.itemInList(result, "toExplore"));
		assertEquals(false, listManager.itemInList(result, "doNotShow"));
		assertEquals(false, listManager.itemInList(result, "fakeList"));
		
		listManager.addToList(result, "favorites");
		listManager.addToList(result2, "favorites");
		assertEquals(true, listManager.itemInList(result2, "favorites"));
		
		listManager.addToList(result, "toExplore");
		listManager.addToList(result2, "toExplore");
		assertEquals(true, listManager.itemInList(result2, "toExplore"));
		
		listManager.addToList(result, "doNotShow");
		listManager.addToList(result2, "doNotShow");
		assertEquals(true, listManager.itemInList(result2, "doNotShow"));
		
	}

}
