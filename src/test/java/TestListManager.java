import org.junit.BeforeClass;
import org.junit.Test;

import hello.ListManager;
import hello.Result;

public class TestListManager {
	private static ListManager listManager;
	
	@BeforeClass
	public static void setup() {
		listManager = new ListManager();
	}
	
	@Test
	public void testAddToList() {
		Result result = new Result("123456");
		listManager.addToList(result, "favorites");
		listManager.addToList(result, "toExplore");
		listManager.addToList(result, "doNotShow");
	}
	

}
