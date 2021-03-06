///** 
// * Created by abrysov at Mar 28, 2012
// */
//package com.rosberry.android.havenappandroid.activity;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapController;
//import com.google.android.maps.MapView;
//import com.google.android.maps.MyLocationOverlay;
//import com.google.android.maps.OverlayItem;
//import com.rosberry.android.havenappandroid.R;
//import com.rosberry.android.havenappandroid.custom.MyOverlays;
//
///**
// * @author abrysov
// *
// */
//public class Map1 extends MapActivity {
//    /** Called when the activity is first created. */
//	private MapController mapController;
//	private MapView mapView;
//	private LocationManager locationManager;
//	private MyOverlays itemizedoverlay;
//	private MyLocationOverlay myLocationOverlay;
//
//	public void onCreate(Bundle bundle) {
//		super.onCreate(bundle);
//		setContentView(R.layout.map); // bind the layout to the activity
//
//		// Configure the Map
//		mapView = (MapView) findViewById(R.id.mapview);
//		mapView.setBuiltInZoomControls(true);
//		mapView.setSatellite(true);
//		mapController = mapView.getController();
//		mapController.setZoom(14); // Zoon 1 is world view
//		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//				0, new GeoUpdateHandler());
//
//		myLocationOverlay = new MyLocationOverlay(this, mapView);
//		mapView.getOverlays().add(myLocationOverlay);
//
//		myLocationOverlay.runOnFirstFix(new Runnable() {
//			public void run() {
//				mapView.getController().animateTo(
//						myLocationOverlay.getMyLocation());
//			}
//		});
//
//		Drawable drawable = this.getResources().getDrawable(R.drawable.point);
//		itemizedoverlay = new MyOverlays(this, drawable);
//		createMarker();
//	}
//
//	@Override
//	protected boolean isRouteDisplayed() {
//		return false;
//	}
//
//	public class GeoUpdateHandler implements LocationListener {
//
//		@Override
//		public void onLocationChanged(Location location) {
//			int lat = (int) (location.getLatitude() * 1E6);
//			int lng = (int) (location.getLongitude() * 1E6);
//			GeoPoint point = new GeoPoint(50, 73);
//			createMarker();
//			mapController.animateTo(point); // mapController.setCenter(point);
//
//		}
//
//		@Override
//		public void onProviderDisabled(String provider) {
//		}
//
//		@Override
//		public void onProviderEnabled(String provider) {
//		}
//
//		@Override
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//		}
//	}
//
//	private void createMarker() {
//		GeoPoint p = mapView.getMapCenter();
//		OverlayItem overlayitem = new OverlayItem(p, "", "");
//		itemizedoverlay.addOverlay(overlayitem);
//		if (itemizedoverlay.size() > 0) {
//			mapView.getOverlays().add(itemizedoverlay);
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		myLocationOverlay.enableMyLocation();
//		myLocationOverlay.enableCompass();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onResume();
//		myLocationOverlay.disableMyLocation();
//		myLocationOverlay.disableCompass();
//	}	
//}
