package uk.ac.aber.guidedwalk.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * This class extends the Activity Class.<!-- --> This Class implements the
 * R.layout.about XML layout file.
 * 
 * @author Karl Parry (kdp8)
 */
public class About extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean wantEnglish = prefs.getBoolean("wantEnglish", true);
		
		if (wantEnglish) {
			setContentView(R.layout.about);
		} else {
			setContentView(R.layout.welshabout);
		}
	}

}
