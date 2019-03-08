import static org.junit.Assert.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import hello.Restaurant;

public class TestRestaurant {
	private static Restaurant restaurant;
	
	@Before 
	public void setup() {
		restaurant = new Restaurant("123456");
		restaurant.setType("Restaurant");
	}
	
	@Test
	public void testGetType() {
		assertEquals("Restaurant", restaurant.getType());
	}
	
	//NOT FINISHED YET
//	@Test
//	public void testWriteToJSON() throws JSONException, JsonProcessingException {
//		JSONObject expectedJSON = new JSONObject();
//		expectedJSON.put("website", null);
//		expectedJSON.put("phoneNumber", null);
//		expectedJSON.put("address", null);
//		expectedJSON.put("name", null);
//		expectedJSON.put("placeId", null);
//		expectedJSON.put("drivingTime", null);
//		expectedJSON.put("priveLevel", null);
//		expectedJSON.put("rating", 0);
//		expectedJSON.put("type", "Restaurant");
//		expectedJSON.put("uniqueId", "123456");
//		assertEquals(expectedJSON, restaurant.writeToJSON());
//	}
	
	@Test
	public void testToString() {
		String expectedString = "Restaurant [name=null, rating=0.0, drivingTime=null, phoneNumber=null, address=null, website=null, priceLevel=null, placeId=null]";
		assertEquals(expectedString, restaurant.toString());
	}
	
	@Test
	public void testGettersAndSetters() {
		restaurant.setName("Burger King");
		restaurant.setRating(10.0);
		restaurant.setDrivingTime("45 minutes");
		restaurant.setPhoneNumber("(123)456-7890");
		restaurant.setAddress("123 West Street");
		restaurant.setWebsite("www.fun.com");
		restaurant.setPriceLevel(10);
		restaurant.setPlaceId("Random Place");
		
		assertEquals("Burger King", restaurant.getName());
		assertEquals(10.0, restaurant.getRating(), 0); 
		assertEquals("45 minutes", restaurant.getDrivingTime());
		assertEquals("(123)456-7890", restaurant.getPhoneNumber());
		assertEquals("123 West Street", restaurant.getAddress());
		assertEquals("www.fun.com", restaurant.getWebsite());
		assertEquals(10, restaurant.getPriceLevel());
		assertEquals("Random Place", restaurant.getPlaceId());
	}

}
