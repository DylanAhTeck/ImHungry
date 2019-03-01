package hello;


// Result class which Recipe and Restaurant will both subclass off of...

public class Restaurant extends Result {

	private String name;
	private double rating;
	private Integer drivingTime;
	private String phoneNumber; 
	private String address;
	private String website;
	private Integer priceLevel; // price range as expressed by Google Places API
	private String placeId; // string pulled from Google Places API


	public Restaurant(String uniqueId, String jsonContent) {
		super(uniqueId, jsonContent);

	}
	
}