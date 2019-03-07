import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import hello.Recipe;

import java.util.ArrayList;

public class TestRecipe {
	private static Recipe recipe;
	
	@Before 
	public void setup() {
		recipe = new Recipe("123456");
		recipe.setType("Recipe");
	}
	
	@Test
	public void testGetType() {
		assertEquals("Recipe", recipe.getType());
	}

	@Test
	public void testToString() {
		String expectedString = "Recipe [name=null, rating=0.0, prepTime=0.0, cookTime=0.0, ingredients=null, instructions=null, sourceURL=null, imageURL=null]";
		assertEquals(expectedString, recipe.toString());
	}
	
	@Test
	public void testGettersAndSetters() {
		recipe.setName("Chicken Parmesan");
		recipe.setRating(5.0);
		recipe.setPrepTime(20);
		recipe.setCookTime(40);

		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("1 oz ham");
		ingredients.add("2oz cheese");
		ingredients.add("2 slices bread");

		ArrayList<String> instructions = new ArrayList<String>();
		instructions.add("1. do the thing");
		instructions.add("2. finish the thing");
		recipe.setIngredients(ingredients);
		recipe.setInstructions(instructions);

		recipe.setSourceURL("https://www.allrecipes.com/recipe/223042/chicken-parmesan/");
		recipe.setImageURL("https://images.media-allrecipes.com/userphotos/560x315/4572704.jpg");
		
		assertEquals("Chicken Parmesan", recipe.getName());
		assertEquals(5.0, recipe.getRating(), 0.01);
		assertEquals(20.0, recipe.getPrepTime(), 0.01);
		assertEquals(40.0, recipe.getCookTime(), 0.01);
		assertEquals(ingredients, recipe.getIngredients());
		assertEquals(instructions, recipe.getInstructions());
		assertEquals("https://www.allrecipes.com/recipe/223042/chicken-parmesan/", recipe.getSourceURL());
		assertEquals("https://images.media-allrecipes.com/userphotos/560x315/4572704.jpg", recipe.getImageURL());
	}

}
