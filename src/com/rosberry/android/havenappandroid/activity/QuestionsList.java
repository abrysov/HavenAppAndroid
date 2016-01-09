/** 
 * Created by abrysov at Mar 22, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

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
import android.widget.ListView;

/**
 * @author abrysov
 *
 */
public class QuestionsList extends LoginForm {

	public static final String TAG = "_ha_QuestionsList.class";
	
	private ListView lvQuestions = null;
	private ArrayList<String> alAnswars = null;
	private ArrayList<String> alQustions = null;
	
	private LinearLayout llHeader = null;
	
	private TopHeader th = null;
	
	/* (non-Javadoc)
	 * @see com.rosberry.android.havenappandroid.activity.LoginForm#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qlist);
		
		llHeader = (LinearLayout)findViewById(R.id.qlist_ll_header);
		String sSubHeaderText = "Info";
		
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), sSubHeaderText, View.VISIBLE,
					getResources().getConfiguration().orientation);
		}else{
			th = new TopHeader(getApplicationContext(), sSubHeaderText, View.INVISIBLE,
					getResources().getConfiguration().orientation);
		}
		th.getTextHeader().setTypeface(getFont());
		th.getTextSubHeader().setTypeface(getFont());
		llHeader.addView(th);
		
		alAnswars = new ArrayList<String>();
		alQustions = new ArrayList<String>();
		
		getQuestions();
		
		lvQuestions = (ListView)findViewById(R.id.qlist_lv_questions);
		lvQuestions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				for (int i = 0; i <= arg2; i++) {
					if (arg2 == i ) {
						Intent intent = new Intent(getApplicationContext(), AnswerForm.class);
						intent.putExtra("Answer", alAnswars.get(i));
						
						startActivity(intent);
					}
				}
				
			}
		});
		
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
//				alQustions);
		ArrayAdapterForCustomList adapter = new ArrayAdapterForCustomList(getApplicationContext(), alQustions, getFont());
		lvQuestions.setAdapter(adapter);
	
	}
	
	private void getQuestions () {
		
		JSONObject jsListQuestion = null;
		
		RequestToServer rts = new RequestToServer(RequestAPIType.HELP);
		SenderToJSONAPI sta = new SenderToJSONAPI(rts, 1001);
		
		try {
			if (sta.getJsObjResponse().get("check").equals("OK")) {
				
				jsListQuestion = sta.getJsObjResponse().getJSONObject("list");
				JSONArray jsaQustions = new JSONArray();
				
				jsaQustions = jsListQuestion.names();
				
				for (int i = 0; i < jsaQustions.length(); i++) {
					
					alAnswars.add(jsListQuestion.getString(jsaQustions.getString(i)));
					alQustions.add(jsaQustions.getString(i).toString());
					Log.i(TAG, "question_" + i + ": " + jsaQustions.getString(i).toString());
					Log.i(TAG, "answer_" + i + ": " + alAnswars.get(i));
					
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage());
		}
		
		
		
	}
	
}
