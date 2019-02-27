package hello;


// Result class which Recipe and Restaurant will both subclass off of...

public class Restaurant extends Result {

	public Restaurant(String uniqueId, String jsonContent) {
		super(uniqueId, jsonContent);
		// this.uniqueId = uniqueId;
		// this.jsonContent = jsonContent;
	}
	
}