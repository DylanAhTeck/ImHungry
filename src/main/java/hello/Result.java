package hello;


// Result class which Recipe and Restaurant will both subclass off of...

public class Result {
	protected String uniqueId;
	protected String jsonContent;

	public Result(String uniqueId, String jsonContent) {
		this.uniqueId = uniqueId;
		this.jsonContent = jsonContent;
	}

	public String getUniqueId() {
		return this.uniqueId;
	}

}