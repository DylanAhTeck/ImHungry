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
		// removes object from originList
		Result itemToMove = removeFromList(uniqueId, originListName);
		
		// itemToMove was not in origin list
		if (itemToMove == null) return false;
		
		// target list is favorites
		if (targetListName.equals("favorites")) {
			favorites.add(itemToMove);
		} 
		// target list is toExplore
		else if (targetListName.equals("toExplore")) {
			toExplore.add(itemToMove);
		}
		// target list is doNotDisturb
		else if (targetListName.equals("doNotShow")) {
			doNotShow.add(itemToMove);
		}
		return true;
	}

	// given uniqueId of item to remove and origin list, removes object from list and returns it 
	public Result removeFromList(String uniqueId, String originListName) {
		Result itemToRemove = null;
		// if toMove originates in favorites list
		if (originListName.equals("favorites")) {
			// removes item from originList
			for(int i=0; i<favorites.size(); i++) {
				// if object in list matches the one to remove 
				if (favorites.get(i).uniqueId.equals(uniqueId)) {
					itemToRemove = favorites.get(i);
					favorites.remove(i);
					break;
				}
			}
		} 
		// if toMove originates in toExplore list
		else if (originListName.equals("toExplore")) {
			// removes item from originList
			for(int i=0; i<toExplore.size(); i++) {
				// if object in list matches the one to remove 
				if (toExplore.get(i).uniqueId.equals(uniqueId)) {
					itemToRemove = toExplore.get(i);
					toExplore.remove(i);
					break;
				}
			}
		}
		// if toMove originates in doNotShow list
		else if (originListName.equals("doNotDisturb")) {
			// removes item from originList
			for(int i=0; i<doNotShow.size(); i++) {
				// if object in list matches the one to remove 
				if (doNotShow.get(i).uniqueId.equals(uniqueId)) {
					itemToRemove = doNotShow.get(i);
					doNotShow.remove(i);
					break;
				}
			}
		}
		return itemToRemove;
	}
}