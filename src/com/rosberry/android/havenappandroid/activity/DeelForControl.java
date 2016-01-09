/** 
 * Created by abrysov at Apr 2, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import java.util.ArrayList;

import org.json.JSONException;

import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.ArrayAdapterForCustomList;
import com.rosberry.android.havenappandroid.custom.TopHeader;
import com.rosberry.android.havenappandroid.netcore.RequestAPIType;
import com.rosberry.android.havenappandroid.netcore.RequestToServer;
import com.rosberry.android.havenappandroid.netcore.SenderToJSONAPI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

/**
 * @author abrysov
 *
 */
public class DeelForControl extends LoginForm {
	
	
	
	private ListView lvForDeels = null;
	private ArrayList<String> alDeels = null;
	
	private LinearLayout llHeader = null;
	private ArrayAdapter<String> adapter = null;
	
	private TopHeader th = null;
	
	
	/* (non-Javadoc)
	 * @see com.rosberry.android.havenappandroid.activity.LoginForm#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deelforcontrol);
		
		llHeader = (LinearLayout)findViewById(R.id.deelforcontrol_ll_customheader);
//		TopHeader th = new TopHeader(getApplicationContext(), "Controle", View.VISIBLE);
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), "Controle", View.VISIBLE,
					getResources().getConfiguration().orientation);
		}else{
			th = new TopHeader(getApplicationContext(), "Controle", View.INVISIBLE,
					getResources().getConfiguration().orientation);
		}
		th.getTextHeader().setTypeface(getFont());
		th.getTextSubHeader().setTypeface(getFont());
		llHeader.addView(th);

		
//		LayoutParams lpForControlDeelList = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//		lpForControlDeelList.setMargins(15, 10, 15, 10);
		lvForDeels = (ListView)findViewById(R.id.deelforcontrol_lv_for_deels);
		lvForDeels.setBackgroundResource(R.color.gray_for_data_fields_bg);
//		lvForDeels.set
//		lvForDeels.setLayoutParams(lpForControlDeelList);

		
		alDeels = new ArrayList<String>();
		getDataAPI();
		
		adapter = new ArrayAdapterForCustomList(getApplicationContext(), alDeels, getFont());
		lvForDeels.setAdapter(adapter);
		
		lvForDeels.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent iControl = new Intent(getApplicationContext(), Control.class);
				// cut first 10 simbole in string , get only string like "122b" 
				//and put it co controle form;
				String sDeel = alDeels.get(arg2).substring(11, alDeels.get(arg2).length());
				iControl.putExtra("Deel", sDeel);
				
				LoginForm.getDataForPost().setDeelgebied(sDeel);
				
				finish();
				startActivityForResult(iControl, HavenAppAndroidActivity.FORM_CONTROL);
			}
		});
		
	}
	
	private void getDataAPI () {
		
		RequestToServer rtsDeel = new RequestToServer(RequestAPIType.DEELGEBIED);
		SenderToJSONAPI staDeel = new SenderToJSONAPI(rtsDeel, 1002);
		
		if (staDeel != null && staDeel.toString() != ""){
			
			try {
				for (int i = 0; i < staDeel.getJsMassResponse().length(); i++) {
					alDeels.add(staDeel.getJsMassResponse().getString(i));
				}
			} catch (JSONException e) {
			} catch ( Exception e) {
				
			}
		}else{
			alDeels.add("No data from server, try again.");
		}
		

	}
	

}
