package hello;

import java.util.ArrayList;

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
	public boolean addToList(Result itemToAdd, String targetListName) {
		if (targetListName.equals("favorites")) {
			favorites.add(itemToAdd);
			return true;
		}
		else if (targetListName.equals("toExplore")) {
			toExplore.add(itemToAdd);
			return true;
		}
		else if (targetListName.equals("doNotShow")) {
			doNotShow.add(itemToAdd);
			return true;
		}
		else {
			return false;
		}
	}

	
	public boolean moveBetweenLists(String uniqueId, String originListName, String targetListName) {
		// TODO: Fill this in!
		return true;
	}


	public boolean removeFromList(Result itemToremove, String originListName) {
		// TODO Fill this in!
		return true;
	}
}