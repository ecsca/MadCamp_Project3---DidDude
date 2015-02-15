package com.example.didplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OnlyshowMap extends FragmentActivity{
	String lat;
	String lon;
	boolean change;
	String changedLat;
	String changedLon;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onlyshowmap);
		Intent dayIntent = getIntent();
		Bundle ex = dayIntent.getExtras();
		lat = ex.getString("lat");
		lon = ex.getString("lon");
		Log.e("kkk", lat+","+lon);
		final GoogleMap gmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		LatLng startingPoint = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,16));
		MarkerOptions markerOptions = new MarkerOptions();
		//markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_3g));
		LatLng t = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
		markerOptions.position(t); //��Ŀ��ġ����
		gmap.animateCamera(CameraUpdateFactory.newLatLng(t));   // ��Ŀ������ġ�� �̵�
		gmap.addMarker(markerOptions); //��Ŀ ����	
}
}