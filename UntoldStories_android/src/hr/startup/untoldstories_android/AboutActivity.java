package hr.startup.untoldstories_android;

import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		findViewById(R.id.relativeLayout).setOnClickListener(
				new LinearLayout.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AboutActivity.this.finish();

					}
				});

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
