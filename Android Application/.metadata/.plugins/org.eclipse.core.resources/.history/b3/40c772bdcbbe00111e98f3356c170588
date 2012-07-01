package uk.ac.aber.guidedwalk.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This Class extends the Activity Class and implements the onClickListener
 * Class.<!-- --> This creates the main menu for the device. It has a set of
 * buttons laid out in the layout XML file. The listener is then attached to
 * each buttons and responds when a click is detected.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class Menu extends Activity implements OnClickListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		Button mapButton1 = (Button) findViewById(R.id.button1);
		mapButton1.setOnClickListener(this);
		Button mapButton2 = (Button) findViewById(R.id.button2);
		mapButton2.setOnClickListener(this);
		Button mapButton3 = (Button) findViewById(R.id.button3);
		mapButton3.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1: {
			startActivity(new Intent("uk.ac.aber.guidedwalk.android.WALKLIST"));
			break;
		}
		case R.id.button2: {
			startActivity(new Intent(
					"uk.ac.aber.guidedwalk.android.UPDATEWALKLIST"));
			break;
		}
		case R.id.button3: {
			startActivity(new Intent("uk.ac.aber.guidedwalk.android.ABOUT"));
			break;
		}
		}
	}

}
