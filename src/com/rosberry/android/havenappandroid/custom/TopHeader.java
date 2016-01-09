/** 
 * Created by abrysov at Mar 9, 2012
 */
package com.rosberry.android.havenappandroid.custom;

import java.io.File;

import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.activity.LoginForm;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * @author abrysov
 *
 */
public class TopHeader extends LinearLayout {

	/**
	 * @param context
	 */
	
	private static ImageView ivHeaderPicture = null;
	private TextView tvTextForSubHeader = null;
	private FrameLayout flSubHeader = null;
	private FrameLayout flDefaultHeader = null;
	private TextView tvDefaultHeader = null;
	
//	private final Typeface tfFont = Typeface.createFromAsset(getAssets(), "/font/HelveticaNeueLTCom-MdCn.ttf");
			
	public TopHeader(Context context, String sSubHeaderText, int visibleDefaultHeader, int _landscapeMode) {
		super(context);
		setOrientation(VERTICAL);	
		
//		Typeface tfFont = Typeface.createFromFile(fFont);
		
		
//		DisplayMetrics metrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		//
////		Log.i(TAG, "metrics.density : " + metrics.densityDpi + 
////					"\nmetrics.widthPixels : " + metrics.widthPixels +
////					"\nmetrics.heightPixels : " + metrics.heightPixels);
////		if (metrics.densityDpi <= 160){
//		
//		// take orientation & resolution for content sizing
//		if (getResources().getConfiguration().orientation == 1) {
//			if  (metrics.heightPixels <= 800 ){ //&& metrics.heightPixels <= 480){
//				deviceTypeResolution = 0;
//			}else{
//				deviceTypeResolution = 1;
//			}
//		}else{
//			if  (metrics.heightPixels <= 480 ){ //&& metrics.heightPixels <= 480){
//				deviceTypeResolution = 0;
//			}else{
//				deviceTypeResolution = 1;
//			}
//		}
		
		
		ivHeaderPicture = new ImageView(context);
		if(_landscapeMode == 1){
			ivHeaderPicture.setBackgroundResource(R.drawable.header_red_for_1280_portret);//header_p
		}else{
			ivHeaderPicture.setBackgroundResource(R.drawable.header_red_for_1280_landscape);//header_p
		}
		

		addView(ivHeaderPicture);
		
		flDefaultHeader = new FrameLayout(context);
		flDefaultHeader.setPadding(0, 10, 0, 10);
		flDefaultHeader.setBackgroundResource(R.color.black);
		
		
		tvDefaultHeader = new TextView(context);
//		tvDefaultHeader.setTypeface(tfFont);
		tvDefaultHeader.setText("Haven Amsterdam");
		tvDefaultHeader.setTextColor(Color.WHITE);
		
//		if (ViewConfiguration.get(context).getS) {
//			
//		}
		tvDefaultHeader.setTextSize(26);

		tvDefaultHeader.setGravity(Gravity.CENTER);
		flDefaultHeader.addView(tvDefaultHeader);
		
		flSubHeader = new FrameLayout(context);
		
		
		
		tvTextForSubHeader = new TextView(context);
		tvTextForSubHeader.setText(sSubHeaderText);
		tvTextForSubHeader.setGravity(Gravity.CENTER);
		tvTextForSubHeader.setTextColor(Color.BLACK);
		tvTextForSubHeader.setTextSize(22);
		tvTextForSubHeader.setPadding(0, 1, 0, 1);		
		
		
		flSubHeader.addView(tvTextForSubHeader);
		
		if (visibleDefaultHeader == View.VISIBLE) {
			ivHeaderPicture.setVisibility(View.GONE);
			flSubHeader.setBackgroundResource(R.color.orange_for_topheader);
			addView(flDefaultHeader);
		}else{
			ivHeaderPicture.setVisibility(View.VISIBLE);
			flSubHeader.setBackgroundResource(R.color.gray_for_topheader);
		}
		addView(flSubHeader);
		
	}
	
	public void setSubHeaderText (String _sSubHeader){
		
		tvTextForSubHeader.setText(_sSubHeader);
		
	}
	
	public String getSubHeaderString () {
		return tvTextForSubHeader.getText().toString();
	}
	
	public TextView getTextHeader () {
		return tvDefaultHeader;
	}
	
	public TextView getTextSubHeader () {
		return tvTextForSubHeader;
	}
	
	
	

}
