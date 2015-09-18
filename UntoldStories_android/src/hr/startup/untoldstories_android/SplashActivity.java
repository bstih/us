package hr.startup.untoldstories_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	/** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		        // Do something after 5s = 5000ms
                Intent mainIntent = new Intent(SplashActivity.this,MenuActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish(); 
		    }
		}, SPLASH_DISPLAY_LENGTH);
	}
}
