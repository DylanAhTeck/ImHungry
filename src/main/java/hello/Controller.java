package hello;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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
	
	@RequestMapping("/testCollage")
    public String handleTestCollage(@RequestParam(defaultValue="null") String searchQuery) {
        ArrayList<String> imageURLs = createCollage(searchQuery);
        return imageURLs.toString();
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

	@RequestMapping("/testRecipe")
	public String handleTestRecipeRequest() {
		return getTestRecipeString();
	}

	@RequestMapping("/testSearchRecipe")
	public String handleTestRecipeRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(retrieveRecipes(searchQuery, numResults));
		} catch (JsonProcessingException e){
			System.out.println("json processing exception when returning test recipe retrievals");
		}
		return "failure";
	}


	@RequestMapping("/search")
	// TODO: Once the internal function calls exist, we'll need to put in the appropriate sequential calls here.
	public String handleSearchRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults) {

		if (searchQuery.equals("null")) {
			return "Thanks for searching!";
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonNode imagesNode = mapper.createObjectNode();
		
		ArrayList<Result> restaurants = retrieveRestaurants(searchQuery, numResults);
		ArrayList<Recipe> recipes = retrieveRecipes(searchQuery, numResults);

		try {
			// using readtree to set these as json nodes
			((ObjectNode) rootNode).set("recipes", mapper.readTree(mapper.writeValueAsString(recipes)));
			((ObjectNode) rootNode).set("restaurants", mapper.readTree(mapper.writeValueAsString(restaurants)));
			((ObjectNode) rootNode).set("imageUrls", imagesNode);

			return mapper.writeValueAsString(rootNode);
			
		} catch (JsonProcessingException e) {
			System.out.println("json processing exception on creating search response");
		} catch (IOException e) {
			System.out.println("ioexception in reading tree");
		}

		return "failure";

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

	public String getTestRecipeString() {

		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("1 oz ham");
		ingredients.add("2oz cheese");
		ingredients.add("2 slices bread");

		ArrayList<String> instructions = new ArrayList<String>();
		instructions.add("1. do the thing");
		instructions.add("2. finish the thing");

		Recipe r = new Recipe("1");
		r.setIngredients(ingredients);
		r.setName("best recipe");
		r.setSourceURL("http://localhost:1000");
		r.setPrepTime(40);
		r.setInstructions(instructions);
		r.setRating(2);

		r.setCookTime(20);

		return r.writeToJSON();

	}

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

	// Retrieve recipes from Spoonacular API, parse relevant JSON, and return list of recipe results
	public ArrayList<Recipe> retrieveRecipes(String searchQuery, Integer numResults) {

		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();

		try {
			HttpResponse<com.mashape.unirest.http.JsonNode> response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search")
					.header("X-RapidAPI-Key", "ebff0f5311msh75407f578a41008p14174ejsnf16b8bcf5559")
					.queryString("query", searchQuery)
					.queryString("number", numResults)
					.asJson();

			
			String allDataString = response.getBody().toString();

			// convert to a usable jackson JSONNode
    		JsonNode root = mapper.readTree(allDataString);
    		
    		JsonNode resultsNode = root.path("results");


    		for (JsonNode result : resultsNode) {
    			// identify the sourceURL, use it to construct the recipes and set the unique id the uniqueID
				Recipe r = new Recipe(result.get("id").toString());
				r.setName(result.get("title").toString().replaceAll("\"", "")); // get rid of quotes in actual results
				if (result.get("image") != null) {
					r.setImageURL("https://spoonacular.com/recipeImages/" + result.get("image").toString().replaceAll("\"", ""));
				}
				recipes.add(r);
    		}

    		// now that we have all the recipes and their IDs, we need to go get the individual info for them....

    		for (Recipe recipe : recipes) {
    			System.out.println("retrieving information for recipe id: " + recipe.getUniqueId());
    			response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/{recipeID}/information")
					.header("X-RapidAPI-Key", "ebff0f5311msh75407f578a41008p14174ejsnf16b8bcf5559")
					.routeParam("recipeID", recipe.getUniqueId())
					.asJson();


				allDataString = response.getBody().toString();
				root = mapper.readTree(allDataString);

				if (root.get("spoonacularScore") != null) {
					recipe.setRating(Integer.parseInt(root.get("spoonacularScore").toString()));	
				}

				if (root.get("sourceUrl") != null) {
					recipe.setSourceURL(root.get("sourceUrl").toString().replaceAll("\"", ""));	
				}
				
				if (root.get("readyInMinutes") != null) {
					recipe.setPrepTime(Integer.parseInt(root.get("readyInMinutes").toString()));	
				}

				// if these fields exist, adjust them.
				if (root.get("preparationMinutes") != null) {
					recipe.setPrepTime(Integer.parseInt(root.get("preparationMinutes").toString()));
					recipe.setCookTime(Integer.parseInt(root.get("cookingMinutes").toString()));
				}


				// let's grab the ingredients first...
				ArrayList<String> ingredients = new ArrayList<String>();
				ArrayList<String> instructions = new ArrayList<String>();

				JsonNode ingredientsNode = root.path("extendedIngredients");
				for (JsonNode ingredient : ingredientsNode) {
					ingredients.add(ingredient.get("originalString").toString().replaceAll("\"", ""));

				}
				recipe.setIngredients(ingredients);

				// if there's an "analyzedInstructions" section, use it...
				JsonNode analyzedInstructionsNode = root.path("analyzedInstructions");
				if (analyzedInstructionsNode != null) {
					JsonNode stepsNode = analyzedInstructionsNode.path(0).path("steps");

					for (JsonNode step : stepsNode) {
						instructions.add(step.get("step").toString().replaceAll("\"", ""));
					}
					recipe.setInstructions(instructions);
				}
				else {
					instructions.add(root.get("instructions").toString().replaceAll("\"", ""));
				}

    		}

    		return recipes;


		} catch (UnirestException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		return null;

	}


	// should take the searchQuery as a parameter and ArrayList of thumnail links for the collage. 
	// NOTE: instead of creating collage on backend, ArrayList containing thumbnail links 
	// will be passed to the frontend 
	public ArrayList<String> createCollage(String searchQuery) {
		// TODO: Pull restaurants from external API and grab relevant information.
		
		// TODO: HOW MANY PICTURES DO WE WANT IN THE COLLAGE?
		
		// TODO: Do we want a data structure to hold the data for queries? 
		final String GET_URL = "https://www.googleapis.com/customsearch/v1?";
		final String cx = "001349756157526882706%3An5pmkqrjpfc";
		final String searchType = "image";
		final String key = "AIzaSyBiGl3y-IJ-tnfO_AhuUoeqIIhIHTqEJyo";
		
		String requestUrl = constructRequest(GET_URL, searchQuery, cx, searchType, key);
		String jsonResponse = getImagesJson(requestUrl);
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
		return "GET request not worked";
	}
	
	// extracts thumbnail links from JSON and returns them in ArrayList
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
			// TODO: CHANGE TO AMOUNT OF PICTURES NEEDED IN COLLAGE
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