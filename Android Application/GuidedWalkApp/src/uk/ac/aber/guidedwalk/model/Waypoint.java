package uk.ac.aber.guidedwalk.model;

import java.util.ArrayList;

/**
 * This class contains the information relating to a single waypoint.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class Waypoint {
	private String title;
	private String description;
	private String welshtitle;
	private String welshdescription;
	private int index;
	private double lat;
	private double lng;
	private int numberOfImages;
	private int imageInView;
	private ArrayList<Image> images;

	/**
	 * Basic constructor instantiating all variables to 0, except the title and
	 * description which are set to null. The images ArrayList is instantiated
	 * as a new ArrayList.
	 */
	public Waypoint() {
		this.title = null;
		this.description = null;
		this.welshtitle = null;
		this.welshdescription = null;
		this.index = 0;
		this.lat = 0;
		this.lng = 0;
		this.numberOfImages = 0;
		this.images = new ArrayList<Image>();
		this.imageInView = 0;
	}

	/**
	 * Returns a String object that contains the title for the waypoint.
	 * 
	 * @return the title for the waypoint.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This sets the title for the waypoint.
	 * 
	 * @param title
	 *            the title for the waypoint.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns a String object that contains the Welsh title for the waypoint.
	 * 
	 * @return the Welsh title for the waypoint.
	 */
	public String getWelshTitle() {
		return welshtitle;
	}

	/**
	 * This sets the Welsh title for the waypoint.
	 * 
	 * @param welshtitle
	 *            the Welsh title for the waypoint.
	 */
	public void setWelshTitle(String welshtitle) {
		this.welshtitle = welshtitle;
	}

	/**
	 * Returns a String object that contains the description for the waypoint.
	 * 
	 * @return the description for the waypoint.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This sets the description for the waypoint.
	 * 
	 * @param description
	 *            the description for the waypoint.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns a String object that contains the Welsh description for the waypoint.
	 * 
	 * @return the Welsh description for the waypoint.
	 */
	public String getWelshDescription() {
		return welshdescription;
	}

	/**
	 * This sets the Welsh description for the waypoint.
	 * 
	 * @param welshdescription
	 *            the Welsh description for the waypoint.
	 */
	public void setWelshDescription(String welshdescription) {
		this.welshdescription = welshdescription;
	}

	/**
	 * Returns an int that contains the index for the waypoint.
	 * 
	 * @return the index for the waypoint.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * This sets the index for the waypoint.
	 * 
	 * @param index
	 *            the index for the waypoint.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Returns an int that contains the latitude for the waypoint.
	 * 
	 * @return the latitude for the waypoint.
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * This sets the latitude for the waypoint.
	 * 
	 * @param lat
	 *            the latitude for the waypoint.
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * Returns an int that contains the longitude for the waypoint.
	 * 
	 * @return the longitude for the waypoint.
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * This sets the longitude for the waypoint.
	 * 
	 * @param lng
	 *            the longitude for the waypoint.
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * This returns the number of images for the waypoint.
	 * 
	 * @return the number of images for the waypoint.
	 */
	public int getnumberOfImages() {
		return numberOfImages;
	}

	/**
	 * This sets the number of images for the waypoint.
	 * 
	 * @param numberofimages
	 *            the number of images for the waypoint.
	 */
	public void setNoimages(int numberofimages) {
		this.numberOfImages = numberofimages;
	}

	/**
	 * Returns an ArrayList containing a list of Image objects.
	 * 
	 * @return the list of image objects linked to the waypoint.
	 */
	public ArrayList<Image> getImages() {
		return this.images;
	}

	/**
	 * This method adds an image to the internal ArrayList of Image Objects.
	 * 
	 * @param img
	 *            an image object to be added to the list linked to the wypoint.
	 */
	public void addImage(Image img) {
		images.add(img);
	}

	/**
	 * This returns the int that contains the index reference for the image
	 * shown in the waypoint dialog.
	 * 
	 * @return the int that contains the image index reference.
	 */
	public int getImageInView() {
		return imageInView;
	}

	/**
	 * This sets the int that contains the index reference for the image shown
	 * in the waypoint dialog.
	 * 
	 * @param imageInView
	 *            the int that contains the image index reference.
	 */
	public void setImageInView(int imageInView) {
		this.imageInView = imageInView;
	}
}
