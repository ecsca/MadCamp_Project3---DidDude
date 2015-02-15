package com.example.didplan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.didplan.R;

public class EditPlanplan extends Activity implements OnClickListener {
	private SQLiteDatabase database = null;
	final String databaseName = "DidDude.db";
	String day;
	String month;
	String year;
	String hour;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editplan);
		Intent dayIntent = getIntent();
		Bundle ex = dayIntent.getExtras();
		day = ex.getString("day");
		month = ex.getString("month");
		year = ex.getString("year");
		hour = ex.getString("hour");
		Button writebtn = (Button) findViewById(R.id.writePlan);
		Button cancelbtn = (Button) findViewById(R.id.cancelPlan);
		database = openOrCreateDatabase(databaseName,
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		String squery = "SELECT * FROM " + "'" + year + "." + month
				+ "' WHERE day = " + day + " and hour = "+ hour +";";
		// String squery = "SELECT * FROM " + "'"+"test.test" + "'";// WHERE day
		// = " + day + ";";
		Cursor c = database.rawQuery(squery, null);
		

		c.moveToFirst();

		String content = c.getString(6);
		String Title = c.getString(7);

		EditText editT = (EditText) findViewById(R.id.editTitle);
		EditText editContent = (EditText) findViewById(R.id.editContent);
		editT.setText(Title);
		editContent.setText(content);
		writebtn.setOnClickListener(this);
		cancelbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) 
		{
			case R.id.writePlan:
				EditText content = (EditText) findViewById(R.id.editContent);
				EditText title = (EditText) findViewById(R.id.editTitle);
				String strCont = content.getText().toString();
				String strTitle = title.getText().toString();
				Log.i("kkk","planplan");
				String q = "UPDATE '"+year+"."+month+"' set plancontent = '"+strCont + "' where hour = "+hour + " and day = "+day + " ;";
				database.execSQL(q);
				q = "UPDATE '"+year+"."+month+"' set plantitle = '"+strTitle + "' where hour = "+hour + " and day = "+day + " ;";
				database.execSQL(q);
				finish();
				break;
			case R.id.cancelPlan:
				finish();
				break;
		}

	}
}
