package hr.startup.untoldstories_android;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SetLanguageActivity extends Activity {

	String language = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences("settings", 0);
		language = settings.getString("language", null);
		
		//if language is set, proceed to Splash screen
		if (language != null) openSplash();
		
		setContentView(R.layout.activity_splash2);
		Button cro = (Button) findViewById(R.id.croBtn);
		Button eng = (Button) findViewById(R.id.engBtn);
		eng.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("settings", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("language","en");
				editor.commit();
				openSplash();

				
			}
		});
		cro.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("settings", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("language","hr");
				editor.commit();
				openSplash();
			}
		});
	}
	
	void openSplash() {
	    Resources res = getApplicationContext().getResources();
	    // Change locale settings in the app.
	    DisplayMetrics dm = res.getDisplayMetrics();
	    android.content.res.Configuration conf = res.getConfiguration();
	    conf.locale = new Locale(language.toLowerCase());
	    res.updateConfiguration(conf, dm);		
		Intent mainIntent = new Intent(SetLanguageActivity.this,
				SplashActivity.class);
		SetLanguageActivity.this.startActivity(mainIntent);
		SetLanguageActivity.this.finish();
	}
}
