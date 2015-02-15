package com.example.didplan;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.didplan.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowHourPage extends FragmentActivity {
	String lat;
	String lon;
	String plancontent;
	String planTitle;
	String content;
	String title;
	String month;
	String day;
	String hour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hourpage);
		Intent dayIntent = getIntent();
		Bundle ex = dayIntent.getExtras();

		plancontent = ex.getString("plancontent");
		planTitle = ex.getString("plantitle");
		content = ex.getString("content");
		title = ex.getString("title");
		lat = ex.getString("lat");
		lon = ex.getString("lon");
		hour = ex.getString("hour");
		day = ex.getString("day");
		month = ex.getString("month");
		
		TextView HourpageDay = (TextView) findViewById(R.id.hourpageDay);
		HourpageDay.setText(month+"월 "+day+"일 "+hour+"시 ");
		final GoogleMap gmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.hourPlanLoc)).getMap();
		LatLng startingPoint = new LatLng(Double.parseDouble(lat),
				Double.parseDouble(lon));
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));
		MarkerOptions markerOptions = new MarkerOptions();
		// markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_3g));
		LatLng t = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
		markerOptions.position(t); // 마커위치설정
		gmap.animateCamera(CameraUpdateFactory.newLatLng(t)); // 마커생성위치로 이동
		gmap.addMarker(markerOptions); // 마커 생성
		
		TextView pTitle = (TextView) findViewById(R.id.hourCellPlanTitle);
		pTitle.setText(planTitle);
		TextView pContent = (TextView) findViewById(R.id.hourPlanPlan);
		pContent.setText(plancontent);
		TextView Title = (TextView) findViewById(R.id.hourCellTitle);
		Title.setText(title);
		TextView Content = (TextView) findViewById(R.id.hourPlan);
		Content.setText(content);

	}
}