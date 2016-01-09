/** 
 * Created by abrysov at Mar 15, 2012
 */
package com.rosberry.android.havenappandroid.custom;

import java.util.ArrayList;

import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.activity.Crow;
import com.rosberry.android.havenappandroid.activity.LoginForm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * @author abrysov
 *
 */
public class ControlElement extends LinearLayout implements OnClickListener{

	/**
	 * @param context
	 */
	private final String TAG = "_ha_ControlElement";
	private final static int CUBID = 100;
	
	private FrameLayout flAspect = null;
	private TextView tvAspect = null;
	private LinearLayout llStatusCubes = null;
	
	private ArrayList<FrameLayout> alCubeElements = null;
	private int iPosElement = 0;
	
	private ArrayList<TheCube> alCubs = null;
	
	private Typeface tf = null;
	
	private Context context = null;
	
	private String sIndex = null;
	
	public ControlElement(Context context, String aspect, int positionBacklight, Typeface _tf , int _typeDivice,
							OnClickListener _listener, String _index) {
		super(context);

		this.context = context;
//		LayoutParams lpForControlElement = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//		lpForControlElement.setMargins(left, top, right, bottom)
		this.sIndex = _index;
		
		tf = _tf;
		
		iPosElement = positionBacklight;
		
//		setBackgroundResource(R.drawable.bg_for_control_element);
		
		tvAspect = new TextView(context);
		tvAspect.setTypeface(_tf);
		tvAspect.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		tvAspect.setPadding(3, 3, 3, 3);
		tvAspect.setText(aspect);
		tvAspect.setTextColor(Color.BLACK);
		
		
		if (_typeDivice == 1) {
			tvAspect.setTextSize(24);
			tvAspect.setPadding(10, 5, 10, 10);
			
		}else{
			tvAspect.setTextSize(16);
			tvAspect.setPadding(4, 2, 4, 4);
		}
		
		
		LayoutParams lpForStringAspects = new LayoutParams(LayoutParams.FILL_PARENT,
											LayoutParams.WRAP_CONTENT);
		
		LayoutParams lpForStringSimbols = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);

		lpForStringAspects.setMargins(0, 1, 0, 1);
		lpForStringAspects.weight = 10;
		
		
		lpForStringSimbols.setMargins(0, 1, 0, 1);
//		lpForStringSimbols.weight = 20;//8
		
		flAspect = new FrameLayout(context);
		flAspect.setLayoutParams(lpForStringAspects);
		flAspect.setBackgroundResource(R.drawable.bg_for_control_element);
//		flAspect.setOnClickListener(this);
		
		flAspect.addView(tvAspect);
		
		super.addView(flAspect);
		
		llStatusCubes = new LinearLayout(context);
		llStatusCubes.setLayoutParams(lpForStringSimbols);
		llStatusCubes.setGravity(Gravity.CENTER_HORIZONTAL);
		
		LayoutParams lpForCubs = new LayoutParams(40, LayoutParams.FILL_PARENT);
		lpForCubs.setMargins(1, 0, 1, 0);
		
		ArrayList<String> alTextForStatus = new ArrayList<String>();
		alCubs = new ArrayList<ControlElement.TheCube>();
		
		for (int i = 0; i < 5; i++) {
		
			if (i == 0) {
				alTextForStatus.add("A+");
			}else if(i == 1){
				alTextForStatus.add("A");
			}else if(i == 2){
				alTextForStatus.add("B");
			}else if(i == 3){
				alTextForStatus.add("C");
			}else if(i == 4){
				alTextForStatus.add("D");
			}
			
			alCubs.add(new TheCube(context, alTextForStatus.get(i), i, _typeDivice));
			alCubs.get(i).setLayoutParams(lpForCubs);
			alCubs.get(i).setBackgroundResource(R.color.white);
			alCubs.get(i).setId(i + ((positionBacklight+1)*CUBID));
			llStatusCubes.addView(alCubs.get(i));
			
		}
		
		super.addView(llStatusCubes);
		
	}
	
	public String getIndexString () {
		
		return sIndex;
	}
	
	public String getAspectValue () {
		
		return tvAspect.getText().toString();
	}
	
	public ArrayList<TheCube> getCustomCubs() {
		return alCubs;
	}
	
	public int getLinePos () {
		return iPosElement;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
//		String sBestecIndex = tvAspect.getText().toString();
//		LoginForm.getDataForPost().setBestekspost(sBestecIndex);
//	//	Toast.makeText(getApplicationContext(), "Clicked " + i, 1000).show();
//		
//		Intent iCrowForCheck = new Intent(context, Crow.class);
//	////	LoginForm.getDataForPost().setBestekspost(alIndexsBestek.get(i));
//	////	gblGetBestek.execute();
//		iCrowForCheck.putExtra("BestekText", sBestecIndex);
//		iCrowForCheck.putExtra("FromControl", true);
//		startActivity(iCrowForCheck, HavenAppAndroidActivity.FORM_CROW);	
	}
	
//	public static void setColorOfCubByID (int _iID, int _pos){
//		
//	}
	
	public class TheCube extends FrameLayout implements OnClickListener{

		private TextView tvTextInCube = null;
		private int iNumElement = 0;
		
		private boolean isChecked = false;
		
		/**
		 * @param context
		 */
		public TheCube(Context context, String _textInCub, int _numElement, int _typeDevice) {
			super(context);

			tvTextInCube = new TextView(context);
			tvTextInCube.setTypeface(tf);
			tvTextInCube.setText(_textInCub);
			tvTextInCube.setGravity(Gravity.CENTER);
			tvTextInCube.setTextColor(Color.BLACK);
			
			if (_typeDevice == 1) {
				tvTextInCube.setTextSize(24);
				tvTextInCube.setPadding(1, 5, 1, 10);
			}else{
				tvTextInCube.setTextSize(16);
				tvTextInCube.setPadding(4, 2, 4, 4);
			}
			
			iNumElement = _numElement;
			
			addView(tvTextInCube);
			this.setOnClickListener(this);
			
		}
		
		public int getPosCub() {
			return iNumElement;
		}
		
		public String getStatusStringValue (){
			return tvTextInCube.getText().toString();
		}
		
		public boolean getCheckedStatus () {
			return isChecked;
		}
		
		public void setToUnchecket (){
			isChecked = false;
		}
		
		public void setCololorToDefault(){
			setBackgroundResource(R.color.white);
		}
		
		public void setCubSelected(){
			setBackgroundResource(R.color.red);
			isChecked = true;
		}
		
		public void setCubUnselected(){
			setBackgroundResource(R.color.white);
			isChecked = false;
		}
		
		@Override
		public void onClick(View v) {
			
			for (int i = 0; i < 5; i++) {
				alCubs.get(i).setCubUnselected();
				if (v.getId() == alCubs.get(i).getId()){
					if (isChecked){
						alCubs.get(i).setBackgroundResource(R.color.white);
						isChecked = false;
					}else{
						alCubs.get(i).setBackgroundResource(R.color.red);
						isChecked = true;
					}
				}
			}
		}
	}



}
