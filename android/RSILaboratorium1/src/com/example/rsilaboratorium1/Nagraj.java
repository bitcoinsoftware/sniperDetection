package com.example.rsilaboratorium1;

import java.io.IOException;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Nagraj extends Activity {
	
	private MediaRecorder myRecorder;
	private MediaPlayer myPlayer;
	private String outputFile = null;
	private Button startBtn;
	private Button stopBtn;
	private Button playBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nagraj);
		outputFile = Environment.getExternalStorageDirectory().

		getAbsolutePath() + "/Recording.3gpp";
		myRecorder = new MediaRecorder();
		myRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		myRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
		myRecorder.setOutputFile(outputFile);

		startBtn = (Button) findViewById(R.id.btnStartNagraj);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				start(v);
			}
		});

		stopBtn = (Button) findViewById(R.id.btnStopNagraj);
		stopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stop(v);
			}
		});

		playBtn = (Button) findViewById(R.id.btnPlayNagraj);
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play(v);
			}
		});

	}

	public void start(View view) {

		try {
			myRecorder.prepare();
			myRecorder.start();

		} catch (IllegalStateException e) {
			e.printStackTrace();

		} catch (IOException e) {			
			e.printStackTrace();
		}

		startBtn.setEnabled(false);
		stopBtn.setEnabled(true);
		
		Toast.makeText(getApplicationContext(), "Start recording",
		Toast.LENGTH_SHORT).show();

	}

	public void stop(View view) {

		try {
			myRecorder.stop();
			myRecorder.release();
			myRecorder = null;

			stopBtn.setEnabled(false);
			playBtn.setEnabled(true);

			Toast.makeText(getApplicationContext(), "Stop recording",
			Toast.LENGTH_SHORT).show();

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

	public void play(View view) {
		
		try {
			myPlayer = new MediaPlayer();
			myPlayer.setDataSource(outputFile);
			myPlayer.prepare();
			myPlayer.start();
			playBtn.setEnabled(false);

			Toast.makeText(getApplicationContext(),
					"Start play the recording",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
