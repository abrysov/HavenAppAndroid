///** 
// * Created by abrysov at Mar 15, 2012
// */
//package com.rosberry.android.havenappandroid.activity;
//
//import com.rosberry.android.havenappandroid.R;
//import com.rosberry.android.havenappandroid.custom.TopHeader;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
///**
// * @author abrysov
// *
// */
//public class Map extends LoginForm {
//	
//	
//	
//	/* (non-Javadoc)
//	 * @see com.rosberry.android.havenappandroid.activity.LoginForm#onCreate(android.os.Bundle)
//	 */
//	
////	private FrameLayout flHeader = null;
//	private LinearLayout llHeader = null;
//	private String sAddress = null;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.map);
//		
//		//TODO adding the subtitle !!! with put EXTRA
//		sAddress = "Address";
//		
////		flHeader = (FrameLayout)findViewById(R.id.map_fl_header);
//		llHeader = (LinearLayout)findViewById(R.id.map_ll_customheader);
//		if (sAddress != null) {
//			TopHeader th = new TopHeader(getApplicationContext(), sAddress, View.VISIBLE,
//					getResources().getConfiguration().orientation);
//			llHeader.addView(th);
//		}
//		
//		
//		
//		
//	}
//
//}
