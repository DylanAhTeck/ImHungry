package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	@CrossOrigin
	public String handleTestRequest() {
		return "Look's like you're up and running!";
	}
	
	@RequestMapping("/testCollage")
	@CrossOrigin
    public String handleTestCollage(@RequestParam(defaultValue="null") String searchQuery) {
        ArrayList<String> imageURLs = createCollage(searchQuery);
        return imageURLs.toString();
    }

	@RequestMapping("/testRestaurant")
	@CrossOrigin
	public void handleTestRecipeRestaurant() {
		try {
			retrieveRestaurants("test", 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/testAddToList")
	@CrossOrigin
	public void handleTestAddToList(@RequestParam(defaultValue="null") String uniqueId, @RequestParam(defaultValue="null") String targetList) {
		ListManager m = new ListManager();
		
		// quick test of add to list
		m.addToList(new Result(uniqueId), targetList);
		ArrayList<Result> favorites = m.getFavorites();
		if (favorites.get(0).uniqueId.equals(uniqueId)) {
			System.out.println("It worked! Added <" + uniqueId + "> to <" + targetList + ">");
		} else {
			System.out.println("It didn't work :(");
		}
		
		
	}
	
	

	@RequestMapping("/search")
	@CrossOrigin
	// TODO: Once the internal function calls exist, we'll need to put in the appropriate sequential calls here.
	public String handleSearchRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults) {

		return "Thanks for searching!";

	}

	// NOTE: this is a test endpoint that you can hit to make sure that you're actually adding a random item
	// to the end of the favorites list.
	@RequestMapping("/addItemToFavorites") 
	@CrossOrigin
	public String addItemToFavorites() {
		Result tempResult = new Result(String.valueOf(counter.incrementAndGet()));
		listManager.addToList(tempResult, "favorites");
		String favoritesString = listManager.getFavorites().toString();
		return "favorites: " + favoritesString;
	}


	// TODO: Need to write this.
	@RequestMapping("/addToList")
	@CrossOrigin
	public String handleAddToList(@RequestParam String itemToAdd, @RequestParam String targetListName) {
		Result temp = new Result("1");
		listManager.addToList(temp, targetListName);
		return "Added item:" + temp.getUniqueId() + " to list: " + targetListName;
	}

	// TODO: Need to write this.
	@RequestMapping("/removeFromList")
	@CrossOrigin
	public String handleRemoveFromList(@RequestParam String itemToRemoveId, @RequestParam String originListName) {
		listManager.removeFromList(itemToRemoveId, originListName);
		return "Removed item:" + itemToRemoveId + " from list: " + originListName;
	}

	// TODO: Need to write this.
	@RequestMapping("/moveBetweenLists")
	@CrossOrigin
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
		// TODO: iterate over the items in the most recently generated results and return it if there's a matching one.
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
		String address = result.getString("formatted_address");
		String phone = result.getString("formatted_phone_number");
		String website = result.getString("website");
		return new String[]{address, phone, website};
	}
	
	private ArrayList<Result> parseJSON(JSONObject json, Integer numResults) throws NumberFormatException, MalformedURLException, IOException{
		ArrayList<Result> res = new ArrayList<Result>();
		
		JSONArray results = json.getJSONArray("results");
	    
	    //to avoid out of bound error
	    int size = Math.min(numResults, results.length());
	    
	    for(int i = 0 ; i < size && i < results.length(); i++) {
	    	JSONObject dataObj = (JSONObject) results.get(i);
	    	String place_id = dataObj.getString("place_id");
	    	String name = dataObj.getString("name");
	    	double rating = dataObj.getDouble("rating");
	    	int priceLevel = 0;
	    	//API does not always provide price level info
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
		    	res.add(restaurant); 	
		    	System.out.println(restaurant);
	    	} else {
	    	    size++;
	    	}	    	
	    }		
		return res;
	}
	
	
	// TODO: Need to write this. 
	public ArrayList<Result> retrieveRestaurants(String searchQuery, Integer numResults) throws IOException {
		// TODO: Pull restaurants from external API and grab relevant information.
		searchQuery = "burger"; // hard coded for now; TODO: remove this line
		String placesRequestURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=34.021240,-118.287209&rankby=distance&type=restaurant&keyword=" + searchQuery + "&key=AIzaSyCFYK31wcgjv4tJAGInrnh52gZoryqQ-2Q";
		
		String res = callAPI(placesRequestURL);
		
		JSONObject json = new JSONObject(res);
	    
		return parseJSON(json, numResults);
	}

	// TODO: Need to write this.
	public ArrayList<Result> retrieveRecipes(String searchQuery, Integer numResults) {
		// TODO: Pull recipes from external API and grab relevant information.
		return new ArrayList<Result>();
	}

	// should take the searchQuery as a parameter and ArrayList of thumnail links for the collage. 
	public ArrayList<String> createCollage(String searchQuery) {
		final String GET_URL = "https://www.googleapis.com/customsearch/v1?";
		final String cx = "001349756157526882706%3An5pmkqrjpfc";
		final String searchType = "image";
		final String key = "AIzaSyBiGl3y-IJ-tnfO_AhuUoeqIIhIHTqEJyo";
		// constructs requestUrl with function call
		String requestUrl = constructRequest(GET_URL, searchQuery, cx, searchType, key);
		// gets JSON response based on GET request
		String jsonResponse = getImagesJson(requestUrl);
		// extracts thumbnail links from JSON, puts in ArrayList
		ArrayList<String> thumbnailLinks = getThumbnailLinks(jsonResponse);
		
		return thumbnailLinks;
	}
	
	// creates the GET request for the Google Image Custom Search
	public String constructRequest(String GET_URL, String searchQuery, String cx, String searchType, String key) {
		return GET_URL + "q=" + searchQuery + "&cx=" + cx + "&searchType=" + searchType + "&key=" + key;
	}
	
	// uses GET request to produce a JSON response for given searchQuery
	public String getImagesJson(String requestUrl) {
		try {
			// creates URL object with previously constructed requestURL (see above) 
			URL obj = new URL(requestUrl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			// response code == 200 means success 
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				// reads data from response 
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// print result
				System.out.println(response.toString());
				// returns the formatted json 
				return response.toString();
			} else {
				return "GET request not worked";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// request did not work because an exception was thrown
		return "GET request not worked";
	}
	
	// extracts thumbnail links from JSON and returns them in ArrayList
	@SuppressWarnings("unchecked")
	public ArrayList<String> getThumbnailLinks(String jsonResponse) {
		ArrayList<String> thumbnailLinks = new ArrayList<String>();
		JSONParser parser = new JSONParser();
		try {
			// obtains JSON to be parsed
			Object obj = parser.parse(jsonResponse);
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
			// extracts results set from query JSON
			org.json.simple.JSONArray results = (org.json.simple.JSONArray) jsonObject.get("items");
			// search query returned no results
			if (results == null) {
				thumbnailLinks.add("Search returned no results");
				return thumbnailLinks;
			}
			// adds thumbnail links to thumbnailLinks array
			Iterator<Object> iterator =  results.iterator();
			for(int i=0; i<10; i++) {
				org.json.simple.JSONObject resultItem = (org.json.simple.JSONObject) iterator.next();
				String thumbnailLink = (String) resultItem.get("link");
				thumbnailLinks.add("\"" + thumbnailLink + "\"");
				System.out.println(i+1 + ") " + thumbnailLink);
			}
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		return thumbnailLinks;
	}

	public String packageResponseString(ArrayList<Result> restaurants, ArrayList<Result> recipes, String collagePath) {
		return "placeholder";
	}
}