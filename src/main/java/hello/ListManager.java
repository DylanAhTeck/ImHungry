package hello;

public class ListManager {
	private ArrayList<Result> toExplore = new ArrayList<Result>();
	private ArrayList<Result> favorites = new ArrayList<Result>();
	private ArrayList<Result> doNotShow = new ArrayList<Result>();


	public ListManager() {

	}

	public ArrayList<Result> getToExplore() {
		return toExplore;
	}

	public ArrayList<Result> getFavorites() {
		return favorites;
	}

	public ArrayList<Result> getdoNotShow() {
		return doNotShow;
	}

	

	// returns whether item was added successfully to list
	public boolean addtoList(Result itemToAdd, String listName) {
		if (listName.equals("favorites")) {
			favorites.add(itemToAdd);
			return true;
		}
		else if (listName.equals("toExplore")) {
			toExplore.add(itemToAdd);
			return true;
		}
		else if (listName.equals("doNotShow")) {
			doNotShow.add(itemToAdd);
			return true;
		}
		else {
			return false;
		}
	}
}