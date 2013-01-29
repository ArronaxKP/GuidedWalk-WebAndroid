package uk.ac.aber.guidedwalk.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Preferences extends Activity implements OnClickListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		Button english = (Button) findViewById(R.id.buttonEnglish);
		Button welsh = (Button) findViewById(R.id.buttonWelsh);

		english.setOnClickListener(this);
		welsh.setOnClickListener(this);
	}

	public void onClick(View v) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		switch (v.getId()) {
		case R.id.buttonEnglish:
			editor.putBoolean("wantEnglish", true);
			editor.commit();
			startActivity(new Intent("uk.ac.aber.guidedwalk.android.MENU"));
			break;
		case R.id.buttonWelsh:
			editor.putBoolean("wantEnglish", false);
			editor.commit();
			startActivity(new Intent("uk.ac.aber.guidedwalk.android.MENU"));
			break;
		}
		
				
	}
}
