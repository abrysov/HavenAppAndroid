/** 
 * Created by abrysov at Mar 13, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.ControlElement;
import com.rosberry.android.havenappandroid.custom.TopHeader;
import com.rosberry.android.havenappandroid.netcore.RequestAPIType;
import com.rosberry.android.havenappandroid.netcore.RequestToServer;
import com.rosberry.android.havenappandroid.netcore.SenderToJSONAPI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author abrysov
 */
public class Control extends LoginForm implements OnClickListener{
	
	private static final int CHOOSE_FIGHTER = 0;
	private final String TAG = "_ha_Control.class";
	private final String POST_URL = "http://havenapp.nl/api/controlepost.php?key=8ie4lusplayoe67uties";
	
	private LinearLayout llElements = null;
	private FrameLayout flSubmitButton = null;
	private LinearLayout llHeader = null;
	
	private String sDeelFromSelection = null;
	private TopHeader th = null;
	
	private JSONArray jsaList = null;
	private int deviceTypeResolution = 0; // default (0) smartphone 800x480; (1) > 800
	
	private ArrayList<ControlElement> alElements = null;
	
//	private GetBestekList gblGetBestek = null;
//	 for SORTING IF NEED
	
	private TreeMap<String, ControlElement> tmSortedElements = null;
	
	private String returnedNameCrow = null;
//	
	private static Integer [][] rememberPossitionMatrix = null;
	
	private Control oldScreen = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// take orientation & resolution for content sizing
		if (getResources().getConfiguration().orientation == 1) { //portrait orientation
			if  (metrics.heightPixels <= 800 ){ //&& metrics.heightPixels <= 480){
				deviceTypeResolution = 0;
			}else{
				deviceTypeResolution = 1;
			}
		}else{
			if  (metrics.heightPixels <= 480 ){ //&& metrics.heightPixels <= 480){
				deviceTypeResolution = 0;
			}else{
				deviceTypeResolution = 1;
			}
		}
		
		// take deell from previous form
		sDeelFromSelection = getIntent().getExtras().getString("Deel");
		
		flSubmitButton = (FrameLayout)findViewById(R.id.control_fl_button_submit);
		llHeader = (LinearLayout)findViewById(R.id.control_ll_customheader);
		
		String sSubHeaderName = "Controle " + "deelgebied " + sDeelFromSelection;
		
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), sSubHeaderName, View.VISIBLE,
					getResources().getConfiguration().orientation);
			
		}else{
			th = new TopHeader(getApplicationContext(), sSubHeaderName, View.INVISIBLE,
					getResources().getConfiguration().orientation);
			flSubmitButton.setBackgroundResource(R.drawable.bg_button_clickable_red);
		}
		
		th.getTextHeader().setTypeface(getFont());
		th.getTextSubHeader().setTypeface(getFont());
		llHeader.addView(th);
		
		alElements = new ArrayList<ControlElement>();
//		alElementsCopy = new ArrayList<ControlElement>();
		// SORTING by NAMES if need
		tmSortedElements = new TreeMap<String, ControlElement>();		

		llElements = (LinearLayout)findViewById(R.id.control_ll_subtitles);
		
		getDataForTable();
		
		// check for rotation (get old Control.class if this happend)
		oldScreen = (Control) getLastNonConfigurationInstance();
		if (oldScreen != null) {
			drawStatusInCubs(false);
		}
		
		flSubmitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCommitDialog("Controle Verzenden?",
						"Door op OK te klikken wordt de controle definitief verzonden.");
			}
		});
		
		TextView tvAspect = (TextView)findViewById(R.id.control_tv_left_string);
		tvAspect.setTypeface(getFont());
		TextView tvKwa = (TextView)findViewById(R.id.control_tv_right_string);
		tvKwa.setTypeface(getFont());
		TextView tvSubmitBut = (TextView)findViewById(R.id.control_tv_button_submit);
		tvSubmitBut.setTypeface(getFont());
		
		rememberPossitionMatrix = new Integer [alElements.size()][5];
		
	}
	
	private void showControlDialog(String title, String message) {
	    new AlertDialog.Builder(this).setMessage(message)
	        .setTitle(title)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	            	 finish();
	             }
	        }).show();
	}
	
	private void showCommitDialog(String title, String message) {
	    new AlertDialog.Builder(this).setMessage(message)
	        .setTitle(title)
	        	        .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	            	 
	             }
	        })
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	            	 post();
	             }
	        }).show();
	}
	
	
	private void showAfterCommitDialog(String title, String message) {
	    new AlertDialog.Builder(this).setMessage(message)
	    	.setTitle(title)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	            	 finish();
	            	 startActivity(new Intent(getApplicationContext(), CameraForm.class));
	             }
	        }).show();
	}
	
	private void showAfterFalseCommitDialog(String title, String message) {
	    new AlertDialog.Builder(this).setMessage(message)
	    	.setTitle(title)
	    	.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	            	 
	             }
	        })// try to submit again
	        .setPositiveButton("Probeer opnieuw", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	            	 post();
	             }
	        }).show();
	}
	
	private void post(){
		// set params for connection...     
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false); 
		HttpConnectionParams.setConnectionTimeout(params, 20000);    
		HttpConnectionParams.setSoTimeout(params, 20000);            
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		
		HttpPost httpPost = new HttpPost(POST_URL);
		httpPost.setParams(params);
//		String pathHpoto  = LoginForm.getDataForPost().getPhotoPath();
		MultipartEntity reqEntity = new MultipartEntity();
		
		try {
			
			StringBody identifier = new StringBody(LoginForm.getDataForPost().getUserDI());
			// 
			StringBody deelgebied = new StringBody(sDeelFromSelection);
			
			//TODO add id transefering
			// set request data
			reqEntity.addPart("identifier", identifier);
			reqEntity.addPart("deelgebied", deelgebied); // string like a "121a"
			// get selected cubs for sending
			for (int i = 0; i < alElements.size(); i++) {
				for (int j = 0; j < 5; j++) {
					if (alElements.get(i).getCustomCubs().get(j).getCheckedStatus() == true){
						// add checked data from table status
						StringBody sbStutosForSend = new StringBody(alElements.get(i).getCustomCubs().get(j).getStatusStringValue());
						reqEntity.addPart( alElements.get(i).getIndexString(), sbStutosForSend); // jsaList.getString(i)
					}
				}
			}
			StringBody sbLat = new StringBody(LoginForm.getDataForPost().getLat().toString());
			reqEntity.addPart("lat", sbLat);
			StringBody sbLong = new StringBody(LoginForm.getDataForPost().getLong().toString());
			reqEntity.addPart("long", sbLong);
			
			httpPost.setEntity(reqEntity);

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			
			String sServerResponse = EntityUtils.toString(resEntity);
			Log.i(TAG, "FROM: POST:" + sServerResponse);
			
			JSONObject jsResp = new JSONObject(sServerResponse);
			
			if (jsResp.get("check").equals("OK")){
				
				showAfterCommitDialog("Controle verzonden", "De controle is succesvol verzonden.");
				
			}else{
				
				showAfterFalseCommitDialog("Controle niet verzonden", 
						"De controle kon niet worden verzonden. Probeer het later opnieuw.");
				
				Log.i(TAG, "POST FALSE");
			}
					
		} catch (IOException e) {
			Log.e(TAG, "io err : " + e.getMessage());
			
		} catch (Exception e) {
			Log.e(TAG, "err in post : " + e.getMessage());
		}		
	}//post
	
	private void getDataForTable(){
		//04-02 17:27:53.392: I/_ha_SenderToAPIClass(1546): FROM: {"check":"OK","skin":"haven","identifier":"faz4drebuv6s4gadre6rupre8a2a"}

		RequestToServer rtsControl = new RequestToServer(LoginForm.getDataForPost().getUserDI(),
				sDeelFromSelection, RequestAPIType.CONTROL);
		SenderToJSONAPI sja = new SenderToJSONAPI(rtsControl, SenderToJSONAPI.TYPE_JSON_OBJ);//1001 type JSONObject
		
		try {
			if (sja.getJsObjResponse().get("check").equals("OK")){
				
				JSONObject jsList = sja.getJsObjResponse().getJSONObject("list");
				jsaList = jsList.names();
				
				String indexBestek = null;
				
				for (int i = 0; i < jsaList.length(); i++) {
					//fill elements for control form
					indexBestek = jsaList.getString(i);
					alElements.add(new ControlElement(getApplicationContext(), 
									jsList.getString(jsaList.getString(i)),
									i, getFont(),
									deviceTypeResolution,
									this,
									indexBestek));
					alElements.get(i).setId(i);
					alElements.get(i).setOnClickListener(this);
				}
				Log.i(TAG, sja.getJsObjResponse().toString());
				
				for (int i = 0; i < alElements.size(); i++) {
					tmSortedElements.put(alElements.get(i).getAspectValue(), alElements.get(i));
				}
				// puts new sorted data
				alElements.clear();

				for ( String e : tmSortedElements.keySet()) {
					alElements.add(tmSortedElements.get(e));
					
				}
				
				for (int i = 0; i < alElements.size(); i++) {
//					alElementsCopy = (ArrayList<ControlElement>) alElements.clone();
					llElements.addView(alElements.get(i));
				}
				
			}else{
				showControlDialog("Geen toegang", 
						"U neeft geen recht om een controle uit te voeren. Vraag een beheerder.");
			}
		} catch (JSONException e) {
			
		}
		
	}//getDataForTable
	//?
	private void drawStatusInCubs (boolean _fromCrowScreen) {
		try {
			for (int i = 0; i < alElements.size(); i++) {
				for (int j = 0; j < 5; j++) {
					//when clicked "back" not clear all previous selections
					
					if ( oldScreen != null && _fromCrowScreen == false) {// check for redrawing
						if (oldScreen.rememberPossitionMatrix [i][j] == 0) {
							alElements.get(i).getCustomCubs().get(j).setCubUnselected();
						}else{
							alElements.get(i).getCustomCubs().get(j).setCubSelected();
						}
						
//						oldScreen.drawStatusInCubs();
						
					}else{
						if (!alElements.get(i).getCustomCubs().get(j).getCheckedStatus()){ // check for status
							alElements.get(i).getCustomCubs().get(j).setCubUnselected();
							// puts status in matrix
							rememberPossitionMatrix[i][j] = 0;
						}
						if (alElements.get(i).getAspectValue().equals(returnedNameCrow) 
								&& alElements.get(i).getCustomCubs().get(j).getStatusStringValue()
								.equals(LoginForm.getDataForPost().getNiveau())) {
								
							alElements.get(i).getCustomCubs().get(j).setCubSelected();
							rememberPossitionMatrix[i][j] = 1;
							
						}		
					}

				}
			}			
		} catch (Exception e) {

			Log.e(TAG, "err on drawing cub : " + e.getMessage());
		}
	}//drawStatusInCubs
	
	@Override
	protected void onResume() {
		super.onResume();
		
		drawStatusInCubs(true);
	}
	
	/* (non-Javadoc)
	 * @see com.rosberry.android.havenappandroid.activity.LoginForm#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		for (int i = 0; i < alElements.size(); i++) {
			for (int j = 0; j < 5; j++) {
				if (alElements.get(i).getCustomCubs().get(j).getCheckedStatus()) {
					rememberPossitionMatrix[i][j] = 1;
				}else{
					rememberPossitionMatrix[i][j] = 0;
				}
			}
		}
		
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		//return all data in class for new rotated screen
		return this;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		switch (resultCode) {
			
		case RESULT_OK:
			
			Log.i(TAG, "result : " + data.getAction());
			returnedNameCrow = data.getAction();
			
			break;

		default:
			break;
		}
	
	}
	
	@Override
	public void onClick(View v) {
		for (int i = 0; i < alElements.size(); i++) {
			if (v.getId() == alElements.get(i).getId()) {
				
				String sBestecIndex = alElements.get(i).getIndexString();//alIndexsBestek.get(i);
				LoginForm.getDataForPost().setBestekspost(sBestecIndex);
				
				Intent iCrowForCheck = new Intent(getApplicationContext(), Crow.class);
				iCrowForCheck.putExtra("BestekText", alElements.get(i).getAspectValue());
				iCrowForCheck.putExtra("SubHeader", th.getSubHeaderString());
				iCrowForCheck.putExtra("FromControl", true);
				startActivityForResult(iCrowForCheck, HavenAppAndroidActivity.FORM_CROW);
				
			}
		}
	}
	
}

