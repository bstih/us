package hr.startup.untoldstories_android;

import android.app.Activity;
import android.content.Intent;
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
						StoryActivity.class);
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
