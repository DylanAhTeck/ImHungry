package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong(0);
	private ListManager listManager = new ListManager();
	
	// NOTE: We'll use this to track our most recent results prior to returning to Wayne
	private ArrayList<Result> mostRecentResults = new ArrayList<Result>(); 




	///////////////////////////////////////////////////
	// 												 //
	// 			OUR ENDPOINTS AND ROUTES   		     //
	// 												 //
	///////////////////////////////////////////////////

	@RequestMapping("/test")
	public String handleTestRequest() {
		return "Look's like you're up and running!";
	}
	
	@RequestMapping("/testRestaurant")
	public void handleTestRecipeRestaurant() {
		try {
			retrieveRestaurants("test", 25);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/search")
	// TODO: Once the internal function calls exist, we'll need to put in the appropriate sequential calls here.
	public String handleSearchRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults) {

		return "Thanks for searching!";

	}

	// NOTE: this is a test endpoint that you can hit to make sure that you're actually adding a random item
	// to the end of the favorites list.
	@RequestMapping("/addItemToFavorites") 
	public String addItemToFavorites() {
		Result tempResult = new Result(String.valueOf(counter.incrementAndGet()));
		listManager.addToList(tempResult, "favorites");
		String favoritesString = listManager.getFavorites().toString();
		return "favorites: " + favoritesString;
	}


	// TODO: Need to write this.
	@RequestMapping("/addToList")
	public String handleAddToList(@RequestParam String itemToAdd, @RequestParam String targetListName) {
		Result temp = new Result("1");
		listManager.addToList(temp, targetListName);
		return "Added item:" + temp.getUniqueId() + " to list: " + targetListName;
	}

	// TODO: Need to write this.
	@RequestMapping("/removeFromList")
	public String handleRemoveFromList(@RequestParam String itemToRemoveId, @RequestParam String originListName) {
		listManager.removeFromList(itemToRemoveId, originListName);
		return "Removed item:" + itemToRemoveId + " from list: " + originListName;
	}

	// TODO: Need to write this.
	@RequestMapping("/moveBetweenLists")
	public String handleMoveLists(@RequestParam String itemToMoveId, @RequestParam String originListName, @RequestParam String targetListName) {
		listManager.moveBetweenLists(itemToMoveId, originListName, targetListName);
		return "Moved item:" + itemToMoveId + " from list: " + originListName + " to list: " + targetListName;
	}



	///////////////////////////////////////////////////
	// 												 //
	// 	EXTERNAL API INTERACTION AND PROCESSING      //
	// NOTE: This is where the actual work happens.  //
	// 												 //
	///////////////////////////////////////////////////

	public Result getResult(String uniqueId) {
		// TOOD: iterate over the items in the most recently generated results and return it if there's a matching one.
		return new Result("placholder");
	}
	
	
	//API call / get request
	private String callAPI(String url) throws MalformedURLException, IOException {

	      URL uurl = new URL(url);
	      HttpURLConnection conn = (HttpURLConnection) uurl.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      StringBuilder result = new StringBuilder();
	      
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      rd.close();
	      return result.toString();
	}
	
	//Google distance matrix API
	private String getDuration(String place_id) throws MalformedURLException, IOException {
		String distanceRequestURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=34.021240,-118.287209&destinations=place_id:" + place_id + "&key=AIzaSyB9ygmPGReQW95GCkHazFsVPZBDI3MJoc0";
		String res = callAPI(distanceRequestURL);
		JSONObject json = new JSONObject(res);
		JSONArray rows = json.getJSONArray("rows");
		JSONObject temp = (JSONObject) rows.get(0);
		JSONArray element = temp.getJSONArray("elements");
		
		JSONObject elements = (JSONObject) element.get(0);

		JSONObject duration = elements.getJSONObject("duration");

		return duration.getString("text");
	}
	  
	
	private String[] placesDetail(String place_id) throws MalformedURLException, IOException {
		String placesDetailURL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&fields=formatted_phone_number,formatted_address,website&key=AIzaSyCFYK31wcgjv4tJAGInrnh52gZoryqQ-2Q";
		String res = callAPI(placesDetailURL);
		JSONObject json = new JSONObject(res);
		JSONObject result = json.getJSONObject("result");
		String address = "unknown";
		if(result.has("formatted_address")) {
			address = result.getString("formatted_address");
		}
		
		String phone = "unknown";
		if(result.has("formatted_phone_number")) {
			phone = result.getString("formatted_phone_number");
		}
				
		String website = "unknown";
		if(result.has("website")) {
			website = result.getString("website");
		}
		return new String[]{address, phone, website};
	}
	
	private ArrayList<Result> parseJSON(JSONObject json, Integer numResults) throws NumberFormatException, MalformedURLException, IOException{
		ArrayList<Result> res = new ArrayList<Result>();
		
		JSONArray results = json.getJSONArray("results");
		
		//it takes too long for the API to response
//	    System.out.println(results.length());
//		if(json.has("next_page_token") && numResults > 20) {
//			String token = json.getString("next_page_token");
//			String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + token + "&key=AIzaSyCFYK31wcgjv4tJAGInrnh52gZoryqQ-2Q";
//			url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=CtQDygEAAOh-eG-aiixPeEmRuKLrGoEtSgSRiAMeCdkDt0Y7a8hXYCPu2aHNyhgeBpLusNBpc4fM8IapHdI60CfCu6SMSgYeXiFT68JiuFemHVsZeowfAl5mqP_-8z11rPiFUz3gGWs3PGroJZM2std2ycGK5OKJwDM9vK-b5eIdga4dl7jaOEC4n9UTI6d6T2PeMSIyYdJVOOj4T23F3bwWHQqxS8588IeF9sh7LdD6drhke7Mj9AkT4HCNpo5nNBNfAKb1a9Ck_-Fcq6lafWbZPaVQPB3gaFX1j_yrXAodt9FpsOXXlixe2Nedgl1izj16jRoAWTjggJnwGtdBF2Amb78lUM-zJPXdKWnNdfbKrTW2rmyEEVIj5tpunUAbKGfCquR-MajCteSnQLLJFzp3ZTaxoZRvGoDz1f05D0fzGHmMEeij6N5eSzeXokAQfrbplk2O-8cohYfewn_v_n3yDm59GNNw4-Kgt-Geb4vxowDnWpCKTKTg5TUMBzVLnyymgHteg0evnx7Jlu5jKQ80pK25OosqIW08_Q5wJslsIABAGzo7xP7vicNJA4WqqFnH2ygcenyp3rtNQd-0OZ8EutG5YUEvp1eG3ILoVRWIQ63XJHffEhBXpcGLtKRO_Z-r3HeDADjUGhRdNKo8kIj3AOfV8BgFRKavuc_rJw&key=AIzaSyCFYK31wcgjv4tJAGInrnh52gZoryqQ-2Q";
//			String more = callAPI(url);
//			JSONObject moreJSON = new JSONObject(more);
//			JSONArray moreResults = moreJSON.getJSONArray("results");
//			System.out.println(moreResults.length());
//			for (int i = 0; i < moreResults.length(); i++) {
//				results.put(moreResults.getJSONObject(i));
//			}
//		}
//		System.out.println(results.length());
	    //to avoid out of bound error
	    int size = Math.min(numResults, results.length());
	    
	    String doNotShow = "", fav = "";
	    
	    for(Result result: listManager.getdoNotShow()) {
	    	doNotShow += result.getUniqueId();
	    }
	    
	    for(Result result: listManager.getFavorites()) {
	    	fav += result.getUniqueId();
	    }
	    
	    for(int i = 0 ; i < size && i < results.length(); i++) {
	    	JSONObject dataObj = (JSONObject) results.get(i);
	    	String place_id = dataObj.getString("place_id");
	    	
	    	//check for do not show
	    	if(doNotShow.contains(place_id)) {
	    		size++;
	    		continue;
	    	}
	    	
	    	String name = dataObj.getString("name");
	    	double rating = dataObj.getDouble("rating");
	    	int priceLevel = 0;
	    	//API does not always provide price level
	    	if(dataObj.has("price_level")){
	    		priceLevel = dataObj.getInt("price_level");
	    		String drivingTime = getDuration(place_id);
	    		String address = placesDetail(place_id)[0];
	    		String phone = placesDetail(place_id)[1];
	    		String website = placesDetail(place_id)[2];
	    		
		    	Restaurant restaurant = new Restaurant(place_id);
		    	restaurant.setName(name);
		    	restaurant.setAddress(address);
		    	restaurant.setDrivingTime(drivingTime);
		    	restaurant.setPhoneNumber(phone);
		    	restaurant.setPlaceId(place_id);
		    	restaurant.setPriceLevel(priceLevel);
		    	restaurant.setRating(rating);
		    	restaurant.setWebsite(website);
		    	restaurant.writeToJSON();
		    	//check for fav
		    	if(fav.contains(place_id)) {
		    		res.add(0, restaurant); //add to front
		    	} else {
		    		res.add(restaurant); 
		    	}
		    		
		    	System.out.println(restaurant);
	    	} else {
	    	    size++;
	    	}	    	
	    }		
		return res;
	}
	
	
	// TOOD: Need to write this. 
	public ArrayList<Result> retrieveRestaurants(String searchQuery, Integer numResults) throws IOException {
		// TODO: Pull restaurants from external API and grab relevant information.
		searchQuery = "asian"; // hard coded for now; TODO: remove this line
		String placesRequestURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=34.021240,-118.287209&rankby=distance&type=restaurant&keyword=" + searchQuery + "&key=AIzaSyCFYK31wcgjv4tJAGInrnh52gZoryqQ-2Q";
		
		String res = callAPI(placesRequestURL);
		
		JSONObject json = new JSONObject(res);
	    
		return parseJSON(json, numResults);
	}

	// TOOD: Need to write this.
	public ArrayList<Result> retrieveRecipes(String searchQuery, Integer numResults) {
		// TODO: Pull recipes from external API and grab relevant information.
		return new ArrayList<Result>();
	}

	// should take the searchQuery as a parameter and return a path to the collage. 
	// NOTE: will likely need submethods here...
	public String createCollage(String searchQuery) {
		// TODO: Pull restaurants from external API and grab relevant information.
		return "placeholder";
	}


	public String packageResponseString(ArrayList<Result> restaurants, ArrayList<Result> recipes, String collagePath) {
		return "placeholder";
	}
}