package hello;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


// Result class which Recipe and Restaurant will both subclass off of...

public class Recipe extends Result {
	private String name;
	private double numStars; // number of stars out of 5
	private double prepTime; // in mins
	private double cookTime;
	private ArrayList<String> ingredients; // by line, including quantity and item name
	private ArrayList<String> instructions; // prep instructions
	private String sourceURL; // source url for our own reference...

	public String writeToJSON() {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "empty";
		try {
			jsonString = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException exception) {
			System.out.println(exception);
		}
		return jsonString;

	}

	public Recipe(String uniqueId) {
		super(uniqueId);
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNumStars() {
		return numStars;
	}

	public void setNumStars(double numStars) {
		this.numStars = numStars;
	}

	public double getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(double prepTime) {
		this.prepTime = prepTime;
	}

	public double getCookTime() {
		return cookTime;
	}

	public void setCookTime(double cookTime) {
		this.cookTime = cookTime;
	}

	public ArrayList<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<String> ingredients) {
		this.ingredients = ingredients;
	}

	public ArrayList<String> getInstructions() {
		return instructions;
	}

	public void setInstructions(ArrayList<String> instructions) {
		this.instructions = instructions;
	}

	public String getSourceURL() {
		return sourceURL;
	}

	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}

}