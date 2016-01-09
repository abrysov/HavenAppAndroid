/** 
 * Created by abrysov at Mar 10, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
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
import com.rosberry.android.havenappandroid.custom.PhotoObject;
import com.rosberry.android.havenappandroid.custom.TopHeader;
import com.rosberry.android.havenappandroid.netcore.RequestAPIType;
import com.rosberry.android.havenappandroid.netcore.RequestToServer;
import com.rosberry.android.havenappandroid.netcore.SenderToJSONAPI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author abrysov
 */
public class Crow extends LoginForm implements OnClickListener{
		
		public final static String postURL = "http://havenapp.nl/api/post.php?key=8ie4lusplayoe67uties";
		public final static String TAG = "_ha_Crow.class";
	
		private FrameLayout flSubmitButton = null;
		private FrameLayout flHorizontalNav = null;
		private LinearLayout llHeader = null;
		private TextView tvAddress = null;
		private TextView tvRedCubDisc = null;
		private TextView tvGreenCubDisc = null;
		private LinearLayout llPicturesFromServer = null;
		
		private int colorForHeaderPhoto = 0;
		
		private ArrayList<PhotoObject> alPhotoElements = null;
		private int [] IDs = null;
		
		private String sMarkAcceptable;
		private TopHeader th = null;
		
		private JSONObject jsPhotos = null;
		private JSONArray jsaImages = null;
		private JSONObject jsText = null;
		private JSONArray jsaText = null;
		
		private boolean bSelectedPhoto = false;
		private PostMelData pmd = null;
		private boolean bPostOK = false;
		
		private String sErrorMessage = null;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.crow);
//			
			alPhotoElements = new ArrayList<PhotoObject>();
			colorForHeaderPhoto = R.color.orange_for_topheader;
			
			flHorizontalNav = (FrameLayout)findViewById(R.id.crow_fl_navigation);
			
			tvAddress = (TextView)findViewById(R.id.crow_tv_subheader);
			
			tvAddress.setText(getIntent().getExtras().getString("BestekText"));
			tvAddress.setTypeface(getFont());
			
			tvRedCubDisc = (TextView)findViewById(R.id.crow_tv_redcolor_cub);
			tvRedCubDisc.setTypeface(getFont());
			
			tvGreenCubDisc = (TextView)findViewById(R.id.crow_tv_greencolor_cub);
			tvGreenCubDisc.setTypeface(getFont());
			
			TextView tvSubmitBut = (TextView)findViewById(R.id.crow_tv_button_submit);
			tvSubmitBut.setTypeface(getFont());
			
			llHeader = (LinearLayout)findViewById(R.id.crow_ll_customheader);
			
			llPicturesFromServer = (LinearLayout)findViewById(R.id.crow_ll_leftright_scroll);
			
			flSubmitButton = (FrameLayout)findViewById(R.id.crow_fl_button_submit);
			flSubmitButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

						if (LoginForm.getDataForPost().getNiveau() == null) {
							
							showCrowDialog("Melding niet verzonden", 
									"Selecteer een plaatje op de CROW meetlat voordat de melding verzonden kan worden.");
						}else{
							// if form create from Control Activity
							if (getIntent().getExtras().getBoolean("FromControl")){
								
								Intent iData = new Intent(tvAddress.getText().toString());
								setResult(RESULT_OK, iData);
								finish();
								
							}else{
								showCrowConfirmDialog("Melding verzenden?",
										"Door op OK te klikken wordt de melding definitief verzonden.");
							}
						}
						
						Log.i(TAG, LoginForm.getDataForPost().viewAllDataString());
				}
			});
			
			if (LoginForm.getSkin()){
				th = new TopHeader(getApplicationContext(), "CROW meetlat (stap 4/4)", View.VISIBLE,
						getResources().getConfiguration().orientation);
				th.getTextHeader().setTypeface(getFont());
				th.getTextSubHeader().setTypeface(getFont());
				
			}else{
				th = new TopHeader(getApplicationContext(), "CROW meetlat (stap 4/4)", View.INVISIBLE,
						getResources().getConfiguration().orientation);
				th.getTextHeader().setTypeface(getFont());
				th.getTextSubHeader().setTypeface(getFont());
				flSubmitButton.setBackgroundResource(R.drawable.bg_button_clickable_red);
				colorForHeaderPhoto = R.color.red;
				flHorizontalNav.setBackgroundResource(R.color.red);
			}
			if (getIntent().getExtras().getBoolean("FromControl")){
				th.setSubHeaderText(getIntent().getExtras().getString("SubHeader"));//SubHeader
			}
			
			llHeader.addView(th);
			
			try {
				getContent();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		private void showCrowDialog(String title, String message) {
		    new AlertDialog.Builder(this).setMessage(message)
		        .setTitle(title)
		        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int which) { 
		            	 
		             }
		        }).show();
		}
		private void showCrowConfirmDialog(String title, String message) {
		    new AlertDialog.Builder(this).setMessage(message)
		        .setTitle(title)
		        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int which) { 
		            	 
		            	 if (pmd == null){
		            		 pmd = new PostMelData();
		            		 pmd.execute();		 
		            	 }
		            	 pmd = null;
		            	 
		             }
		        }).setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int which) { 
		            	 
		             }
		        }).show();
		}
		
		private void postReport() {
			
//			boolean res = false;
			HttpResponse httpResponse = null;
			
			// set params for connection...     
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setStaleCheckingEnabled(params, false); 
			HttpConnectionParams.setConnectionTimeout(params, 20000);    
			HttpConnectionParams.setSoTimeout(params, 20000);            
			DefaultHttpClient httpClient = new DefaultHttpClient(params);
			
			HttpPost httpPost = new HttpPost(postURL);
//			httpResponse = httpClient.execute(httpPost);
			httpPost.setParams(params);
			
			String pathHpoto  = LoginForm.getDataForPost().getPhotoPath();
			MultipartEntity reqEntity = new MultipartEntity();
			
			try {
				
				FileBody userphoto = new FileBody(new File(pathHpoto));
				StringBody identifier = new StringBody(LoginForm.getDataForPost().getUserDI());
				// general fields
				StringBody lat = new StringBody(LoginForm.getDataForPost().getLat().toString());
				StringBody longlong = new StringBody(LoginForm.getDataForPost().getLong().toString());
				StringBody adres = new StringBody(LoginForm.getDataForPost().getAdres());
				StringBody deelgebied = new StringBody(LoginForm.getDataForPost().getDeelgebied());
				StringBody opmerkingen = new StringBody(LoginForm.getDataForPost().getOpmerkingen());
				StringBody bestekspost = new StringBody(LoginForm.getDataForPost().getBestekspost() + 
															" " + LoginForm.getDataForPost().getBestekspostName());
				StringBody niveau = new StringBody(LoginForm.getDataForPost().getNiveau());
				
				reqEntity.addPart("identifier", identifier);
				reqEntity.addPart("lat", lat);
				reqEntity.addPart("long", longlong);
				reqEntity.addPart("adres", adres); //adress
				reqEntity.addPart("deelgebied", deelgebied); // string like a "121a"
				reqEntity.addPart("opmerkingen", opmerkingen); // comment
				reqEntity.addPart("bestekspost", bestekspost); // index like a 110123
				reqEntity.addPart("niveau", niveau); // string status like "A+"
				reqEntity.addPart("userphoto", userphoto); // jpeg from camera
				
				httpPost.setEntity(reqEntity);

				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity resEntity = response.getEntity();
				
				String sServerResponse = EntityUtils.toString(resEntity);
				Log.i(TAG, "FROM: POST:" + sServerResponse);
				
				if ((new JSONObject(sServerResponse)).get("check").equals("OK")) {
					bPostOK = true;
				}else{
					sErrorMessage = (new JSONObject(sServerResponse)).get("reason").toString();
					bPostOK = false;
				}
						
			} catch (IOException e) {
				Log.e(TAG, "io error in post forming : " + e.getMessage());	
				
			} catch (Exception e) {
				Log.e(TAG, "error in post forming: " + e.getMessage());
			}
			
		}//postReport
		
		private void getContent() throws JSONException{
			String sDeel = LoginForm.getDataForPost().getDeelgebied();
			String sBestek = LoginForm.getDataForPost().getBestekspost();
			
			RequestToServer rts = new RequestToServer(RequestAPIType.CROW, sDeel, sBestek);
			SenderToJSONAPI sta = new SenderToJSONAPI(rts, SenderToJSONAPI.TYPE_JSON_OBJ); //1001 - json obj type
			
			sMarkAcceptable = sta.getJsObjResponse().get("acceptable").toString();
			Log.i(TAG, sMarkAcceptable);
			
			jsPhotos = sta.getJsObjResponse().getJSONObject("images");
			jsaImages = jsPhotos.names();
			
			jsText = sta.getJsObjResponse().getJSONObject("text");
			jsaText = jsText.names();
			
			//creating A+ to D elements from server
			boolean isMarked;

			for (int i = 0; i < jsaImages.length(); i++) { //"http://havenapp.nl/img/crow/ap_foto_19.png"
				
//				sSta = jsaText.getString(i);
				if (sMarkAcceptable.equals(jsaText.getString(i))){
					isMarked = true;
				}else{
					isMarked = false;
				}
				
				PhotoObject po = new PhotoObject(getApplicationContext(), 
						jsPhotos.getString(jsaImages.getString(i)), 
						jsText.getString(jsaText.getString(i)),
						jsaText.getString(i),
						colorForHeaderPhoto,
						isMarked,
						getFont());
				
				po.setId(i);
				po.setOnClickListener(this);
				alPhotoElements.add(i, po);
				
			}
			
			llPicturesFromServer.addView(alPhotoElements.get(1));
			llPicturesFromServer.addView(alPhotoElements.get(2));
			llPicturesFromServer.addView(alPhotoElements.get(3));
			llPicturesFromServer.addView(alPhotoElements.get(4));
			llPicturesFromServer.addView(alPhotoElements.get(0));
			
		}//getContent
		
		private void deleteWorkDir() {
			//delete work directory 
			String sNameHavenWorkDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/havenappandroid/";
			File workDirectoryHavenApp = new File(sNameHavenWorkDirectory);
			if (workDirectoryHavenApp != null && workDirectoryHavenApp.isDirectory()){
//				
				String[] sFilesInDir = workDirectoryHavenApp.list();
				try {
					for (int i = 0; i < sFilesInDir.length; i++) {
						new File(workDirectoryHavenApp, sFilesInDir[i]).delete();
					}
					workDirectoryHavenApp.delete();
					Log.i(TAG, "working dir has been deleted correct.");
				} catch (Exception e) {
					Log.e(TAG, "working dir not deleted, or some files can't be deleted, reason: " + e.getMessage());
				}

			}
		}
		
		@Override
		protected void onDestroy() {
			super.onDestroy();
			
			deleteWorkDir();
		}

		@Override
		public void onClick(View v) {
			
			for (PhotoObject e : alPhotoElements) {
				
				e.getFrameForColorSelected().setBackgroundResource(R.color.gray_for_topheader);
				
				if (e.getStatusString().equals(sMarkAcceptable)){
					e.getFrameForColorSelected().setBackgroundResource(R.color.green);
				}
				if (v.getId() == e.getId()){
					
					e.getFrameForColorSelected().setBackgroundResource(R.color.red);
					LoginForm.getDataForPost().setNiveau(e.getStatusString());
				}
			}
			
			bSelectedPhoto = true;
			
		}
		
		
		private class PostMelData extends AsyncTask<Void, Void, Void> {

			private ProgressDialog pd = null;
			
			@Override
			protected void onPreExecute() {

				pd = ProgressDialog.show(Crow.this, "", "");
				super.onPreExecute();
			}
			
			@Override
			protected Void doInBackground(Void... params) {

				postReport();
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				pd.dismiss();
				
				if (bPostOK) {

					finish();
					startActivityForResult(new Intent(getApplicationContext(), CameraForm.class),
											HavenAppAndroidActivity.FORM_CAMERA);
				}else{
//					Toast.makeText(getApplicationContext(), "Error: " + sErrorMessage, 1500).show();
					Log.e(TAG, "Error: " + sErrorMessage);
				}
				
//				super.onPostExecute(result);
			}
			
			@Override
			protected void onCancelled() {
				cancel(true);
				pd.dismiss();
			}
			
		}
		
		
}

