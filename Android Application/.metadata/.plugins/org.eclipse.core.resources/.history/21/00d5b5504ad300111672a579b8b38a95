package uk.ac.aber.guidedwalk.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * This Class extends the Activity Class and creates the 'splash' screen when
 * the applicaiton first starts. This is done by having a thread run for a set
 * period of time and once the time lapses it launches a new Intent to start the
 * Menu Activity.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class Splash extends Activity {
	private Thread logoTimer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		logoTimer = new Thread() {
			@Override
			public void run() {
				try {
					int logoTimer = 0;
					while (logoTimer < 3000) {
						sleep(100);
						logoTimer = logoTimer + 100;
					}
					startActivity(new Intent(
							"uk.ac.aber.guidedwalk.android.PREFERENCES"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					finish();
				}
			}
		};
		logoTimer.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		return;
	}

}
