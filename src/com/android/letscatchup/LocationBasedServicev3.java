package com.android.letscatchup;



import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
 
public class LocationBasedServicev3 extends MapActivity {
 
	private MapView myMap;
	private LocationManager locManager;
	private LocationListener locListener;
 
  	
  	
  	
  	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
 
		initMap();
		initLocationManager();
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
		
		SharedPreferences sp=getSharedPreferences("settings.txt",MODE_PRIVATE);
	  	int freq_to_update=((60*1000)*(sp.getInt("time_interval",0)));
	  	int dist_to_update=((1000)*sp.getInt("walk_dist",0));
	  	
	  	System.out.println(freq_to_update+".............."+dist_to_update+"............");
	  	
	  	
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
 
		locListener = new LocationListener() {
 
			public void onLocationChanged(Location newLocation) {
				createAndShowCustomOverlay(newLocation);
			}
 
			public void onProviderDisabled(String arg0) {
			}
 
			public void onProviderEnabled(String arg0) {
			}
 
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}
		};
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,freq_to_update , dist_to_update,locListener);

		
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
		GeoPoint geopoint = new GeoPoint(
				(int) (newLocation.getLatitude() * 1E6), (int) (newLocation
						.getLongitude() * 1E6));
 
		
		double latitude = geopoint.getLatitudeE6() / 1E6;
		double longitude = geopoint.getLongitudeE6() / 1E6;


		System.out.println(latitude+"lat n long"+longitude);
		
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
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
	protected boolean isRouteDisplayed() {
		return false;
	}
}