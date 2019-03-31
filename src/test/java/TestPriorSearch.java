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
		Integer num = 5;
		Integer radius = 100;
		assertEquals("burgers", search.getTerm());
		assertEquals(search.getNum(), num);
		assertEquals(radius, search.getRadius());
	}

}
