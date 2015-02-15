package com.example.didplan;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.didplan.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class showMap extends FragmentActivity{
	String lat;
	String lon;
	boolean change;
	String changedLat;
	String changedLon;
	String year;
	String month;
	String day;
	String hour;
	Context mContext;
	private SQLiteDatabase db;
	final String databaseName = "DidDude.db";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showmap);
		mContext = this;
		Intent dayIntent = getIntent();
		Bundle ex = dayIntent.getExtras();
		lat = ex.getString("lat");
		lon = ex.getString("lon");
		month = ex.getString("month");
		year = ex.getString("year");
		day = ex.getString("day");
		hour = ex.getString("hour");
		Log.e("kkk", lat+","+lon);
		final GoogleMap gmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		LatLng startingPoint = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,16));
		MarkerOptions markerOptions = new MarkerOptions();
		//markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_3g));
		LatLng t = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
		markerOptions.position(t); //마커위치설정
		gmap.animateCamera(CameraUpdateFactory.newLatLng(t));   // 마커생성위치로 이동
		gmap.addMarker(markerOptions); //마커 생성
		if (db == null) {

			db = openOrCreateDatabase(databaseName,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
		}
		
		Button resetPos = (Button) findViewById(R.id.setPos);
		resetPos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String query = "update '"+year+"."+month+"' set latitude = "+changedLat+" where day = "+day+" and hour = "+hour+";";
				//String query = "update '"+y+"."+m+"' set latitude = "+LATITUDE+" where day = "+d+" and hour = "+t+";";
				Log.e("kkk", query);
				db.execSQL(query);
				query = "update '"+year+"."+month+"' set longitude = "+changedLon+" where day = "+day+" and hour = "+hour+";";
				//query = "update '"+y+"."+m+"' set longitude = "+LONGTITUDE+" where day = "+d+" and hour = "+t+";";
				db.execSQL(query);
				Toast toast = Toast.makeText(mContext, "결제되었습니다.",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
				Log.e("kkk", query);
			}
		});
		gmap.setOnMapClickListener(new OnMapClickListener() {

            public void onMapClick(LatLng arg0) {
            	Log.i("kkk", arg0.toString());
            	String latlng = arg0.toString();
            	changedLat = latlng.split(",")[0].split("\\(")[1];
            	changedLon = latlng.split(",")[1].split("\\)")[0];
            	Log.i("kkk", changedLat+"");
            	Log.i("kkk", changedLon+"");
                 if(arg0!=null){
                     change=false;
                      //clear map
                     gmap.clear(); 
                        // create marker
                         MarkerOptions marker = new MarkerOptions().position(arg0);

                         gmap.addMarker(marker);
                         }
            }
        });
	}

	
}
