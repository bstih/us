package hr.startup.untoldstories_android;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

//import android.widget.TextView;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		// TextView title = (TextView) findViewById(R.id.menuTitle);
		// Initialization of views
		ImageView thumb1 = (ImageView) findViewById(R.id.thumb1);
		ImageView thumb2 = (ImageView) findViewById(R.id.thumb2);
		ImageView thumb3 = (ImageView) findViewById(R.id.thumb3);
		ImageView settings = (ImageView) findViewById(R.id.about);
		final ImageView language = (ImageView) findViewById(R.id.language);

		SharedPreferences sp = getSharedPreferences("settings", 0);
		if (sp.getString("language", null).equals("en"))

			language.setImageDrawable(getResources().getDrawable(R.drawable.flag_eng));
		else
			language.setImageDrawable(getResources().getDrawable(R.drawable.flag_cro));
		// Defining thumbnails size
		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		float dpHeight = displayMetrics.heightPixels;// displayMetrics.density;
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
		language.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sp = getSharedPreferences("settings", 0);
				SharedPreferences.Editor editor = sp.edit();

				editor.commit();
				if (sp.getString("language", null).equals("en")) {
					editor.putString("language", "hr");
					language.setImageDrawable(getResources().getDrawable(R.drawable.flag_cro));
				} else {
					editor.putString("language", "en");
					language.setImageDrawable(getResources().getDrawable(R.drawable.flag_eng));
				}
				
				editor.commit();
			    Resources res = getApplicationContext().getResources();
			    // Change locale settings in the app.
			    DisplayMetrics dm = res.getDisplayMetrics();
			    android.content.res.Configuration conf = res.getConfiguration();
			    conf.locale = new Locale(sp.getString("language", null).toLowerCase());
			    res.updateConfiguration(conf, dm);				
			    finish();
			    startActivity(getIntent());
//				Locale locale = new Locale(sp.getString("language", null));
//				Locale.setDefault(locale);
//				Configuration config = new Configuration();
//				config.locale = locale;
//				getApplicationContext().getResources().updateConfiguration(config,
//						getApplicationContext().getResources().getDisplayMetrics());
			}
		});

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
