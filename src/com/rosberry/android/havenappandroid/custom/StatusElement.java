/** 
 * Created by abrysov at Mar 14, 2012
 */
package com.rosberry.android.havenappandroid.custom;

import com.rosberry.android.havenappandroid.R;

import android.R.color;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @author abrysov
 *
 */
public class StatusElement extends FrameLayout{
	
	private TextView tvAddress = null;
	private TextView tvTime = null;
	private TextView tvStatus = null;
	
	private FrameLayout flArrow = null;
	private View vSeperator = null;
//	private ImageView ivArrow = null;
	private int cColorStatusInElement = -1;
	private FrameLayout flForStatus = null;
	
	
	/**
	 * 
	 */
	public StatusElement(final Context context, String sAddress, String sTime, String sStatus,
							int cStatusColor, Typeface _tf, int _textSize) {
		super(context);
		
		cColorStatusInElement = cStatusColor;
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 80);
		params.setMargins(2, 2, 2, 2);
		super.setPadding(2, 5, 2, 1);
		super.setLayoutParams(params);
		super.setBackgroundResource(R.drawable.bg_status_element);
	
		
//		MarginLayoutParams mparam = new MarginLayoutParams(10, 10);
//		mparam.setMargins(5, 5, 5, 5);
		
		tvAddress = new TextView(context);
		tvAddress.setPadding(5, 7, 0, 0);
		tvAddress.setText(sAddress);
		tvAddress.setTypeface(_tf);
		tvAddress.setGravity(Gravity.TOP);
		tvAddress.setTextColor(Color.BLACK);
		tvAddress.setTextSize(_textSize);
		super.addView(tvAddress);
		
		tvTime = new TextView(context);
		tvTime.setPadding(5, 0, 0, 4);
		tvTime.setText(sTime);
		tvTime.setTypeface(_tf);
		tvTime.setTextSize(_textSize - 1);
		tvTime.setTextColor(Color.BLACK);
		tvTime.setGravity(Gravity.BOTTOM);
		super.addView(tvTime);
		
		flForStatus = new FrameLayout(context);
		LayoutParams lpForStatus = new LayoutParams(170,
				LayoutParams.FILL_PARENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		lpForStatus.setMargins(10, 10, 20, 10);
		flForStatus.setLayoutParams(lpForStatus);
		
		
		tvStatus = new TextView(context);
		tvStatus.setTypeface(_tf);
		
		if (cColorStatusInElement == 1){ // set red color
			tvStatus.setTextColor(Color.RED);
			flForStatus.setBackgroundResource(R.drawable.bg_border);
			
		}else if(cColorStatusInElement == 0){ // set orange color
			tvStatus.setTextColor(Color.rgb(255, 106, 8));
		}else if(cColorStatusInElement == 2){ // set green color
			tvStatus.setTextColor(Color.rgb(134, 219, 8));
		}else{
			tvStatus.setTextColor(Color.BLACK);
		}
		
		tvStatus.setTextSize(17);
		tvStatus.setText(sStatus);
		tvStatus.setGravity(Gravity.CENTER);//(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

		flForStatus.addView(tvStatus);
		
		super.addView(flForStatus);
		
		flArrow = new FrameLayout(context);
		flArrow.setBackgroundResource(R.drawable.arrow);
		LayoutParams lpForArrow = new LayoutParams(LayoutParams.WRAP_CONTENT, 
													LayoutParams.WRAP_CONTENT,
													Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		flArrow.setLayoutParams(lpForArrow);
		super.addView(flArrow);
		
		vSeperator = new View(context);
		LayoutParams lpForSeparator = new LayoutParams(LayoutParams.FILL_PARENT, 1);
		vSeperator.setLayoutParams(lpForSeparator);
		vSeperator.setBackgroundColor(R.color.gray_for_separator);
		super.addView(vSeperator);
	
	}
	
	public void setVisibleSeparator (int _visibility){
		vSeperator.setVisibility(_visibility);
	}
	
	public int getColorIndex(){
		return cColorStatusInElement;
	}
	
	public FrameLayout getButtonFromElement() {
		return flForStatus;
	}
	
	public TextView getNameStatus() {
		return tvStatus;
	}


}
