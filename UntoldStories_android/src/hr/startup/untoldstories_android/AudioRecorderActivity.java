package hr.startup.untoldstories_android;

import android.app.Activity;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.content.res.Resources;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	private boolean currentAudioExists = false;
	private boolean allAudioExists = false;
	private String textSufix;
	private String customAudioSufix;
	private int slideNum = 0;
	private int storyNum = 0;
	private MediaPlayer mPlayer = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_audio_recorder);

		relativePath = getString(R.string.relativePath);
		prefix = getString(R.string.prefix);
		textSufix = getString(R.string.textSufix);
		customAudioSufix = getString(R.string.customAudioSufix);
		
		// init graphical elements
		playButton = (Button) findViewById(R.id.playRecording);
		recordButton = (Button) findViewById(R.id.startRecording);
		nextButton = (Button) findViewById(R.id.nextRecording);
		textRecording = (TextView) findViewById(R.id.textRecording);
		
		// get info from main activity (which story is selected)
		Bundle b = getIntent().getExtras();
		storyNum = b.getInt("storyNum");
		
		//set absolute directory path to folder on externalStorage
		absolutePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + relativePath;
		
		//initially disable all buttons
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

				nextSlide();

			}
		});
		
		//open first slide
		nextSlide();

	}

	private void nextSlide() {

		String txt = null;
		
		//find and show next slide that contains text. 
		do {
			slideNum++;
			try {

				String textName = prefix + String.valueOf(storyNum) + "_" + String.valueOf(slideNum) + textSufix;
				Resources resources = getResources();
				int x = resources
						.getIdentifier(textName, "raw", this.getPackageName());
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
		} while ((txt == null) || (slideNum > 32));
		customAudioName = absolutePath + prefix + String.valueOf(storyNum)
				+ "_" + slideNum + customAudioSufix;
		textRecording.setText(txt);
		recordButton.setEnabled(true);
		playButton.setText(getString(R.string.playBtn));
	}

	private void startPlaying() {
		audioPlaying = true;
		playButton.setText(getString(R.string.stopBtn));

		recordButton.setEnabled(false);
		nextButton.setEnabled(false);
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
		nextButton.setEnabled(true);
	}

	private void startRecording() {
		recording = true;
		recordButton.setText(getString(R.string.stopRecBtn));
		playButton.setEnabled(false);
		nextButton.setEnabled(false);
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
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
		recordButton.setText(getString(R.string.recBtn));
		nextButton.setEnabled(true);
		playButton.setEnabled(true);
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
}
