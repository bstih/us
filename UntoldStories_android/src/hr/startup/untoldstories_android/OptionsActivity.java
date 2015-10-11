package hr.startup.untoldstories_android;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		//init
		Button play = (Button) findViewById(R.id.play);
		Button playCustom = (Button) findViewById(R.id.playCustom);
		Button record = (Button) findViewById(R.id.record);
		
		//setting playCustom button
		if (!customAudioExists())
			playCustom.setEnabled(false);
		
		//setting play button
		play.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(OptionsActivity.this,
						StoryActivity.class);
				Bundle b = new Bundle();
				b.putInt("storyNum", 1);
				b.putBoolean("customAudio",false);
				mainIntent.putExtras(b);
				OptionsActivity.this.startActivity(mainIntent);
			}
		});
		
		//setting playCustom button
		playCustom.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(OptionsActivity.this,
						StoryActivity.class);
				Bundle b = new Bundle();
				b.putInt("storyNum", 1); 
				b.putBoolean("customAudio",true);
				mainIntent.putExtras(b);
				
				OptionsActivity.this.startActivity(mainIntent);
			}
		});
		//setting record button
		record.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(OptionsActivity.this,
						AudioRecorderActivity.class);
				Bundle b = new Bundle();
				b.putInt("storyNum", 1); //Your id
				mainIntent.putExtras(b);
				OptionsActivity.this.startActivity(mainIntent);
			}
		});
	}
	boolean customAudioExists(){
		String relativePath = getString(R.string.relativePath);
		String absolutePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + relativePath;
		String customAudioName;
		String textName;
		String prefix = getString(R.string.prefix);
		int slideNum;
		int numTxt=0;
		int numAudio=0;
		int storyNum = 1;
		String textSufix =getString(R.string.textSufix);
		
		for (slideNum=1;slideNum<=32;slideNum++) {			
			textName = prefix + String.valueOf(storyNum) + "_" + String.valueOf(slideNum) + textSufix;
			int file = getResources().getIdentifier(textName, "raw", this.getPackageName());
			if (file != 0)  numTxt++;   
		}
		
		if (numTxt==0) return false;
		
		for (slideNum=1;slideNum<=32;slideNum++) {
			customAudioName = absolutePath + prefix + String.valueOf(storyNum)
					+ "_" + slideNum + "_ca.3gp";
			File file = new File(customAudioName);
			if(file.exists())   numAudio++;   
		}
		if (numAudio == numTxt) return true;
		return false;
	}
	
	@Override
	public void onBackPressed() {
		OptionsActivity.this.finish();
	}
}
