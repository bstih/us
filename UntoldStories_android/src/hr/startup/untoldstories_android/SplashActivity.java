package hr.startup.untoldstories_android;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;

public class SplashActivity extends Activity {

	/** Duration of wait **/
	private final int SPLASH_DISPLAY_LENGTH = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		int glowDrawableId = getResources().getIdentifier("overscroll_glow", "drawable", "android");
        int edgeDrawableId = getResources().getIdentifier("overscroll_edge", "drawable", "android");
        Drawable androidGlow = ContextCompat.getDrawable(this, glowDrawableId);
        Drawable androidEdge = ContextCompat.getDrawable(this, edgeDrawableId);
        androidGlow.setAlpha(0);        
        androidGlow.setVisible(false, false);
       androidEdge.setVisible(false, false);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after 5s = 5000ms
				Intent mainIntent = new Intent(SplashActivity.this,
						MenuActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("STATUS", "Activity Stopped");

		setLanguage();
	}

	private void setLanguage() {
		SharedPreferences sp = getSharedPreferences("settings", 0);
		Resources res = getBaseContext().getResources();
		// Change locale settings in the app.
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(sp.getString("language", null).toLowerCase());
		res.updateConfiguration(conf, dm);
		Log.v("LANGUAGE_CHANGE",
				getResources().getConfiguration().locale.getLanguage()
						+ " lang");
	}
}
