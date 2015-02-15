package com.example.didplan;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DailyCompCell {
	private String day;
	private String hour;
	private String latitude;
	private String longitude;
	private String content;
	private String Title;
	private String plancontent;
	private String plantitle;

	public DailyCompCell(String d, String h, String lat, String lon, String con, String Title, String plancon, String plant) {
		this.day = d;
		this.hour = h;
		this.latitude = lat;
		this.longitude = lon;
		this.content = con;
		this.Title = Title;
		this.plancontent=plancon;
		this.plantitle=plant;
	}

	public String getDay() {
		return day;
	}

	public String getPlancontent() {
		return plancontent;
	}

	public void setPlancontent(String plancontent) {
		this.plancontent = plancontent;
	}

	public String getPlantitle() {
		return plantitle;
	}

	public void setPlantitle(String plantitle) {
		this.plantitle = plantitle;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
