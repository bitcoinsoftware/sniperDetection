package com.example.rsilaboratorium1;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NoiseAlert extends Activity implements SensorEventListener,
		LocationListener {

	SoundMeter sMeter = new SoundMeter();
	GPS gps;

	private static final int POLL_INTERVAL = 300;
	private boolean mRunning = false;
	private int mThreshold;
	private PowerManager.WakeLock mWakeLock;
	private Handler mHandler = new Handler();
	private TextView mStatusView;
	private TextView mResult;
	private Button mSend;
	private SoundLevelView mDisplay;
	private SensorManager mSensorManager;
	private SoundMeter mSensor;

	double amp;

	float axisX;
	float axisY;
	float axisZ;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			start();
		}
	};

	private Runnable mPollTask = new Runnable() {
		public void run() {

			amp = mSensor.getAmplitude();
			updateDisplay("Monitoring Voice...", amp);

			if ((amp > mThreshold)) {
				Alert();
			}

			// Runnable(mPollTask) will again execute after POLL_INTERVAL
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_monitoring);
		mStatusView = (TextView) findViewById(R.id.status);
		mResult = (TextView) findViewById(R.id.result_monitoring);
		mSend = (Button) findViewById(R.id.bSend);
		
		mSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mResult.setText("Button Send on Click");
			}
		});
		
		// Used to record voice
		mSensor = new SoundMeter();
		mDisplay = (SoundLevelView) findViewById(R.id.volume);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"NoiseAlert");

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	@Override
	public void onResume() {
		super.onResume();

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);

		initializeApplicationConstants();
		mDisplay.setLevel(0, mThreshold);

		if (!mRunning) {
			mRunning = true;
			start();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		stop();

	}

	private void start() {

		mSensor.start();
		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire();
		}

		// Noise monitoring start
		// Runnable(mPollTask) will execute after POLL_INTERVAL
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		Log.i("Noise", "==== Stop Noise Monitoring===");
		if (mWakeLock.isHeld()) {
			mWakeLock.release();
		}
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		mDisplay.setLevel(0, 0);
		updateDisplay("stopped...", 0.0);
		mRunning = false;

	}

	private void initializeApplicationConstants() {
		// Set Noise Threshold
		mThreshold = 5;

	}

	private void updateDisplay(String status, double signalEMA) {
		mStatusView.setText(status);
		//
		mDisplay.setLevel((int) signalEMA, mThreshold);
	}

	private void Alert() {
		/*
		 * Calendar calendar = Calendar.getInstance(); java.util.Date now =
		 * calendar.getTime(); java.sql.Timestamp currentTimestamp = new
		 * java.sql.Timestamp(now.getTime());
		 * 
		 * String sTimestamp = String.valueOf(currentTimestamp);
		 */

		Timestamp stamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());

		// stop();
//		mSensor.recording();
		gps = new GPS(NoiseAlert.this);

		String latitude = String.valueOf(gps.getLatitude());
		String longitude = String.valueOf(gps.getLongitude());

		// TODO zmieniæ na StringBuilder!!!

		mResult.setText("'timestamp':"
				+ stamp
				+ ",'gps_latitude':'"
				+ latitude
				+ "','gps_longitude':'"
				+ longitude
				+ "',gps_altitude:'"
				+ getElevationFromGoogleMaps(gps.getLongitude(),
						gps.getLatitude()) + "','magnetometer':'"
				+ xyzMagnetometer() + "','intensity':'" + amp + "'");

	
	
	}

	public String xyzMagnetometer() {

		return String.valueOf(axisX) + "," + String.valueOf(axisY) + ","
				+ String.valueOf(axisZ);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		axisX = event.values[0];
		axisY = event.values[1];
		axisZ = event.values[2];

	}

	private double getElevationFromGoogleMaps(double longitude, double latitude) {
		double result = Double.NaN;
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		String url = "http://maps.googleapis.com/maps/api/elevation/"
				+ "xml?locations=" + String.valueOf(latitude) + ","
				+ String.valueOf(longitude) + "&sensor=true";
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				int r = -1;
				StringBuffer respStr = new StringBuffer();
				while ((r = instream.read()) != -1)
					respStr.append((char) r);
				String tagOpen = "<elevation>";
				String tagClose = "</elevation>";
				if (respStr.indexOf(tagOpen) != -1) {
					int start = respStr.indexOf(tagOpen) + tagOpen.length();
					int end = respStr.indexOf(tagClose);
					String value = respStr.substring(start, end);
					/* results return in meters */
					result = (double) Double.parseDouble(value);

					/* convert from meters to feet */
					// result = (double)(Double.parseDouble(value)*3.2808399);
				}
				instream.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		return result;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

}
