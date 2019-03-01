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
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getRating() {
		return rating;
	}


	public void setRating(double rating) {
		this.rating = rating;
	}


	public Integer getDrivingTime() {
		return drivingTime;
	}


	public void setDrivingTime(Integer drivingTime) {
		this.drivingTime = drivingTime;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}


	public Integer getPriceLevel() {
		return priceLevel;
	}


	public void setPriceLevel(Integer priceLevel) {
		this.priceLevel = priceLevel;
	}


	public String getPlaceId() {
		return placeId;
	}


	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	private String placeId; // string pulled from Google Places API


	public Restaurant(String uniqueId, String jsonContent) {
		super(uniqueId, jsonContent);

	}
	
}