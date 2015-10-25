package hr.startup.untoldstories_android;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.os.Vibrator;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoryActivity extends Activity {

	String prefix;
	String audioSufix;
	String imageSufix;
	String textSufix;
	String imageName;
	String audioName;
	String textName;
	boolean audioPlaying = false;
	byte[] b;
	int slideNum;
	TextView storyText;
	ImageView storyImage;
	ImageView iconClose;
	ImageView iconSound;
	ImageView iconBack;
	ImageView iconFwd;
	ImageView textBackground;
	MediaPlayer mPlayer;
	DisplayMetrics displayMetrics;
	int height;// displayMetrics.density;
	int width;
	TextView test;
	int audioPosition = 0;
	int soundMode = 0;
	boolean customAudio= false;
	boolean textFound=false;	
	private String relativePath;
	private String absolutePath;
	private String customAudioSufix = "_ca.3gp";
	int storyNum = 1;
	private Vibrator v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
		prefix = getString(R.string.prefix);
		audioSufix = getString(R.string.audioSufix);
		imageSufix = getString(R.string.imageSufix);
		textSufix = getString(R.string.textSufix);
		relativePath =  getString(R.string.relativePath);
		
		// get info from main activity (which story is selected)
		Bundle b = getIntent().getExtras();
		//storyNum = b.getInt("storyNum");
		customAudio = b.getBoolean("customAudio");
		
		// Initialization of variables
		displayMetrics = this.getResources().getDisplayMetrics();
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;
		storyText = (TextView) findViewById(R.id.storyText);
		storyImage = (ImageView) findViewById(R.id.storyImage);
		iconClose = (ImageView) findViewById(R.id.closeIcon);
		iconSound = (ImageView) findViewById(R.id.soundIcon);
		iconBack = (ImageView) findViewById(R.id.backIcon);
		iconFwd = (ImageView) findViewById(R.id.fwdIcon);
		textBackground = (ImageView) findViewById(R.id.textBackground);
		test = (TextView) findViewById(R.id.test);
		slideNum = 1;
		absolutePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + relativePath;
		 v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
		 
		//set icon positions
		RelativeLayout.LayoutParams paramsClose = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		RelativeLayout.LayoutParams paramsBack = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams paramsSound = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams paramsFwd = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsClose.leftMargin = (int) (height/20); // X coordinate
		paramsClose.topMargin = (int) (height*(17/20.0f)); // Y coordinate
		iconClose.setLayoutParams(paramsClose);
		paramsSound.leftMargin = (int) (height/5); // X coordinate
		paramsSound.topMargin = (int) (height*(17/20.0f)); // Y coordinate
		iconSound.setLayoutParams(paramsSound);
		paramsBack.leftMargin = (int) (height/20); // X coordinate
		paramsBack.topMargin = (int) (height/20); // Y coordinate
		iconBack.setLayoutParams(paramsBack);
		paramsFwd.leftMargin = (int) (width-3*height/20.0f); // X coordinate
		paramsFwd.topMargin = (int) (height/20); // Y coordinate
		iconFwd.setLayoutParams(paramsFwd);
		paramsText.leftMargin = (int) (0.2f*width); // X coordinate - width of rectangle is 60% of screen width
		paramsText.topMargin = (int) (0.8f*height); // Y coordinate - height of rectangle is 20% of screen height
		textBackground.setLayoutParams(paramsText); 
		
		
		// for api11+
//		iconClose.setX(height/20);
//		iconClose.setY(height*(9/10.0f));
//		iconSound.requestLayout();
//		iconSound.setX(height/5);
//		iconSound.setY(height*(9/10.0f));
//		iconBack.requestLayout();
//		iconBack.setX(height/20);		
//		iconBack.setY(height/20);
		
		//set icon sizes
		iconClose.getLayoutParams().width=(int)height/10;
		iconClose.getLayoutParams().height=(int)height/10;
		iconSound.getLayoutParams().width=(int)height/10;
		iconSound.getLayoutParams().height=(int)height/10;
		iconBack.getLayoutParams().width=(int)height/10;
		iconBack.getLayoutParams().height=(int)height/10;
		iconFwd.getLayoutParams().width=(int)height/10;
		iconFwd.getLayoutParams().height=(int)height/10;
		textBackground.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
				getResources().getIdentifier("text_background","drawable", this.getPackageName()), (int) (width*0.6f), (int)(height*0.2f)));
//		textBackground.setScaleType(ScaleType.FIT_XY);
//		textBackground.getLayoutParams().width=(int) (width*0.6f);
//		textBackground.getLayoutParams().height=(int)(height*0.2f);
		
		iconBack.setVisibility(View.INVISIBLE);
		textBackground.setVisibility(View.INVISIBLE);
		
		// onClick listeners for close and back buttons
		iconClose.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backToMenu();
			}
		});
		iconBack.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				previousSlide();
			}
		});
		
		iconSound.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggleSound();
			}
		});
		
		storyImage.setOnTouchListener(new ImageView.OnTouchListener() {        

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	            	v.vibrate(3000);
	                return true; // if you want to handle the touch event
	            case MotionEvent.ACTION_UP:
	            	v.cancel();
	            	return true; // if you want to handle the touch event
	        }
				
				return false;
			}
		});

		// goes to slide for slideNum = 1
		changeSlide();

	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (audioPlaying == true){	
			mPlayer.seekTo(audioPosition);
			mPlayer.start();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub		
		super.onPause();
		if (audioPlaying == true){			
			mPlayer.pause();
			audioPosition=mPlayer.getCurrentPosition();
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (audioPlaying == true){			
			mPlayer.pause();
			audioPosition=mPlayer.getCurrentPosition();
		}
	}

	void toggleSound() {
		if (soundMode==1){
			soundMode=2;
			iconSound.setImageResource(R.drawable.icons_sound_on);
			mPlayer.setVolume(1,1);
			textBackground.setVisibility(View.INVISIBLE);
			storyText.setVisibility(View.INVISIBLE);

		}else if (soundMode==2) {
			soundMode=0;
			iconSound.setImageResource(R.drawable.icons_both_on);	
			if (textFound) {
			textBackground.setVisibility(View.VISIBLE);
			storyText.setVisibility(View.VISIBLE);
			}
			
		}else {
			soundMode=1;
			iconSound.setImageResource(R.drawable.icons_text_on);		
			mPlayer.setVolume(0,0);			
		}
	}
	void changeSlide() {
		boolean customAudioFound = false; 
		boolean nextImageFound = false;
		Resources resources = getResources();
		test.setText(String.valueOf(slideNum));
		imageName = prefix + String.valueOf(storyNum) + "_" + String.valueOf(slideNum) + imageSufix;
		int imageFile = getResources().getIdentifier(imageName, "drawable", this.getPackageName());
		if (imageFile != 0)  nextImageFound = true;
		if (customAudio) {
			audioName = absolutePath + prefix +  String.valueOf(storyNum) + "_"  +String.valueOf(slideNum) + customAudioSufix;
			File file = new File(audioName);
			customAudioFound = true;
			if(!file.exists())      {
				audioName = prefix + String.valueOf(storyNum) + "_" + String.valueOf(slideNum) + audioSufix;
				customAudioFound = false;}
		}
		else audioName = prefix + String.valueOf(storyNum) + "_" + String.valueOf(slideNum) + audioSufix;
		textName = prefix + String.valueOf(storyNum) + "_" + String.valueOf(slideNum) + textSufix;
		storyText.setText("");
		storyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);	
		
		// Recycle bitmap immediately to save memory
		if (nextImageFound) {
			if (storyImage.getDrawable() != null)
				((BitmapDrawable)storyImage.getDrawable()).getBitmap().recycle();
			storyImage.setImageBitmap(null);
		}
		try {

			InputStream in_s = resources.openRawResource(resources
					.getIdentifier(textName, "raw", this.getPackageName()));
			InputStreamReader in_s_reader = new InputStreamReader(in_s, "UTF-8");
			BufferedReader buffreader = new BufferedReader(in_s_reader);
			StringBuilder total = new StringBuilder();
			String txt;
			while ((txt = buffreader.readLine()) != null) {
			    total.append(txt);
			}
			txt=total.toString();
	        storyText.setText(txt);

			
			RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsText.leftMargin = (int) (0.2f*width); //Your X coordinate
			paramsText.topMargin = (int) (0.8f*height); //Your Y coordinate

			storyText.setLayoutParams(paramsText);
			// for api11+
			//storyText.setX(textX);
			//storyText.setY(textY);
			storyText.setWidth((int) (width*0.6f));
			storyText.setHeight((int) (height*0.2f));
			textBackground.setVisibility(View.VISIBLE);
			storyText.setVisibility(View.VISIBLE);
			textFound=true;
			if (soundMode==2){
				textBackground.setVisibility(View.INVISIBLE);
				storyText.setVisibility(View.INVISIBLE);
			}
			

		} catch (Exception e) {
			textBackground.setVisibility(View.INVISIBLE);
			textFound=false;
			e.printStackTrace();
		}
		if (nextImageFound)
			storyImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
					getResources().getIdentifier(imageName,"drawable", this.getPackageName()), width, height));
		iconFwd.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextSlide();
			}
		});
		if (audioPlaying == true){
			mPlayer.release();
			audioPlaying = false;
		}
		if (customAudioFound){
			try {
				mPlayer = new MediaPlayer();
				mPlayer.setDataSource(audioName);
				mPlayer.prepare();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		else{
		mPlayer = MediaPlayer.create(StoryActivity.this, getResources()
				.getIdentifier(audioName, "raw", this.getPackageName()));

		}
		if (soundMode==1){
			mPlayer.setVolume(0,0);
		}else {
			mPlayer.setVolume(1,1);
		}
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				nextSlide();
			}
		});

		//back button is invisible on first slide
		if (slideNum<2) 
			iconBack.setVisibility(View.INVISIBLE);
		else
			iconBack.setVisibility(View.VISIBLE);

		mPlayer.start();
		audioPlaying = true;
		
	}

	void backToMenu() {
		if (slideNum < 32) {
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getString(R.string.btmTitle))
        .setMessage(getString(R.string.btmMsg))
        .setPositiveButton(getString(R.string.btmYes), new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
    		if (audioPlaying == true){
    			mPlayer.release();
    			audioPlaying = false;
    		}
    		StoryActivity.this.finish(); 
        }

    })
    .setNegativeButton(getString(R.string.btmNo), null)
    .show();}
		else{
			if (audioPlaying == true){
    			mPlayer.release();
    			audioPlaying = false;
    		}
			Intent mainIntent = new Intent(StoryActivity.this,
					RatingActivity.class);
			StoryActivity.this.startActivity(mainIntent);
    		StoryActivity.this.finish(); 
		}
			


	}

	void nextSlide() {
		if (slideNum < 32) {
			slideNum++;
			changeSlide();
		} else {
			backToMenu();
		}
	}
	
	@Override
	public void onBackPressed() {
	    backToMenu();
	}
	
	void previousSlide() {
			slideNum--;
			changeSlide();
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
	
	
}
