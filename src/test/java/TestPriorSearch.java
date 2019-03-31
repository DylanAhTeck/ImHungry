import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import hello.PriorSearch;

import java.util.ArrayList;

public class TestPriorSearch {
	private static PriorSearch search;
	
	@Before 
	public void setup() {
		search = new PriorSearch("burgers", 5, 100);
	}
	
	@Test
	public void testGetters() {
		assertEquals("burgers", search.getTerm());
		assertEquals(5, search.getNum());
		assertEquals(100, search.getRadius());
	}

}
