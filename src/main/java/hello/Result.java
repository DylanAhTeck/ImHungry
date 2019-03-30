package hello;

import java.util.HashMap;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


// Result class which Recipe and Restaurant will both subclass off of...
public class Result {
	protected String uniqueId; // sourceURL for Recipe, placeID for Restaurant
	
	// NOTE: we may not need this, but this is just the JSON-ified representation of an object in the class
	// look up Jackson ObjectWriter for how to convert Java objects to JSON
	protected String jsonContent; 

	public Result(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUniqueId() {
		return this.uniqueId;
	}

}