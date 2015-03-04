package com.example.rsilaboratorium1;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class GPSActivity extends Activity {

	Button btnShowLocation;
	Button btnShowMap;
	
	GPS gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		 btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
		 btnShowMap = (Button) findViewById(R.id.btnGpsMap);
         

	        btnShowLocation.setOnClickListener(new View.OnClickListener() {
	             
	            @Override
	            public void onClick(View arg0) {        
	                gps = new GPS(GPSActivity.this);
	 
	                
	                if(gps.canGetLocation()){
	                    
	                	String latitude = String.valueOf(gps.getLatitude());
	                	String longitude = String.valueOf(gps.getLongitude());
	      
	                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + turnSexagesimalToDecimal(latitude) + "\nLong: " + turnSexagesimalToDecimal(longitude), Toast.LENGTH_LONG).show();    
	                }else{
	                    // can't get location
	                    // GPS or Network is not enabled
	                    // Ask user to enable GPS/network in settings
	                    gps.showSettingsAlert();
	                }
	                 
	            }
	        });
	        
	        btnShowMap.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	String lat = String.valueOf(gps.getLatitude());
                	String lon = String.valueOf(gps.getLongitude());
	            	 Uri geoUri = Uri.parse("geo:"+turnSexagesimalToDecimal(lat)+","+turnSexagesimalToDecimal(lon));
	            	    Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
	            	    if(isIntentAvailable(intent)) {
	            	        startActivity(intent);
	            	        stopService(intent);
	            	    } else {
	            	        Toast.makeText(getApplicationContext(),
	            	                "Your system hasn't necessary application for this intent",
	            	                Toast.LENGTH_LONG)
	            	                .show();
	            	    }
	            }

				private boolean isIntentAvailable(Intent intent) {
					PackageManager packageManager = GPSActivity.this.getPackageManager();
		            List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
					return resolveInfo.size() > 0;
				}
	        });
	        
	}
	
private double turnSexagesimalToDecimal(String number) {
	    double sumDivMinutes = 0;
	        String degree = number;
	        String degrees = degree.substring(0, 2);
	        String minutes = degree.substring(3, 5);

	        String second1 = degree.substring(5, 7);
	        String second2 = degree.substring(7);
	        String seconds = second1 + "." + second2;
	        double divideSeconds = Double.parseDouble(seconds) / 60;

	        sumDivMinutes = (Double.parseDouble(minutes) + divideSeconds) / 60;

	        sumDivMinutes = (double)Math.round(sumDivMinutes * 1000000) / 1000000;
	        sumDivMinutes = sumDivMinutes + Double.parseDouble(degrees);

	    return sumDivMinutes;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.g, menu);
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
	
/*	public String gpsResult(){
		String latitude = String.valueOf(gps.getLatitude());
    	   String longitude = String.valueOf(gps.getLongitude());
    	   String altitude = String.valueOf(gps.getAltitude());
    	   
    	   return ",'gps_latitude':'"+latitude+"','gps_longitude':'"+longitude+"',gps_altitude:'"+altitude;
	}*/
}
