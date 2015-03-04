package com.example.rsilaboratorium1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btnGPS;
	private Button btnKamera;
	private Button btnKompas;
	private Button btnNagraj;
	private Button btnMonitoring;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupView();
		initButtonsOnClickListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setupView() {
		btnGPS = (Button) findViewById(R.id.btnGPS_MAIN);
		btnKamera = (Button) findViewById(R.id.btnCamera);
		btnKompas = (Button) findViewById(R.id.btnCompass);
		btnNagraj = (Button) findViewById(R.id.btnRecording);
		btnMonitoring = (Button) findViewById(R.id.btnMonitoring);
	}

	private void initButtonsOnClickListeners() {
		btnGPS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, GPSActivity.class);
				startActivity(intent);
			}

		});
		
		btnKamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						KameraActivity.class);
				startActivity(intent);
			}
		});
		btnKompas.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						KompasActivity.class);
				startActivity(intent);
			}
		});
		btnNagraj.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						Nagraj.class);
				startActivity(intent);
			}
		});
		btnMonitoring.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						NoiseAlert.class);
				startActivity(intent);
			}
		});
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
