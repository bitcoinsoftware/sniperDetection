package com.example.rsilaboratorium1;

import java.io.File;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class KameraActivity extends Activity {

	private static final int VIDEO_CAPTURE = 101;
	private Uri fileUri;
	private Button recordButton;
	
	@Override 
	protected void onStart() {
		super.onStart();
		recordButton = 
                (Button) findViewById(R.id.btnRecord);
		
		if (!hasCamera())
			recordButton.setEnabled(false);
		String SrcPath = Environment.getExternalStorageDirectory().getAbsolutePath() 
	              + "/myvideo.mp4";
		
		VideoView myVideoView = (VideoView)findViewById(R.id.videoView);
	       myVideoView.setVideoURI(Uri.parse(SrcPath));
	       myVideoView.setMediaController(new MediaController(this));
	       myVideoView.requestFocus();
	}

	private boolean hasCamera() {
	    if (getPackageManager().hasSystemFeature(
                       PackageManager.FEATURE_CAMERA)){
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public void startRecording(View view)
	{
	    File mediaFile = new
       File(Environment.getExternalStorageDirectory().getAbsolutePath() 
              + "/myvideo.mp4");	
	
	    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    fileUri = Uri.fromFile(mediaFile);
		
 	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	    startActivityForResult(intent, VIDEO_CAPTURE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	    
	    if (requestCode == VIDEO_CAPTURE) {
	        if (resultCode == RESULT_OK) {
	             Toast.makeText(this, "Video has been saved to:\n" +
	                data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	        	Toast.makeText(this, "Video recording cancelled.", 
                      Toast.LENGTH_LONG).show();
	        } else {
	        	Toast.makeText(this, "Failed to record video", 
                      Toast.LENGTH_LONG).show();
	        }
	    }
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kamera);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kamera, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
