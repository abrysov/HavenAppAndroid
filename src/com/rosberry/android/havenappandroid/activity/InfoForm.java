/**
 * 
 */
package com.rosberry.android.havenappandroid.activity;

import org.w3c.dom.Text;

import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.TopHeader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author abrysov
 *
 */
public class InfoForm extends LoginForm {
	
	private FrameLayout flHelp = null;
	private FrameLayout flUnlogin = null;
	private LinearLayout llHeader = null;
	
	private TextView tvHelpText = null;
	
	private TopHeader th = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		
		llHeader = (LinearLayout)findViewById(R.id.info_ll_customheader);
		tvHelpText = (TextView)findViewById(R.id.info_tv_the_text);
		tvHelpText.setTextSize(20);
		String sText;
		if (LoginForm.getSkin()){
			sText = "Loren ipsum dolor sit er elite lamet, consectetaur cillium adipisicing pecu, " +
					"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
					"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut " +
					"aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
					"valuptate velit esse cillum dolore eu fugiat nulla pariatur. " +
					"Exceptteur sint occaecat culpa qui officia deserunt mollit anim id est laborum. " +
					"Nam liber te conscient to factor tum poen legum odioque  civiuda.";
		}else{
			sText = "Hier kompt infomatie over deze appt ID.";
		}
		
		
		tvHelpText.setText(sText);
		
//		TopHeader tp = new TopHeader(getApplicationContext(), "Info", View.VISIBLE);

		
		flHelp = (FrameLayout)findViewById(R.id.info_fl_help);
		flHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(getApplicationContext(), QuestionsList.class));
				
			}
		});
		
		flUnlogin = (FrameLayout)findViewById(R.id.info_fl_uitloggen);
		flUnlogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//TODO destroy all cash 
				
//				SharedPreferences spSettings = getSharedPreferences("LOGIN", 0);
				SharedPreferences.Editor editor = LoginForm.shSettings.edit();
				editor.putBoolean(LOGIN_INFO, false);
				editor.commit();
				
				finish();
				
				Intent iLogin = new Intent(getApplicationContext(), LoginForm.class);
				startActivityForResult(iLogin, HavenAppAndroidActivity.FORM_CAMERA);
				
			}
		});
		
		
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), "Info", View.VISIBLE,
					getResources().getConfiguration().orientation);
			
		}else{
			th = new TopHeader(getApplicationContext(), "Info", View.INVISIBLE,
					getResources().getConfiguration().orientation);
			flHelp.setBackgroundResource(R.drawable.bg_button_clickable_red);
			flUnlogin.setBackgroundResource(R.drawable.bg_button_clickable_red);
		}
		th.getTextHeader().setTypeface(getFont());
		th.getTextSubHeader().setTypeface(getFont());
		llHeader.addView(th);
		
		TextView tvGeneralText = (TextView)findViewById(R.id.info_tv_the_text);
		tvGeneralText.setTypeface(getFont());
		TextView tvHelpText = (TextView)findViewById(R.id.info_tv_help);
		tvHelpText.setTypeface(getFont());
		TextView tvUnloginText = (TextView)findViewById(R.id.info_tv_unlogin);
		tvUnloginText.setTypeface(getFont());
		
	};
	
	@Override
	public void onPause(){
		super.onPause();
//		finish();
	}

}
