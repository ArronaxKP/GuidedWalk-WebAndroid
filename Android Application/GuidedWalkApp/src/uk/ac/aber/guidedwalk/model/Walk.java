package uk.ac.aber.guidedwalk.model;

import java.util.ArrayList;

/**
 * This class contains the information relating a single walk.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class Walk {
	private String id;
	private String walktitle;
	private String walkdesc;
	private int walkdifficulty;
	private int walklength;
	private ArrayList<Waypoint> route;
	private boolean selected;
	private int version;
	private boolean update;


	/**
	 * Basic constructor instantiating all variables to null or 0.
	 */
	public Walk() {
		this.id = null;
		this.walktitle = null;
		this.walkdesc = null;
		this.walklength = 0;
		this.route = new ArrayList<Waypoint>();
		this.update = false;
	}

	/**
	 * Returns a String object that contains the unique ID for the walk.
	 * 
	 * @return the unique ID for the map.
	 */
	public String getId() {
		return id;
	}

	/**
	 * This sets the unique ID for the walk.
	 * 
	 * @param id
	 *            the unique ID for the walk.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns a String object that contains the title for the walk.
	 * 
	 * @return the title for the walk.
	 */
	public String getWalkTitle() {
		return walktitle;
	}

	/**
	 * This sets the title for the walk.
	 * 
	 * @param walktitle
	 *            the title for the walk.
	 */
	public void setWalkTitle(String walktitle) {
		this.walktitle = walktitle;
	}

	/**
	 * Returns a String object that contains the description for the walk.
	 * 
	 * @return the description for the walk.
	 */
	public String getWalkDesc() {
		return walkdesc;
	}

	/**
	 * This sets the description for the walk.
	 * 
	 * @param walkdesc
	 *            the description for the walk.
	 */
	public void setWalkDesc(String walkdesc) {
		this.walkdesc = walkdesc;
	}

	/**
	 * Returns an int for the number of waypoints in the walk.
	 * 
	 * @return the number of waypoints in the walk.
	 */
	public int getWalklength() {
		return walklength;
	}

	/**
	 * This sets the number of waypoints in the walk.
	 * 
	 * @param walklength
	 *            the number of waypoints in the walk.
	 */
	public void setWalklength(int walklength) {
		this.walklength = walklength;
	}

	/**
	 * Returns an ArrayList containing a list of waypoints for the walk.
	 * 
	 * @return the the list of waypoints in the walk.
	 */
	public ArrayList<Waypoint> getRoute() {
		return route;
	}

	/**
	 * This method adds a waypoint to the internal ArrayList, which contains a
	 * list of waypoints making up the walk.
	 * 
	 * @param waypoint
	 *            the number of waypoints in the walk.
	 */
	public void addWaypoint(Waypoint waypoint) {
		route.add(waypoint);
	}

	/**
	 * This method gets the difficulty of the walk. 0 = easy. 1 = normal. 2 = hard.
	 * 
	 * @return the walk difficulty. 0 = easy. 1 = normal. 2 = hard.
	 */
	public int getWalkDifficulty() {
		return walkdifficulty;
	}

	/**
	 * This method sets the difficulty of the walk. 0 = easy. 1 = normal. 2 = hard.
	 * 
	 * @param difficulty the walk difficulty to be set. 0 = easy. 1 = normal. 2 = hard.
	 */
	public void setWalkDifficulty(int difficulty) {
		this.walkdifficulty = difficulty;
	}
	
	/**
	 * This method gets whether a walk is selected or not.
	 * 
	 * @return boolean value if walk is selected or not. True == selected.
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * This method sets whether the walk has been selected.
	 * 
	 * @param selected boolean value of whether item is selected. True == item selected.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * This method gets the version of the walk
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * This methods sets the version number for the walk
	 * 
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * This method returns a boolean value if an update is available
	 * 
	 * @return the true if an update is avialable else false
	 */
	public boolean isUpdateAvailable() {
		return update;
	}

	/**
	 * This method allows you to set whether an update is available.
	 * 
	 * @param update the boolean value to be set. True means update available else false.
	 */
	public void hasUpdateAvailable(boolean update) {
		this.update = update;
	}
}
