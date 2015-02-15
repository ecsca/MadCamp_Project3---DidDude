package com.example.didplan;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {

	private ArrayList<DayInfo> mDayList;
	private Context mContext;
	private int mResource;
	private LayoutInflater mLiInflater;

	public CalendarAdapter(Context context, int textResource,
			ArrayList<DayInfo> dayList) {
		this.mContext = context;
		this.mDayList = dayList;
		this.mResource = textResource;
		this.mLiInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;

		DayInfo day = mDayList.get(position);

		DayViewHolde dayViewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(mResource, null);

			convertView.setLayoutParams(new GridView.LayoutParams(
					screenWidth / 7, screenHeight * 3 / 4 / 7));

			dayViewHolder = new DayViewHolde();

			dayViewHolder.llBackground = (LinearLayout) convertView
					.findViewById(R.id.dayCell);
			dayViewHolder.tvDay = (TextView) convertView
					.findViewById(R.id.dayCellDate);

			convertView.setTag(dayViewHolder);
		} else {
			dayViewHolder = (DayViewHolde) convertView.getTag();
		}

		if (day != null) {
			dayViewHolder.tvDay.setText(day.getDay());
			if (day.isInMonth()) {
				if (position % 7 == 0) {
					dayViewHolder.tvDay.setTextColor(Color.RED);
				} else if (position % 7 == 6) {
					dayViewHolder.tvDay.setTextColor(Color
							.parseColor("#33b5e5"));
				} else {
					dayViewHolder.tvDay.setTextColor(Color.BLACK);
				}
			} else {
				dayViewHolder.tvDay.setTextColor(Color.GRAY);
			}
		}
		return convertView;
	}

	public class DayViewHolde {
		public LinearLayout llBackground;
		public TextView tvDay;

	}

	public int getDay(int position) {
		DayInfo day = mDayList.get(position);
		return Integer.parseInt(day.getDay());
	}

}