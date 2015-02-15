package com.example.didplan;

import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.ListView;

public class Daily extends FragmentActivity {

	private Context mContext = null;
	final String databaseName = "DidDude.db";
	private SQLiteDatabase database = null;
	private ArrayList<DailyCell> dayCell;
	private DailyAdapter mDailyAdapter;
	private ListView mHourList;

	String year;
	String month;
	String day;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode)
		{
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
		setContentView(R.layout.daily);
		mContext = this;
		Intent calInt = getIntent();
		Bundle ex = calInt.getExtras();
		day = ex.getString("day");
		month = ex.getString("month");
		year = ex.getString("year");
		dayCell = new ArrayList<DailyCell>();
		mHourList = (ListView) findViewById(R.id.hourList);
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
		Button showlist = (Button) findViewById(R.id.editmode);
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
			dayCell.add(new DailyCell(String.valueOf(c.getInt(0)), String
					.valueOf(c.getInt(1)), String.valueOf(c.getDouble(2)),
					String.valueOf(c.getDouble(3)), c.getString(4), c.getString(5)));
		} while (c.moveToNext());
		initDailyAdapter();

	}

	void callOnlyList() {
		Intent dayIntent = new Intent(mContext, DailyComp.class);
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
		do {
			dayCell.add(new DailyCell(String.valueOf(c.getInt(0)), String
					.valueOf(c.getInt(1)), String.valueOf(c.getDouble(2)),
					String.valueOf(c.getDouble(3)), c.getString(4),c.getString(5)));
		} while (c.moveToNext());
		mDailyAdapter.notifyDataSetChanged();
	}

	private void initDailyAdapter() {
		mDailyAdapter = new DailyAdapter(this, R.layout.hour, dayCell, year,month);
		mHourList.setAdapter(mDailyAdapter);
	}

}
