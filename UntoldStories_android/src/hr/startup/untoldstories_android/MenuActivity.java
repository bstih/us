package hr.startup.untoldstories_android;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
//import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//import android.widget.TextView;

public class MenuActivity extends Activity {

	ImageView thumb1;
	ImageView thumb2;
	ImageView thumb3;
	BounceScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		// TextView title = (TextView) findViewById(R.id.menuTitle);
		// Initialization of views
		thumb1 = (ImageView) findViewById(R.id.thumb1);
		thumb2 = (ImageView) findViewById(R.id.thumb2);
		thumb3 = (ImageView) findViewById(R.id.thumb3);

		scrollView = (BounceScrollView) findViewById(R.id.bounceScroll);

		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		// scrollView.setPadding((int)(0.24f * displayMetrics.widthPixels), 0,
		// 0, 0);

		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;
		ImageView settings = (ImageView) findViewById(R.id.settings);
		ImageView cro = (ImageView) findViewById(R.id.cro);
		ImageView eng = (ImageView) findViewById(R.id.eng);
		ImageView srb = (ImageView) findViewById(R.id.srb);
		srb.requestLayout();
		eng.requestLayout();
		cro.requestLayout();
		settings.requestLayout();

		int iconSize = (int) (width * 0.0625f);
		RelativeLayout.LayoutParams paramsSettings =  new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams paramsCro =  new RelativeLayout.LayoutParams(				
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams paramsEng =  new RelativeLayout.LayoutParams(				
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams paramsSrb =  new RelativeLayout.LayoutParams(				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		settings.setImageBitmap(decodeSampledBitmapFromResource(
				getResources(),
				getResources().getIdentifier("icons_settings", "drawable",
						this.getPackageName()), iconSize,
						iconSize));
		cro.setImageBitmap(decodeSampledBitmapFromResource(
				getResources(),
				getResources().getIdentifier("flag_cro", "drawable",
						this.getPackageName()), iconSize,
						iconSize));
		eng.setImageBitmap(decodeSampledBitmapFromResource(
				getResources(),
				getResources().getIdentifier("flag_eng", "drawable",
						this.getPackageName()), iconSize,
						iconSize));
		srb.setImageBitmap(decodeSampledBitmapFromResource(
				getResources(),
				getResources().getIdentifier("flag_srb", "drawable",
						this.getPackageName()), iconSize,
						iconSize));
		int toolbarItemsNo = 4;

		paramsSettings.topMargin = 0; // Y coordinate - height
		settings.setLayoutParams(paramsSettings);
		int xfdsf = (int) (2 * height / ((float) toolbarItemsNo));
		paramsCro.topMargin = (int) (height / ((float) toolbarItemsNo)); // Y
																			// coordinate
																			// -
																			// height
		cro.setLayoutParams(paramsCro);
		paramsEng.topMargin = (int) (2 * height / ((float) toolbarItemsNo)); // Y
																				// coordinate
																				// -
																				// height
		eng.setLayoutParams(paramsEng);
		paramsSrb.topMargin = (int) (3 * height / ((float) toolbarItemsNo)); // Y
																				// coordinate
																				// -
																				// height
		
		srb.setLayoutParams(paramsSrb);
		
		int leftPadding = (int)(0.0125f * displayMetrics.widthPixels);
		int topPadding = (int)(0.02f * displayMetrics.widthPixels);
		int rightPadding = (int)(0.15f * displayMetrics.widthPixels);
		int bottomPadding = (int)(0.01f * displayMetrics.widthPixels);
		srb.setPadding(leftPadding,0,rightPadding, 0);
		eng.setPadding(leftPadding,0,rightPadding, 0);
		cro.setPadding(leftPadding,0,rightPadding, 0);
		settings.setPadding(leftPadding,0,rightPadding, 0);
		
		initThumbs();
		thumb1.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(MenuActivity.this,
						OptionsActivity.class);
				MenuActivity.this.startActivity(mainIntent);
			}
		});

		settings.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(MenuActivity.this,
						AboutActivity.class);
				MenuActivity.this.startActivity(mainIntent);
			}
		});
		cro.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sp = getSharedPreferences("settings", 0);
				
if(!sp.getString("language", null).toLowerCase().equals("hr")){
	SharedPreferences.Editor editor = sp.edit();
					editor.putString("language", "hr");

				editor.commit();
				setLanguage("hr");
				finish();
				startActivity(getIntent());
}
			}
		});		eng.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sp = getSharedPreferences("settings", 0);
				if(!sp.getString("language", null).toLowerCase().equals("en")){
				SharedPreferences.Editor editor = sp.edit();
				

					editor.putString("language", "en");

				editor.commit();
				setLanguage("en");
				finish();
				startActivity(getIntent());
				}
			}
		});

				

		}


	private void initThumbs() {

		// Defining thumbnails size
		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		float dpHeight = displayMetrics.heightPixels;// displayMetrics.density;
		float dpWidth = displayMetrics.widthPixels;// displayMetrics.density;
		if (dpHeight > dpWidth)
			dpHeight = dpWidth;
		int thumbWidth = (int) (dpHeight * 0.7 * 0.8 * 1.25);
		int thumbHeight = (int) (dpHeight * 0.7 * 0.8);

		// Setting thumbnails sizes
		thumb1.requestLayout();
		thumb2.requestLayout();
		thumb3.requestLayout();
		thumb1.getLayoutParams().width = thumbWidth;
		thumb2.getLayoutParams().width = thumbWidth;
		thumb3.getLayoutParams().width = thumbWidth;
		thumb1.getLayoutParams().height = thumbHeight;
		thumb2.getLayoutParams().height = thumbHeight;
		thumb3.getLayoutParams().height = thumbHeight;

		// Setting thumbnail drawables with appropriate image sizes (memory
		// saving)
		thumb1.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
				R.drawable.thumb_1_1, thumbWidth, thumbHeight));
		thumb2.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
				R.drawable.thumb_1_2, thumbWidth, thumbHeight));
		thumb3.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
				R.drawable.thumb_1_3, thumbWidth, thumbHeight));
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferences sp = getSharedPreferences("settings", 0);

		setLanguage((sp.getString("language", null).toLowerCase()));
	}

	private void setLanguage(String language) {

		Resources res = getBaseContext().getResources();
		// Change locale settings in the app.
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(language);
		res.updateConfiguration(conf, dm);
		Log.v("LANGUAGE_CHANGE",
				getResources().getConfiguration().locale.getLanguage()
						+ " lang");
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Image size optimization functions
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
