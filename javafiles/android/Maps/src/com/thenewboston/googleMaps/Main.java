package com.thenewboston.googleMaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class Main extends MapActivity implements LocationListener {
	/** Called when the activity is first created. */
	MapView map;
	long start;
	long stop;
	MyLocationOverlay compass;
	MapController controller;
	int x, y;
	GeoPoint touchedPoint;
	Drawable d;
	List<Overlay> overlayList;
	LocationManager lm;
	String towers;
	int lat;
	int longi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		map = (MapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);

		Touchy t = new Touchy();
		overlayList = map.getOverlays();
		overlayList.add(t);
		compass = new MyLocationOverlay(Main.this, map);
		overlayList.add(compass);
		controller = map.getController();
		GeoPoint point = new GeoPoint(51643234, 7848593);
		controller.animateTo(point);
		controller.setZoom(6);
	
		d = getResources().getDrawable(R.drawable.icon);
		
		
		//Placing pinpoint at location
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		
		towers = lm.getBestProvider(crit, false);
		Location location = lm.getLastKnownLocation(towers);
		
		if (location != null){
			lat = (int) (location.getLatitude() *1E6);
			longi = (int) (location.getLongitude() * 1E6);
			GeoPoint ourLocation = new GeoPoint(lat, longi);
			OverlayItem overlayItem = new OverlayItem(ourLocation, "What's up", "2nd String");
			CustomPinpoint custom = new CustomPinpoint(d, Main.this);
			custom.insertPinpoint(overlayItem);
			overlayList.add(custom);
		}else{
			Toast.makeText(Main.this, "Couldn't get provider", Toast.LENGTH_SHORT).show();
		}
		
		
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		compass.disableCompass();
		super.onPause();
		lm.removeUpdates(this);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		compass.enableCompass();
		super.onResume();
		lm.requestLocationUpdates(towers, 500, 1, this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touchy extends Overlay {
		public boolean onTouchEvent(MotionEvent e, MapView m) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = map.getProjection().fromPixels(x, y);
				
			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			if (stop - start > 1500) {
				AlertDialog alert = new AlertDialog.Builder(Main.this).create();
				alert.setTitle("Pick an Option");
				alert.setMessage("I told to pick an option");
				alert.setButton("place a pinpoint",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
						
								OverlayItem overlayItem = new OverlayItem(touchedPoint, "What's up", "2nd String");
								CustomPinpoint custom = new CustomPinpoint(d, Main.this);
								custom.insertPinpoint(overlayItem);
								overlayList.add(custom);
								
								
								
								
							}
						});
				alert.setButton2("get address",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								
							Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
								try{
									
									List<Address> address = geocoder.getFromLocation(touchedPoint.getLatitudeE6() /1E6, touchedPoint.getLongitudeE6()/1E6, 1);							
								
									if (address.size() > 0){
										String display = "";												
										for (int i = 0; i < address.get(0).getMaxAddressLineIndex(); i++){
											
											display += address.get(0).getAddressLine(i) + "\n";
										}
										Toast t3 = Toast.makeText(getBaseContext(), display, Toast.LENGTH_LONG);
										t3.show();
									}
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}finally{
							
								}
								
							}
						});
				alert.setButton3("Toggle View", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						if (map.isSatellite()){
							map.setSatellite(false);
							map.setStreetView(true);
						}else{
							map.setStreetView(false);
							map.setSatellite(true);
						}
						
						
					}
				});
				alert.show();
				return true;
			}

			return false;
		}
	}

	@Override
	public void onLocationChanged(Location l) {
		// TODO Auto-generated method stub
		lat = (int) (l.getLatitude() *1E6);
		longi = (int) (l.getLongitude()* 1E6);
		 
		GeoPoint ourLocation = new GeoPoint(lat, longi);
		OverlayItem overlayItem = new OverlayItem(ourLocation, "What's up", "2nd String");
		CustomPinpoint custom = new CustomPinpoint(d, Main.this);
		custom.insertPinpoint(overlayItem);
		overlayList.add(custom);
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		Toast.makeText(Main.this, "StatusChanged" +arg0, Toast.LENGTH_SHORT).show();
	}
}
