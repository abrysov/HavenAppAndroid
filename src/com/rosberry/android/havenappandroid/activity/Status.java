/** 
 * Created by abrysov at Mar 13, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import java.io.IOException;
import java.util.*; 

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
import com.rosberry.android.havenappandroid.custom.StatusElement;
import com.rosberry.android.havenappandroid.custom.TopHeader;
import com.rosberry.android.havenappandroid.netcore.RequestAPIType;
import com.rosberry.android.havenappandroid.netcore.RequestToServer;
import com.rosberry.android.havenappandroid.netcore.SenderToJSONAPI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author abrysov
 *
 */
public class Status extends LoginForm implements OnClickListener{
	
	private static final int ID_ELEMENT = 1000;
	private static final int ID_BUTTON_IN_ELEMENT = 10000;
	private final String URL_FOR_CHANGE_STATUS = "http://havenapp.nl/api/statchange.php?key=8ie4lusplayoe67uties";
	private final String TAG = "_ha_Status.class";
	/* (non-Javadoc)
	 * @see com.rosberry.android.havenappandroid.activity.LoginForm#onCreate(android.os.Bundle)
	 */
	
	private LinearLayout llHeader = null;
	private LinearLayout llStrings = null;
//	private ListView lvElementsList = null;
	
	private ArrayList<StatusElement> alElelments = null;
	private ArrayList<JSONObject> alJSONElements = null;
	
	private static boolean isChangeble = false;
	
	private JSONObject jsList = null;
	private JSONArray jsaList = null;
	
	private static int cStatusColor = -1;
	private int iNumToChangeButt = 0;
	
	private TopHeader th = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
//		set
		
		alElelments = new ArrayList<StatusElement>();
		alJSONElements = new ArrayList<JSONObject>();
		
		llHeader = (LinearLayout)findViewById(R.id.status_ll_customheader);
//		TopHeader th = new TopHeader(getApplicationContext(), "Status van meldingen", View.VISIBLE);
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), "Openstaande meldingen", View.VISIBLE,
					getResources().getConfiguration().orientation);
		}else{
			th = new TopHeader(getApplicationContext(), "Openstaande meldingen", View.INVISIBLE,
					getResources().getConfiguration().orientation);
		}
		th.getTextHeader().setTypeface(getFont());
		th.getTextSubHeader().setTypeface(getFont());
		llHeader.addView(th);
		
		
		try {
			takeStatusList();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		llStrings = (LinearLayout)findViewById(R.id.status_ll_elements);
		llStrings.setBackgroundResource(R.color.gray_for_separator);
		
		for (StatusElement e : alElelments) {
			llStrings.addView(e);
		}
		
		// set invisible separator in #1 element
		alElelments.get(0).setVisibleSeparator(View.INVISIBLE);
	
	}
	
	public static int getStatusColorType () {
		
		return cStatusColor;
	}
	
	
	private void post(String _meldidForGange){
		// set params for connection...     
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false); 
		HttpConnectionParams.setConnectionTimeout(params, 20000);    
		HttpConnectionParams.setSoTimeout(params, 20000);            
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		
		HttpPost httpPost = new HttpPost(URL_FOR_CHANGE_STATUS);
		httpPost.setParams(params);
//		String pathHpoto  = LoginForm.getDataForPost().getPhotoPath();
		MultipartEntity reqEntity = new MultipartEntity();
		
		try {
			
			StringBody identifier = new StringBody(LoginForm.getDataForPost().getUserDI());
			reqEntity.addPart("identifier", identifier);
			
			StringBody meldid = new StringBody(_meldidForGange);
			reqEntity.addPart("meldid", meldid); 
//			
			httpPost.setEntity(reqEntity);

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			
			String sServerResponse = EntityUtils.toString(resEntity);
			Log.i(TAG, "FROM: POST:" + sServerResponse);
			
			JSONObject jsResp = new JSONObject(sServerResponse);
			
			if (jsResp.get("check").equals("OK")){
//				Toast.makeText(getApplicationContext(), "POST OK", 1000).show();
			}else{
				Toast.makeText(getApplicationContext(), "POST FALSE", 1500).show();
			}
					
		} catch (IOException e) {
			Log.e(TAG, "io err : " + e.getMessage());
			Toast.makeText(getApplicationContext(), "Network problem. Can't send post, try again.", 1000).show();
			
		} catch (Exception e) {
			Log.e(TAG, "err : " + e.getMessage());
		}		
	}
	
	private void takeStatusList () throws JSONException {
		
		// request to Status API
		RequestToServer rtsReport = new RequestToServer(LoginForm.getDataForPost().getUserDI(), RequestAPIType.STATUS);
		SenderToJSONAPI staReport = new SenderToJSONAPI(rtsReport, 1001); // 1001 - object
		
		//FIXME add check for a OK status 
		jsList = staReport.getJsObjResponse().getJSONObject("list");
		jsaList = jsList.names();
		
		TreeMap<Integer, JSONObject> tm = new TreeMap<Integer, JSONObject>();
//		
		for (int i = 0; i < jsaList.length(); i++) {
			tm.put(Integer.valueOf(jsaList.getString(i)), jsList.getJSONObject(jsaList.getString(i)));
		}
		
		if (staReport.getJsObjResponse().get("allowchange").equals("ja")) {
			isChangeble = true;
		}else{
			isChangeble = false;
		}
		 
		ArrayList<JSONObject> alJSOSorted = new ArrayList<JSONObject>();
		// sorting with a key
		for (Integer e : tm.keySet()) {
			alJSOSorted.add(tm.get(e));
		}
		
		for (int i = 0; i < jsaList.length() ; i++) {
			
			if (alJSOSorted.get(i).getString("status").equals("IN BEHANDELING")) {
				cStatusColor = 0;
			}else if (alJSOSorted.get(i).getString("status").equals("OPEN")){
				cStatusColor = 1;
			}else if (alJSOSorted.get(i).getString("status").equals("AFGEMELD")){
				cStatusColor = 2;
			}
			
				alElelments.add(new StatusElement(getApplicationContext(), 
						alJSOSorted.get(i).getString("adres"),
						alJSOSorted.get(i).getString("date"),
						alJSOSorted.get(i).getString("status"),
						cStatusColor,
						getFont(),
						setSizeForTxtInElements()));
				
				// make elements and butts active
				alElelments.get(i).setId(ID_ELEMENT + i);
				alElelments.get(i).setOnClickListener(this);
				
				if (isChangeble){
					alElelments.get(i).getButtonFromElement().setId(ID_BUTTON_IN_ELEMENT + i);
					alElelments.get(i).getButtonFromElement().setOnClickListener(this);
				}else{
				}
				
				// capture a element data in json format
				if (alJSOSorted.get(i) != null){
					alJSONElements.add(alJSOSorted.get(i));
				}
		}
	}
	
	private int setSizeForTxtInElements() {

		int size = 0;
//		
//		DisplayMetrics metrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		//
//		Log.i(TAG, "metrics.density : " + metrics.densityDpi + 
//					"metrics.widthPixels : " + metrics.widthPixels +
//					"metrics.heightPixels : " + metrics.heightPixels);
////		if (metrics.densityDpi <= 160){
//		if  (metrics.widthPixels <= 800){ //&& metrics.heightPixels <= 480){
//			size = 16;
//		}else{
//			size = 23;
//		}
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//
//		Log.i(TAG, "metrics.density : " + metrics.densityDpi + 
//					"\nmetrics.widthPixels : " + metrics.widthPixels +
//					"\nmetrics.heightPixels : " + metrics.heightPixels);
//		if (metrics.densityDpi <= 160){
		
		// take orientation & resolution for content sizing
		if (getResources().getConfiguration().orientation == 1) {
			if  (metrics.heightPixels <= 800 ){ //&& metrics.heightPixels <= 480){
				size = 16;
			}else{
				size = 25;
			}
		}else{
			if  (metrics.heightPixels <= 480 ){ //&& metrics.heightPixels <= 480){
				size = 16;
			}else{
				size = 25;
			}
		}
		
		
		return size;
		
	}
	
	private void showCommitDialog(String title, String message, final int _pos) {
	    new AlertDialog.Builder(this).setMessage(message)
	        .setTitle(title)
	        	        .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	            	 
	             }
	        })
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	            	 try {
						post(alJSONElements.get(_pos).getString("meldid"));
						alElelments.get(iNumToChangeButt).getButtonFromElement()
							.setBackgroundResource(R.color.gray_for_topheader);
						alElelments.get(iNumToChangeButt).getNameStatus().setText("IN BEHANDELING");
						alElelments.get(iNumToChangeButt).getNameStatus().setTextColor(Color.rgb(255, 106, 8));
					} catch (JSONException e) {
						e.printStackTrace();
					}
	             }
	        }).show();
	}

	@Override
	public void onClick(View v) {
		
		for (int i = 0; i < alElelments.size(); i++) {
			// for line element
			if (v.getId() == ID_ELEMENT + i){
			
				try {
					Intent iDetails = new Intent (getApplicationContext(), Details.class);
					//for set color
					iDetails.putExtra("ColorIndex", alElelments.get(i).getColorIndex());
					iDetails.putExtra("Staat", alJSONElements.get(i).getString("adres"));// name
					iDetails.putExtra("Datum", alJSONElements.get(i).getString("date"));
					iDetails.putExtra("Status", alJSONElements.get(i).getString("status"));
					iDetails.putExtra("Gebiet", alJSONElements.get(i).getString("gebied"));
					iDetails.putExtra("Bestek", alJSONElements.get(i).getString("bestek"));
					iDetails.putExtra("Melder", alJSONElements.get(i).getString("name"));// was meldid
					iDetails.putExtra("Acceptable", alJSONElements.get(i).getString("acceptable"));
					iDetails.putExtra("CommentsOpm", alJSONElements.get(i).getString("comments"));
					iDetails.putExtra("Allowchange", isChangeble );
					iDetails.putExtra("PhotoURL", alJSONElements.get(i).getString("photo"));
					iDetails.putExtra("Geconstatier", alJSONElements.get(i).getString("reported"));
					startActivityForResult(iDetails, HavenAppAndroidActivity.FORM_DETAILS);
				} catch (JSONException e) {
					Log.e(TAG, "can't set data (JSONException) for details form, reason: " + e.getMessage());
				}	
				
			}
			// for "OPEN" button 
			else if (v.getId() == ID_BUTTON_IN_ELEMENT + i){
				
				showCommitDialog("In behandeling nemen?", 
						"Door op OK te klikken wordt de status aangepast naer IN BEHANDELING.", i);
				iNumToChangeButt = i;
				
			}
		}
	}

}

/*
 {"check":"OK",
 "allowchange":"ja",
 	"list":{
 		"1":{"meldid":"133213899573",
 			"adres":"Eva Besny\u00f6straat",
 			"lat":"52.349457",
 			"long":"5.008116",
 			"date":"19\/03\/2012 07:35",
 			"status":"OPEN",
 			"gebied":"101",
 			"bestek":"110310 Groen-boomspiegel-zwerfafval fijn",
 			"acceptable":"B",
 			"reported":"B",
 			"name":"Jan Smit",
 			"photo":"http:\/\/havenapp.nl\/img\/photo\/133213899580641877.jpg",
 			"comments":""},
 			
 			"2":{"meldid":"133155534331","adres":"69 Rabinovicha Ulitsa","lat":"54.996534","long":"73.365264","date":"12\/03\/2012 13:03","status":"OPEN","gebied":"112b","bestek":"230210 Groen-beplanting-zwerfafval fijn","acceptable":"B","reported":"A+","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/133155534383386077.jpg","comments":"70%"},"3":{"meldid":"133155501030","adres":"69 Rabinovicha Ulitsa","lat":"54.996534","long":"73.365264","date":"12\/03\/2012 13:30","status":"IN BEHANDELING","gebied":"112b","bestek":"230210 Groen-beplanting-zwerfafval fijn","acceptable":"B","reported":"A","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/133155501021889097.jpg","comments":"SSS"},"4":{"meldid":"133155413195","adres":"Kanaaldijk","lat":"52.342878","long":"4.994085","date":"12\/03\/2012 13:51","status":"IN BEHANDELING","gebied":"107","bestek":"170420 Groen-gras-gazon zwerfafval grof","acceptable":"B","reported":"B","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/133155413176859961.png","comments":"Ijsjes"},"5":{"meldid":"133155404148","adres":"Eva Besny\u00f6straat","lat":"52.349481","long":"5.008238","date":"12\/03\/2012 13:21","status":"IN BEHANDELING","gebied":"112b","bestek":"230810 Meubilair-afvalbak-beplakking-graffiti","acceptable":"","reported":"B","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/133155404149673652.png","comments":""},"6":{"meldid":"133155360258","adres":"2","lat":"2","long":"2","date":"12\/03\/2012 13:02","status":"IN BEHANDELING","gebied":"101","bestek":"346","acceptable":"","reported":"","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/133155360268628492.png","comments":"ergg"},"7":{"meldid":"133155345018","adres":"ssg","lat":"325","long":"35","date":"12\/03\/2012 12:30","status":"OPEN","gebied":"101","bestek":"sg","acceptable":"","reported":"","name":"Jan Smit","photo":"","comments":"sg"},"8":{"meldid":"133155306988","adres":"Han Rensenbrinkpad","lat":"52.351924","long":"4.986570","date":"12\/03\/2012 12:09","status":"IN BEHANDELING","gebied":"102","bestek":"120230 Groen-beplanting-grof vuil","acceptable":"","reported":"A","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/133155306913190762.png","comments":""},"9":{"meldid":"133155183630","adres":"ssg","lat":"325","long":"35","date":"12\/03\/2012 12:36","status":"OPEN","gebied":"101","bestek":"sg","acceptable":"","reported":"","name":"Jan Smit","photo":"","comments":""},"10":{"meldid":"133155122483","adres":"69 Rabinovicha Ulitsa","lat":"54.996534","long":"73.365264","date":"12\/03\/2012 12:24","status":"IN BEHANDELING","gebied":"112b","bestek":"230210 Groen-beplanting-zwerfafval fijn","acceptable":"","reported":"A+","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/133155122484156382.userphoto","comments":""},"11":{"meldid":"133128799718","adres":"40 Frunze Ulitsa","lat":"54.994909","long":"73.362324","date":"09\/03\/2012 11:17","status":"IN BEHANDELING","gebied":"112d","bestek":"250210","acceptable":"B","reported":"A","name":"Jan Smit","photo":"","comments":""},"12":{"meldid":"1","adres":"Coenhavenweg 2","lat":"52.401675","long":"4.865778","date":"08\/03\/2012 10:53","status":"IN BEHANDELING","gebied":"101","bestek":"110210 Groen-beplanting-grof vuil","acceptable":"A+","reported":"A","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/photo1.jpg","comments":"Geen opmerkingen"}}}
 */
