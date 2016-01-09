/** 
 * Created by abrysov at Apr 2, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.MyOverlays;


/**
 * @author abrysov
 *
 */
public class Map2 extends MapActivity {
	
	private MapView mv = null;

	private MapController mapController;
//	private MapView mapView;
	private LocationManager locationManager;
	private MyOverlays itemizedoverlay;
	private MyLocationOverlay myLocationOverlay;
	
	private GeoPoint coordinates = null;
	
	
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		
//		setContentView(R.layout.map2);
		
//		mv = (MapView)findViewById(R.id.map2);
//		mv.setVisibility(View.VISIBLE);
		mv = new MapView(this, "09CeDjCtNRoe21CbJpI7rah6Qnz3rQ7E2bBlkTw");//09CeDjCtNRoe21CbJpI7rah6Qnz3rQ7E2bBlkTw
		
		mv.setBuiltInZoomControls(true);
		mv.setSatellite(true);
		
		mapController = mv.getController();
		mapController.setZoom(20); // Zoon 1 is world view
//		coordinates = new GeoPoint(73, 55);
//		mapController.setCenter(coordinates);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());
		
		myLocationOverlay = new MyLocationOverlay(this, mv);
		mv.getOverlays().add(myLocationOverlay);		
		
		
		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mv.getController().animateTo(
						myLocationOverlay.getMyLocation());
			}
		});
		
		Drawable drawable = this.getResources().getDrawable(R.drawable.point);
		itemizedoverlay = new MyOverlays(this, drawable);
//		createMarker();
		
		LinearLayout ll = new LinearLayout(getApplicationContext());
		
		FrameLayout flForMap = new FrameLayout(getApplicationContext());
		
		LayoutParams lpForFrameMap = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		flForMap.setLayoutParams(lpForFrameMap);
		flForMap.addView(mv);
		ll.addView(flForMap);
		
		TextView tv = new TextView(getApplicationContext());
		tv.setText("The MAP");
		
		FrameLayout flForText = new FrameLayout(getApplicationContext());
		flForText.addView(tv);
		flForText.setLayoutParams(lpForFrameMap);
		
		ll.addView(flForText);
		ll.setOrientation(LinearLayout.VERTICAL);
		setContentView(ll);
		
	}
	
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	public class GeoUpdateHandler implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			GeoPoint point = new GeoPoint(lat, lng);
			createMarker();
			mapController.animateTo(point); // mapController.setCenter(point);

		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	private void createMarker() {
		GeoPoint p = mv.getMapCenter();
		OverlayItem overlayitem = new OverlayItem(p, "111", "1111");
		itemizedoverlay.addOverlay(overlayitem);
		if (itemizedoverlay.size() > 0) {
			mv.getOverlays().add(itemizedoverlay);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
	}

	@Override
	protected void onPause() {
		super.onResume();
		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableCompass();
		
//		locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
	}	

}
