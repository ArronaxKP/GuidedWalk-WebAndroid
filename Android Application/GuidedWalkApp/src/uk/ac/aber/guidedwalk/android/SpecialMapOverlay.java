package uk.ac.aber.guidedwalk.android;

import java.util.ArrayList;

import uk.ac.aber.guidedwalk.model.Walk;
import uk.ac.aber.guidedwalk.model.Waypoint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

/**
 * This Class extends the ItermizedOverlay Class.<!-- --> This class is
 * responsible for the list of items that will be overlaid on the map display.
 * This class will also deal with the events caused by the overlaid items.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class SpecialMapOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	private MapController mControl;
	private MapScreen mapscreen;
	private Walk walk;
	private Dialog dialog;

	public SpecialMapOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		this.populate();

	}

	/**
	 * Constructor that links the map context to the overlay as well as defining
	 * the default icon for the items on the overlay.
	 * 
	 * @param defaultMarker
	 *            default icon for the marker
	 * @param mapscreen
	 *            map context passed from the Activity Class implementing this
	 *            overlay.
	 */
	public SpecialMapOverlay(Drawable defaultMarker, MapScreen mapscreen) {
		this(defaultMarker);
		this.mapscreen = mapscreen;
		mControl = null;
		walk = null;
		dialog = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return mapOverlays.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected boolean onTap(final int index) {
		return this.markerClicked(index);
	}

	/**
	 * This is the main method used through out the Android Application. This
	 * method is called wan an overlay items, such as a waypoint, is tapped or
	 * clicked on the map. Its role is to create a dialog displaying all the
	 * necessary information about the tapped item.
	 * 
	 * @param index
	 *            the index of the item on the overlay which was Clicked.
	 * @return the boolean value true if method succeeded.
	 */
	private boolean markerClicked(final int index) {
		if (this.checkWaypoint(index)) {
			mControl.animateTo(mapOverlays.get(index).getPoint());
			OverlayItem item = mapOverlays.get(index);
			dialog = new Dialog(mapscreen);
			dialog.setTitle(item.getTitle());
			dialog.setContentView(R.layout.maindialog);

			/**
			 * The final items listed below creates a reference to each item in
			 * the dialog view to allow for listeners to be created.
			 */
			TextView dial_desc = (TextView) dialog
					.findViewById(R.id.dial_desc);
			
			dial_desc.setMovementMethod(new ScrollingMovementMethod());

			Button close = (Button) dialog.findViewById(R.id.dial_close);
			Button next = (Button) dialog.findViewById(R.id.dial_next);
			Button prev = (Button) dialog.findViewById(R.id.dial_prev);

			Button imgnext = (Button) dialog
					.findViewById(R.id.dial_img_next);
			Button imgprev = (Button) dialog
					.findViewById(R.id.dial_img_prev);
			Button imghide = (Button) dialog
					.findViewById(R.id.dial_img_hide);

			ImageView imgV = (ImageView) dialog
					.findViewById(R.id.dial_ImageView);

			/**
			 * Sets the waypoint description
			 */
			Waypoint waypoint = walk.getRoute().get(index);
			dial_desc.setText(waypoint.getDescription());

			/**
			 * Sets the listener on the close button to close the dialog when
			 * pressed.
			 */
			close.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
					Button startButton = (Button) mapscreen.findViewById(R.id.start_button);
					startButton.setText("Continue walk");
	            	startButton.setVisibility(View.VISIBLE);
	            	mapscreen.setLastWaypoint(index);
				}
			});

			/**
			 * This if checks if the waypoint is the last one, if it is it
			 * removes the next button. If it isn't it then adds a listener to
			 * the next button. This listener deals with the issues of when the
			 * next button is pressed by calling this function again except
			 * parsing the next waypoint's index.
			 */
			if (index == (walk.getWalklength() - 1)) {
				next.setVisibility(View.GONE);
			} else {
				final int nextInt = index + 1;
				next.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						dialog.cancel();
						onTap(nextInt);
					}
				});
			}

			/**
			 * This if checks if the waypoint is the first one, if it is it
			 * removes the previous button. If it isn't it then adds a listener
			 * to the previous button. This listener deals with the issues of
			 * when the previous button is pressed by calling this function
			 * again except parsing the next waypoint's index.
			 */
			if (0 == index) {
				prev.setVisibility(View.GONE);
			} else {
				final int prevInt = index - 1;
				prev.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						dialog.cancel();
						onTap(prevInt);
					}
				});
			}

			/**
			 * This if checks if the waypoint has any linked images attached.
			 */
			if (0 != waypoint.getImages().size()) {
				/**
				 * The next line gets the image bitmap from the waypoint. Which
				 * image is currently in view is teacked by the variable
				 * imageInView in the waypoint Class.
				 */
				imgV.setImageBitmap(waypoint.getImages()
						.get(waypoint.getImageInView()).getBitmap());

				/**
				 * This if checks if it is the first image and then removes the
				 * previous image button if it is.
				 */
				if (0 == waypoint.getImageInView()) {
					imgprev.setVisibility(View.GONE);
				}
				/**
				 * This if checks if it is the last image and then removes the
				 * next image button if it is.
				 */
				if (waypoint.getImages().size() - 1 == waypoint
						.getImageInView()) {
					imgnext.setVisibility(View.GONE);
				}

				/**
				 * The next sections sets up the button listeners on each of the
				 * buttons.
				 */

				imgnext.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						walk.getRoute()
								.get(index)
								.setImageInView(
										walk.getRoute().get(index)
												.getImageInView() + 1);
						ImageView imgV = (ImageView) dialog
								.findViewById(R.id.dial_ImageView);
						imgV.setImageBitmap(walk
								.getRoute()
								.get(index)
								.getImages()
								.get(walk.getRoute().get(index)
										.getImageInView()).getBitmap());
						Button imgprev = (Button) dialog
								.findViewById(R.id.dial_img_prev);
						imgprev.setVisibility(View.VISIBLE);
						if (walk.getRoute().get(index).getImageInView() == walk
								.getRoute().get(index).getImages().size() - 1) {
							Button imgnext = (Button) dialog
									.findViewById(R.id.dial_img_next);
							imgnext.setVisibility(View.GONE);
						}
					}
				});
				imgprev.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						walk.getRoute()
								.get(index)
								.setImageInView(
										walk.getRoute().get(index)
												.getImageInView() - 1);
						ImageView imgV = (ImageView) dialog
								.findViewById(R.id.dial_ImageView);
						imgV.setImageBitmap(walk
								.getRoute()
								.get(index)
								.getImages()
								.get(walk.getRoute().get(index)
										.getImageInView()).getBitmap());
						Button imgnext = (Button) dialog
								.findViewById(R.id.dial_img_next);
						imgnext.setVisibility(View.VISIBLE);
						if (0 == walk.getRoute().get(index).getImageInView()) {
							Button imgprev = (Button) dialog
									.findViewById(R.id.dial_img_prev);
							imgprev.setVisibility(View.GONE);
						}
					}
				});
				imghide.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						ImageView imgV = (ImageView) dialog
								.findViewById(R.id.dial_ImageView);
						if (View.GONE == imgV.getVisibility()) {
							imgV.setVisibility(View.VISIBLE);
							Button hide = (Button) dialog
									.findViewById(R.id.dial_img_hide);
							hide.setText("Hide");
						} else {
							imgV.setVisibility(View.GONE);
							Button hide = (Button) dialog
									.findViewById(R.id.dial_img_hide);
							hide.setText("Show");
						}

					}
				});
			} else {
				imgV.setVisibility(View.GONE);
				imgnext.setVisibility(View.GONE);
				imgprev.setVisibility(View.GONE);
				imghide.setVisibility(View.GONE);
			}
			if(dialog!=null){
				dialog.show();
			}
		}
		
		else {
			/**
			 * This section shows a custom dialog saying waypoint is empty and
			 * proceeding to the next waypoint if there is one.
			 */
			if (index == walk.getRoute().size() - 1) {
				new AlertDialog.Builder(mapscreen)
						.setMessage(
								"You have reached the end of the walk. Click OK to go back to the map")
						.setTitle("Waypoint Information")
						.setCancelable(false)
						.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.cancel();
										Button startButton = (Button) mapscreen.findViewById(R.id.start_button);
										startButton.setText("Start again walk");
						            	startButton.setVisibility(View.VISIBLE);
						            	mapscreen.setLastWaypoint(0);
									}
								}).show();
			} else {
				new AlertDialog.Builder(mapscreen)
						.setMessage(
								"The next waypoint is empty. Click OK to go to the one after or cancel to go back to the map")
						.setTitle("Waypoint Information")
						.setCancelable(true)
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										onTap(index + 1);
									}
								})
						.setNegativeButton(android.R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.cancel();
										Button startButton = (Button) mapscreen.findViewById(R.id.start_button);
										startButton.setText("Continue walk");
						            	startButton.setVisibility(View.VISIBLE);
						            	mapscreen.setLastWaypoint(index);
									}
								}).show();
			}
		}
		return true;
	}

	/**
	 * The method checks the a waypoint based on the index to see if it contains
	 * a title, description or any linked images. If any are in the waypoint it
	 * returns true else false. Currently If statement doesnt work properly and
	 * never detects a blank waypoint
	 * 
	 * @param index
	 *            the index of the waypoint in the list of waypoints
	 * @return returns false if waypoint is empty else true
	 */
	private boolean checkWaypoint(int index) {
		Waypoint waypoint = walk.getRoute().get(index);
		// if not working.
		boolean bool = true;
		if (0 == waypoint.getnumberOfImages()) {
			if (waypoint.getTitle() == null) {
				if (waypoint.getDescription() == null) {
					bool = false;
				} else {
					if (waypoint.getDescription().equalsIgnoreCase("")) {
						bool = false;
					}
				}
			}else{
				if (waypoint.getTitle().equalsIgnoreCase("")) {
					if (waypoint.getDescription() == null) {
						bool = false;
					} else {
						if (waypoint.getDescription().equalsIgnoreCase("")) {
							bool = false;
						}
					}
				}
			}
		}
		return bool;
	}

	/**
	 * The method that deals with the icon choices when an item is added to the
	 * overlay.
	 * 
	 * This code was influenced by the following site: -
	 * http://developmentality.
	 * wordpress.com/2009/10/16/android-overlayitemsetmarkerdrawable-icon/
	 * 
	 * @param waypoint
	 *            the overlayitem referring the the waypoint that will be
	 *            displayed on the map.
	 * @param index
	 *            the index of the waypoint in the list of waypoints.
	 * @param size
	 *            the size of the list of waypoints.
	 */
	public void addWaypoint(OverlayItem waypoint, int index, int size) {
		if (index == 0) {
			waypoint.setMarker(boundCenterBottom(mapscreen.getResources()
					.getDrawable(R.drawable.green_dot)));
		} else if (index == size - 1) {
			waypoint.setMarker(boundCenterBottom(mapscreen.getResources()
					.getDrawable(R.drawable.final_dot)));
		} else {
			waypoint.setMarker(boundCenterBottom(mapscreen.getResources()
					.getDrawable(R.drawable.red_dot)));
		}
		mapOverlays.add(waypoint);
		this.populate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.maps.ItemizedOverlay#draw(android.graphics.Canvas,
	 * com.google.android.maps.MapView, boolean)
	 * 
	 * This method deals with the drawing of the overlay onto the Map screen.
	 */
	@Override
	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
		super.draw(canvas, mapv, shadow);

		Paint mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);

		Projection projection = mapv.getProjection();
		Path path = new Path();
		Boolean firstTime = true;
		for (OverlayItem item : mapOverlays) {
			GeoPoint marker = item.getPoint();
			Point point = new Point();
			projection.toPixels(marker, point);
			if (firstTime) {
				path.moveTo(point.x, point.y);
				firstTime = false;
			} else {
				path.lineTo(point.x, point.y);
			}
		}
		canvas.drawPath(path, mPaint);
	}

	/**
	 * This method adds the map controller to the Class providing the ability to
	 * control the map using the overlaid items.
	 * 
	 * @param mControl
	 *            the map controller passed from the MapActivity Class.
	 */
	public void addMapController(MapController mControl) {
		this.mControl = mControl;
	}

	/**
	 * This method adds the walk to the Class providing information, such as the
	 * waypoint details, so this can be used to create the guided walk.
	 * 
	 * @param walk
	 *            the walk that was selected and loaded.
	 */
	public void addWalk(Walk walk) {
		this.walk = walk;
	}

}
