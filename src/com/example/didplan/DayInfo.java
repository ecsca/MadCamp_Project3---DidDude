package com.example.didplan;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class DayInfo {
	private String day;
	private boolean inMonth;
	
	public String getDay()
	{
		return day;
	}
	
	public void setDay(String day)
	{
		this.day = day;
	}
	public boolean isInMonth()
	{
		return inMonth;
	}
	public void setInMonth(boolean inMonth)
	{
		this.inMonth=inMonth;
	}
}