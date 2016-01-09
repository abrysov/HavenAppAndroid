/** 
 * Created by abrysov at Mar 26, 2012
 */
package com.rosberry.android.havenappandroid.custom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.rosberry.android.havenappandroid.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author abrysov
 *
 */
public class PhotoObject extends FrameLayout {

	private final String TAG = "_da_PhotoObject.class";
	
//	private WebView wvPhoto = null;
	private TextView tvDescript = null;
	private TextView tvStatus = null;
	private LinearLayout llWrapPhotoAndDisc = null;
	
	private FrameLayout flForPhoto = null;
	
	private String sRedSelected = null;
	private String sStatusOfPhotoObj = null;
	
	/**
	 * @param context
	 */
	public PhotoObject(Context context, String sContentURL, String sTextDescript, String sStatus, 
							int colorHeaderPhoto, boolean _mark, Typeface _tf) {
		super(context);
		
		sStatusOfPhotoObj = sStatus;
		//wrap element
		setPadding(10, 10, 10, 10);
		
		MarginLayoutParams mlp = new MarginLayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT);
		LayoutParams lp = new LayoutParams(mlp);
		lp.setMargins(10, 0, 0, 0);
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(lp);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		
		FrameLayout flForStatusText = new FrameLayout(context);
		
		MarginLayoutParams mlpFillWrap = new MarginLayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT);
		
		flForStatusText.setLayoutParams(mlpFillWrap);
		flForStatusText.setBackgroundResource(colorHeaderPhoto);
		
		tvStatus = new TextView(context);
		tvStatus.setText(sStatus);
		tvStatus.setTypeface(_tf);
		tvStatus.setPadding(5, 0, 0, 0);
		tvStatus.setTextAppearance(context, R.style.pictureElementsInCrow);
		//add status element
		ll.addView(flForStatusText);
		
		llWrapPhotoAndDisc = new LinearLayout(context);
		llWrapPhotoAndDisc.setPadding(5, 0, 5, 5);
		llWrapPhotoAndDisc.setOrientation(LinearLayout.VERTICAL);
		if (_mark) {
			llWrapPhotoAndDisc.setBackgroundResource(R.color.green);
		}else{
			llWrapPhotoAndDisc.setBackgroundResource(R.color.gray_for_topheader);
		}
		
		
		flForStatusText.addView(tvStatus);
		
//		wvPhoto = new WebView(context);
		LayoutParams lpForFramePhoto = new LayoutParams(200, 200);
		lpForFramePhoto.setMargins(3, 0, 3, 3);
		
		flForPhoto  = new FrameLayout(context);
		flForPhoto.setLayoutParams(lpForFramePhoto);
		
		
		flForPhoto.setBackgroundDrawable(getDrawableFromUrl(sContentURL));
		
		// add photo element
		llWrapPhotoAndDisc.addView(flForPhoto);
		
		FrameLayout flForDescription = new FrameLayout(context);
		LayoutParams lpForFrameDessc = new LayoutParams(LayoutParams.FILL_PARENT, 80);
		flForDescription.setLayoutParams(lpForFrameDessc);
		flForDescription.setBackgroundResource(R.color.white);
		
		tvDescript = new TextView(context);
		tvDescript.setText(sTextDescript);
		tvDescript.setTextColor(Color.BLACK);
		tvDescript.setPadding(3, 3, 3, 3);
		tvDescript.setGravity(Gravity.LEFT);
		tvDescript.setTypeface(_tf);
		// add description element
		flForDescription.addView(tvDescript);
		
		llWrapPhotoAndDisc.addView(flForDescription);
		
		ll.addView(llWrapPhotoAndDisc);
		
		super.addView(ll);
	
	}
	
	/**
     * Pass in an image url to get a drawable object
     * 
     * @return a drawable object
     */
    private static Drawable getDrawableFromUrl(final String url) {
        String filename = url;
        filename = filename.replace("/", "+");
        filename = filename.replace(":", "+");
        filename = filename.replace("~", "s");
        final File file = new File(Environment.getExternalStorageDirectory()
                + "/havenappandroid/" + File.separator + filename);
        boolean exists = file.exists();
        if (!exists) {
            try {
                URL myFileUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                final Bitmap result = BitmapFactory.decodeStream(is);
                is.close();
                new Thread() {
                    public void run() {
                    	
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        result.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                        try {
                        	
                            if (file.createNewFile()){
                                //
                            }
                            else{
                                //
                            }

                            FileOutputStream fo;
                            fo = new FileOutputStream(file);
                            fo.write(bytes.toByteArray());
                            fo.flush();
                            fo.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("_da_PhotoObject.class : ", e.getMessage());
                        }
                    }
                }.start();
                
                BitmapDrawable returnResult = new BitmapDrawable(result);
                return returnResult;
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return new BitmapDrawable(BitmapFactory.decodeFile(file.toString()));
        }
    }
	
	public String getStatusString () {
		return tvStatus.getText().toString();
	}
	
	public LinearLayout getFrameForColorSelected(){
		return llWrapPhotoAndDisc;
	}
	
	

}
