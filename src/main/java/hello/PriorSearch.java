package hello;

public class PriorSearch {
	protected String term;
	protected Integer numberOfresults;
	protected Integer radius;
	protected String collageURL;

	public PriorSearch(String term, Integer numberOfresults, Integer radius) {
		this.term = term;
		this.numberOfresults = numberOfresults;
		this.radius = radius;
		this.collageURL = "";
	}

	// NOTE: second constructor to be used if we know the collageURL and object generation time
	public PriorSearch(String term, Integer numberOfresults, Integer radius, String collageURL) {
		this.term = term;
		this.numberOfresults = numberOfresults;
		this.radius = radius;
		this.collageURL = collageURL;
	}

	public String getTerm() {
		return this.term;
	}

	public Integer getNum() {
		return this.numberOfresults;
	}

	public Integer getRadius() {
		return this.radius;
	}

	public void setCollageURL(String collageURL) {
		this.collageURL = collageURL;
	}

}
