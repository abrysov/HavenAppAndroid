/** 
 * Created by abrysov at Apr 5, 2012
 */
package com.rosberry.android.havenappandroid.activity;

import com.rosberry.android.havenappandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * @author abrysov
 *
 */
public class EnlargePhotoForm extends Activity {
	
	private ImageView ivPhoto = null;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enlarge_photo);
		
		ivPhoto = (ImageView)findViewById(R.id.enlargephoto_iv_photo);
		ivPhoto.setImageDrawable(Details.getPhoto());
		ivPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
	}

}
