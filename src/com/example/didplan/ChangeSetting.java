package com.example.didplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ChangeSetting extends Activity {
	final String databaseName = "DidDude.db";
	private SQLiteDatabase database = null;
	private Context mContext;
	private String pwd;
	private int mHour;
	private int mMinute;
	static final int TIME_DIALOG_ID = 0;

	void setT() {
		if (database == null) {
			database = openOrCreateDatabase(databaseName,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
		}
		String squery = "SELECT * FROM " + "'settings';";
		Cursor c = database.rawQuery(squery, null);
		c.moveToFirst();
		int time1 = c.getInt(0);
		int min1 = c.getInt(1);
		int time2 = c.getInt(2);
		int min2 = c.getInt(3);
		pwd = c.getString(4);
		mHour = time1;
		mMinute = min1;

		TextView dTime = (TextView) findViewById(R.id.diarytime);
		TextView intervalTime = (TextView) findViewById(R.id.recordterm);

		if (time1 < 12) {
			if (min1 == 0)
				dTime.setText("���� " + time1 + "�� " + "00��");
			else
				dTime.setText("���� " + time1 + "�� " + min1 + "��");
		} else if (time1 == 12) {
			if (min1 == 0)
				dTime.setText("����" + time1 + "�� " + "00��");
			else
				dTime.setText("���� " + time1 + "�� " + min1 + "��");
		} else {
			if (min1 == 0)
				dTime.setText("���� " + (time1 - 12) + "�� " + "00��");
			else
				dTime.setText("���� " + (time1 - 12) + "�� " + min1 + "��");
		}

		if(min2==0)
			intervalTime.setText(time2 + "�� " + "00�� ����");
		else
			intervalTime.setText(time2 + "�� " + min2 + "�� ����");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id){
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
		}
		return null;
	}
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = 
	        new TimePickerDialog.OnTimeSetListener() {
	             
	            @Override
	            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	                // TODO Auto-generated method stub
	                mHour = hourOfDay;
	                mMinute = minute;
	                String query = "update settings set alarmtime = "+mHour+";";
					database.execSQL(query);
					query = "update settings set alarmmin = "+mMinute+";";
					database.execSQL(query);
					Month.setDiaryAlarmTime();
	                setT();
	            }
	        };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.editsetting);
		setT();

		Button fin = (Button) findViewById(R.id.completebtn);
		fin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		Button changeDiarybtn = (Button) findViewById(R.id.changediarytimebtn);
		changeDiarybtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast toast = Toast.makeText(mContext, "������ �ʿ��մϴ�. 1",Toast.LENGTH_SHORT);
				//toast.setGravity(Gravity.CENTER, 0, 0);
				//toast.show();
				showDialog(TIME_DIALOG_ID);
				
			}
		});

		Button changetermbtn = (Button) findViewById(R.id.changetimeintervalbtn);
		changetermbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast toast = Toast.makeText(mContext, "������ �ʿ��մϴ�",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				setT();
			}
		});

		Button changepwdbtn = (Button) findViewById(R.id.changepwdbtn);
		changepwdbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final LinearLayout linear = (LinearLayout) View.inflate(mContext,  R.layout.changepwd, null);
				new AlertDialog.Builder(mContext)
				.setTitle("��й�ȣ �����ϱ�")
				.setView(linear)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EditText pastpwd = (EditText) linear.findViewById(R.id.pastpwd);
						EditText newpwd = (EditText) linear.findViewById(R.id.newpwd);
						EditText newpwdre = (EditText) linear.findViewById(R.id.newpwdre);
						
						String pastpwdstr = pastpwd.getText().toString();
						String newpwdstr = newpwd.getText().toString();
						String newpwdrestr = newpwdre.getText().toString();
						
						if(pwd ==null)
						{
							if (newpwdstr.equals(newpwdrestr))
							{	
								String query = "update settings set password = '"+newpwdstr+"';";
								database.execSQL(query);
								Toast toast = Toast.makeText(mContext, "��й�ȣ�� ����Ǿ����ϴ�.",
										Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}
							else
							{
								Toast toast = Toast.makeText(mContext, "�� ��й�ȣ�� �޶��.",
										Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}
						}
						else if(pwd.equals(pastpwdstr))
						{
							if (newpwdstr.equals(newpwdrestr))
							{	
								String query = "update settings set password = '"+newpwdstr+"';";
								database.execSQL(query);
								Toast toast = Toast.makeText(mContext, "��й�ȣ�� ����Ǿ����ϴ�.",
										Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}
							else
							{
								Toast toast = Toast.makeText(mContext, "�� ��й�ȣ�� �޶��.",
										Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}
						}
						else
						{
							Toast toast = Toast.makeText(mContext, "���� ��й�ȣ�� Ʋ�Ⱦ��.",
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
						
					}
				})
				.setNegativeButton("���", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();

				setT();
			}
		});

	}
}
