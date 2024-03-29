package uk.ac.aber.guidedwalk.android;

import java.util.ArrayList;

import uk.ac.aber.guidedwalk.model.LoadXML;
import uk.ac.aber.guidedwalk.model.Walk;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import uk.ac.aber.guidedwalk.log.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This Class presents a list of available walks to the user where the user can
 * decide which walk to load.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class WalkList extends ListActivity {

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Walk> walk_list = null;
	private WalkAdapter m_adapter;
	private Runnable viewWalks;
	private WalkList context = this;

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walklist);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Boolean wantEnglish = prefs.getBoolean("wantEnglish", true);
		TextView txt = (TextView) context.findViewById(R.id.walklist_top_text);
		if(wantEnglish){
			txt.setText(R.string.walk_list_top_text);
		}else{
			txt.setText(R.string.welsh_walk_list_top_text);
		}
		walk_list = new ArrayList<Walk>();
		this.m_adapter = new WalkAdapter(this, R.layout.walklistitem, walk_list);
		this.setListAdapter(this.m_adapter);
		viewWalks = new Runnable() {
			public void run() {
				getWalks();
			}
		};

		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);
		m_ProgressDialog.setCancelable(false);
		Thread thread = new Thread(null, viewWalks, "MagentoBackground");
		thread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Walk walk = (Walk) this.getListAdapter().getItem(position);
		String walkid = walk.getId();
		Intent i = new Intent("uk.ac.aber.guidedwalk.android.DISPLAYMAP");
		i.putExtra("walkid", walkid);
		this.startActivity(i);

	}

	/**
	 * This method gets the list of walks from the local copy of the XML Map
	 * file. Once this has been read it runs another thread to redraw the list
	 * to show the list of walks.
	 */
	private void getWalks() {
		LoadXML lxml = new LoadXML();
		walk_list = lxml.readXMLMap(this, "publish.xml");
		if (walk_list != null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Log.e("GETWALKS", e.toString());
				Log.e("GETWALKS", Log.getStackTraceString(e));
			}
			Log.i("ARRAY", "" + walk_list.size());
		} else {
			Log.e("NOMS_MISMATCH", "Noms != arraylist.size");
		}
		runOnUiThread(returnRes);
	}
	
	/**
	 * This variable contains a Runnable Thread that is run once it is called.
	 * It is called once the getWalks method completes. This method passes the
	 * walklist information onto the list adapter Class.
	 */
	private Runnable returnRes = new Runnable() {
		public void run() {
			if (walk_list != null && walk_list.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (Walk walk : walk_list) {
					m_adapter.add(walk);
				}
				m_ProgressDialog.dismiss();
				m_adapter.notifyDataSetChanged();
			} else {
				m_ProgressDialog.dismiss();
				m_adapter.notifyDataSetChanged();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				Boolean wantEnglish = prefs.getBoolean("wantEnglish", true);
				String message = "";
				if(wantEnglish){
					message = "No walks currently downloaded. Please go to the menu and click update walk list.";
				} else {
					message = "Dim llwytho i lawr cerdded ar hyn o bryd. Ewch i'r ddewislen a chliciwch rhestr daith ddiweddaraf.";
				}
				String title = "";
				if(wantEnglish){
					title = "Notification";
				} else {
					title = "Hysbysu";
				}
				new AlertDialog.Builder(context)
						.setMessage(message)
						.setTitle(title)
						.setCancelable(false)
						.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										context.finish();
									}
								}).show();
			}
		}
	};
}
