package com.example.didplan;

import java.util.ArrayList;

import com.example.didplan.DailyAdapter.locListener;

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
import android.widget.ListView;
import android.widget.TextView;

public class DailyShowAdapter extends BaseAdapter {

	private ArrayList<DailyCell> dayCell;
	private Context mContext;
	private int mResource;
	private LayoutInflater mLiInflater;
	private String year;
	private String month;

	public DailyShowAdapter(Context context, int textResource,
			ArrayList<DailyCell> daycell, String year, String month) {
		this.dayCell = daycell;
		this.mContext = context;
		this.mResource = textResource;
		this.mLiInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;

		if (convertView == null) {
			convertView = mLiInflater.inflate(mResource, parent, false);

		}
		convertView.setLayoutParams(new ListView.LayoutParams(screenWidth,
				screenHeight / 7));
		TextView hour = (TextView) convertView.findViewById(R.id.hourCellTime);
		TextView plan = (TextView) convertView.findViewById(R.id.hourCellPlan);
		ImageView loc = (ImageView) convertView.findViewById(R.id.hourCellLoc);
		TextView Title = (TextView) convertView.findViewById(R.id.hourCellPlanTitle);
		Title.setText(dayCell.get(position).getTitle());
		loc.setOnClickListener(new locListener(position, dayCell, this));
		// plan.setText("abcba");
		// plan.setOnClickListener(new mListener(position, dayCell, this));

		// hour.setText(cell.getHour());
		// plan.setText(cell.getContent());
		hour.setText(dayCell.get(position).getHour()+"½Ã");
		plan.setText(cell.getContent());
		// if(!(Integer.parseInt(cell.getLatitude())==0 &&
		// Integer.parseInt(cell.getLongitude())==0))
		// {
		if(cell.getLatitude().contentEquals("0.0") && cell.getLongitude().contentEquals("0.0")){
	         loc.setImageResource(R.drawable.defaults);
	      }
	      else{
	         new worker(loc, "http://maps.googleapis.com/maps/api/staticmap?center="+cell.getLatitude()+","+cell.getLongitude()+"&zoom=17&size=100x100&markers=color:blue%7Clabel:S%7C"+cell.getLatitude()+","+cell.getLongitude()+"&sensor=true_or_false", mContext).execute();
	         //loc.setText(cell.getLatitude() +","+ cell.getLongitude() +"");
	      }
	      return convertView;
	}

	public void callEditPlan(String hour, String day) {
		Intent editor = new Intent(mContext, EditPlan.class);
		editor.putExtra("hour", hour);
		editor.putExtra("day", day);
		editor.putExtra("month", month);
		editor.putExtra("year", year);
		((Activity) mContext).startActivityForResult(editor, 1);
		// Log.e("utf","activity finished");

	}

	public class mListener implements OnClickListener {
		int pos;
		ArrayList<DailyCell> dayCell;
		DailyShowAdapter ad;

		public mListener(int pos, ArrayList<DailyCell> dayCell,
				DailyShowAdapter ad) {
			this.pos = pos;
			this.dayCell = dayCell;
			this.ad = ad;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ad.callEditPlan(dayCell.get(pos).getHour(), dayCell.get(pos)
					.getDay());
		}

	}

	public class locListener implements OnClickListener {
		int pos;
		ArrayList<DailyCell> dayCell;
		DailyShowAdapter ad;

		public locListener(int pos, ArrayList<DailyCell> dayCell,
				DailyShowAdapter ad) {
			this.pos = pos;
			this.dayCell = dayCell;
			this.ad = ad;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ad.callMap(dayCell.get(pos).getLatitude(), dayCell.get(pos)
					.getLongitude());
		}

	}

	public void callMap(String lat, String lon) {
		Intent editor = new Intent(mContext, OnlyshowMap.class);
		editor.putExtra("lat", lat);
		editor.putExtra("lon", lon);
		((Activity) mContext).startActivityForResult(editor, 1);
	}
}