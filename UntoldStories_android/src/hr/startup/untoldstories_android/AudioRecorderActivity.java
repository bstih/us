package hr.startup.untoldstories_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class AudioRecorderActivity extends Activity {
	private static final String LOG_TAG = "AudioRecordTest";

	private MediaRecorder mRecorder = null;
	private Button playButton;
	private Button recordButton;
	private Button nextButton;
	private TextView textRecording;
	private String relativePath;
	private String absolutePath;
	private String customAudioName;
	private String prefix;
	private boolean audioPlaying = false;
	private boolean recording = false;
	private boolean currentAudioRecorded = false;
	String imageSufix;
	private boolean allAudioExists = false;
	String imageName;
	private String textSufix;
	private String customAudioSufix;
	private int slideNum = 0;
	private int storyNum = 0;
	private MediaPlayer mPlayer = null;
	ImageView storyImage;
	DisplayMetrics displayMetrics;
	int height;// displayMetrics.density;
	int width;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_audio_recorder);

		relativePath = getString(R.string.relativePath);
		prefix = getString(R.string.prefix);
		textSufix = getString(R.string.textSufix);
		customAudioSufix = getString(R.string.customAudioSufix);
		storyImage = (ImageView) findViewById(R.id.storyImage);
		// init graphical elements
		playButton = (Button) findViewById(R.id.playRecording);
		recordButton = (Button) findViewById(R.id.startRecording);
		nextButton = (Button) findViewById(R.id.nextRecording);
		textRecording = (TextView) findViewById(R.id.textRecording);
		imageSufix = getString(R.string.imageSufix);
		displayMetrics = this.getResources().getDisplayMetrics();
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;
		// get info from main activity (which story is selected)
		Bundle b = getIntent().getExtras();
		storyNum = b.getInt("storyNum");
		ImageView textBackground = (ImageView) findViewById(R.id.textBackground);
		textBackground.setImageBitmap(decodeSampledBitmapFromResource(
				getResources(),
				getResources().getIdentifier("text_background", "drawable",
						this.getPackageName()), (int) (width * 0.6f),
				(int) (height * 0.2f)));

		// set absolute directory path to folder on externalStorage
		absolutePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + relativePath;

		// initially disable all buttons
		playButton.setEnabled(false);
		recordButton.setEnabled(false);
		nextButton.setEnabled(false);

		// have the object build the directory structure, if needed.
		File dir = new File(absolutePath);
		dir.mkdirs();

		// setting listeners
		playButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (audioPlaying) {

					stopPlaying();

				} else {

					startPlaying();
				}

			}
		});

		recordButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (recording) {
					stopRecording();
				} else {
					startRecording();
				}

			}
		});

		nextButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (slideNum > 34)
					finalSlide();
				else
					nextSlide();

			}
		});

		// open first slide
		if (slideNum > 34)
			finalSlide();
		else
			nextSlide();

	}

	private void finalSlide() {
		nextButton.setEnabled(true);
		playButton.setEnabled(false);
		recordButton.setEnabled(false);

		nextButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AudioRecorderActivity.this.finish();

			}
		});
		nextButton.setText(getString(R.string.finishButton));
	}

	private void nextSlide() {

		String txt = null;
		boolean nextImageFound = false;

		currentAudioRecorded = false;
		// find and show next slide that contains text.
		do {
			slideNum++;

			try {

				String textName = prefix + String.valueOf(storyNum) + "_"
						+ String.valueOf(slideNum) + textSufix;
				Resources resources = getResources();
				int x = resources.getIdentifier(textName, "raw",
						this.getPackageName());
				InputStream in_s = resources.openRawResource(x);
				InputStreamReader in_s_reader = new InputStreamReader(in_s,
						"UTF-8");
				BufferedReader buffreader = new BufferedReader(in_s_reader);
				StringBuilder total = new StringBuilder();

				while ((txt = buffreader.readLine()) != null) {
					total.append(txt);
				}
				txt = total.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} while ((txt == null) && (slideNum < 34));
		customAudioName = absolutePath + prefix + String.valueOf(storyNum)
				+ "_" + slideNum + customAudioSufix;
		imageName = prefix + String.valueOf(storyNum) + "_"
				+ String.valueOf(slideNum) + imageSufix;
		int imageFile = getResources().getIdentifier(imageName, "drawable",
				this.getPackageName());
		if (imageFile != 0)
			nextImageFound = true;
		if (nextImageFound) {
			if (storyImage.getDrawable() != null)
				((BitmapDrawable) storyImage.getDrawable()).getBitmap()
						.recycle();
			storyImage.setImageBitmap(null);
		}
		if (nextImageFound)
			storyImage.setImageBitmap(decodeSampledBitmapFromResource(
					getResources(),
					getResources().getIdentifier(imageName, "drawable",
							this.getPackageName()), width, height));
		textRecording.setText(txt);
		setVisibilityForButtons();
		playButton.setText(getString(R.string.playBtn));
	}

	private void startPlaying() {
		audioPlaying = true;
		playButton.setText(getString(R.string.stopBtn));

		setVisibilityForButtons();
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(customAudioName);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					stopPlaying();
				}
			});
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
	}

	private void stopPlaying() {
		audioPlaying = false;
		playButton.setText(getString(R.string.playBtn));
		mPlayer.release();
		mPlayer = null;
		setVisibilityForButtons();
	}

	private void startRecording() {
		recording = true;
		recordButton.setText(getString(R.string.stopRecBtn));
		setVisibilityForButtons();
		// mRecorder = new MediaRecorder();
		// mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// mRecorder.setOutputFile(customAudioName);
		// mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mRecorder.setOutputFile(customAudioName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
		mRecorder.start();
	}

	private void stopRecording() {
		recording = false;
		currentAudioRecorded = true;
		recordButton.setText(getString(R.string.recBtn));
		setVisibilityForButtons();
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
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

	private void setVisibilityForButtons() {
		// setting when recordButton is enabled
		if (audioPlaying)
			recordButton.setEnabled(false);
		else
			recordButton.setEnabled(true);
		// setting when playButton is enabled
		if (recording || !currentAudioRecorded)
			playButton.setEnabled(false);
		else
			playButton.setEnabled(true);
		// setting when recordButton is enabled
		if (audioPlaying || recording || !currentAudioRecorded)
			nextButton.setEnabled(false);
		else
			nextButton.setEnabled(true);

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
	
	@Override
	public void onBackPressed() {
		backToMenu();
	}
	
	void backToMenu() {
		if (slideNum < 34) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(getString(R.string.btmTitle))
					.setMessage(getString(R.string.btmMsg2))
					.setPositiveButton(getString(R.string.btmYes),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (audioPlaying == true) {
										mPlayer.release();
										audioPlaying = false;
									}
									AudioRecorderActivity.this.finish();
								}

							})
					.setNegativeButton(getString(R.string.btmNo), null).show();
		} else {
			if (audioPlaying == true) {
				mPlayer.release();
				audioPlaying = false;
			}
			
			AudioRecorderActivity.this.finish();
		}

	}
}
