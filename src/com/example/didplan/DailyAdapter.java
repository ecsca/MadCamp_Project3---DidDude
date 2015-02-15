package com.example.didplan;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DailyAdapter extends BaseAdapter{
	
	private ArrayList<DailyCell> dayCell;
	private Context mContext;
	private int mResource;
	private LayoutInflater mLiInflater;
	private String year;
	private String month;
	
	public DailyAdapter(Context context, int textResource, ArrayList<DailyCell> daycell, String year, String month)
	{
		this.dayCell = daycell;
		this.mContext = context;
		this.mResource = textResource;
		this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.year = year;
		this.month = month;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayCell.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dayCell.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		DailyCell cell = dayCell.get(position);
    	DisplayMetrics metrics = new DisplayMetrics();
    	WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
    	windowManager.getDefaultDisplay().getMetrics(metrics);
    	int screenWidth = metrics.widthPixels;
    	int screenHeight = metrics.heightPixels;
		
		
		if(convertView == null)
		{
			convertView = mLiInflater.inflate(mResource,  parent, false);
			
		}
		convertView.setLayoutParams(new ListView.LayoutParams(screenWidth, screenHeight/7));
		TextView hour = (TextView) convertView.findViewById(R.id.hourCellTime);
		TextView plan = (TextView) convertView.findViewById(R.id.hourCellPlan);
		TextView Title = (TextView) convertView.findViewById(R.id.hourCellPlanTitle);
		LinearLayout a = (LinearLayout) convertView.findViewById(R.id.contentCell);
		ImageView loc = (ImageView) convertView.findViewById(R.id.hourCellLoc);
		//plan.setText("abcba");
		Title.setText(dayCell.get(position).getTitle());
		hour.setText(dayCell.get(position).getHour()+"½Ã");
		plan.setText(dayCell.get(position).getContent());
		loc.setOnClickListener(new locListener(position, dayCell, this));
		a.setOnClickListener(new mListener(position, dayCell, this));
		if(cell.getLatitude().contentEquals("0.0") && cell.getLongitude().contentEquals("0.0")){
	         loc.setImageResource(R.drawable.defaults);
	      }
	      else{
	         new worker(loc, "http://maps.googleapis.com/maps/api/staticmap?center="+cell.getLatitude()+","+cell.getLongitude()+"&zoom=17&size=100x100&markers=color:blue%7Clabel:S%7C"+cell.getLatitude()+","+cell.getLongitude()+"&sensor=true_or_false", mContext).execute();
	         //loc.setText(cell.getLatitude() +","+ cell.getLongitude() +"");
	      }
	      return convertView;
	}
	
	public void callEditPlan(String hour, String day)
	{
		Intent editor = new Intent(mContext, EditPlan.class);
		editor.putExtra("hour",  hour);
		editor.putExtra("day",  day);
		editor.putExtra("month", month);
		editor.putExtra("year", year);
		((Activity) mContext).startActivityForResult(editor, 1);
		//Log.e("utf","activity finished");
		
	}

	public class mListener implements OnClickListener
	{
		int pos;
		ArrayList<DailyCell> dayCell;
		DailyAdapter ad;
		public mListener(int pos, ArrayList<DailyCell> dayCell, DailyAdapter ad)
		{
			this.pos= pos;
			this.dayCell = dayCell;
			this.ad = ad;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ad.callEditPlan(dayCell.get(pos).getHour(), dayCell.get(pos).getDay());
		}
		
	}
	public class locListener implements OnClickListener
	{
		int pos;
		ArrayList<DailyCell> dayCell;
		DailyAdapter ad;
		public locListener(int pos, ArrayList<DailyCell> dayCell, DailyAdapter ad)
		{
			this.pos= pos;
			this.dayCell = dayCell;
			this.ad = ad;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ad.callMap(dayCell.get(pos).getLatitude(), dayCell.get(pos).getLongitude(), dayCell.get(pos).getDay(), dayCell.get(pos).getHour());
			
			
		}
		
	}
	public void callMap(String lat, String lon, String day, String hour)
	{
		Intent editor = new Intent(mContext, showMap.class);
		editor.putExtra("lat",  lat);
		editor.putExtra("lon", lon);
		editor.putExtra("year",  year);
		editor.putExtra("month",  month);
		editor.putExtra("day",  day);
		editor.putExtra("hour", hour);
		((Activity) mContext).startActivityForResult(editor, 1);
	}
}