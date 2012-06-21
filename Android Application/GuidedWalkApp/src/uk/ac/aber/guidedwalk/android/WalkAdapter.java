package uk.ac.aber.guidedwalk.android;

import java.util.ArrayList;
import android.content.Context;
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
			TextView list_id = (TextView) v.findViewById(R.id.list_item_id);
			TextView list_title = (TextView) v
					.findViewById(R.id.list_item_title);
			TextView list_desc = (TextView) v.findViewById(R.id.list_item_desc);
			list_id.setText(walk.getId());
			list_title.setText("Title: " + walk.getWalkTitle());
			list_desc.setText("Desc: " + walk.getWalkDesc());
		}
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		return v;
	}

}
