package hello;


// Result class which Recipe and Restaurant will both subclass off of...

public class Recipe extends Result {

	public Recipe(String uniqueId, String jsonContent) {
		super(uniqueId, jsonContent);
	}

}