package hello;

import java.util.ArrayList;
import java.lang.Comparable;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;



// Result class which Recipe and Restaurant will both subclass off of...
@JsonIgnoreProperties
public class Recipe extends Result implements Comparable<Recipe> {
	private String name;
	private double rating; // number of stars out of 5
	private double prepTime; // in mins
	private double cookTime;
	private ArrayList<String> ingredients; // by line, including quantity and item name
	private ArrayList<String> instructions; // prep instructions
	private String sourceURL; // source url for our own reference...
	private String imageURL;
	private String type;
	private boolean isFavorite;
	
	public Recipe(String uniqueId) {
		super(uniqueId);
		type = "Recipe";
		// set default image url for those cases where there is no image url
		this.imageURL = "https://thumbs.dreamstime.com/z/freshly-cooked-feast-brazilian-dishes-top-down-view-various-home-made-recipes-displayed-colorful-textures-66645901.jpg";
		this.isFavorite = false;
	}
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public void setAsFavorite() {
		this.isFavorite = true;
	}

	public boolean getIsFavorite() {
		return this.isFavorite;
	}

	@Override
	 public int compareTo(Recipe compareRecipe) {
			 double comparePrepTime=((Recipe)compareRecipe).getPrepTime();
			 boolean compareIsFavorite = ((Recipe)compareRecipe).getIsFavorite();
			 /* For Ascending order*/
			 if(compareIsFavorite && this.isFavorite) {
				 if(this.prepTime < comparePrepTime) {
					 return -1;
				 } else if (this.prepTime < comparePrepTime) {
					 return 1;
				 } else return 0;
			 } else if(compareIsFavorite && !this.isFavorite) {
				 return 1;
			 } else if(!compareIsFavorite && this.isFavorite) {
				 return -1;
			 } else {
				 if(this.prepTime < comparePrepTime) {
					 return -1;
				 } else if (this.prepTime < comparePrepTime) {
					 return 1;
				 } else return 0;
			 }

	 }
}
