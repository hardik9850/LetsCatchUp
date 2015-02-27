package com.android.letscatchup;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
 
public class LetsMap extends MapActivity {
 
	String result;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	String username,password,uid_cont,uid;
	String dist_to_update;
	SharedPreferences sp;
	private MapView myMap;
	private LocationManager locManager;
	private LocationListener locListener;
	GeoPoint geopoint,point;
	 private MyLocationOverlay myLocationOverlay;
  	Double latitude,longitude,flat,flon;
  	String friendnames;
  	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "GPS is Disabled in your devide", Toast.LENGTH_SHORT).show();
		}
		
 
		initMap();
		initLocationManager();
		myLocationOverlay = new MyLocationOverlay(getApplicationContext(), myMap);
        myLocationOverlay.enableMyLocation();

	}
 
	/**
	 * Initialise the map and adds the zoomcontrols to the LinearLayout.
	 */
	private void initMap() {
		myMap = (MapView) findViewById(R.id.mymap);
 
		View zoomView = myMap.getZoomControls();
		LinearLayout myzoom = (LinearLayout) findViewById(R.id.myzoom);
		myzoom.addView(zoomView);
		myMap.displayZoomControls(true);
		
 
	}
 
	private void initLocationManager() {
		
		sp=getSharedPreferences("settings.txt",MODE_PRIVATE);
	  	int freq_to_update=((60*1000)*(sp.getInt("time_interval",1)));
	    dist_to_update=sp.getString("walk_dist","1");
	  	uid=(sp.getString("uid", "0"));

		System.out.println("uid is "+uid);
	  	System.out.println(freq_to_update+".............."+dist_to_update+"............");
	  	
	  	
		//locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria= new  Criteria();;
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
        String providername = locManager.getBestProvider(criteria, true);
        
		
		locListener = new LocationListener() {
 
			public void onLocationChanged(Location newLocation) {
				Double lati=newLocation.getLatitude();
				Double longi=newLocation.getLongitude();
				

								Connection conn=null;
					try
						{ 
					//		Toast.makeText(addContact.this, "sending data", Toast.LENGTH_SHORT).show();
							HttpClient httpclient = new DefaultHttpClient();
							//file name to be changed 
					 
							HttpPost httppost = new HttpPost("http://letscatch.comyr.com/location.php");
							//add your data
							nameValuePairs = new ArrayList<NameValuePair>(4);
							// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
							
							nameValuePairs.add(new BasicNameValuePair("uid",uid));
							nameValuePairs.add(new BasicNameValuePair("latitude",Double.toString(lati)));
							nameValuePairs.add(new BasicNameValuePair("longitude",Double.toString(longi)));
							nameValuePairs.add(new BasicNameValuePair("range",dist_to_update));
							
							
							httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
							//Execute HTTP Post Request
							 HttpResponse response=httpclient.execute(httppost);
				       
							
						
							HttpEntity entity = response.getEntity();
							
					//		String is=entity.getContent().toString();
						InputStream is= entity.getContent();	
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
			                StringBuilder sb = new StringBuilder();
			                String line = null;
			                while((line = reader.readLine())!=null){
			                    sb.append(line+"\n");
			                }
			                is.close();
			                result=sb.toString();
			                
			                JSONObject j = new JSONObject(result);
			                if(j.length()<=1)
                            {}
			                else
                            {	
			                
			                JSONArray jr = new JSONArray(j.getString("list"));
                            for(int i=0;i<jr.length();i++)
			                {
			                	if(i==0)
			                	friendnames=jr.getString(i);
			                	else
			                	if(i==1)	
			                    flat=jr.getDouble(i);
			                	else
			                    flon=jr.getDouble(i);    	
			                  	//
			                }
			                	Geocoder geocoder;
			                  	List<Address> addresses;
			                  	//geocoder = new Geocoder(this, Locale.getDefault());
			                  	geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
			                  	addresses = geocoder.getFromLocation(flat,flon, 1);

			                  	String address = addresses.get(0).getAddressLine(0);
			                  	String city = addresses.get(0).getAddressLine(1);
			                  	String country = addresses.get(0).getAddressLine(2);
			                  	
			                  	System.out.println("address is "+address);
			                  	
			                	NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			         	        Notification noti = new Notification(R.drawable.ic_launcher,"A Friend is nearby you at "+address,1);
			         	        
			         	        Intent intent = new Intent(getApplication(),LetsMap.class);
			         	        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 1, intent, 0);
			         	        
			         	        noti.setLatestEventInfo(getBaseContext(), "A friend is found ",friendnames+" is at "+address , contentIntent);
			         	        nm.notify(1,noti);

                            }
			                }
			           
						//}
					catch(Exception e){
							e.printStackTrace();
							
						}

				
				Log.e("LONG", longi+"");
                Log.e("LAT", lati+"");
                
				createAndShowCustomOverlay(newLocation);
			}
 
			public void onProviderDisabled(String arg0) {
			}
 
			public void onProviderEnabled(String arg0) {
			}
 
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}
		};
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,freq_to_update ,500,locListener);

		
	}
 
	protected void createAndShowCustomOverlay(Location newLocation) {
		List overlays = myMap.getOverlays();
 
		// first remove old overlay
		if (overlays.size() > 0) {
			for (Iterator iterator = overlays.iterator(); iterator
					.hasNext();) {
				iterator.next();
				iterator.remove();
			}
		}
 
		// transform the location to a geopoint
		
		System.out.println("calculating geo");
		 geopoint = new GeoPoint((int) (newLocation.getLatitude() * 1E6), (int) (newLocation.getLongitude() * 1E6));
		 System.out.println("calculating geo"+geopoint);
			
		
		double latitude = geopoint.getLatitudeE6() / 1E6;
		double longitude = geopoint.getLongitudeE6() / 1E6;

		Editor e = sp.edit();
		e.putFloat("latitude", (float) latitude);
			Toast.makeText(getApplicationContext(), latitude+"nnnnnn"+longitude,100000).show();
		
		System.out.println(latitude+"lat n long"+longitude);
		
		
		
		Drawable drawable = this.getResources().getDrawable(android.R.drawable.btn_radio);
		// Create new Overlay
		CustomOverlay overlay = new CustomOverlay(geopoint);
 
		myMap.getOverlays().add(overlay);
 
		// move to location
		myMap.getController().animateTo(geopoint);
		myMap.getController().setZoom(20);
		myMap.getController().setCenter(geopoint);
		// redraw mapc
		myMap.postInvalidate();
		
		
	}
 
	 @Override
     public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getTitle().equals("My Position"))
            {
            
            	if(point==null)
            	{
            	Toast.makeText(getApplicationContext(), "point null", 10000);
            	}
            MapController myMapController = myMap.getController();
            myMapController.setCenter(geopoint);
            }
            return true;
     }
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	public boolean onCreateOptionsMenu(Menu menu) {		
		// TODO Auto-generated method stub
		 menu.add("My Position");
		 	return true;
	}

	private void centerCurrentLocation(final boolean force) {
         myLocationOverlay.runOnFirstFix(new Runnable() {
                 public void run() {
                         if (force || !CustomOverlay.hasCurrentOverlayItem()) {
                                 Location location = myLocationOverlay.getLastFix();
                                 if (location != null) {
                                	 centerLocation(location.getLatitude(), location.getLongitude());
                                         myMap.getController().setZoom(myMap.getMaxZoomLevel() - 1);
                                 }
                         }
                 }
         });
         
 }
	 private void centerLocation(double latitude, double longitude) {
          point = new GeoPoint((int) (latitude * 1E6),
                         (int) (longitude * 1E6));
         myMap.getController().animateTo(point);
 }
}

