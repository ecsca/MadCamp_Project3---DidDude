package com.example.didplan;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class DailyComp extends FragmentActivity implements OnItemClickListener{

	private Context mContext = null;
	final String databaseName = "DidDude.db";
	private SQLiteDatabase database = null;
	private ArrayList<DailyCompCell> dayCell;
	private DailyCompAdapter mDailyAdapter;
	private ListView mHourList;

	String year;
	String month;
	String day;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent t = new Intent(mContext, Month.class);
			startActivity(t);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailycomp);
		mContext = this;
		Intent calInt = getIntent();
		Bundle ex = calInt.getExtras();
		day = ex.getString("day");
		month = ex.getString("month");
		year = ex.getString("year");
		dayCell = new ArrayList<DailyCompCell>();
		mHourList = (ListView) findViewById(R.id.hourList);
		mHourList.setOnItemClickListener(this);
		Log.e("qqqq", year + "/" + month + "/" + day);
		Button selectDaybtn = (Button) findViewById(R.id.selectDay);
		selectDaybtn.setText(month + "¿ù " + day + "ÀÏ");

		Button editlist = (Button) findViewById(R.id.editmode2);
		editlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t = new Intent(mContext, Daily.class);
				t.putExtra("day", day);
				t.putExtra("month", month);
				t.putExtra("year", year);
				startActivity(t);
				finish();
			}
		});
		/*
		Button showlist = (Button) findViewById(R.id.editmode2);
		showlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t = new Intent(mContext, DailyShow.class);
				t.putExtra("day", day);
				t.putExtra("month", month);
				t.putExtra("year", year);
				startActivity(t);
				finish();
			}
		});
		*/
		Button goCal = (Button) findViewById(R.id.goCalendar);

		goCal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t = new Intent(mContext, Month.class);
				t.putExtra("day", day);
				t.putExtra("month", month);
				t.putExtra("year", year);
				startActivity(t);
				finish();
			}
		});
		
		/*
		Button showPlan = (Button) findViewById(R.id.showplanmode);
		showPlan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t = new Intent(mContext, DailyPlanShow.class);
				t.putExtra("day", day);
				t.putExtra("month", month);
				t.putExtra("year", year);
				startActivity(t);
				finish();
			}
		});
		*/
		Button editPlan = (Button) findViewById(R.id.editplanmode);
		editPlan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t = new Intent(mContext, DailyPlan.class);
				t.putExtra("day", day);
				t.putExtra("month", month);
				t.putExtra("year", year);
				startActivity(t);
				finish();
			}
		});
		
		Button comp = (Button) findViewById(R.id.showcompmode);
		comp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t = new Intent(mContext, DailyComp.class);
				t.putExtra("day", day);
				t.putExtra("month", month);
				t.putExtra("year", year);
				startActivity(t);
				finish();
			}
		});
		
		if (database == null) {

			database = openOrCreateDatabase(databaseName,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
			// database.execSQL("drop table '2014.7';");
			String cQuery = "CREATE TABLE IF NOT EXISTS "
					+ "'"
					+ year
					+ "."
					+ month
					+ "'"
					+ "(day INTEGER, hour INTEGER, latitude DOUBLE, longitude DOUBLE, content TEXT, title TEXT, plancontent TEXT, plantitle TEXT);";
			Log.e("utf", cQuery);
			database.execSQL(cQuery);
		}
		for (int i = 0; i < 24; i++) {
			String query = "INSERT INTO " + "'" + year + "." + month + "'"
					+ " (day, hour, latitude, longitude) select " + day + ","
					+ String.valueOf(i)
					+ ",0,0 where not exists ( select 1 from " + "'" + year
					+ "." + month + "'" + " where day = " + String.valueOf(day)
					+ " and hour = " + String.valueOf(i) + ");";
			// String query = "INSERT INTO " + "'" + year+"."+month +"'"+
			// " (day, hour) select " + day + "," + String.valueOf(i) +
			// " where not exists ( select 1 from "
			// + "'"+ year+"."+month +"'"+ " where day = " + String.valueOf(day)
			// + " and hour = " +String.valueOf(i) + ");";

			// String query = "insert into "+ "'" + year + "." + month
			// +"' (day)" + " values (1)";
			// Log.e("utf", query);
			database.execSQL(query);
			// Log.e("kkk", query);
		}

		String squery = "SELECT * FROM " + "'" + year + "." + month
				+ "' WHERE day = " + day + ";";
		// String squery = "SELECT * FROM " + "'"+"test.test" + "'";// WHERE day
		// = " + day + ";";
		Cursor c = database.rawQuery(squery, null);

		c.moveToFirst();

		do {
			if(!(c.getString(4)==null &&c.getString(5)==null &&c.getString(6)==null &&c.getString(7)==null))
			dayCell.add(new DailyCompCell(String.valueOf(c.getInt(0)), String
					.valueOf(c.getInt(1)), String.valueOf(c.getDouble(2)),
					String.valueOf(c.getDouble(3)), c.getString(4), c
							.getString(5), c.getString(6), c.getString(7)));
		} while (c.moveToNext());
		initDailyAdapter();

	}

	void callOnlyList() {
		Intent dayIntent = new Intent(mContext, DailyShow.class);
		dayIntent.putExtra("day", day);
		dayIntent.putExtra("month", month);
		dayIntent.putExtra("year", year);
		startActivity(dayIntent);
		finish();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		String squery = "SELECT * FROM " + "'" + year + "." + month
				+ "' WHERE day = " + day + ";";
		Cursor c = database.rawQuery(squery, null);

		c.moveToFirst();

		dayCell.clear();
		do {if(!(c.getString(4)==null &&c.getString(5)==null &&c.getString(6)==null &&c.getString(7)==null))
			dayCell.add(new DailyCompCell(String.valueOf(c.getInt(0)), String
					.valueOf(c.getInt(1)), String.valueOf(c.getDouble(2)),
					String.valueOf(c.getDouble(3)), c.getString(4), c
							.getString(5), c.getString(6), c.getString(7)));
		} while (c.moveToNext());
		mDailyAdapter.notifyDataSetChanged();
	}

	private void initDailyAdapter() {
		mDailyAdapter = new DailyCompAdapter(this, R.layout.compareplan,
				dayCell, year, month);
		mHourList.setAdapter(mDailyAdapter);
	}
	
	public final void callDailyCell(int selectedIndex) {
		Intent hourIntent = new Intent(mContext, ShowHourPage.class);
		hourIntent.putExtra("plancontent", dayCell.get(selectedIndex).getPlancontent());
		hourIntent.putExtra("plantitle", dayCell.get(selectedIndex).getPlantitle());
		hourIntent.putExtra("content", dayCell.get(selectedIndex).getContent());
		hourIntent.putExtra("title",  dayCell.get(selectedIndex).getTitle());
		hourIntent.putExtra("lat",  dayCell.get(selectedIndex).getLatitude());
		hourIntent.putExtra("lon",  dayCell.get(selectedIndex).getLongitude());
		hourIntent.putExtra("month",  month);
		hourIntent.putExtra("day",  dayCell.get(selectedIndex).getDay());
		hourIntent.putExtra("hour",  dayCell.get(selectedIndex).getHour());
		startActivity(hourIntent);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		callDailyCell(position);
	}

}
