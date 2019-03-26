Ppackage hello;


// Result class which Recipe and Restaurant will both subclass off of...

public class PriorSearch {
	protected String term;
	protected Integer numberOfresults;

	public PriorSearch(String term, Integer numberOfresults) {
		this.term = term;
		this.numberOfresults = numberOfresults;
	}

	public String getTerm() {
		return this.term;
	}

	public Integer getNum() {
		return this.numberOfresults;
	}

}
