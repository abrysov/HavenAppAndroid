/** 
 * Created by abrysov at Mar 20, 2012
 */
package com.rosberry.android.havenappandroid.netcore;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author abrysov
 *
 */
public class SenderToJSONAPI {
	
	private static final String TAG = "_ha_SenderToAPIClass";
	
	public static final int TYPE_JSON_OBJ = 1001;
	public static final int TYPE_JSON_MASS = 1002;
	
	private HttpClient httpclient = null;
	private HttpParams sendParams = null;
	
	private HttpResponse httpResponse = null;
	private HttpGet httpget = null;
	
	private JSONObject jsResponse = null;
	private JSONArray jsaResponse = null;
	
	/**
	 * 
	 */
	public SenderToJSONAPI(RequestToServer _rtsHaven, int _typeResp) {

		httpclient = new DefaultHttpClient();
		sendParams = new BasicHttpParams();
		
		HttpConnectionParams.setConnectionTimeout(sendParams, 20000);
		HttpConnectionParams.setSoTimeout(sendParams, 20000);
		
		String sRequestBody = _rtsHaven.getREQUEST();
		Log.i(TAG , "TO: " + sRequestBody);
		httpget = new HttpGet(sRequestBody);
		
		try {
			
			httpResponse = httpclient.execute(httpget);
			String sServerResponse = EntityUtils.toString(httpResponse.getEntity());
			Log.i(TAG, "FROM: " + sServerResponse);	
			
			if (_typeResp == TYPE_JSON_OBJ) {
				
				try {
					jsResponse = new JSONObject(sServerResponse);
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(TAG, "json error: " + e.getMessage());
				}	
				
			}else if (_typeResp == TYPE_JSON_MASS){
				
				try {
					
					jsaResponse = new JSONArray(sServerResponse);
					
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(TAG, "json error: " + e.getMessage());
				}
				
			}

			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e(TAG, "ClientProtocolException error: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "IOException error: " + e.getMessage());
		}
		
	}

	/**
	 * @return the jsResponse (JSON object)
	 */
	public JSONObject getJsObjResponse() {
		return jsResponse != null ? jsResponse : null;
	}
	
	public JSONArray getJsMassResponse() {
		return jsaResponse != null ? jsaResponse : null;
	}
	

}
