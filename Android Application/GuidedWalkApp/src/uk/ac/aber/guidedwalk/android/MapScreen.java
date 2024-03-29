package uk.ac.aber.guidedwalk.android;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aber.guidedwalk.model.LoadXML;
import uk.ac.aber.guidedwalk.model.Walk;
import uk.ac.aber.guidedwalk.model.Waypoint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * This class extends the MapActivity Class which provides the ability to
 * dispaly an Android Map. This Class was heavily influenced by the following
 * site: -
 * 
 * http://www.javacodegeeks.com/2011/02/android-google-maps-tutorial.html
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class MapScreen extends MapActivity {

	private MapScreen mapscreen = this;
	private MyLocationOverlay me = null;
	private Walk walk;
	private String walkid;
	private SpecialMapOverlay specialOverlay;
	private ProgressDialog m_ProgressDialog = null;
	private Runnable viewWalk;
	private int lastWaypoint;

	/**
	 * This is ran after the SpecialMapOverlay has finsihed being setup. The
	 * below thread sets up the map and adds the overlay.
	 */
	private Runnable returnRes = new Runnable() {
		public void run() {
			m_ProgressDialog.dismiss();
			if (null != specialOverlay) {
				Button startButton = (Button) findViewById(R.id.start_button);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mapscreen);
				Boolean wantEnglish = prefs.getBoolean("wantEnglish", true);
				if(!wantEnglish){
					startButton.setText(R.string.welsh_start_walk);
				}
				startButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						specialOverlay.onTap(lastWaypoint);
						Button startButton = (Button) findViewById(R.id.start_button);
						startButton.setVisibility(View.GONE);
					}
				});
				MapView mapView = (MapView) findViewById(R.id.map_view);
				mapView.setBuiltInZoomControls(true);
				List<Overlay> mapOverlays = mapView.getOverlays();
				me = new MyLocationOverlay(mapscreen, mapView);
				me.enableCompass();
				me.enableMyLocation();
				mapOverlays.add(me);
				mapOverlays.add(specialOverlay);
				MapController mControl = mapView.getController();
				specialOverlay.addMapController(mControl);
				specialOverlay.addWalk(walk);
				mControl.animateTo(specialOverlay.getCenter());
				mControl.setZoom(17);
				mapView.invalidate();
			} else {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mapscreen);
				Boolean wantEnglish = prefs.getBoolean("wantEnglish", true);
				String message = "";
				if(wantEnglish){
					message = "An error occurred loading the walk file. Please go to the menu and click update walk list.";
				}else{
					message = "Gwall lwytho'r ffeil daith. Ewch i'r ddewislen a chliciwch rhestr daith ddiweddaraf.";
				}
				new AlertDialog.Builder(mapscreen)
						.setMessage(message)
						.setTitle("ERROR")
						.setCancelable(false)
						.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										mapscreen.finish();
									}
								}).show();
			}

		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
	 * 
	 * This method creates a thread to do the loading of the walk file and
	 * displays a loading box as the maps loads.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastWaypoint = 0;
		setContentView(R.layout.mapscreen);
		Bundle extras = getIntent().getExtras();
		walkid = null;
		if (extras != null) {
			walkid = extras.getString("walkid");
		} else {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			Boolean wantEnglish = prefs.getBoolean("wantEnglish", true);
			String message = "";
			if(wantEnglish){
				message = "An error occurred loading the walk file. Please go to the menu and click update walk list.";
			}else{
				message = "Gwall lwytho'r ffeil daith. Ewch i'r ddewislen a chliciwch rhestr daith ddiweddaraf.";
			}
			new AlertDialog.Builder(mapscreen)
			.setMessage(message)
			.setTitle("ERROR")
			.setCancelable(false)
			.setNeutralButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					}).show();
		}

		viewWalk = new Runnable() {
			public void run() {
				specialOverlay = setupSys(walkid);
				runOnUiThread(returnRes);
			}
		};
		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);
		Thread thread = new Thread(null, viewWalk, "MagentoBackground");
		thread.start();
	}

	/**
	 * This method is the setup method for the Map display. The method calls the
	 * loadXML readwalk method which returns the walk data. This walk data is
	 * then parsed and passed onto the SpecialMapOverlay Class.
	 * 
	 * @param walkid
	 *            the unique ID relating to the selected walk file.
	 * @return the SpecialMapOverlay contains an Itemized Overlay which can be
	 *         drawn on top of the map display.
	 */
	public SpecialMapOverlay setupSys(String walkid) {
		Drawable drawable = this.getResources().getDrawable(R.drawable.red_dot);
		SpecialMapOverlay specialOverlay = new SpecialMapOverlay(drawable, this);
		LoadXML lxml = new LoadXML();
		walk = lxml.readWalk(this, walkid);
		if (walk != null) {
			Double lat;
			double lng;
			GeoPoint point;
			OverlayItem overlayWaypoint;
			ArrayList<Waypoint> route = walk.getRoute();
			int index = 0;
			for (Waypoint waypoint : route) {
				lat = waypoint.getLat();
				lng = waypoint.getLng();
				point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
				overlayWaypoint = new OverlayItem(point, waypoint.getTitle(),
						waypoint.getDescription());
				specialOverlay
						.addWaypoint(overlayWaypoint, index, route.size());
				index++;
			}
			return specialOverlay;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#onPause()
	 */
	@Override
	protected void onPause() {
		if (me != null) {
			me.disableCompass();
			me.disableMyLocation();
		}
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#onResume()
	 */
	@Override
	protected void onResume() {
		if (me != null) {
			me.enableMyLocation();
			me.enableCompass();
		}
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (me != null) {
			me.disableCompass();
			me.disableMyLocation();
		}
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public int getLastWaypoint() {
		return lastWaypoint;
	}

	public void setLastWaypoint(int lastWaypoint) {
		this.lastWaypoint = lastWaypoint;
	}
}
