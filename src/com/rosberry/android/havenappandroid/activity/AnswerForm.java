/** 
 * Created by abrysov at Mar 22, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.TopHeader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author abrysov
 */

public class AnswerForm extends QuestionsList {
	
	private LinearLayout llHeader = null;
	private TopHeader th = null;
	
	private FrameLayout flColorBG = null;
	
	/* (non-Javadoc)
	 * @see com.rosberry.android.havenappandroid.activity.QuestionsList#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.answerf);
		
		llHeader = (LinearLayout)findViewById(R.id.answerf_ll_header);
		flColorBG = (FrameLayout)findViewById(R.id.answerf_fl_color_frame);
		
		String sSubHeaderText = "Info";
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), sSubHeaderText, View.VISIBLE,
					getResources().getConfiguration().orientation);
		}else{
			th = new TopHeader(getApplicationContext(), sSubHeaderText, View.INVISIBLE,
					getResources().getConfiguration().orientation);
			flColorBG.setBackgroundResource(R.color.red);
		}
		th.getTextHeader().setTypeface(getFont());
		th.getTextSubHeader().setTypeface(getFont());
		llHeader.addView(th);
		
		TextView tv = (TextView)findViewById(R.id.answerf_tv_text);
		try {
			tv.setText(getIntent().getExtras().getString("Answer"));
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Can't find answer", 1000).show();
			Log.e(TAG, "Can't find answer");
		}
		
		TextView tvGenText = (TextView)findViewById(R.id.answerf_tv_text);
		tvGenText.setTypeface(getFont());
		
	}

}
