package hr.startup.untoldstories_android;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Splash2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash2);
		Button cro = (Button) findViewById(R.id.croBtn);
		Button eng = (Button) findViewById(R.id.engBtn);
		eng.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Locale locale = new Locale("en");
				Locale.setDefault(locale);
				Configuration config = new Configuration();
				config.locale = locale;
				getBaseContext().getResources().updateConfiguration(config,
						getBaseContext().getResources().getDisplayMetrics());

				Intent mainIntent = new Intent(Splash2Activity.this,
						MenuActivity.class);
				Splash2Activity.this.startActivity(mainIntent);
				Splash2Activity.this.finish();
			}
		});
		cro.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Locale locale = new Locale("hr");
				Locale.setDefault(locale);
				Configuration config = new Configuration();
				config.locale = locale;
				getBaseContext().getResources().updateConfiguration(config,
						getBaseContext().getResources().getDisplayMetrics());

				Intent mainIntent = new Intent(Splash2Activity.this,
						MenuActivity.class);
				Splash2Activity.this.startActivity(mainIntent);
				Splash2Activity.this.finish();
			}
		});
	}
}
