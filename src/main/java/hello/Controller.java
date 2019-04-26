package hello;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;


import javax.swing.text.Document;

// for image manipulation
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.net.URL;


import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;

// import com.google.cloud.storage.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auth.oauth2.GoogleCredentials;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Transaction;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.FirebaseApp;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


import io.github.cdimascio.dotenv.Dotenv;

// Imports the Google Cloud client library
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;




@RestController
public class Controller {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong(0);
	private ListManager listManager = new ListManager();
	private Firestore db = null;

	Dotenv dotenv = Dotenv.load();

	private String imagePathBaseURL = "https://storage.googleapis.com/csci-310-images/";



	// used for Google Images Searching
	public final String GET_URL = "https://www.googleapis.com/customsearch/v1?";
	public final String cx = "000316813068596776800:nkwqoquwebi";
	public final String searchType = "image";
	public final String key = dotenv.get("GOOGLE_IMAGE_API_KEY");

	// NOTE: We'll use this to track our most recent results prior to returning to Wayne
	private ArrayList<Recipe> mostRecentRecipes = new ArrayList<Recipe>();
	private ArrayList<Restaurant> mostRecentRestaurants = new ArrayList<Restaurant>();

	//User logged in ID
	private String userId;


	///////////////////////////////////////////////////
	// 												 //
	// 			OUR ENDPOINTS AND ROUTES   		     //
	// 												 //
	///////////////////////////////////////////////////

	public Controller() {
		super();
		//Configuration for Google Firebase Auth and database
		try {
			FileInputStream serviceAccount;
			serviceAccount = new FileInputStream("src/public/assets/serviceAccountKey.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
					  .setDatabaseUrl("https://csci310project2.firebaseio.com")
					  .build();
			FirebaseApp.initializeApp(options);
			db = FirestoreClient.getFirestore();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Working Directory = " +
		              System.getProperty("user.dir"));
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	@RequestMapping("/test")
//	@CrossOrigin
//	public String handleTestRequest() {
//		return "Look's like you're up and running!";
//	}
//
//	@RequestMapping("/testCollage")
//	@CrossOrigin
//    public String handleTestCollage(@RequestParam(defaultValue="null") String searchQuery) {
//        ArrayList<String> imageURLs = fetchImageURLs(searchQuery);
//        return imageURLs.toString();
//    }
//
//	@RequestMapping("/testRestaurant")
//	@CrossOrigin
//	public void handleTestRecipeRestaurant() {
//		try {
//			retrieveRestaurants("cream", 25);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@RequestMapping("/testAddToList")
//	@CrossOrigin
//	public void handleTestAddToList(@RequestParam(defaultValue="null") String uniqueId, @RequestParam(defaultValue="null") String targetList) {
//		ListManager m = new ListManager();
//
//		// quick test of add to list
//		m.addToList(new Result(uniqueId), targetList);
//		ArrayList<Result> favorites = m.getFavorites();
//		if (favorites.get(0).uniqueId.equals(uniqueId)) {
//			System.out.println("Added <" + uniqueId + "> to <" + targetList + ">");
//		} else {
//			System.out.println("Couldn't add <" + uniqueId + "> to <favorites>");
//		}
//		// move Result from one list to another
//		m.moveBetweenLists(uniqueId, "favorites", "toExplore");
//		if (favorites.size() == 0) {
//			System.out.println("<" + uniqueId + "> was removed from favorites");
//		} else {
//			System.out.println("<" + uniqueId + "> was NOT removed from favorites");
//		}
//		if (m.getToExplore().get(0).uniqueId.equals(uniqueId)) {
//			System.out.println("<" + uniqueId + "> was successfully moved to toExplore");
//		} else {
//			System.out.println("<" + uniqueId + "> was NOT successfully moved to toExplore");
//		}
//		// remove Result from toExplore
//		m.removeFromList(uniqueId, "toExplore");
//		if (m.getToExplore().size() == 0) {
//			System.out.println("<" + uniqueId + "> was removed from toExplore");
//		}
//
//	}
//

//	@RequestMapping("/testRecipe")
//	public String handleTestRecipeRequest() {
//		return getTestRecipeString();
//	}

//
//	// creates a Result with uniqueId: "test" and adds it to the recent recipe list and tried to retrieve it.
//	// a query which has "uniqueId" set to "test" should retrieve this result.
//	@RequestMapping("/testGetResult")
//	public String handleTestGetResult(@RequestParam(defaultValue="null") String uniqueId) {
//
//		ObjectMapper mapper = new ObjectMapper();
//		mostRecentRecipes.add(new Recipe("test"));
//
//		String resultString = "";
//		try {
//			resultString = mapper.writeValueAsString(getResult(uniqueId));
//		} catch (JsonProcessingException e) {
//			System.out.println(e);
//		}
//
//		return resultString;
//	}

	// returns the JSON of the result object if it exists, null otherwise
	@RequestMapping("/getResult")
	public String handleGetResult(@RequestParam(defaultValue="null") String uniqueId) {
		// tests for invalid parameters
		if (uniqueId == null) return "uniqueId == null";
		else if (uniqueId.equals("")) return "uniqueId is empty";

		ObjectMapper mapper = new ObjectMapper();

		String resultString = "";
		try {
			resultString = mapper.writeValueAsString(getResult(uniqueId));
		} catch (Exception e) { // JSONProcessingException
			System.out.println(e);
		}
		return resultString;
	}

//	@RequestMapping("/testSearchRecipe")
//	public String handleTestRecipeRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults) {
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			return mapper.writeValueAsString(retrieveRecipes(searchQuery, numResults));
//		} catch (JsonProcessingException e){
//			System.out.println("json processing exception when returning test recipe retrievals");
//		}
//		return "failure";
//	}



	@RequestMapping("/search")
	@CrossOrigin
	// TODO: Once the internal function calls exist, we'll need to put in the appropriate sequential calls here.
	public String handleSearchRequest(@RequestParam(defaultValue="null") String searchQuery, @RequestParam(defaultValue="5") Integer numResults, @RequestParam(defaultValue="5") Integer radius) {

		if (searchQuery == null || radius == 0) {
			return "Thanks for searching!";
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonNode imagesNode = mapper.createObjectNode();

		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			restaurants = retrieveRestaurants(searchQuery, numResults, radius);
			// saved list of restaurants returned from query in "cache"
			mostRecentRestaurants = restaurants;
		} catch (IOException e) {
			System.out.println("ioexception retrieving restaurants");
		}

		System.out.println("about to retrieve recipes");
		ArrayList<Recipe> recipes = retrieveRecipes(searchQuery, numResults);
		System.out.println("recipes retrieved");
		// saved list of recipes returned from query in "cache"
		mostRecentRecipes = recipes;
		
		// pull the images from the bing image search API 
		ArrayList<ImageData> collageImageData = retrieveImageData(searchQuery, numResults);
		ArrayList<String> collageURLs = new ArrayList<String>();
		for (ImageData i : collageImageData) {
			collageURLs.add(i.getThumbnailUrl());
		}


		// TODO: Generate the collage from the 10 image urls we retrieved...
		String collagePublicURL = generateCollage(collageURLs);
		System.out.println("public collage URL: " + collagePublicURL);
		
		// TODO: once collage has been created and its URL posted to Cloud Stoarge, set it here before adding the search to the db.
		// TODO: note that right now we're putting in the first image url as the collageURL -- will need to change this to the
		// actual collage URL when we have it
		
		// TODO: once generateCollage() is done, swap out collageURLs.get(0) with combinedCollageURL
		PriorSearch recentQuery = new PriorSearch(searchQuery, numResults, radius, collagePublicURL);
		addSearchToDB("priorSearchQueries", recentQuery);
		
		ArrayList<String> oldCollageURLs = fetchImageURLs(searchQuery);
    
		try {
			// using readtree to set these as json nodes
			((ObjectNode) rootNode).set("recipes", mapper.readTree(mapper.writeValueAsString(recipes)));
			((ObjectNode) rootNode).set("restaurants", mapper.readTree(mapper.writeValueAsString(restaurants)));
			((ObjectNode) rootNode).set("imageUrls", mapper.readTree(mapper.writeValueAsString(collageURLs)));
			((ObjectNode) rootNode).set("collageURL", mapper.readTree(mapper.writeValueAsString(collagePublicURL)));

			return mapper.writeValueAsString(rootNode);

		} catch (JsonProcessingException e) {
			System.out.println("json processing exception on creating search response");
		} catch (IOException e) {
			System.out.println("ioexception in reading tree");
		}
		return "failure";
	}

	@RequestMapping("/getList")
	@CrossOrigin
	public String getList(@RequestParam(defaultValue="null") String listName) {
		if (listName == null) {
			return "Invalid list name";
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		try {
			if (listName.equals("favorites")) {
				return new Gson().toJson(listManager.getFavorites());
				//((ObjectNode) rootNode).set("favorites", mapper.readTree(mapper.writeValueAsString(listManager.getFavorites())));
			} else if (listName.equals("toExplore")) {
				return new Gson().toJson(listManager.getToExplore());
				//((ObjectNode) rootNode).set("toExplore", mapper.readTree(mapper.writeValueAsString(listManager.getToExplore())));
			} else if (listName.equals("doNotShow")) {
				return new Gson().toJson(listManager.getdoNotShow());
				//((ObjectNode) rootNode).set("doNotShow", mapper.readTree(mapper.writeValueAsString(listManager.getdoNotShow())));
			} else {
				return "Invalid list name";
			}

			//return mapper.writeValueAsString(rootNode);
			

		} catch (Exception e) {
			e.printStackTrace();
			return "IOException";
		}
	}

//	// NOTE: this is a test endpoint that you can hit to make sure that you're actually adding a random item
//	// to the end of the favorites list.
//	@RequestMapping("/addItemToFavorites")
//	@CrossOrigin
//	public String addItemToFavorites() {
//		Result tempResult = new Result(String.valueOf(counter.incrementAndGet()));
//		listManager.addToList(tempResult, "favorites");
//		String favoritesString = listManager.getFavorites().toString();
//		return "favorites: " + favoritesString;
//	}

	@RequestMapping("/addToList")
	@CrossOrigin
	public String handleAddToList(@RequestParam String itemToAddId, @RequestParam String targetListName) {
		// check for missing parameters
		if (itemToAddId == null) {
			return "uniqueId == null";
		} else if (itemToAddId.equals("")) {
			return "No uniqueId provided";
		}
		if (targetListName == null) {
			return "targetListName == null";
		}
		else if (targetListName.equals("")) {
			return "No targetListName provided";
		}

		Result toAdd = null;
		// allows to skip searching second list if result is found in first list
		boolean foundItem = false;
		// search mostRecentRecipes for uniqueId
		for (int i=0; i<mostRecentRecipes.size(); i++) {
			// current element uniqueId matches uniqueId searching for
			System.out.println(mostRecentRecipes.get(i).getUniqueId());
			if (mostRecentRecipes.get(i).getUniqueId().equals(itemToAddId)) {
				// change value of toAdd to corresponding result
				toAdd = mostRecentRecipes.get(i);
				foundItem = true;
				break;
			}
		}
		// couldn't find result in Recipes List
		if (!foundItem) {
			// search mostRecentRestaurants for uniqueId
			for (int i=0; i<mostRecentRestaurants.size(); i++) {
				// current element uniqueId matches uniqueId searching for
				System.out.println(mostRecentRestaurants.get(i).getUniqueId());
				if (mostRecentRestaurants.get(i).getUniqueId().equals(itemToAddId)) {
					toAdd = mostRecentRestaurants.get(i);
					foundItem = true;
				}
			}
		}
		// couldn't find result in Restaurants or Recipes
		if (!foundItem) {
			return "Couldn't find uniqueId";
		}

		boolean added = listManager.addToList(toAdd, targetListName);

		if(added) {
			System.out.println("adding to list");
			addToDB(targetListName, toAdd);
		}
		return "Added item: " + toAdd.getUniqueId() + " to list: " + targetListName;
	}

	@RequestMapping("/removeFromList")
	@CrossOrigin
	public String handleRemoveFromList(@RequestParam String itemToRemoveId, @RequestParam String originListName) {
		// checks for invalid parameters
		if (itemToRemoveId == null) return "itemToRemoveId == null";
		else if (itemToRemoveId.equals("")) return "itemToRemoveId is empty";
		if (originListName == null) return "originListName == null";
		else if (originListName.equals("")) return "originListName is empty";
		// performs removal
		Result toBeRemoved = listManager.removeFromList(itemToRemoveId, originListName);

		removeFromDB(originListName, toBeRemoved);


		return "Removed item: " + itemToRemoveId + " from list: " + originListName;
	}

	@RequestMapping("/moveBetweenLists")
	@CrossOrigin
	public String handleMoveLists(@RequestParam String itemToMoveId, @RequestParam String originListName, @RequestParam String targetListName) {
		// checks for invalid parameters
		if (itemToMoveId == null) return "itemToMoveId == null";
		else if (itemToMoveId.equals("")) return "itemToMoveId is empty";
		if (originListName == null) return "originListName == null";
		else if (originListName.equals("")) return "originListName is empty";
		if (targetListName == null) return "targetListName == null";
		else if (targetListName.equals("")) return "targetListName is empty";

		Result result = listManager.moveBetweenLists(itemToMoveId, originListName, targetListName);

		removeFromDB(originListName, result);
		addToDB(targetListName, result);

		return "Moved item: " + itemToMoveId + " from list: " + originListName + " to list: " + targetListName;
	}

	//Endpoints for interacting with Firebase Authentication
	@RequestMapping("/loginUser")
	@CrossOrigin
	public String loginUser(@RequestParam String id) {

		UserRecord userRecord;
		try {
			userRecord = FirebaseAuth.getInstance().getUser(id);
			// See the UserRecord reference doc for the contents of userRecord.
			System.out.println("Successfully fetched user data: " + userRecord.getEmail());
			this.userId = id;

			DocumentReference docRef = db.collection("users").document(userId);
			System.out.println(userId);

			ApiFuture<DocumentSnapshot> future = docRef.get();

			try {
				DocumentSnapshot document = future.get();
				if (document.exists()) {
					setList(document);
				}
			} catch (Exception e){
				e.printStackTrace();
			}

			return "success";
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
		}



		return "failed";
	}
	
	public void setList(DocumentSnapshot document) {
		//Setting all the list to the ones from the database. Have to convert from string to object though.
		ArrayList<String> doNotShowString = (ArrayList<String>) document.get("doNotShow");
		ArrayList<Result> doNotShow = new ArrayList<Result>();
		for(int i = 0; i < doNotShowString.size(); i++) {
			Gson gson = new Gson();
			JsonElement element = gson.fromJson(doNotShowString.get(i), JsonElement.class);
			JsonObject object = element.getAsJsonObject();
			if(object.get("type").getAsString().equals("Recipe")) {
				doNotShow.add(gson.fromJson(doNotShowString.get(i), Recipe.class));
			} else {
				doNotShow.add(gson.fromJson(doNotShowString.get(i), Restaurant.class));
			}

		}
		ArrayList<String> favoritesString = (ArrayList<String>) document.get("favorites");
		ArrayList<Result> favorites = new ArrayList<Result>();
		for(int i = 0; i < favoritesString.size(); i++) {
			Gson gson = new Gson();
			JsonElement element = gson.fromJson(favoritesString.get(i), JsonElement.class);
			JsonObject object = element.getAsJsonObject();
			if(object.get("type").getAsString().equals("Recipe")) {
				favorites.add(gson.fromJson(favoritesString.get(i), Recipe.class));
			} else {
				favorites.add(gson.fromJson(favoritesString.get(i), Restaurant.class));
			}
		}
		ArrayList<String> toExploreString = (ArrayList<String>) document.get("toExplore");
		ArrayList<Result> toExplore = new ArrayList<Result>();
		for(int i = 0; i < toExploreString.size(); i++) {
			Gson gson = new Gson();
			JsonElement element = gson.fromJson(toExploreString.get(i), JsonElement.class);
			JsonObject object = element.getAsJsonObject();
			if(object.get("type").getAsString().equals("Recipe")) {
				toExplore.add(gson.fromJson(toExploreString.get(i), Recipe.class));
			} else {
				toExplore.add(gson.fromJson(toExploreString.get(i), Restaurant.class));
			}
		}
		listManager.setDoNotShow(doNotShow);
		listManager.setFavorites(favorites);
		listManager.setToExplore(toExplore);
	}


	//Signs a user out
	@RequestMapping("/signUserOut")
	@CrossOrigin
	public String signUserOut() {
		this.userId = "";
		listManager.setDoNotShow(new ArrayList<Result>());
		listManager.setFavorites(new ArrayList<Result>());
		listManager.setToExplore(new ArrayList<Result>());
		return "success";
	}
	//Registering a user and setting database

	@RequestMapping("/registerUser")
	@CrossOrigin
	public String registerUser(@RequestParam String email, @RequestParam String password) {
		CreateRequest request = new CreateRequest()
			    .setEmail(email)
			    .setEmailVerified(false)
			    .setPassword(password)
			    .setDisabled(false);

		try {
			UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

			// TODO: create a user database entry
			// TODO: create list entries for the user (doNotShow, favorites, toExplore, and priorSearches)
			// TODO: maybe establish some baseline properties of the lists (e.g. what info they contain?)

			Map<String, Object> docData = new HashMap<>();
			docData.put("userEmail", email);
			docData.put("doNotShow", new ArrayList<Result>());
			docData.put("favorites", new ArrayList<Result>());
			docData.put("toExplore", new ArrayList<Result>());
			docData.put("groceryList", new ArrayList<String>());
			docData.put("priorSearchQueries", new ArrayList<PriorSearch>()); // NOTE: this will depend on chris and dylan's work
			this.userId = userRecord.getUid();
			ApiFuture<WriteResult> future = db.collection("users").document(userId).set(docData);


			System.out.println("Successfully created new user: " + userRecord.getUid());
			return "success";
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
			return "failed";
		}

	}

	//Moving and item position up one in the array list
	@RequestMapping("/moveUpOne")
	@CrossOrigin
	public boolean moveUpOne(@RequestParam String uniqueId, @RequestParam String listName) {
		System.out.println("Attempting to move on " + listName);
		listManager.moveUpOne(uniqueId, listName);
		DocumentReference userDocRef = db.collection("users").document(userId);
		if(listName.equals("favorites")) {
			ArrayList<Result> favorites = listManager.getFavorites();
			ArrayList<String> jsonFavorites = new ArrayList<String>();
			for(int i = 0; i < favorites.size(); i++) {
				jsonFavorites.add(new Gson().toJson(favorites.get(i)));
			}
			Map<String, Object> updates = new HashMap<>();
			updates.put("favorites", jsonFavorites);
			ApiFuture<WriteResult> writeResult = userDocRef.update(updates);
			return true;
		} else if(listName.equals("toExplore")) {
			System.out.println("Attempting to move up on explore list");
			ArrayList<Result> toExplore = listManager.getToExplore();
			ArrayList<String> json = new ArrayList<String>();
			for(int i = 0; i < toExplore.size(); i++) {
				json.add(new Gson().toJson(toExplore.get(i)));
			}
			Map<String, Object> updates = new HashMap<>();
			updates.put("toExplore", json);
			ApiFuture<WriteResult> writeResult = userDocRef.update(updates);
			return true;
		} else if(listName.equals("doNotShow")) {
			ArrayList<Result> doNotShow = listManager.getdoNotShow();
			ArrayList<String> json = new ArrayList<String>();
			for(int i = 0; i < doNotShow.size(); i++) {
				json.add(new Gson().toJson(doNotShow.get(i)));
			}
			Map<String, Object> updates = new HashMap<>();
			updates.put("doNotShow", json);
			ApiFuture<WriteResult> writeResult = userDocRef.update(updates);
			return true;
		}
		return false;
	}

	//Moving and item position down one in the array list
	@RequestMapping("/moveDownOne")
	@CrossOrigin
	public boolean moveDownOne(@RequestParam String uniqueId, @RequestParam String listName) {
		listManager.moveDownOne(uniqueId, listName);
		DocumentReference userDocRef = db.collection("users").document(userId);
		if(listName.equals("favorites")) {
			ArrayList<Result> favorites = listManager.getFavorites();
			ArrayList<String> jsonFavorites = new ArrayList<String>();
			for(int i = 0; i < favorites.size(); i++) {
				jsonFavorites.add(new Gson().toJson(favorites.get(i)));
			}
			Map<String, Object> updates = new HashMap<>();
			updates.put("favorites", jsonFavorites);
			ApiFuture<WriteResult> writeResult = userDocRef.update(updates);
			return true;
		} else if(listName.equals("toExplore")) {
			ArrayList<Result> toExplore = listManager.getToExplore();
			ArrayList<String> json = new ArrayList<String>();
			for(int i = 0; i < toExplore.size(); i++) {
				json.add(new Gson().toJson(toExplore.get(i)));
			}
			Map<String, Object> updates = new HashMap<>();
			updates.put("toExplore", json);
			ApiFuture<WriteResult> writeResult = userDocRef.update(updates);
			return true;
		} else if(listName.equals("doNotShow")) {
			ArrayList<Result> doNotShow = listManager.getdoNotShow();
			ArrayList<String> json = new ArrayList<String>();
			for(int i = 0; i < doNotShow.size(); i++) {
				json.add(new Gson().toJson(doNotShow.get(i)));
			}
			Map<String, Object> updates = new HashMap<>();
			updates.put("doNotShow", json);
			ApiFuture<WriteResult> writeResult = userDocRef.update(updates);
			return true;
		}
		return false;
	}

	@RequestMapping("/addIngredient")
	@CrossOrigin
	//TODO: Ingredient string is added to database
	public boolean addIngredient(@RequestParam(defaultValue="null") String ingredient)
	{
		if(ingredient == null || ingredient == "" || userId == null || userId == "") return false;
		DocumentReference docRef = db.collection("users").document(userId);
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		boolean canAdd = false;
		int amount = 0;
		
		DocumentSnapshot document;
		//Seperate into amount and rest of string
		String arr[] = ingredient.split(" ", 2);
		try {
			amount = Integer.parseInt(arr[0]);
			canAdd = true;
		} catch(NumberFormatException e) {
			canAdd = false;
		}
		String theRest = arr[1];
		try {
			document = future.get();
			if (document.exists()) {
				  ArrayList<String> groceryList = (ArrayList<String>) document.getData().get("groceryList");
				  int i = 0;
				  if(canAdd) {
					  for(i = 0; i < groceryList.size(); i++) {
						  JsonObject o = new Gson().fromJson(groceryList.get(i), JsonObject.class);
						  String currentIngredient = o.get("ingredient").toString();
						  currentIngredient = currentIngredient.substring(1, currentIngredient.length()-1);
						  System.out.println(currentIngredient + " vs. " + ingredient);
						  String splitArray[] = currentIngredient.split(" ", 2);
						  int currentAmount = Integer.parseInt(splitArray[0]);
						  String currentRest = splitArray[1];
						  if(currentRest.contentEquals(theRest)) {
							  amount += currentAmount;
							  JsonObject temp = new JsonObject();
							  temp.addProperty("checked", false);
							  temp.addProperty("ingredient", amount + " " + theRest);
							  groceryList.set(i, temp.toString());
							  break;
						  }
						  
					  }
				  }
				  if(i == groceryList.size() || canAdd == false) {
					  JsonObject temp = new JsonObject();
					  temp.addProperty("checked", false);
					  temp.addProperty("ingredient", ingredient);
					  groceryList.add(temp.toString());
				  }
				  System.out.println("Grocery list now: " + groceryList.toString());
				  //upated in firebase
				  DocumentReference updateRef = db.collection("users").document(userId);

				  // (async) Update one field
				  ApiFuture<WriteResult> futureUpdate = docRef.update("groceryList", groceryList);
				  
				  WriteResult result = futureUpdate.get();
				  System.out.println("Write result: " + result);
				  
				  return true;
					
				} else {
				  System.out.println("No such document!");
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@RequestMapping("/removeIngredient")
	@CrossOrigin
	public boolean removeIngredient(@RequestParam(defaultValue="null") String ingredient)
		{
			if(this.userId == "" || this.userId == null || ingredient == "") return false;
			DocumentReference docRef = db.collection("users").document(userId);
			ApiFuture<DocumentSnapshot> future = docRef.get();
			// ...
			// future.get() blocks on response
			DocumentSnapshot document;
			
			try {
				document = future.get();
				if (document.exists()) {
					  ArrayList<String> groceryList = (ArrayList<String>) document.getData().get("groceryList");
					  int i;
					  for(i = 0; i < groceryList.size(); i++) {
						  JsonObject o = new Gson().fromJson(groceryList.get(i), JsonObject.class);
						  String currentIngredient = o.get("ingredient").toString();
						  currentIngredient = currentIngredient.substring(1, currentIngredient.length()-1);
						  System.out.println(currentIngredient + " vs. " + ingredient);
						  if(currentIngredient.equals(ingredient)) {
							  groceryList.remove(i);
						  }
						  
					  }
					 
					  System.out.println("Grocery list now: " + groceryList.toString());
					  //upated in firebase
					  DocumentReference updateRef = db.collection("users").document(userId);

					  // (async) Update one field
					  ApiFuture<WriteResult> futureUpdate = docRef.update("groceryList", groceryList);
					  
					  WriteResult result = futureUpdate.get();
					  System.out.println("Write result: " + result);
					  
					  return true;
						
					} else {
					  System.out.println("No such document!");
					}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	
	@RequestMapping("/updateIngredient")
	@CrossOrigin
	public boolean updateIngredient(@RequestParam(defaultValue="null") String ingredient, @RequestParam boolean checked) {
		System.out.println("attempting to update " + ingredient + " to " + checked);
		if(ingredient == null || ingredient == "" || userId == null) return false;
		DocumentReference docRef = db.collection("users").document(userId);
		ApiFuture<DocumentSnapshot> future = docRef.get();
		// ...
		// future.get() blocks on response
		DocumentSnapshot document;
		try {
			document = future.get();
			if (document.exists()) {
				  ArrayList<String> groceryList = (ArrayList<String>) document.getData().get("groceryList");
				  for(int i = 0; i < groceryList.size(); i++) {
					  JsonObject o = new Gson().fromJson(groceryList.get(i), JsonObject.class);
					  String currentIngredient = o.get("ingredient").toString();
					  System.out.println(currentIngredient.substring(1, currentIngredient.length()-1) + " vs. " + ingredient);
					  if(currentIngredient.substring(1, currentIngredient.length()-1).equals(ingredient)) {
						  JsonObject updated = new JsonObject();
						  updated.addProperty("checked", checked);
						  updated.addProperty("ingredient", ingredient);
						  groceryList.set(i, updated.toString());
					  }
					  
				  }
				  System.out.println("Grocery list now: " + groceryList.toString());
				  //upated in firebase
				  DocumentReference updateRef = db.collection("users").document(userId);

				  // (async) Update one field
				  ApiFuture<WriteResult> futureUpdate = docRef.update("groceryList", groceryList);
				  
				  WriteResult result = futureUpdate.get();
				  System.out.println("Write result: " + result);
				  
				  return true;
					
				} else {
				  System.out.println("No such document!");
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
  
	@RequestMapping("/testRetrieveImages")
	@CrossOrigin
	public String handleTestRecipeRestaurant() {
		ArrayList<ImageData> allImageData = retrieveImageData("burger", 10);
		ArrayList<String> imageURLs = new ArrayList<String>();
		if (allImageData.isEmpty()) {
			return "";
		}
		
		for (ImageData image : allImageData) {
			imageURLs.add(image.thumbnailUrl);
		}
		return imageURLs.toString();
	}

	///////////////////////////////////////////////////
	// 												 //
	// 	EXTERNAL API INTERACTION AND PROCESSING      //
	// NOTE: This is where the actual work happens.  //
	// 												 //
	///////////////////////////////////////////////////


	// public String getTestRecipeString() {

	// 	ArrayList<String> ingredients = new ArrayList<String>();
	// 	ingredients.add("1 oz ham");
	// 	ingredients.add("2oz cheese");
	// 	ingredients.add("2 slices bread");

	// 	ArrayList<String> instructions = new ArrayList<String>();
	// 	instructions.add("1. do the thing");
	// 	instructions.add("2. finish the thing");

	// 	Recipe r = new Recipe("1");
	// 	r.setIngredients(ingredients);
	// 	r.setName("best recipe");
	// 	r.setSourceURL("http://localhost:1000");
	// 	r.setPrepTime(40);
	// 	r.setInstructions(instructions);
	// 	r.setRating(2);

	// 	r.setCookTime(20);

	// 	return r.writeToJSON();

	// }

//	public String getTestRecipeString() {
//
//		ArrayList<String> ingredients = new ArrayList<String>();
//		ingredients.add("1 oz ham");
//		ingredients.add("2oz cheese");
//		ingredients.add("2 slices bread");
//
//		ArrayList<String> instructions = new ArrayList<String>();
//		instructions.add("1. do the thing");
//		instructions.add("2. finish the thing");
//
//		Recipe r = new Recipe("1");
//		r.setIngredients(ingredients);
//		r.setName("best recipe");
//		r.setSourceURL("http://localhost:1000");
//		r.setPrepTime(40);
//		r.setInstructions(instructions);
//		r.setRating(2);
//
//		r.setCookTime(20);
//
//		return r.writeToJSON();
//
//	}

	//
	public Result getResult(String uniqueId) {
		for (Recipe recipe : mostRecentRecipes) {
			System.out.println(recipe.getUniqueId());
			if (recipe.getUniqueId().equals(uniqueId)) {
				return recipe;
			}
		}

		for (Restaurant restaurant : mostRecentRestaurants) {
			System.out.println(restaurant.getUniqueId());
			if (restaurant.getUniqueId().equals(uniqueId)) {
				return restaurant;
			}
		}

		return null;
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
		String distanceRequestURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=34.021240,-118.287209&destinations=place_id:" + place_id + "&key=" + dotenv.get("GOOGLE_API_KEY");
		String res = callAPI(distanceRequestURL);
		try {
		JSONObject json = new JSONObject(res);
		JSONArray rows = json.getJSONArray("rows");
		JSONObject temp = (JSONObject) rows.get(0);
		JSONArray element = temp.getJSONArray("elements");

		JSONObject elements = (JSONObject) element.get(0);

		JSONObject duration = elements.getJSONObject("duration");
		

		return duration.getString("text");
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private String[] placesDetail(String place_id) throws MalformedURLException, IOException {
		String placesDetailURL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&fields=formatted_phone_number,formatted_address,website&key=" + dotenv.get("GOOGLE_API_KEY");
		String res = callAPI(placesDetailURL);
		try {
		JSONObject json = new JSONObject(res);
		JSONObject result = json.getJSONObject("result");
		String address = result.getString("formatted_address");

		//String phone = result.getString("formatted_phone_number");
		String phone = "unknown";
		if(result.has("formatted_phone_number")) {
			phone = result.getString("formatted_phone_number");
		} else {
			System.out.println("no info");
		}

		String website = "unknown";
		if(result.has("website")) {
			website = result.getString("website");
		}
		return new String[]{address, phone, website};
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private String generateCollage(ArrayList<String> imageURLs) {
		System.out.println("attempting to generte collage");
		/* load the images into memory
		// take the middle 100 x 100 pixels of each tumbnail
		// combine them in a 2x5 grid into a new image
		// write the new image to firebase and get the URL
		*/

		// ArrayList<URL> actualCollageURLs = new ArrayList<URL>();
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		ArrayList<BufferedImage> croppedImages = new ArrayList<BufferedImage>();

		URL url;
		// This will limit the size of the crop that we can use...
		int minWidth = 1000000;
		int minHeight = 1000000;
		
		for (String imageURL : imageURLs) {

			try {
				// actualCollageURL.add(new URL(imageURL));
				url = new URL(imageURL);
				BufferedImage bi = ImageIO.read(url);
				images.add(bi);

				if (bi.getHeight() < minHeight) {
					minHeight = bi.getHeight();
				}
				if (bi.getWidth() < minWidth) {
					minWidth = bi.getWidth();
				}
			} catch (MalformedURLException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			}

		}




		// create blob and upload to bucket

		Random rand = new Random();
	    int randInt = rand.nextInt(10000);
	    String collageName = "collage-" + randInt;

		// test to see if we're actually retrieving the images correctly...
		int counter = 0;
		// for (BufferedImage i : images) {
		// 	String singleImageFileName = collageName + "-" + counter + ".jpg";
		// 	File outFile = new File(singleImageFileName);
		// 	counter++;
		// 	try {
		// 		ImageIO.write(i, "jpg", outFile);
		// 	} catch (IOException e) {
		// 		System.out.println(e);
		// 	}


		// }


		// NOTE: Change these if you want the individual images to be smaller or larger
		// square it off based off the min of 
		int targetHeight = (minWidth <= minHeight) ? minWidth : minHeight;
		int targetWidth = (minWidth <= minHeight) ? minWidth : minHeight;

		for (BufferedImage image : images) {
			BufferedImage crop = image.getSubimage(image.getWidth()/2 - targetWidth/2, image.getHeight()/2-targetHeight/2, targetWidth, targetHeight);
			croppedImages.add(crop);
		}

		// for (BufferedImage i : croppedImages) {
		// 	String singleImageFileName = collageName + "-" + counter + ".jpg";
		// 	File outFile = new File(singleImageFileName);
		// 	counter++;
		// 	try {
		// 		ImageIO.write(i, "jpg", outFile);
		// 	} catch (IOException e) {
		// 		System.out.println(e);
		// 	}

		// }

		// loop through the images and write them to a new bufferedImage
		BufferedImage collage = new BufferedImage(targetWidth * 5, targetHeight * 2, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = collage.createGraphics();
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, targetWidth * 5, targetHeight * 2);
		
		int xStart = 0;
		int yStart = 0;

		// System.out.println("attempting to draw at: xStart=" + xStart + ", yStart=" + yStart);
		// g2.drawImage(croppedImages.get(0), xStart, yStart, targetWidth, targetHeight, null);

		for (BufferedImage croppedImage : croppedImages) {
			System.out.println("attempting to draw at: xStart=" + xStart + ", yStart=" + yStart);
			g2.drawImage(croppedImage, xStart, yStart, targetWidth, targetHeight, null);

			xStart += targetWidth;
			
			// loop down to second row...
			if (xStart == targetWidth * 5) {
				yStart = targetHeight;
				xStart = 0;
			}
		}

		g2.dispose();


		// // may not need to save to file before uploading...
		String fileName = collageName + ".jpg";
		// File outputFile = new File(fileName);
	 //    try {
		// 	ImageIO.write(collage, "jpg", outputFile);
			System.out.println("check out the collage at " + fileName);
		// } catch (IOException e) {
		// 	System.out.println(e);
		// }

		// get bytes of collage

		byte[] collageBytes = null;
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			ImageIO.write(collage, "jpg", outStream);
			outStream.flush();
			collageBytes = outStream.toByteArray();
			outStream.close();
		} catch (IOException e) {
			System.out.println(e);
		}

		Storage storage = StorageOptions.getDefaultInstance().getService();
		BlobId blobId = BlobId.of("csci-310-images", collageName + ".jpg");
		String publicCollagePath = imagePathBaseURL + collageName + ".jpg";
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
		Blob blob = storage.create(blobInfo, collageBytes);

		
		// return "http://www.calendarpedia.com/when-is/images/thanksgiving-day-turkey.jpg";
		return publicCollagePath;
	}

	private ArrayList<Restaurant> parseJSON(JSONObject json, Integer numResults) throws NumberFormatException, MalformedURLException, IOException{
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
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
		    		restaurants.add(0, restaurant); //add to front
		    	} else {
		    		restaurants.add(restaurant);
		    	}
	    	} else {
	    	    size++;
	    	}
	    }
		return restaurants;
		} catch (Exception e) {
			return null;
		}
	}

	//only for testing purposes
	public void setFav(ArrayList<Result> list){
		listManager.setFavorites(list);
	}

	//only for testing purposes
	public void setDoNotShow(ArrayList<Result> list){
		listManager.setDoNotShow(list);
	}

	// Retrieves the first "numResult" number of Restaurants from the Google Places API and returns them as an ArrayList
	public ArrayList<Restaurant> retrieveRestaurants(String searchQuery, Integer numResults, Integer radius) throws IOException {
		// TODO: Pull restaurants from external API and grab relevant information.
		double meters = toMeters(radius);
		String encodeQuery = URLEncoder.encode(searchQuery, "UTF-8");

		String placesRequestURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=34.021240,-118.287209&radius="+meters+"&type=restaurant&keyword=" + encodeQuery + "&key=" + dotenv.get("GOOGLE_API_KEY");

		String res = callAPI(placesRequestURL);
		try {
		JSONObject json = new JSONObject(res);
		System.out.println("Restaurants: " + json.toString());
		return parseJSON(json, numResults);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Retrieves the first "numResult" number of REcipes from the Spoonacular API and returns them as an ArrayList
	public ArrayList<Recipe> retrieveRecipes(String searchQuery, Integer numResults) {

		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();

		String doNotShow = "", fav = "";
		int numExtra = 0;

		for(Result result: listManager.getdoNotShow()) {
			if(result.getUniqueId().matches("[0-9]+") && result.getType() == "Recipe") {
				doNotShow += result.getUniqueId();
				numExtra += 1;
			}

		}

		for(Result result: listManager.getFavorites()) {
			fav += result.getUniqueId();
		}

		try {
			System.out.println("retrieving recipes...");
			HttpResponse<com.mashape.unirest.http.JsonNode> response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search")
					.header("X-RapidAPI-Key", dotenv.get("SPOONACULAR_API_KEY"))
					.queryString("query", searchQuery)
					.queryString("number", numResults + numExtra)
					.asJson();


			String allDataString = response.getBody().toString();
			System.out.println("all data:" + allDataString);

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
					if(!doNotShow.contains(r.getUniqueId())) {
						if(fav.contains(r.getUniqueId())) {
							r.setAsFavorite();
							recipes.add(0, r);
						} else {
							recipes.add(r);
						}
					}
    		}

    		// now that we have all the recipes and their IDs, we need to go get the individual info for them....

    		for (Recipe recipe : recipes) {
//    			System.out.println("retrieving information for recipe id: " + recipe.getUniqueId());
    			response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/{recipeID}/information")
					.header("X-RapidAPI-Key", dotenv.get("SPOONACULAR_API_KEY"))
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

			Collections.sort(recipes);
    		return recipes;


		} catch (UnirestException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		return null;

	}



	public ArrayList<ImageData> retrieveImageData(String searchQuery, Integer numResults) {
		ArrayList<ImageData> allImageData = new ArrayList<ImageData>();

		if (searchQuery == null || searchQuery.equals("")) {
			return allImageData;
		}

		try {
			System.out.println("Retrieving image URLs...");
			HttpResponse<com.mashape.unirest.http.JsonNode> response = Unirest.get("https://api.cognitive.microsoft.com/bing/v7.0/images/search")
					.header("Ocp-Apim-Subscription-Key", "8aa5d7ae8be04b1ca0b6bab3df69cbd1")
					.queryString("q", searchQuery)
					.queryString("count", 10)
					.asJson();

			
			String allDataString = response.getBody().toString();
			// System.out.println(allDataString);

			// convert to a usable jackson JSONNode
			ObjectMapper mapper = new ObjectMapper();
    		JsonNode root = mapper.readTree(allDataString);

    		JsonNode resultsNode = root.path("value");

    		for (JsonNode result : resultsNode) {
    			
    			ImageData image = new ImageData();
    			
				if (result.get("name") != null) {
					image.setName(result.get("name").toString());
				}

				if (result.get("thumbnailUrl") != null) {
					image.setThumbnailUrl(result.get("thumbnailUrl").toString().replaceAll("\"", ""));
					// NOTE: for logging
					System.out.println(result.get("thumbnailUrl").toString().replaceAll("\"", ""));
				}

				if (result.get("contentUrl") != null) {
					image.setContentUrl(result.get("contentUrl").toString().replaceAll("\"", ""));
				}

				if (result.get("height") != null) {
					image.setHeight(Integer.parseInt(result.get("height").toString()));
				}

				if (result.get("width") != null) {
					image.setWidth(Integer.parseInt(result.get("width").toString()));
				}

				if (image.thumbnailUrl != null || image.contentUrl != null) {
					allImageData.add(image);
				}

    		}
    		return allImageData;

		} catch (UnirestException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}









	// retrieves the first 10 results that match the search query from the Google Images API and return an ArrayList of URLs to them
	public ArrayList<String> fetchImageURLs(String searchQuery) {

		String encodeQuery = "";
		try {
			encodeQuery = URLEncoder.encode(searchQuery, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException");
			return null;
		}
		// constructs requestUrl with function call
		String requestUrl = constructRequest(GET_URL, encodeQuery, cx, searchType, key);
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
//				System.out.println(response.toString());
				// returns the formatted json
				return response.toString();
			} else {
				return "GET request not worked";
			}
		} catch (IOException e) {
			return "IOException";
		}
	}

	// extracts thumbnail links from JSON and returns them in ArrayList
	@SuppressWarnings("unchecked")
	public ArrayList<String> getThumbnailLinks(String jsonResponse) {
		ArrayList<String> thumbnailLinks = null;
		if (jsonResponse.equals("GET request not worked")) {
			return thumbnailLinks;
		}
		thumbnailLinks = new ArrayList<String>();
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
				thumbnailLinks.add(thumbnailLink);
			}
		} catch (org.json.simple.parser.ParseException e) {
			System.out.println("ParserException");
			return new ArrayList<String>();
		}
		return thumbnailLinks;
	}

	//add result to db
	public Boolean addSearchToDB(String originListName, PriorSearch search) {
		if(this.userId == "" || this.userId == null) return false;
		ObjectMapper mapper = new ObjectMapper();
		Gson gson = new Gson();
		System.out.println("current user id: " + userId);
		DocumentReference docRef = db.collection("users").document(userId);
		try {
			ApiFuture<WriteResult> arrayUnion = docRef.update(originListName,
				    FieldValue.arrayUnion(gson.toJson(search)));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}


	}

	//add result to db
	public Boolean addToDB(String originListName, Result result) {
		if(this.userId == "" || this.userId == null) return false;
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		DocumentReference docRef = db.collection("users").document(userId);
		try {
			ApiFuture<WriteResult> arrayUnion = docRef.update(originListName,
				    FieldValue.arrayUnion(new Gson().toJson(result)));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}


	}
	//remove result from db
	public Boolean removeFromDB(String originListName, Result result) {
		System.out.println("user id is " + this.userId);
		if(this.userId == "" || this.userId == null) return false;
		System.out.println("attempting to remove");
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.enableDefaultTyping();
		Gson gson = new Gson();
		// run an asynchronous transaction
		DocumentReference docRef = db.collection("users").document(userId);
		try {
			ApiFuture<WriteResult> arrayRm = docRef.update(originListName,
				    FieldValue.arrayRemove(gson.toJson(result)));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public double toMeters(int miles) {
		return miles * 1609.34;
	}

	public ListManager getListManager() {
		return listManager;
	}


}
