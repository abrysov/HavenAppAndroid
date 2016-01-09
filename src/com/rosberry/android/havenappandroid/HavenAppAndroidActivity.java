package com.rosberry.android.havenappandroid;

import java.io.File;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.activity.LoginForm;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
/**
 * @author abrysov
 *
 */
public class HavenAppAndroidActivity extends Activity {
	
	public static final int FORM_SPLASH           = 1000;
	public static final int FORM_LOGIN            = 1001;
	public static final int FORM_MAINFORM         = 1002;
	public static final int FORM_CAMERA           = 1003;
	public static final int FORM_ENTER_LOCATION   = 1004;
	public static final int FORM_INFO             = 1005;
	public static final int FORM_CROW             = 1006;
	public static final int FORM_STATUS           = 1007;
	public static final int FORM_CONTROL          = 1008;
	public static final int FORM_DETAILS          = 1009;
	public static final int FORM_MAP              = 1010;
	public static final int FORM_LIST_BESTEKSPOST = 1011;
	public static final int FORM_CONTROL_DEEL		= 1012;
	
	private final String TAG = "_ha_HavenAppAndroidActivity";

	protected boolean _active = true;
	protected int _splashTime = 2000; // time to display the splash screen in ms
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        viewSplash();
        creatingAppDir();
        
    }
    
//    /**
//	 * Creating working directory for app on SD-CARD (Memory card).
//	 */
	private void creatingAppDir() {
		
		File fDir = new File("/sdcard/havenappandroid/");
		try {
			if (!fDir.exists()){
				fDir.mkdir();
				Log.d(TAG, "work dir created");
			}
		} catch (Exception e) {
			Log.d(TAG, "can't creat work directory, check memory card");
		}
		
	}

	private void viewSplash () {
    	
		Thread splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {
	                int waited = 0;
	                while(_active && (waited < _splashTime)) {
	                    sleep(100);
	                    if(_active) {
	                        waited += 100;
	                    }
	                }
	            } catch(InterruptedException e) {
	                // do nothing
	            } finally {
	                finish();
	                
	                Intent iLoginForm = new Intent(getApplicationContext(), LoginForm.class);
	                // for started status
	                iLoginForm.putExtra("LoginFormActiveStatus", true);
	                startActivityForResult(iLoginForm, FORM_LOGIN); //"com.droidnova.android.splashscreen.MyApp"
	                interrupt();
	            }
	        }
	    };
	    splashTread.start();  
    	
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        _active = false;
	    }
	    return true;
	}
    
}