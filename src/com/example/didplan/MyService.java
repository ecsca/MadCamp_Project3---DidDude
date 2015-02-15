package com.example.didplan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

	private static final String TAG = "Test";
	private LocationManager mLocationManager =null;
	private static final int LOCATION_INTERVAL = 0;
	private static final float LOCATION_DISTANCE = 0;
	//private TestDB testdb;
	private SQLiteDatabase db;
	final String databaseName = "DidDude.db";

	
	public void onStart(Intent intent, int startId){
	super.onStart(intent, startId);
	Log.i("test2","onStart");
	//testdb= new TestDB(this);
	
}

private class LocationListener implements android.location.LocationListener{
	Location mLastLocation;
	public LocationListener(String provider)
	{
		mLastLocation = new Location(provider);
		
	}
	public void onLocationChanged(Location location){
		mLastLocation.set(location);
		double LATITUDE =location.getLatitude();
		double LONGTITUDE = location.getLongitude();
		String Latitude =String.valueOf(LATITUDE);
		String Longtitude = String.valueOf(LONGTITUDE);
		if (db == null) {

			db = openOrCreateDatabase(databaseName,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
		}
		System.out.println("LocationChanged "+Latitude+" "+Longtitude); 
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
		Date currentTime = new Date ( );
		String mTime = mSimpleDateFormat.format ( currentTime );
		Log.e("kkk", mTime+"");
		System.out.println ( mTime );
		
		String y = mTime.split("\\.")[0];
		String m = mTime.split("\\.")[1];
		String d = mTime.split("\\.")[2].split("\\s+")[0];
		String t = mTime.split("\\.")[2].split("\\s+")[1].split(":")[0];
		Log.e("qqqq", Integer.parseInt(m)+"");
		if(Integer.parseInt(m)<10 && Integer.parseInt(m)>0)
		{
			Log.e("qqqq", m);
			m = m.split("0")[1];
		}
		if(Integer.parseInt(d)<10 && Integer.parseInt(d)>0)
		{
			Log.e("qqqq", d);
			d = d.split("0")[1];
		}
		if(Integer.parseInt(t)<10 && Integer.parseInt(t)>0)
		{
			Log.e("qqqq", t);
			t = t.split("0")[1];
		}
		String cQuery = "CREATE TABLE IF NOT EXISTS "
				+ "'"
				+ y
				+ "."
				+ m
				+ "'"
				+ "(day INTEGER, hour INTEGER, latitude DOUBLE, longitude DOUBLE, content TEXT, title TEXT, plancontent TEXT, plantitle TEXT);";
		db.execSQL(cQuery);
    	for (int i = 0; i<24; i++)
    	{
    		String query = "INSERT INTO " + "'" + y+"."+m +"'"+ " (day, hour, latitude, longitude) select " + d + "," + String.valueOf(i) +",0,0 where not exists ( select 1 from "
    				+ "'"+ y+"."+m +"'"+ " where day = " + String.valueOf(d) + " and hour = " +String.valueOf(i) + ");";
//    		String query = "INSERT INTO " + "'" + year+"."+month +"'"+ " (day, hour) select " + day + "," + String.valueOf(i) + " where not exists ( select 1 from "
//    				+ "'"+ year+"."+month +"'"+ " where day = " + String.valueOf(day) + " and hour = " +String.valueOf(i) + ");";

    		//String query = "insert into "+ "'" + year + "." + month +"' (day)" + " values (1)";
    		//Log.e("utf", query);
    		db.execSQL(query);
    		//Log.e("kkk", query);
    	}
		//String query = "INSERT INTO '"+y+"."+m+"' (day, hour) select " + d + "," + t + 
		//		" where not exists ( select 1 from '"+y+"."+m+"' where day = "+d + " and hour = "+ t+");";
		//Log.e("kkk", query);
		//db.execSQL(query);
		//query = "INSERT INTO '"+y+"."+m+"' (latitude, longitude) values (" + LATITUDE+", "+LONGTITUDE+") where day = "+d+" and hour = "+t+";";
		String query = "update '"+y+"."+m+"' set latitude = "+LATITUDE+" where day = "+d+" and hour = "+t+" and latitude = 0;";
		//String query = "update '"+y+"."+m+"' set latitude = "+LATITUDE+" where day = "+d+" and hour = "+t+";";
		Log.e("kkk", query);
		db.execSQL(query);
		query = "update '"+y+"."+m+"' set longitude = "+LONGTITUDE+" where day = "+d+" and hour = "+t+" and longitude = 0;";
		//query = "update '"+y+"."+m+"' set longitude = "+LONGTITUDE+" where day = "+d+" and hour = "+t+";";
		
		
		Log.e("kkk", query);
		db.execSQL(query);
		/*///////////////////////////////////////
		
		db에다가 latitude, longtitude, 필요하면 시간도 집어넣는 코드
		
		
		//////////////////////////////////////*/
		
		onDestroy();
		/*db=testdb.getWritableDatabase();
		Log.i("dbtest","INSERT INTO TestDB VALUES (null, "+Latitude+", "+Longtitude+")");
		db.execSQL("INSERT INTO TestDB VALUES (null, "+Latitude+", "+Longtitude+")");
		db.close();*/
		
	
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		System.out.println("statuschanged");
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
LocationListener[] mLocationListeners = new LocationListener[] {
        new LocationListener(LocationManager.GPS_PROVIDER),
        new LocationListener(LocationManager.NETWORK_PROVIDER)
};
@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
}

public int onStartCommand(Intent intent, int flags, int startId)
{
	super.onStartCommand(intent, flags, startId);
	Log.i("service","startcommand");
	
	
	
	try{
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
				mLocationListeners[1]);	
		//push LATITUDE and LONGTITUDE in DB
	} catch (java.lang.SecurityException ex){
		System.out.println("catch");
	}catch (IllegalArgumentException ex){
		System.out.println("catch2");
	}
	
	try{
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, 
				mLocationListeners[0]);
	} catch (java.lang.SecurityException ex){
		System.out.println("catch3");
	}catch (IllegalArgumentException ex){
		System.out.println("catch4");
	}
	
	
	
	return START_NOT_STICKY;
}

public void onDestroy(){
	super.onDestroy();
	Log.i("end","stopservice");
	if(mLocationManager !=null){
		for (int i=0; i<mLocationListeners.length; i++){
			try{
				mLocationManager.removeUpdates(mLocationListeners[i]);
				System.out.println("Delete Listener's data");
			} catch (Exception ex){
				;
			}
		}
	}
}

public void onCreate(){
	initializeLocationManager();
	System.out.println("create");
	Log.i("service","start");

}

public void initializeLocationManager() {
	if(mLocationManager ==null){
		mLocationManager=(LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
	}
} 
}
