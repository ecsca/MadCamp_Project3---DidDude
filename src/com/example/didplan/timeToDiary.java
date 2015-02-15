package com.example.didplan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class timeToDiary extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("kkk", "alim on");
		/*
		Date currentTime = new Date ( );
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
		String mTime = mSimpleDateFormat.format ( currentTime );
		String y = mTime.split("\\.")[0];
		String m = mTime.split("\\.")[1];
		String d = mTime.split("\\.")[2].split("\\s+")[0];
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
		Intent t = new Intent(context.getApplicationContext(), Daily.class);
		t.putExtra("day",  d);
		t.putExtra("month",  m);
		t.putExtra("year",  y);
		*/
		Intent t = new Intent(context.getApplicationContext(), CheckPwdtoEdit.class);
		PendingIntent dPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, t, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationManager getDiary = (NotificationManager) (context.getSystemService(Context.NOTIFICATION_SERVICE));
		
		Notification diaryNoti = new NotificationCompat.Builder(context.getApplicationContext())
				.setContentTitle("일기 쓸 시간~")
				.setContentText("오늘 한 일을 기록해보세요")
				.setSmallIcon(R.drawable.diary)
				.setTicker("일기 쓸 시간~")
				.setAutoCancel(true)
				.setContentIntent(dPendingIntent)
				.build();
		getDiary.notify(7777, diaryNoti);
		Log.i("kkk", "alim on");
	}
}