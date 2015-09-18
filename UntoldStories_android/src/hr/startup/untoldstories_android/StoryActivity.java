package hr.startup.untoldstories_android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoryActivity extends Activity {

	String prefix = "story_1_";
	String audioSufix = "_a";
	String imageSufix = "_i";
	String textSufix = "_t";
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
	MediaPlayer mPlayer;
	float textX = 0;
	float textY = 0;
	float textW = 0;
	float textH = 0;
	DisplayMetrics displayMetrics;
	int height;// displayMetrics.density;
	int width;
	TextView test;
	int audioPosition = 0;
	boolean mute = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
		
		// Initialization of variables
		displayMetrics = this.getResources().getDisplayMetrics();
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;
		storyText = (TextView) findViewById(R.id.storyText);
		storyImage = (ImageView) findViewById(R.id.storyImage);
		iconClose = (ImageView) findViewById(R.id.closeIcon);
		iconSound = (ImageView) findViewById(R.id.soundIcon);
		iconBack = (ImageView) findViewById(R.id.backIcon);
		test = (TextView) findViewById(R.id.test);
		slideNum = 1;

		//set icon positions
		iconClose.requestLayout();
		RelativeLayout.LayoutParams paramsClose = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		RelativeLayout.LayoutParams paramsBack = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams paramsSound = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsClose.leftMargin = (int) (height/20); //Your X coordinate
		paramsClose.topMargin = (int) (height*(17/20.0f)); //Your Y coordinate
		iconClose.setLayoutParams(paramsClose);
		paramsSound.leftMargin = (int) (height/5); //Your X coordinate
		paramsSound.topMargin = (int) (height*(17/20.0f)); //Your Y coordinate
		iconSound.setLayoutParams(paramsSound);
		paramsBack.leftMargin = (int) (height/20); //Your X coordinate
		paramsBack.topMargin = (int) (height/20); //Your Y coordinate
		iconBack.setLayoutParams(paramsBack);
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
		
		iconBack.setVisibility(View.INVISIBLE);
		
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
		if (mute){
			mute = false;
			iconSound.setImageResource(R.drawable.icons_sound_on);
			mPlayer.setVolume(1,1);
		}else {
			mute = true;
			iconSound.setImageResource(R.drawable.icons_sound_off);		
			mPlayer.setVolume(0,0);
		}
	}
	void changeSlide() {
		
		Resources resources = getResources();
		test.setText(String.valueOf(slideNum));
		imageName = prefix + String.valueOf(slideNum) + imageSufix;
		audioName = prefix + String.valueOf(slideNum) + audioSufix;
		textName = prefix + String.valueOf(slideNum) + textSufix;
		storyText.setText("");
		storyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);	
		
		// Recycle bitmap immediately to save memory
		if (storyImage.getDrawable() != null)
			((BitmapDrawable)storyImage.getDrawable()).getBitmap().recycle();
		storyImage.setImageBitmap(null);
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
	        byte[] b = new byte[in_s.available()];
	        in_s.read(b);
	        storyText.setText(txt);
			setTextPosition();
			textX=textX-textW/2;
			textY=textY-textH/2;
			textH =  textH / 1200 * height;
			textW =  textW / 1600 * width;
			textX = textX / 1600 * width;
			textY = textY / 1200 * height;
			
			RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsText.leftMargin = (int) textX; //Your X coordinate
			paramsText.topMargin = (int) textY; //Your Y coordinate
			storyText.setLayoutParams(paramsText);
			// for api11+
			//storyText.setX(textX);
			//storyText.setY(textY);
			storyText.setWidth((int) textW);
			storyText.setHeight((int) textH);

		} catch (Exception e) {
			e.printStackTrace();
		}
		storyImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
				getResources().getIdentifier(imageName,"drawable", this.getPackageName()), width, height));
		storyImage.setOnClickListener(new ImageView.OnClickListener() {

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
		mPlayer = MediaPlayer.create(StoryActivity.this, getResources()
				.getIdentifier(audioName, "raw", this.getPackageName()));
		if (mute){
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
		iconBack.bringToFront();
		
		//back button is invisible on first slide
		if (slideNum<2) 
			iconBack.setVisibility(View.INVISIBLE);
		else
			iconBack.setVisibility(View.VISIBLE);
		iconClose.bringToFront();
		iconSound.bringToFront();
		mPlayer.start();
		audioPlaying = true;
		
	}

	void backToMenu() {
		if (audioPlaying == true){
			mPlayer.release();
			audioPlaying = false;
		}
		StoryActivity.this.finish();

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
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void setTextPosition() {
		switch (slideNum) {
		case 2:

			textX = 560;
			textY = 260;
			textW = 730;
			textH = 235;
			break;
		case 3:
			textX = 560;
			textY = 260;
			textW = 730;
			textH = 235;
			;
		case 4:
			textX = 560;
			textY = 260;
			textW = 730;
			textH = 235;
			break;
		case 6:
			textX = 800;
			textY = 246;
			textW = 1060;
			textH = 296;
			break;
		case 7:
			textX = 800;
			textY = 246;
			textW = 1060;
			textH = 296;
			break;
		case 8:
			textX = 800;
			textY = 246;
			textW = 1060;
			textH = 296;
			break;
		case 10:
			textX = 875;
			textY = 362;
			textW = 698;
			textH = 377;
			break;

		case 11:
			textX = 875;
			textY = 362;
			textW = 698;
			textH = 377;
			break;
		case 12:
			textX = 875;
			textY = 362;
			textW = 698;
			textH = 377;
			break;
		case 13:
			textX = 875;
			textY = 362;
			textW = 698;
			textH = 377;
			break;
		case 15:
			;
			textX = 1123;
			textY = 339;
			textW = 660;
			textH = 416;
			break;
		case 16:
			textX = 1123;
			textY = 339;
			textW = 660;
			textH = 416;
			break;
		case 17:
			textX = 1123;
			textY = 339;
			textW = 660;
			textH = 416;
			break;
		case 19:
			textX = 783;
			textY = 251;
			textW = 1015;
			textH = 297;
			break;
		case 20:
			textX = 783;
			textY = 251;
			textW = 1015;
			textH = 297;
			break;
		case 22:
			textX = 345;
			textY = 553;
			textW = 390;
			textH = 802;
			break;
		case 23:
			textX = 345;
			textY = 553;
			textW = 390;
			textH = 802;
			break;
		case 24:
			textX = 345;
			textY = 553;
			textW = 390;
			textH = 802;
			break;
		case 25:
			textX = 811;
			textY = 274;
			textW = 962;
			textH = 320;
			break;
		case 26:
			textX = 811;
			textY = 274;
			textW = 962;
			textH = 320;
			break;
		case 27:
			textX = 811;
			textY = 274;
			textW = 962;
			textH = 320;
			break;
		case 28:
			textX = 811;
			textY = 274;
			textW = 962;
			textH = 320;
			break;
		case 29:
			textX = 330;
			textY = 330;
			textW = 455;
			textH = 447;
			break;
		case 30:
			textX = 330;
			textY = 330;
			textW = 455;
			textH = 447;
			break;
		case 31:
			textX = 330;
			textY = 330;
			textW = 455;
			textH = 447;
			break;
		case 32:
			textX = 330;
			textY = 330;
			textW = 455;
			textH = 447;
			break;

		default:
			textX = 0;
			textY = 0;
			textW = 0;
			textH = 0;
			break;
		}
	}
}
