package uk.ac.aber.guidedwalk.android;

import java.util.ArrayList;

import uk.ac.aber.guidedwalk.model.LoadXML;
import uk.ac.aber.guidedwalk.model.Walk;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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

	/**
	 * This variable contains a Runnable Thread that is run once it is called.
	 * It is called once the getWalks method completes. This method passes the
	 * walklist information onto the list adapter Class.
	 */
	private Runnable returnRes = new Runnable() {
		public void run() {
			if (walk_list != null && walk_list.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < walk_list.size(); i++){
					m_adapter.add(walk_list.get(i));
				}
				m_ProgressDialog.dismiss();
				m_adapter.notifyDataSetChanged();
			}else{
				m_ProgressDialog.dismiss();
				m_adapter.notifyDataSetChanged();
				new AlertDialog.Builder(context)
			      .setMessage("An error occured loading the walk list. Please go to the menu and click update walk list.")
			      .setTitle("Error")
			      .setCancelable(true)
			      .setNeutralButton(android.R.string.ok,
			         new DialogInterface.OnClickListener() {
				         public void onClick(DialogInterface dialog, int whichButton){
				        	 context.finish();
				         }
				     })
			      .show();
			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walklist);
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
		walk_list = lxml.readXMLMap(this);
		if (walk_list != null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Log.i("ARRAY", "" + walk_list.size());
		} else {
			Log.e("NOMS_MISMATCH", "Noms != arraylist.size");
		}
		runOnUiThread(returnRes);
	}

}
