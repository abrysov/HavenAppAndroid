/**
 * 
 */
package com.rosberry.android.havenappandroid.activity;

import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.ArrayAdapterForCustomList;
import com.rosberry.android.havenappandroid.custom.TopHeader;
import com.rosberry.android.havenappandroid.netcore.RequestAPIType;
import com.rosberry.android.havenappandroid.netcore.RequestToServer;
import com.rosberry.android.havenappandroid.netcore.SenderToJSONAPI;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author abrysov
 */
public class ListBestekspost extends EnterALocation{
	
	private ListView lvElements = null;
	private ArrayAdapterForCustomList adapter =  null;
	private final String TAG = "_ha_ListBest form";
	private ArrayList<ContentListBestek> alElementsOnList = null;
	private LinearLayout llHeader = null;
	private String sBestek = null; // selected bestek
	private TopHeader th = null;
	private GetBestekList gblGetList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listbest);

		alElementsOnList = new ArrayList<ListBestekspost.ContentListBestek>();
		
		gblGetList = new GetBestekList();
		gblGetList.execute();

		llHeader = (LinearLayout)findViewById(R.id.listbest_ll_customheader);
		
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), "Bestekspost (stap 3/4)", View.VISIBLE,
					getResources().getConfiguration().orientation);
			th.getTextHeader().setTypeface(getFont());
			th.getTextSubHeader().setTypeface(getFont());
			
		}else{
			th = new TopHeader(getApplicationContext(), "Bestekspost (stap 3/4)", View.INVISIBLE,
					getResources().getConfiguration().orientation);
			th.getTextHeader().setTypeface(getFont());
			th.getTextSubHeader().setTypeface(getFont());
		}
		
		llHeader.addView(th);
		
		lvElements = (ListView)findViewById(R.id.listbest_lv_elements);
		lvElements.setBackgroundResource(R.color.gray_for_separator);
		lvElements.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
//				sBestek = alElementsOnList.get(arg2).getIndex();	//alIndexs.get(arg2); // i
				
				LoginForm.getDataForPost().setBestekspost(alElementsOnList.get(arg2).getIndex());
				LoginForm.getDataForPost().setBestekspostName(alElementsOnList.get(arg2).getName());
				Log.i(TAG, "_db adding element \"bestekspost\" : " + alElementsOnList.get(arg2).getBody());
				
				Intent iCrow = new Intent(getApplicationContext(), Crow.class);
				iCrow.putExtra("BestekText", alElementsOnList.get(arg2).getName());	//alNames.get(arg2)); // 
				startActivityForResult(iCrow, HavenAppAndroidActivity.FORM_CROW);
			}
		
		});
		
	}
	
	private void getListBest () throws JSONException {
		
		// working with API
		// get Extra "Deelgebied" from previus form             like              "112e"
		RequestToServer rts = new RequestToServer(RequestAPIType.DEELGEBIED,
				getIntent().getStringExtra("Deelgebied"));
		SenderToJSONAPI sta = new SenderToJSONAPI(rts, SenderToJSONAPI.TYPE_JSON_OBJ);
		
		if (sta.getJsObjResponse().get("check").equals("OK")){
			
			JSONObject jsList = sta.getJsObjResponse().getJSONObject("list");
			JSONArray jsaIndexs = jsList.names();
			
			for (int i = 0; i < jsaIndexs.length(); i++) {
				try {
//					// take names of object by "names" in JSON objects
//					// take index from JSON array
//					// fill array list for ListView
					alElementsOnList.add(new ContentListBestek(jsList.getString(jsaIndexs.getString(i)),
							jsaIndexs.getString(i),
							jsaIndexs.getString(i) + " " + jsList.getString(jsaIndexs.getString(i)) ));
					
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
						Log.e(TAG, "exp : " + e.getMessage());
					
				}
			}	
			
			TreeMap<String, ContentListBestek> tmSortedElements = new TreeMap<String, ListBestekspost.ContentListBestek>();
			
			for (int i = 0; i < alElementsOnList.size(); i++) {
				tmSortedElements.put(alElementsOnList.get(i).getName(), alElementsOnList.get(i));
			}
			
			alElementsOnList.clear();
			
			for (String e : tmSortedElements.keySet()) {
				alElementsOnList.add(tmSortedElements.get(e));
			}
			
		}else{
//			alElementsForList.add("No data");
			alElementsOnList.add(new ContentListBestek("no data", "no data", "no data"));
			
			Log.i(TAG, "check status not OK, no data for list");
		}
		
	}
	
	
	private class GetBestekList extends AsyncTask<Void, Void, Void>
    							implements DialogInterface.OnCancelListener{
	
		private ProgressDialog dialog;
	
		protected void onPreExecute(){
			dialog = ProgressDialog.show(ListBestekspost.this, "", "", true);
		}
		
		protected Void doInBackground(Void... unused)
		{
			try {
				getListBest();
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e(TAG, "can't get json listBest, err: " + e.getMessage());
			}
			return null;
		}
		
		protected void onPostExecute(Void unused)
		{
			dialog.dismiss();
			
//			adapter = new ArrayAdapterForCustomList(getApplicationContext(), alElementsForList, getFont());
			ArrayList<String> namesBody = new ArrayList<String>();
			for (int i = 0; i < alElementsOnList.size(); i++) {
				namesBody.add(alElementsOnList.get(i).getBody());
			}
			adapter = new ArrayAdapterForCustomList(getApplicationContext(), namesBody, getFont());
			lvElements.setAdapter(adapter);
		}
	
		public void onCancel(DialogInterface dialog)
		{
			cancel(true);
			dialog.dismiss();
		}
	
	}
	
	private class ContentListBestek {
		
		private String sName = null;
		private String sIndex = null;
		private String sBody = null;
		
		
		/**
		 * 
		 */
		public ContentListBestek(String _name, String _index, String _body) {

			this.sName = _name;
			this.sIndex = _index;
			this.sBody = _body;
			
		
		}
		
		public String getName() {
			return sName;
		}


		public void setName(String sName) {
			this.sName = sName;
		}


		public String getIndex() {
			return sIndex;
		}


		public void setIndex(String sIndex) {
			this.sIndex = sIndex;
		}


		public String getBody() {
			return sBody;
		}


		public void setBody(String sBody) {
			this.sBody = sBody;
		}		
		
		
	}
	

}
