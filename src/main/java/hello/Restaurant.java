package hello;


// Result class which Recipe and Restaurant will both subclass off of...
// Will use Google Places API

public class Restaurant extends Result {

	public Restaurant(String uniqueId, String jsonContent) {
		super(uniqueId, jsonContent);
		// this.uniqueId = uniqueId;
		// this.jsonContent = jsonContent;
	}
	
	//lat and lon for tommy trojan: 34.021240,-118.287209
}