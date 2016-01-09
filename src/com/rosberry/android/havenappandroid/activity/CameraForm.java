package com.rosberry.android.havenappandroid.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.TopHeader;
import com.rosberry.android.havenappandroid.netcore.RequestToServer;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CameraForm extends LoginForm implements OnClickListener, PictureCallback, Callback{
	
	private final String TAG = "_ha_Camera";
	private static String imageFilePath = null;
	private Uri imageFileUri = null;
	
	private FrameLayout flCameraButt = null;
	
	private SurfaceView svCameraView = null;
	private SurfaceHolder surfaceHolder = null;
	private Camera camera = null;
	
	private boolean bHasPicture = false;
	
	private LinearLayout llHeader = null;
	private RequestToServer rtsLocationData = null;

	private TopHeader th = null;
	private String sAddress = null;
//	private boolean isPortrait = false;
//	private int iOrientation = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		
		// force Landscape layout
//		int iOrientation = getResources().getConfiguration().orientation;
		
//		if (getResources().getConfiguration().orientation == 1){ // if device portrait in mode , rotation the picture for 90 degrees
//			isPortrait = true;
//			iOrientation = 1;
//		}else{
//			isPortrait = false;
//		}
		
		llHeader = (LinearLayout)findViewById(R.id.camera_ll_customheader);
		flCameraButt = (FrameLayout)findViewById(R.id.camera_fl_camerabutt);
		
		flCameraButt.setOnClickListener(this);
		
		String sSubHeaderName = "Maak een foto (stap 1/4)";
		
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), sSubHeaderName, View.VISIBLE,
					getResources().getConfiguration().orientation);
			th.getTextHeader().setTypeface(getFont());
			th.getTextSubHeader().setTypeface(getFont());
		}else{
			th = new TopHeader(getApplicationContext(), sSubHeaderName, View.INVISIBLE,
					getResources().getConfiguration().orientation);
			th.getTextHeader().setTypeface(getFont());
			th.getTextSubHeader().setTypeface(getFont());
		}
		llHeader.addView(th);
		//
		
		svCameraView = (SurfaceView)findViewById(R.id.camera_sv_general);
		surfaceHolder = svCameraView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);

		svCameraView.setFocusable(true);
		svCameraView.setFocusableInTouchMode(true);
		
		rtsLocationData = new RequestToServer(dLatitude, dLongitude);
		
	}
	
	private int getPhoneRotation () {
		int iRotation = 0;
		Display display = ((WindowManager) getWindowManager()).getDefaultDisplay();
		iRotation = display.getRotation();
		return iRotation;
	}
	
	public static String getPhotoPath () {
		return imageFilePath;
	}
	
	public void onClick(View v) {
		
		Camera.Parameters params = camera.getParameters();
		params.setPictureFormat(ImageFormat.JPEG);
		// choose true orientation in all (4) device positions, also consider a version android API
		switch (getPhoneRotation()) {
		case 0: // normal
			
			if (android.os.Build.VERSION.SDK_INT <= 8){ // 2.2
				params.set("rotation", 90);
			}else if (android.os.Build.VERSION.SDK_INT == 9){// 2.3.0
				params.set("rotation", 0);
			}else if (android.os.Build.VERSION.SDK_INT == 10){ // 2.3.3
				params.set("rotation", 0);
			}else if (android.os.Build.VERSION.SDK_INT >= 11){ // 3.0 and above
				params.set("rotation", 0);
			}
			break;
			
		case 1: // to left
			
			if (android.os.Build.VERSION.SDK_INT <= 8){
				params.set("rotation", 0);
			}else if (android.os.Build.VERSION.SDK_INT == 9){// 2.3.0
				params.set("rotation", 270);
			}else if (android.os.Build.VERSION.SDK_INT == 10){
				params.set("rotation", 270);
			}else if (android.os.Build.VERSION.SDK_INT >= 11){
				params.set("rotation", 270);
			}
			break;

		case 2: // upside
			
			if (android.os.Build.VERSION.SDK_INT <= 8) {
				params.set("rotation", 270);
			}else if (android.os.Build.VERSION.SDK_INT == 9){// 2.3.0
				params.set("rotation", 180);
			}else if (android.os.Build.VERSION.SDK_INT == 10){
				params.set("rotation", 180);
			}else if (android.os.Build.VERSION.SDK_INT >= 11){
				params.set("rotation", 180);
			}
			break;

		case 3:// to right
			
			if (android.os.Build.VERSION.SDK_INT <= 8) {
				params.set("rotation", 180);
			}else if (android.os.Build.VERSION.SDK_INT == 9){// 2.3.0
				params.set("rotation", 90);
			}else if (android.os.Build.VERSION.SDK_INT == 10){
				params.set("rotation", 90);
			}else if (android.os.Build.VERSION.SDK_INT >= 11){
				params.set("rotation", 90);
			}
			break;

		default:
			break;
		}
		
		camera.setParameters(params);
		camera.takePicture(null, null, this);
		
		flCameraButt.setClickable(false);
				
				new Thread() {
					@Override
					public void run() {
						
						try {
							
							sleep(1000);
							flCameraButt.setClickable(true);
							Intent iEnterLocation = new Intent(getApplicationContext(), EnterALocation.class);
							iEnterLocation.putExtra("Address", sAddress);
							startActivityForResult(iEnterLocation, HavenAppAndroidActivity.FORM_ENTER_LOCATION);
							bHasPicture = true;
							
						} catch (Exception e) {
							Log.e(TAG, "can't open enter location form");
						}finally{
							
						}
					};
				}.start();

	}
	
	public void onPictureTaken(byte[] data, Camera camera) {
		
		imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
        				"/havenappandroid/havenpic_tmp.jpg";
		LoginForm.getDataForPost().setPhotoPath(imageFilePath);
//		imageFilePath = Environment.getDataDirectory().getAbsolutePath() +
//				"/havenappandroid/havenpic_tmp.jpg";
		File fImage = new File(imageFilePath);
		
		Log.i(TAG, "photo camera file will be saved in : " + imageFilePath);
		
		imageFileUri = Uri.fromFile(fImage);
		
		try {
			OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
			
			imageFileOS.write(data);
			imageFileOS.flush();
			imageFileOS.close();
			
		} catch (FileNotFoundException e) {
			Toast t = Toast.makeText(this, "Sava file, error: " + e.getMessage(), Toast.LENGTH_SHORT);
			Log.e(TAG, "error: " + e.getMessage());
			t.show();
		} catch (IOException e) {
			Toast t = Toast.makeText(this, "error: " + e.getMessage(), Toast.LENGTH_SHORT);
			Log.e(TAG, "Sava file, error: " + e.getMessage());
			t.show();
		}

	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		camera.startPreview();
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		
		try {
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
			Camera.Parameters parameters = camera.getParameters();
			
			Log.i(TAG, "version android API : " + android.os.Build.VERSION.SDK_INT);
			
			switch (getPhoneRotation()) {
			case 0://normal position
				if (android.os.Build.VERSION.SDK_INT <= 8){ // 2.2
					camera.setDisplayOrientation(90);
				}else if (android.os.Build.VERSION.SDK_INT == 9){// 2.3.0
					camera.setDisplayOrientation(0);
				}else if (android.os.Build.VERSION.SDK_INT == 10){ // 2.3.3
					camera.setDisplayOrientation(0);
				}else if (android.os.Build.VERSION.SDK_INT >= 11){ // 3.0 and above
					camera.setDisplayOrientation(0);
				}
				
				break;
			case 1:// turn left
				if (android.os.Build.VERSION.SDK_INT <= 8){
					camera.setDisplayOrientation(0);
				}else if (android.os.Build.VERSION.SDK_INT == 9){// 2.3.0
					camera.setDisplayOrientation(270);
				}else if (android.os.Build.VERSION.SDK_INT == 10){
					camera.setDisplayOrientation(270);
				}else if (android.os.Build.VERSION.SDK_INT >= 11){
					camera.setDisplayOrientation(270);
				}
				
				break;
			case 2:// turn upside
				if (android.os.Build.VERSION.SDK_INT <= 8) {
					camera.setDisplayOrientation(270);
				}else if (android.os.Build.VERSION.SDK_INT == 9){// 2.3.0
					camera.setDisplayOrientation(180);
				}else if (android.os.Build.VERSION.SDK_INT == 10){
					camera.setDisplayOrientation(180);
				}else if (android.os.Build.VERSION.SDK_INT >= 11){
					camera.setDisplayOrientation(180);
				}
				
				break;
			case 3:// turn right
				if (android.os.Build.VERSION.SDK_INT <= 8) {
					camera.setDisplayOrientation(180);
				}else if (android.os.Build.VERSION.SDK_INT == 9){// 2.3.0
					camera.setDisplayOrientation(90);
				}else if (android.os.Build.VERSION.SDK_INT == 10){
					camera.setDisplayOrientation(90);
				}else if (android.os.Build.VERSION.SDK_INT >= 11){
					camera.setDisplayOrientation(90);
				}
				
				break;

			default:
				break;
			}

			camera.setParameters(parameters);
			
		} catch (IOException exception) {
			camera.release();
			Log.d(TAG, "error in camera starts");
		}
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		LoginForm.updateGPS();
	}
	
	
}
