/**
 * 
 */
package com.rosberry.android.havenappandroid.activity;

import java.util.ArrayList;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.ArrayAdapterSpinnerLoc;
import com.rosberry.android.havenappandroid.custom.TopHeader;
import com.rosberry.android.havenappandroid.netcore.RequestAPIType;
import com.rosberry.android.havenappandroid.netcore.RequestToServer;
import com.rosberry.android.havenappandroid.netcore.SenderToJSONAPI;
//import com.rosberry.android.havenappandroid.netcore.SenderToJSONAPI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author abrysov
 *
 */
public class EnterALocation extends LoginForm {
	
	public static final String TAG = "_ha_EnterALocation.class";
	private FrameLayout flSubmit = null;
	private LinearLayout llHeader = null;
	
	private ArrayList<String> alAddress = null;
	private ArrayList<String> alDeel = null;
	private String sDeel = null;
	
	private EditText etComents = null;
	
	private TopHeader th = null;
	private Spinner sSelectDeel = null;
	
	private GetDataBackground gdbGetData = null;
	
	private String sAddressFromApi = null;
	private String sDeelFromAPI = null;
	private GetAddressViaApi gavaGetAdress = null;
	
	private AutoCompleteTextView acAddress = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enterlocation);
		
		if (gavaGetAdress == null){
			gavaGetAdress = new GetAddressViaApi();
			gavaGetAdress.execute(); 
//			gava = null;
		}
//		gavaGetAdress = null;
		
		acAddress = (AutoCompleteTextView)findViewById(R.id.enterlocation_actv_enteraddr);
		acAddress.setTypeface(getFont());
		
		alAddress = new ArrayList<String>();
		alDeel = new ArrayList<String>();
		
		gdbGetData = new GetDataBackground();
		gdbGetData.execute();
		
		flSubmit = (FrameLayout)findViewById(R.id.enterlocation_fl_button);
		
		etComents = (EditText)findViewById(R.id.enterlocation_et_description);
		etComents.setTypeface(getFont());
		etComents.setMaxLines(8);
		
		llHeader = (LinearLayout)findViewById(R.id.enterlocation_ll_customheader);
		
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), "Locatie (stap 2/4)", View.VISIBLE,
					getResources().getConfiguration().orientation);
			th.getTextHeader().setTypeface(getFont());
			th.getTextSubHeader().setTypeface(getFont());
			
		}else{
			th = new TopHeader(getApplicationContext(), "Locatie (stap 2/4)", View.INVISIBLE,
					getResources().getConfiguration().orientation);
			th.getTextHeader().setTypeface(getFont());
			th.getTextSubHeader().setTypeface(getFont());
			flSubmit.setBackgroundResource(R.drawable.bg_button_clickable_red);
		}
		
		llHeader.addView(th);
		
		sSelectDeel = (Spinner)findViewById(R.id.enterlocation_s_adresses);
		
		sSelectDeel.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				Log.i(TAG, "deel item clicked");
				
				for (int i = 0; i <= arg2; i++) {
					if (arg2 == i) {
						// get a part of the string in alDeel from 11 element (digits)
						sDeel = alDeel.get(i).substring(11, alDeel.get(i).length());
						LoginForm.getDataForPost().setDeelgebied(sDeel);
						
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		// autocomplete textEditText
		final AutoCompleteTextView actvAddress = (AutoCompleteTextView)findViewById(R.id.enterlocation_actv_enteraddr);
	    ArrayAdapter<String> adapterAddress = new ArrayAdapter<String>(this, 
	    										R.layout.location_auto_complete, 
	    										alAddress);
	    
	    actvAddress.setAdapter(adapterAddress);

	    flSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// write a comments
				LoginForm.getDataForPost().setOpmerkingen(etComents.getText().toString());
				
				Intent i = new Intent(getApplicationContext(), ListBestekspost.class);//ListBestekspost.class BestekspostListCustom.class
				
				i.putExtra("Deelgebied", sDeel);
//				LoginForm.getDataForPost().setDeelgebied(sDeel);
//				Log.i(TAG, "_db adding element \"deelgebied\" : " + sDeel);
				
				LoginForm.getDataForPost().setAdres(actvAddress.getText().toString());
				Log.i(TAG, "_db adding element \"adres\" : " + actvAddress.getText().toString());
				LoginForm.getDataForPost().setOpmerkingen(etComents.getText().toString());
				Log.i(TAG, "_db comment element \"adres\" : " + LoginForm.getDataForPost().getOpmerkingen());
				
				startActivityForResult(i, HavenAppAndroidActivity.FORM_LIST_BESTEKSPOST);
				
			}
		});
		
		TextView tvSubmit = (TextView)findViewById(R.id.enterlocation_tv_button);
		tvSubmit.setTypeface(getFont());
		
	}
	
	private void postLocation () throws JSONException {
		
		RequestToServer rts = new RequestToServer(LoginForm.getDataForPost().getLat(),
				LoginForm.getDataForPost().getLong());
		
		SenderToJSONAPI stj = new SenderToJSONAPI(rts, 1001);
		Log.i(TAG, stj.getJsObjResponse().toString());
		
		sAddressFromApi = stj.getJsObjResponse().get("adres").toString();
		sDeelFromAPI = stj.getJsObjResponse().get("deelgebied").toString();
		
	}
	
	/**
	 * 
	 */
	private void getsDataForForm() {
		
		RequestToServer rtsAddress = new RequestToServer(RequestAPIType.ADDRESSES);
		//1002 for Array type  in "sender" constructor
		SenderToJSONAPI staAddress = new SenderToJSONAPI(rtsAddress, 1002);
		
		RequestToServer rtsDeel = new RequestToServer(RequestAPIType.DEELGEBIED);
		SenderToJSONAPI staDeel = new SenderToJSONAPI(rtsDeel, 1002);
		
		try {
			
			for (int i = 0; i < staDeel.getJsMassResponse().length(); i++) {
				alDeel.add(staDeel.getJsMassResponse().getString(i));
			}
			
			for (int j = 0; j < staAddress.getJsMassResponse().length(); j++) {
				alAddress.add(staAddress.getJsMassResponse().getString(j));
			}
			
//			LoginForm.getDataForPost().setDeelgebied(deelgebied)
			
		} catch (Exception e) {
			alAddress.add("No json data");
			Log.e(TAG, "json error: " + e.getMessage());
		}
		
	}
	
	protected class GetDataBackground extends AsyncTask<Void, Void, Void> implements DialogInterface.OnCancelListener{
	
		private ProgressDialog dialog;
	
		protected void onPreExecute(){
			dialog = ProgressDialog.show(EnterALocation.this, "", ""); // true
		}
		
		protected Void doInBackground(Void... unused)
		{
			getsDataForForm ();
			
			return null;
		}
		
		protected void onPostExecute(Void unused)
		{
			dialog.dismiss();
	
			try {
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(EnterALocation.this,
						android.R.layout.simple_spinner_item, alDeel);
				
				adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				sSelectDeel.setAdapter(adapter);
				
				//set deel from api
				int iPositionSpinerDeel = 0;
				CharSequence csDeel = sDeelFromAPI;
				
				for (int i = 0; i < alDeel.size(); i++) {
					if (alDeel.get(i).contains(csDeel)) {
						iPositionSpinerDeel = i;
					}
				}
				
				
				sSelectDeel.setSelection(iPositionSpinerDeel);
				
			} catch (Exception e) {

				Log.e(TAG, "err in post executing : " + e.getMessage());
				e.getStackTrace();
			}


			
//			gavaGetAdress.execute();
			
		}
		
		public void onCancel(DialogInterface dialog){
			cancel(true);
			dialog.dismiss();
		}
	}
	
	
	private class GetAddressViaApi extends AsyncTask<Void, Void, Void> implements DialogInterface.OnCancelListener{

		private ProgressDialog dialog = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = ProgressDialog.show(EnterALocation.this, "", "", true);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			dialog.dismiss();
			acAddress.setText(sAddressFromApi);
			
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				postLocation();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		public void onCancel(DialogInterface dialog) {

			cancel(true);
			dialog.dismiss();
			
		}
		
	}



}
