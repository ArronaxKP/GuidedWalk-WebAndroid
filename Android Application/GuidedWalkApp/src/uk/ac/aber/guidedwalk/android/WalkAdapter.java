package uk.ac.aber.guidedwalk.android;

import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import uk.ac.aber.guidedwalk.model.Walk;

/**
 * This Class extends the ArrayAdapter Class.<!-- --> This is done to create a
 * custom adapter Class that will take an ArrayList of Walks and create a custom
 * list showing the walks title and description.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class WalkAdapter extends ArrayAdapter<Walk> {

	private ArrayList<Walk> items;

	/**
	 * The constructor creates a reference to the Activity Class instantiating
	 * the adapter. This is done as the context is required in order to create
	 * the list view.
	 * 
	 * @param context
	 *            the context of the Activity Class instantiating the adapter
	 * @param textViewResourceId
	 *            the resource ID for a single TextView
	 * @param items
	 *            the ArrayList of Walks that you want displayed in the custom
	 *            list.
	 */
	public WalkAdapter(Context context, int textViewResourceId,
			ArrayList<Walk> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) super.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.walklistitem, null);
		}
		Walk walk = items.get(position);
		if (walk != null) {

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(v.getContext());
			Boolean wantEnglish = prefs.getBoolean("wantEnglish", false);

			TextView list_id = (TextView) v.findViewById(R.id.list_item_id);
			TextView list_title = (TextView) v
					.findViewById(R.id.list_item_title);
			TextView list_desc = (TextView) v.findViewById(R.id.list_item_desc);
			TextView list_difficulty = (TextView) v
					.findViewById(R.id.list_item_difficulty);
			list_id.setText(walk.getId());
			if (wantEnglish) {
				list_title.setText("Title: " + walk.getWalkTitle());
				list_desc.setText("Desc: " + walk.getWalkDesc());
			} else {
				list_title.setText("Title: " + walk.getWelshWalkTitle());
				list_desc.setText("Desc: " + walk.getWelshWalkDesc());
			}
			if (wantEnglish) {
				switch (walk.getWalkDifficulty()) {
				case 0:
					list_difficulty.setText("Easy");
					break;
				case 1:
					list_difficulty.setText("Normal");
					break;
				case 2:
					list_difficulty.setText("Hard");
					break;
				}
			} else {
				switch (walk.getWalkDifficulty()) {
				case 0:
					list_difficulty.setText("Hawdd");
					break;
				case 1:
					list_difficulty.setText("Areferol");
					break;
				case 2:
					list_difficulty.setText("Caled");
					break;
				}
			}

		}
		return v;
	}

}
