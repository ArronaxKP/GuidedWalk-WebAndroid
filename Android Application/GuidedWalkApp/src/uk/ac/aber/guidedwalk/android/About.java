package uk.ac.aber.guidedwalk.android;

import android.app.Activity;
import android.os.Bundle;

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
		setContentView(R.layout.about);
	}

}