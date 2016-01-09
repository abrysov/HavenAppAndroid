package com.rosberry.android.havenappandroid.activity;

import com.rosberry.android.havenappandroid.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainForm extends Activity implements OnClickListener{

	private final String SUBMENU_MELDING = "Melding";
//	private final String SUBMENU_STATUS = "Status";
//	private final String SUBMENU_CONTROLE = "Controle";
//	private final String SUBMENU_INFO = "Info";
	
//	private LinearLayout llGeneral = null;
//	private LinearLayout llGeneralFromXML = null;
//	private FrameLayout flLoginButton = null;
	
	private TextView tvSubHeader = null;
	
//	private LayoutInflater li = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainform);
		// main buttons from bottom
//		determineButtonsOnForm();
		
//		li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); //was final
//		llGeneral = (LinearLayout)findViewById(R.id.mainf_ll_general);
		
		tvSubHeader = (TextView)findViewById(R.id.mainf_tv_subheader);
		
//		determineLoginFormComponents();
		

	}



	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.mainf_fl_melding_content:
			
//			llGeneral.removeAllViews();
			
//			llGeneralFromXML = (LinearLayout)li.inflate(R.layout.loginform, null);
//			llGeneral.addView(llGeneralFromXML);
			
//			determineLoginFormComponents();
			
			Toast.makeText(this, SUBMENU_MELDING, 200).show();
			tvSubHeader.setText(SUBMENU_MELDING);
			
			break;
			
//		case R.id.mainf_fl_status_content:
//			llGeneral.removeAllViews();
//			
//			Toast.makeText(this, SUBMENU_STATUS, 200).show();
//			tvSubHeader.setText(SUBMENU_STATUS);
//			break;
//			
//		case R.id.mainf_fl_controle_content:
//			llGeneral.removeAllViews();
//			
//			Toast.makeText(this, SUBMENU_CONTROLE, 200).show();
//			tvSubHeader.setText(SUBMENU_CONTROLE);
//			break;
//			
//		case R.id.mainf_fl_info_content:
//			llGeneral.removeAllViews();
//			
//			llGeneralFromXML = (LinearLayout)li.inflate(R.layout.info, null);
//			llGeneral.addView(llGeneralFromXML);
//			
//			Toast.makeText(this, SUBMENU_INFO, 200).show();
//			tvSubHeader.setText(SUBMENU_INFO);
//			break;
//		
//		case R.id.loginf_fl_loginbutton:
//			
//			Log.i("_Click", "_Clicked");
//			
//			break;

		default:
			break;
		}
		
	}
	
//	private void determineButtonsOnForm() {
//		
////		flBackTopButton = (FrameLayout)findViewById(R.id.mainf_fl_back_topnav);
////		flBackTopButton.setOnClickListener(this);
//		
//		flMeldingButton = (FrameLayout)findViewById(R.id.mainf_fl_melding_content);
//		flMeldingButton.setOnClickListener(this);
//		
//		flStatusButton = (FrameLayout)findViewById(R.id.mainf_fl_status_content);
//		flStatusButton.setOnClickListener(this);
//		
//		flControleButton = (FrameLayout)findViewById(R.id.mainf_fl_controle_content);
//		flControleButton.setOnClickListener(this);
//		
//		flInfoButton = (FrameLayout)findViewById(R.id.mainf_fl_info_content);
//		flInfoButton.setOnClickListener(this);
//		
//	}
	
//	private void determineLoginFormComponents () {
//		llGeneralFromXML = (LinearLayout)li.inflate(R.layout.loginform, null);
//		llGeneral.addView(llGeneralFromXML);
//		
//		flLoginButton = (FrameLayout)llGeneralFromXML.findViewById(R.id.loginf_fl_loginbutton);
//		flLoginButton.setOnClickListener(this);
//	}
	

//	private void inica
	
}
