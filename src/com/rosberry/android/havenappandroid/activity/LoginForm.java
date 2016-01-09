/**
 * 
 */
package com.rosberry.android.havenappandroid.activity;

import org.json.JSONException;
import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.activity.Map2.GeoUpdateHandler;
import com.rosberry.android.havenappandroid.custom.TopHeader;
import com.rosberry.android.havenappandroid.db.HavenDataForPost;
import com.rosberry.android.havenappandroid.netcore.RequestToServer;
import com.rosberry.android.havenappandroid.netcore.SenderToJSONAPI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author abrysov
 */

public class LoginForm extends Activity {
	
	private final String TAG = "_ha_Login FORM";
	protected static final String LOGIN_INFO = "LOGIN_INFO";
	private static boolean isDefaultSkin = true;
	
	// Menu constants
	private final int MENU_MELDING = 101;
	private final int MENU_STATUS = 102;
	private final int MENU_CONTROLE = 103;
	private final int MENU_INFO = 104;

	protected static boolean bLoginPassValid = false;
	protected static boolean defaultInteface = true;
	
	// Interface elements
	private FrameLayout flLogin = null;
	private LinearLayout llHeader = null;
	private EditText etLogin = null;
	private EditText etPass = null;
	
	protected Double dLatitude = 0.0;
	protected Double dLongitude = 0.0;
	
	private static String sProvider = null;
	//navigation
	private static LocationListener locListener = null;
	private static LocationManager locManager = null;
	
	private static HavenDataForPost hafpData = null;
	private DoInBackground dibLogin = null;
	
	private Typeface tfFont = null;
	
	protected static SharedPreferences shSettings = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginform);
		
		tfFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTCom-MdCn.ttf");
		// var for taken info in app
		hafpData = new HavenDataForPost();
//		dibLogin = new DoInBackground();
		TextView tvSubmit = (TextView)findViewById(R.id.loginf_tv_loginbutton);
		tvSubmit.setTypeface(tfFont);
		
		Criteria criteria = new Criteria();
		startGPS();	
		
		sProvider = locManager.getBestProvider(criteria, false);
		
		llHeader = (LinearLayout)findViewById(R.id.loginf_ll_customheader);
		
		TopHeader th = new TopHeader(this, "Inloggen", View.VISIBLE,
				getResources().getConfiguration().orientation);
		th.getTextHeader().setTypeface(getFont());
		th.getTextSubHeader().setTypeface(getFont());
		llHeader.addView(th);
		
		shSettings = getSharedPreferences(LOGIN_INFO, 0);

		etLogin = (EditText)findViewById(R.id.loginf_et_login);
		etLogin.setTypeface(getFont());
		etLogin.setMaxWidth(100);
		
		etPass = (EditText)findViewById(R.id.loginf_et_password);
		
		if (shSettings.getBoolean("isLogin", false)) {
			
			etLogin.setText(shSettings.getString("login", "no login"));
			etPass.setText(shSettings.getString("pass", "no pass"));
		}
		
		flLogin = (FrameLayout)findViewById(R.id.loginf_fl_loginbutton);
		flLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (dibLogin != null){
					dibLogin.execute();
				}else{
					dibLogin = new DoInBackground();
					dibLogin.execute();
					
					SharedPreferences.Editor editor = shSettings.edit();
					editor.putString("login", etLogin.getText().toString());
					editor.putString("pass", etPass.getText().toString());
					editor.putBoolean("isLogin", true);
					editor.commit();
					
				}
				
				dibLogin = null;
				
			}
		});
		
	}
	
	public static LocationManager getLocationManagerHaven() {
		
		return locManager;
	}
	
	public Typeface getFont() {
		return tfFont;
	}
	
    /*
     * Checking for valid login-pass pair
     * */
    private boolean checkPasswordLogin (){
    	boolean checkResult = false;
    	
    	String sLogin = etLogin.getText().toString();
    	String sPassword = etPass.getText().toString();
    	
    	//TODO NEED TEST FOR ' ' charecters else the app may have been crashed
    	
    	try {
			if (login(sLogin, sPassword)){
				checkResult = true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i(TAG, "login function faild; " + e.getMessage());
			
		}
    	
    	bLoginPassValid = checkResult;
    	return checkResult;
    }
    
    public static HavenDataForPost	getDataForPost() {
    	return hafpData;
	}
    
    public static boolean getSkin(){
    	return isDefaultSkin;
    }
    
    /**
	 * 
	 */
	private boolean login(String _login, String _pass) throws JSONException {
		
		boolean isCorrect = false;
		
		RequestToServer rtsHaven = new RequestToServer(_login, _pass);
		SenderToJSONAPI staSender = new SenderToJSONAPI(rtsHaven, 1001);
		
		try {
			if (staSender.getJsObjResponse().get("check").equals("OK")){
				
				// check for skin style
				if (!staSender.getJsObjResponse().get("skin").equals("haven")) {
					isDefaultSkin = false;
				}
				// def a USER ID 
				hafpData.setUserID(staSender.getJsObjResponse().getString("identifier"));
//				
		    	Log.i(TAG, "access allowed");
		    	isCorrect = true;
		    	
		    }else{
		    	Log.i(TAG, "problem is: " + staSender.getJsObjResponse().get("reason").toString());
		    }
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage());
		}
		
		return isCorrect;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    if (getSkin()){
	    	menu.add(Menu.NONE, MENU_MELDING, Menu.NONE, "Melding").setIcon(R.drawable.ic_menu_mel_def_48);
		    menu.add(Menu.NONE, MENU_STATUS, Menu.NONE, "Status").setIcon(R.drawable.ic_menu_status_def_48);
		    menu.add(Menu.NONE, MENU_CONTROLE, Menu.NONE, "Controle").setIcon(R.drawable.ic_menu_control_def_48);
		    menu.add(Menu.NONE, MENU_INFO, Menu.NONE, "Info").setIcon(R.drawable.ic_menu_info_def_48);
		    	
	    }else{
	    	menu.add(Menu.NONE, MENU_MELDING, Menu.NONE, "Melding").setIcon(R.drawable.ic_menu_mel_red_48);
		    menu.add(Menu.NONE, MENU_STATUS, Menu.NONE, "Status").setIcon(R.drawable.ic_menu_status_red_48);
		    menu.add(Menu.NONE, MENU_CONTROLE, Menu.NONE, "Controle").setIcon(R.drawable.ic_menu_control_red_48);
		    menu.add(Menu.NONE, MENU_INFO, Menu.NONE, "Info").setIcon(R.drawable.ic_menu_info_red_48);
	    }
	    
	    return true;
	}
	
	/*
	 * Subscribe the menu Items.
	 * And actions from it.
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {

	        case MENU_MELDING:     
	        	
	        	// if logform has started it will open
	        	if(!LoginForm.bLoginPassValid){
	        		
		        	try {
		        		finish();
		        		finishingGeneralPathForms();
		        		
		        		Intent iLoginForm = new Intent (this, LoginForm.class);
		        		startActivityForResult(iLoginForm, HavenAppAndroidActivity.FORM_LOGIN);
					} catch (RuntimeException e) {
						Log.d("_menu_b_melding", "_RuntimeException: " + e.getMessage());
					}	        		
	        	}else {
	        		finish();
	        		finishingGeneralPathForms();
	        		
				    Intent iCamera = new Intent (getApplicationContext(), CameraForm.class);
				    startActivityForResult(iCamera, HavenAppAndroidActivity.FORM_CAMERA);
	        	}
	        	break;
	        	
	        case MENU_STATUS:
	        	if (LoginForm.bLoginPassValid){
	        		try {
	        			finish();
		        		Intent iStatusForm = new Intent (this, Status.class);
		        		startActivityForResult(iStatusForm, HavenAppAndroidActivity.FORM_STATUS);
		        		finishingGeneralPathForms();
					} catch (RuntimeException e) {
						Log.d(TAG, "_RuntimeException: " + e.getMessage());
					}
	        	}
	        	break;
	        
	        case MENU_CONTROLE:
	        	if (LoginForm.bLoginPassValid) {
		        	try {
		        		finish();
		        		Intent iControlForm = new Intent (this, DeelForControl.class);
		        		startActivityForResult(iControlForm, HavenAppAndroidActivity.FORM_CONTROL_DEEL);
		        		finishingGeneralPathForms();
					} catch (RuntimeException e) {
						Log.d(TAG , "_RuntimeException: " + e.getMessage());
					}
	        	}
	        	break;
	        
	        case MENU_INFO:     
		        	try {
		        		finish();
		        		Intent iInfoForm = new Intent (this, InfoForm.class);
		        		startActivityForResult(iInfoForm, HavenAppAndroidActivity.FORM_INFO);
		        		finishingGeneralPathForms();
					} catch (RuntimeException e) {
						Log.d(TAG , "_RuntimeException: " + e.getMessage());
					}
	        	break;

	    }
	    return true;
	}
	
	protected void finishingGeneralPathForms() {
		finishActivity(HavenAppAndroidActivity.FORM_CAMERA);
		finishActivity(HavenAppAndroidActivity.FORM_ENTER_LOCATION);
		finishActivity(HavenAppAndroidActivity.FORM_CROW);
	}
	
	private void startGPS () {
		
		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locListener = new HavenLocationListener();
		
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 
				0, locListener);
		
//		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//				0, new GeoUpdateHandler());
		
		boolean enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if (!enabled) {
			
			Toast.makeText( getApplicationContext(), 
					"GPS Disabled, please enable GPS on your phone", 1500 ).show();
			
			new Thread() {
				public void run() {
					
					try {
						sleep(2000);
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					} catch (Exception e) {
						Log.d(TAG, "can't open GPS settings");
					}finally{
						interrupt();
					}
				};
			}.start();
			
		}
		
//		Location coordinates = new Location(sProvider);
//		coordinates = locManager.getLastKnownLocation(sProvider);
////		locManager.getLastKnownLocation(sProvider);
//		
//		if (coordinates != null) {
//			dLatitude = coordinates.getLatitude();
//			dLongitude = coordinates.getLongitude();
//		}
//
//		
//		hafpData.setLat(dLatitude);
//		hafpData.setLong(dLongitude);
	}
	
	/*
	 * work with GPS 
	 */
	public class HavenLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location loc){
			
			if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0){

//				Log.i(TAG, "location: " + loc.getLatitude() + ", " + loc.getLongitude());
				
				dLatitude = loc.getLatitude();
				dLongitude = loc.getLongitude();
				getDataForPost().setLat(dLatitude);
				getDataForPost().setLong(dLongitude);
				
//				Log.i(TAG, "_db adding element \"lat\" : " + dLatitude.toString());
//				Log.i(TAG, "_db adding element \"long\" : " + dLongitude.toString());
				
			}else{
				Log.i(TAG, "No coordinates from GPS");
			}
		}

		@Override
		public void onProviderDisabled(String provider){
			
		}

		@Override
		public void onProviderEnabled(String provider){
//			Toast.makeText( getApplicationContext(),"Gps Enabled", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras){

		}

	}/* End of Class MyLocationListener */
	
	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
//		locManager.requestLocationUpdates(sProvider, 10000, 0, locListener);
		updateGPS();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public static void updateGPS () {
		locManager.requestLocationUpdates(sProvider, 10000, 0, locListener);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
//		locManager.removeUpdates(locListener);
	}
	
	private class DoInBackground extends AsyncTask<Void, Void, Void>
	    implements DialogInterface.OnCancelListener{
		
		private ProgressDialog dialog;
	
		protected void onPreExecute(){
			dialog = ProgressDialog.show(LoginForm.this, "", "", true);
		}
		
		protected Void doInBackground(Void... unused)
		{
			
			checkPasswordLogin();
			
			return null;
		}
		
		protected void onPostExecute(Void unused)
		{
			dialog.dismiss();
			
				if (bLoginPassValid == true){
					try {
					    Intent iCamera = new Intent (getApplicationContext(), CameraForm.class);
					    startActivityForResult(iCamera, HavenAppAndroidActivity.FORM_CAMERA);						
					} catch (Exception e) {
						Log.d(TAG, "can't open camera form; reson: " + e.getMessage());
					}
				}else{
//					Toast.makeText(getApplicationContext(), "Login or pass not correct.", 1500).show();
				}
				
		}
		
		public void onCancel(DialogInterface dialog)
		{
			cancel(true);
			dialog.dismiss();
		}
	}
	
}
