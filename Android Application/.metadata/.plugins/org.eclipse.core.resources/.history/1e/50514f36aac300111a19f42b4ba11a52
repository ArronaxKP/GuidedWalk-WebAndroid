package uk.ac.aber.guidedwalk.android;

import java.util.ArrayList;

import uk.ac.aber.guidedwalk.model.LoadXML;
import uk.ac.aber.guidedwalk.model.Walk;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * This Class deals with the updating of available walks saved locally on the
 * device.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class UpdateWalkList extends Activity {

	private ProgressDialog m_ProgressDialog = null;
	private Runnable viewWalkList;
	private boolean download_success;
	private String errorMessage = "";
	private final UpdateWalkList context = this;

	/**
	 * This is the return thread that is run once the getWalks method finishes
	 * running.
	 */
	private Runnable returnRes = new Runnable() {
		public void run() {
			m_ProgressDialog.dismiss();
			if (download_success) {
				builder(true,"Successfully updated");
			} else {
				builder(false,"Failed to update: \n \n" + errorMessage);
			}
		}
	};
	
	
	/**
	 * The method creates a custom dismissible alert dialog using the passed message parameter as the message.
	 * 
	 * @param successOrError the value true gives the dialog the title "success" and false gives "Error".
	 * @param message the message that will appear in the dialog box.
	 * @param context link to the Activity where the dialog should appear.
	 */
	private void builder(Boolean successOrError, String message){
		AlertDialog.Builder dialog = new AlertDialog.Builder(context)
	      .setMessage(message)
	      .setCancelable(true)
	      .setNeutralButton(android.R.string.ok,
	         new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int whichButton){
		 			context.finish();
		         }
		     });
		if(successOrError){
			dialog.setTitle("Success");
		}else{
			dialog.setTitle("Error");
		}
		dialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * This method creates a new thread that goes off and download the list of
	 * walks and showing a loading window when this is occuring. This prevent
	 * the User interface locking up when downloading large files.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatewalklist);
		viewWalkList = new Runnable() {
			public void run() {
				getWalks();
			}
		};
		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);
		Thread thread = new Thread(null, viewWalkList, "MagentoBackground");
		thread.start();
	}

	/**
	 * This function calls the LoadXML Class which first gets the map.xml file
	 * from the web server, containing a reference to all the available walks.
	 * The method then iterates through each walk in the XML Map file and
	 * downloads each walk file. There is currently no checking to deal with
	 * previously downloaded files.
	 */
	private void getWalks() {
		LoadXML lxml = new LoadXML();
		if (lxml.downloadXML(this, "map.xml")) {
			download_success = true;
			ArrayList<Walk> walks = lxml.readXMLMap(this, "map.xml");
			if(walks!=null){
				for (int i = 0; i < walks.size(); i++) {
					String id = walks.get(i).getId();
					if (!lxml.downloadXML(this, id + ".xml",
							"walks/")) {
						download_success = false;
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				download_success = false;
				errorMessage = "An error occured on the client. We appologise for this."
						+ "Please try again.";
				Log.e("NOMS_MISMATCH", "Number of walks != arraylist.size");

			}
		} else {
			download_success = false;
			errorMessage = "An error occured on the server. We appologise for any"
					+ "inconviernce caused. Please try again later.";
			Log.e("NOMS_MISMATCH", "Number of walks != arraylist.size");
		}
		runOnUiThread(returnRes);
	}

}
