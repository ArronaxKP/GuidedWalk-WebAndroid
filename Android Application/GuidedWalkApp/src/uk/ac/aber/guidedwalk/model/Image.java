package uk.ac.aber.guidedwalk.model;

import android.graphics.Bitmap;

/**
 * This class hold the information relating to a single image, such as bitmap
 * reference and filename.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class Image {

	private String filename;
	private Bitmap bitmap;

	/**
	 * Constructor for the class, which when instantiated all variables to null.
	 */
	public Image() {
		this.filename = null;
		this.bitmap = null;
	}

	/**
	 * Returns a String object that contains the image filename.
	 * 
	 * @return the filename of the linked bitmap.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * This method sets the filename for the image.
	 * 
	 * @param filename
	 *            the bitmap of the linked bitmap.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Returns a Bitmap object and this is bitmap for the image.
	 * 
	 * @return the bitmap for the image.
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * This method sets the bitmap for the image.
	 * 
	 * @param bitmap
	 *            the bitmap for the image.
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
