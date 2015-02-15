package com.example.didplan;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Month extends Activity implements OnItemClickListener,
		OnClickListener {

	public static int SUNDAY = 1;
	public static int MONDAY = 2;
	public static int TUESDAY = 3;
	public static int WEDNESDAY = 4;
	public static int THURSDAY = 5;
	public static int FRIDAY = 6;
	public static int SATURDAY = 7;
	private BackPressCloseHandler backPressCloseHandler;
	private TextView mTitle;
	private GridView mGridCalendar;
	

	private static Context mContext;
	static Calendar calendar2;
	private ArrayList<DayInfo> mDayList;
	private CalendarAdapter mCalendarAdapter;
	private static AlarmManager alarmManager2;
	Calendar mLastMonthCalendar;
	Calendar mThisMonthCalendar;
	Calendar mNextMonthCalendar;
	private PendingIntent pendingIntent;
	private static PendingIntent pendingIntent2;
	final String databaseName = "DidDude.db";
	private static SQLiteDatabase database = null;

	public static Context getAppContext(){
	    return mContext;
	}
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		if (database == null) {
			database = openOrCreateDatabase(databaseName,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
			String cQuery = "CREATE TABLE IF NOT EXISTS "
					+ "'"
					+ "settings"
					+ "'"
					+ "(alarmtime INTEGER, alarmmin INTEGER, timeterm INTEGER, minterm INTEGER, password TEXT);";
			Log.e("utf", cQuery);

			database.execSQL(cQuery);
			String query = "INSERT INTO " + "'settings'"
					+ " (alarmtime,alarmmin,timeterm,minterm) select "
					+ "17,0,1,0 " + "where not exists ( select 1 from "
					+ "'settings'" + " );";
			database.execSQL(query);
		}
		backPressCloseHandler = new BackPressCloseHandler(this);
		Button pMonth = (Button) findViewById(R.id.prevMonth);
		Button nMonth = (Button) findViewById(R.id.nextMonth);

		mTitle = (TextView) findViewById(R.id.monthTitle);
		mGridCalendar = (GridView) findViewById(R.id.calendarMonth);

		pMonth.setOnClickListener(this);
		nMonth.setOnClickListener(this);
		mGridCalendar.setOnItemClickListener(this);

		mDayList = new ArrayList<DayInfo>();

		Button start = (Button) findViewById(R.id.setting);

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent settingsIntent = new Intent(mContext,
						ChangeSetting.class);
				startActivityForResult(settingsIntent, 1);
			}

		});

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Intent intent = new Intent(Month.this, MyReceiver.class);
		// service로 바꿀땐 getService로

		pendingIntent = PendingIntent.getBroadcast(Month.this, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),60*60 * 1000, pendingIntent);
		
		String squery = "SELECT * FROM " + "'settings';";
		Cursor c = database.rawQuery(squery, null);
		c.moveToFirst();
		int time1 = c.getInt(0);
		int min1 = c.getInt(1);
		int time2 = c.getInt(2);
		int min2 = c.getInt(3);
		calendar2 = Calendar.getInstance();

		calendar2.set(Calendar.HOUR_OF_DAY, time1);
		calendar2.set(Calendar.MINUTE, min1);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		Intent intent2 = new Intent(Month.this, timeToDiary.class);
		pendingIntent2 = PendingIntent.getBroadcast(Month.this, 0, intent2, 0);
		alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager2.setRepeating(AlarmManager.RTC,calendar2.getTimeInMillis(),5* 60 * 1000, pendingIntent2);

	}

	static void setDiaryAlarmTime()
	{
		String squery = "SELECT * FROM " + "'settings';";
		Cursor c = database.rawQuery(squery, null);
		c.moveToFirst();
		Log.i("kkk", "abcde");
		int time1 = c.getInt(0);
		int min1 = c.getInt(1);
		int time2 = c.getInt(2);
		int min2 = c.getInt(3);
		calendar2.set(Calendar.HOUR_OF_DAY, time1);
		calendar2.set(Calendar.MINUTE, min1);
		calendar2.set(Calendar.SECOND, 0);
		alarmManager2.setRepeating(AlarmManager.RTC,calendar2.getTimeInMillis(), 60 * 1000, pendingIntent2);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mThisMonthCalendar = Calendar.getInstance();
		mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
		getCalendar(mThisMonthCalendar);
	}

	private void getCalendar(Calendar calendar) {
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;

		mDayList.clear();

		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, -1);

		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, 1);

		if (dayOfMonth == SUNDAY) {
			dayOfMonth += 7;
		}

		lastMonthStartDay -= (dayOfMonth - 1) - 1;

		mTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

		DayInfo day;

		for (int i = 0; i < dayOfMonth - 1; i++) {
			int date = lastMonthStartDay + i;
			day = new DayInfo();
			day.setDay(Integer.toString(date));
			;
			day.setInMonth(false);

			mDayList.add(day);
		}

		for (int i = 1; i <= thisMonthLastDay; i++) {
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(true);

			mDayList.add(day);
		}

		for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(false);
			mDayList.add(day);
		}

		initCalendarAdapter();
	}

	private Calendar getLastMonth(Calendar calendar) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.add(Calendar.MONTH, -1);
		mTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
		return calendar;
	}

	private Calendar getNextMonth(Calendar calendar) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.add(Calendar.MONTH, +1);
		mTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
		return calendar;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.prevMonth:
			mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
			getCalendar(mThisMonthCalendar);
			break;
		case R.id.nextMonth:
			mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
			getCalendar(mThisMonthCalendar);
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		callDailyCell(position);

	}

	public final void callDailyCell(int selectedIndex) {
		Intent dayIntent = new Intent(mContext, DailyComp.class);
		String year = String.valueOf(mThisMonthCalendar.get(Calendar.YEAR));
		String month = String
				.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1);
		String day = String.valueOf(mCalendarAdapter.getDay(selectedIndex));
		dayIntent.putExtra("day", day);
		dayIntent.putExtra("month", month);
		dayIntent.putExtra("year", year);
		startActivity(dayIntent);
		finish();
	}

	private void initCalendarAdapter()// TODO: Adapter까지 만들기.
	{
		mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList);
		mGridCalendar.setAdapter(mCalendarAdapter);
	}
}
