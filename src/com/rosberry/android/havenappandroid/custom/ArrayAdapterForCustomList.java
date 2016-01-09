/** 
 * Created by abrysov at Apr 5, 2012
 */
package com.rosberry.android.havenappandroid.custom;

import java.util.ArrayList;

import com.rosberry.android.havenappandroid.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author abrysov
 *
 */
public class ArrayAdapterForCustomList extends ArrayAdapter {

	
	private final Context context;
	private final ArrayList<String> alNames;
	private Typeface tfText = null;
	
	/**
	 * @param context
	 * @param textViewResourceId
	 */
	public ArrayAdapterForCustomList(Context context, ArrayList<String> _alNames, Typeface _tf) {
		super(context, R.layout.rowlayout, _alNames);
		
		this.context = context;
		this.alNames = _alNames;
		this.tfText = _tf;
	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
//		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		textView.setText(alNames.get(position));
		textView.setTypeface(tfText);
		return rowView;
	}

//	/* (non-Javadoc)
//	 * @see com.rosberry.android.havenappandroid.FontForHaven#getTypeFont()
//	 */
//	@Override
//	public Typeface getTypeFont() {
//		return null;
//	}

}
