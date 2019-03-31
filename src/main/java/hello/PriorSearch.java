package hello;

public class PriorSearch {
	protected String term;
	protected Integer numberOfresults;
	protected Integer radius;

	public PriorSearch(String term, Integer numberOfresults, Integer radius) {
		this.term = term;
		this.numberOfresults = numberOfresults;
		this.radius = radius;
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

}
