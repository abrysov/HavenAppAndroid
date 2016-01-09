/** 
 * Created by abrysov at Mar 14, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.rosberry.android.havenappandroid.HavenAppAndroidActivity;
import com.rosberry.android.havenappandroid.R;
import com.rosberry.android.havenappandroid.custom.TopHeader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author abrysov
 *
 */
public class Details extends LoginForm implements OnClickListener{
	
	/* (non-Javadoc)
	 * @see com.rosberry.android.havenappandroid.activity.LoginForm#onCreate(android.os.Bundle)
	 */
	
	public final static String STRAAT = "Straat";
	public final static String DATUM = "Datum";
	public final static String STATUS = "Status";
	public final static String GEBIET = "Gebiet";
	public final static String BESTEK = "Bestek";
	public final static String MELDER = "Melder";
	public final static String ACCEPTABEL = "Acceptabel";
	public final static String GECONSTATEER = "Geconstatier";
	private static final String TAG = "_ha_Details.class";
	
	private TextView tvStraatDATA = null;
	private TextView tvDatumDATA = null;
	private TextView tvStatusDATA = null;
	private TextView tvGebietDATA = null;
	private TextView tvBestekDATA = null;
	private TextView tvMelderDATA = null;
	private TextView tvAcceptabelDATA = null;
	private TextView tvGeconstatierDATA = null;
	private TextView tvCommentsOpmDATA = null;

	private LinearLayout llHeader = null;
	private FrameLayout flAddressForOpen = null;
	
	private FrameLayout flStatusButton = null;
	
	private TopHeader th = null;
	
	private String sPhotoURL = null;
	private ImageView ivPhoto = null;
	
	private static Drawable dPhoto = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		TextView tvStraat = (TextView)findViewById(R.id.details_tv_straat);
		tvStraat.setTypeface(getFont());
		TextView tvDatum = (TextView)findViewById(R.id.details_tv_datum);
		tvDatum.setTypeface(getFont());
		TextView tvStatus = (TextView)findViewById(R.id.details_tv_status);
		tvStatus.setTypeface(getFont());
		TextView tvGebie = (TextView)findViewById(R.id.details_tv_gebiet);
		tvGebie.setTypeface(getFont());
		TextView tvBestek = (TextView)findViewById(R.id.details_tv_bestek);
		tvBestek.setTypeface(getFont());
		TextView tvMelder = (TextView)findViewById(R.id.details_tv_amelder);
		tvMelder.setTypeface(getFont());
		TextView tvAcceptabel = (TextView)findViewById(R.id.details_tv_acceptabel);
		tvAcceptabel.setTypeface(getFont());
		TextView tvGeconstatier = (TextView)findViewById(R.id.details_tv_geconstatier);
		tvGeconstatier.setTypeface(getFont());
		TextView tvCommentsOpm = (TextView)findViewById(R.id.details_tv_opmerkingen);
		tvCommentsOpm.setTypeface(getFont());
		
		sPhotoURL = getIntent().getExtras().getString("PhotoURL");
		
		ivPhoto = (ImageView)findViewById(R.id.details_iv_photo);
		ivPhoto.setOnClickListener(this);
		try {
			dPhoto = getDrawableFromUrl(sPhotoURL);
			if (dPhoto != null){
				ivPhoto.setBackgroundDrawable(dPhoto);
			}
		} catch (Exception e) {
			Log.e(TAG, "err: " + e.getMessage());
		}
		

		llHeader = (LinearLayout)findViewById(R.id.details_ll_customheader);
//		TopHeader tp = new TopHeader(getApplicationContext(), "Melding details", View.VISIBLE);
		
		String sAddress = getIntent().getExtras().getString("Staat");
		
		if (LoginForm.getSkin()){
			th = new TopHeader(getApplicationContext(), sAddress, View.VISIBLE,
					getResources().getConfiguration().orientation);
		}else{
			th = new TopHeader(getApplicationContext(), sAddress, View.INVISIBLE,
					getResources().getConfiguration().orientation);
		}
		th.getTextHeader().setTypeface(getFont());
		th.getTextSubHeader().setTypeface(getFont());
		llHeader.addView(th);
		
		flStatusButton = (FrameLayout)findViewById(R.id.details_fl_status_button);
		//check for able set changes
//		checkStatusChanging();
		
//		inicializingTextViews();
		
		flAddressForOpen = (FrameLayout)findViewById(R.id.details_fl_strings1);
		flAddressForOpen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent iMap = new Intent(getApplicationContext(), Map2.class);
				iMap.putExtra("LAT", LoginForm.getDataForPost().getLat());
				iMap.putExtra("LNG", LoginForm.getDataForPost().getLong());
				startActivityForResult(iMap, HavenAppAndroidActivity.FORM_MAP);

//				Intent intentMap = new Intent(android.content.Intent.ACTION_VIEW, 
//						Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
//						startActivity(intentMap);
				
//				String uri = "geo:"+ "20.5666" + "," + "45.345";
//				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
				
			}
		});
		
		try {
			tvStraatDATA = (TextView)findViewById(R.id.details_tv_straat_body);
			tvStraatDATA.setText(getIntent().getExtras().getString("Staat"));
			tvStraatDATA.setTypeface(getFont());
			
			tvDatumDATA = (TextView)findViewById(R.id.details_tv_datum_body);
			tvDatumDATA.setText(getIntent().getExtras().getString("Datum"));
			tvDatumDATA.setTypeface(getFont());
			
			tvStatusDATA = (TextView)findViewById(R.id.details_tv_status_body);
			tvStatusDATA.setTypeface(getFont());

			
			if(tvStatusDATA.getText().toString() == "OPEN"){
				tvStatusDATA.setTextColor(Color.RED);
			}else if(tvStatusDATA.getText().toString() == "IN BEHANDELING"){
				tvStatusDATA.setTextColor(Color.rgb(255, 106, 8));
			}else{
				tvStatusDATA.setTextColor(Color.BLACK);
			}

//			if (getIntent().getExtras().getString("Status"))
			tvStatusDATA.setText(getIntent().getExtras().getString("Status"));

			switch (getIntent().getExtras().getInt("ColorIndex", -1)) {
			case 0:
				tvStatusDATA.setTextColor(Color.rgb(255, 106, 8));
				break;
			case 1:
				tvStatusDATA.setTextColor(Color.RED);
				break;
			case 2:
				tvStatusDATA.setTextColor(Color.rgb(134, 219, 8));
				break;			
			default:
				tvStatusDATA.setTextColor(Color.BLACK);
				break;
			}		
			
			//
			tvGebietDATA = (TextView)findViewById(R.id.details_tv_gebiet_body);
			tvGebietDATA.setText(getIntent().getExtras().getString("Gebiet"));
			tvGebietDATA.setTypeface(getFont());
			//
			tvBestekDATA = (TextView)findViewById(R.id.details_tv_bestek_body);
			tvBestekDATA.setText(getIntent().getExtras().getString("Bestek"));
			tvBestekDATA.setTypeface(getFont());
			//Melder
			tvMelderDATA = (TextView)findViewById(R.id.details_tv_amelder_body);
			tvMelderDATA.setText(getIntent().getExtras().getString("Melder"));
			tvMelderDATA.setTypeface(getFont());
			//Acceptable
			tvAcceptabelDATA = (TextView)findViewById(R.id.details_tv_acceptabel_body);
			tvAcceptabelDATA.setText(getIntent().getExtras().getString("Acceptable"));
			tvAcceptabelDATA.setTypeface(getFont());
			//Geconstatier
			tvGeconstatierDATA = (TextView)findViewById(R.id.details_tv_geconstatier_body);
			tvGeconstatierDATA.setTypeface(getFont());
			tvGeconstatierDATA.setText(getIntent().getExtras().getString("Geconstatier"));
			
			tvCommentsOpmDATA = (TextView)findViewById(R.id.details_tv_opmerkingen_body);
			tvCommentsOpmDATA.setText(getIntent().getExtras().getString("CommentsOpm"));
			tvCommentsOpmDATA.setTypeface(getFont());			
		} catch (Exception e) {
			Log.e(TAG, "err:" + e.getMessage());
		}

		
		
	}

	public static Drawable getPhoto () {
		return dPhoto;
	}
	
    private static Drawable getDrawableFromUrl(final String url) {
        String filename = url;
        filename = filename.replace("/", "+");
        filename = filename.replace(":", "+");
        filename = filename.replace("~", "s");
        final File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + filename);
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
                        try {
                        	if (result != null){
                        		result.compress(Bitmap.CompressFormat.JPEG, 100, bytes);	
                        	}
                        	
						} catch (Exception e) {
							Log.i(TAG, "compress err:" + e.getMessage());
						}
                        
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

	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.details_fl_status_button:
			break;
			
		case R.id.details_iv_photo:
			
			startActivity(new Intent(getApplicationContext(), EnlargePhotoForm.class));
			
			break;

		default:
			break;
		}
	}


}
