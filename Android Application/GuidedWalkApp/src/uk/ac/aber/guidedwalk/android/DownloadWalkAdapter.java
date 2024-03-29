package uk.ac.aber.guidedwalk.android;

import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
public class DownloadWalkAdapter extends ArrayAdapter<Walk> {

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
	public DownloadWalkAdapter(Context context, int textViewResourceId,
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
			v = vi.inflate(R.layout.downloadwalkitem, null);
		}

		Walk walk = items.get(position);
		if (walk != null) {
			TextView list_id = (TextView) v
					.findViewById(R.id.download_walk_item_id);
			TextView list_title = (TextView) v
					.findViewById(R.id.download_walk_item_title);
			TextView list_desc = (TextView) v
					.findViewById(R.id.download_walk_item_desc);
			TextView list_difficulty = (TextView) v
					.findViewById(R.id.download_walk_item_difficulty);

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(v.getContext());
			Boolean wantEnglish = prefs.getBoolean("wantEnglish", false);

			list_id.setText(walk.getId());
			if (wantEnglish) {
				list_title.setText("Title: " + walk.getWalkTitle());

				if (walk.getWalkDesc().length() >= 30) {
					list_desc.setText("Desc: "
							+ walk.getWalkDesc().substring(0, 29));
				} else {
					list_desc.setText("Desc: " + walk.getWalkDesc());
				}
			} else {
				list_title.setText("Teitl: " + walk.getWelshWalkTitle());

				if (walk.getWelshWalkDesc().length() >= 30) {
					list_desc.setText("Disgrifiad: "
							+ walk.getWelshWalkDesc().substring(0, 29));
				} else {
					list_desc.setText("Disgrifiad: " + walk.getWelshWalkDesc());
				}
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
			if (walk.isUpdateAvailable()) {
				TextView list_text = (TextView) v
						.findViewById(R.id.download_walk_item_text);
				if (wantEnglish) {
					list_text.setText("UPDATE Available");
				} else {
					list_text.setText("Y NEWYDDION DIWEDDARAF Ar gael");
				}
				list_text
						.setTextColor(v.getResources().getColor(R.color.green));
				list_text.setVisibility(View.VISIBLE);
			} else if (walk.toBeDeleted()) {
				TextView list_text = (TextView) v
						.findViewById(R.id.download_walk_item_text);

				if (wantEnglish) {
					list_text.setText("DELETE: No longer Supported");
				} else {
					list_text.setText("DILEU: Dim hwy � Chymorth");
				}
				list_text.setTextColor(v.getResources().getColor(R.color.red));
				list_text.setVisibility(View.VISIBLE);
			} else {
				TextView list_text = (TextView) v
						.findViewById(R.id.download_walk_item_text);
				list_text.setText("");
				list_text.setVisibility(View.GONE);
			}

			final int pos = position;
			CheckBox chkbox = (CheckBox) v.findViewById(R.id.checkbox);
			chkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					items.get(pos).setSelected(isChecked);
				}
			});

		}
		return v;
	}

}
