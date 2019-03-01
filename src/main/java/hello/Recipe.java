package hello;

import java.util.ArrayList;


// Result class which Recipe and Restaurant will both subclass off of...

public class Recipe extends Result {

	private String name;
	private double numStars; // nnumber of stars out of 5
	private double prepTime; // in mins
	private double cookTime;
	private ArrayList<String> ingredients; // by line, including quantity and item name
	private ArrayList<String> instructions; // prep instructrions
	private String sourceURL; // source url for our own reference...

	public Recipe(String uniqueId, String jsonContent) {
		super(uniqueId, jsonContent);
	}

}