package com.example.didplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

public class CheckPwd extends Activity {
	final String databaseName = "DidDude.db";
	private SQLiteDatabase database = null;
	private String password;
	public Context mContext; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkpwd);
		mContext = this;
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
					+ "22,0,1,0 " + "where not exists ( select 1 from "
					+ "'settings'" + " );";
			database.execSQL(query);
		}
		String squery = "SELECT * FROM " + "'settings';";
		Cursor c = database.rawQuery(squery, null);
		c.moveToFirst();
		password = c.getString(4);
		if(password==null)
		{
			Intent startapp = new Intent(mContext,Month.class);
			startActivity(startapp);
			finish();
		}
		
		Button check = (Button) findViewById(R.id.checkpwd);
		check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText pwd = (EditText) findViewById(R.id.getpwd);
				String strpwd = pwd.getText().toString();
				
				if(password.equals(strpwd))
				{				
					Intent startapp = new Intent(mContext,Month.class);
					startActivity(startapp);
					finish();
				}
				else
				{
					Toast toast = Toast.makeText(mContext, "비밀번호가 틀렸어요.",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});

		
	}
}