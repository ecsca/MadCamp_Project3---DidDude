package com.example.didplan;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class worker extends AsyncTask<Void, Void, Bitmap>{

	private Drawable img;
	private String surl;
	private Context context = null;
	public AsyncResponse delegate = null;
	private ImageView mImageView;

	public worker(ImageView imageView, String givenUrl, Context givenContext){
		this.mImageView=imageView;
		this.surl = givenUrl;
		Log.v("pic", surl);
		this.context = givenContext;
	}
	public interface AsyncResponse
	{
		void processFinish(String output);
	}
	
	public Bitmap downloadBitmap(String imageUrl){
		Bitmap bm = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(imageUrl);
		HttpResponse response;
		
		try {
			response = httpclient.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK){
				Log.w("egg", "error");
				return null;
			}
			HttpEntity entity = response.getEntity();
			if(entity !=null){
				InputStream inputStream = null;
				inputStream = entity.getContent();
				final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				bm = bitmap;
				Log.v("pic", "in if");
				if (inputStream != null){
					inputStream.close();
				}
				entity.consumeContent();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}
	
	private void writeFile(Bitmap bmp, File f){
		FileOutputStream out = null;
		
		try {
			out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG,  30,  out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			try {
				if (out!=null)
				{
				out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	private boolean sameAs(Bitmap bitmap1, Bitmap bitmap2){
		ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
		bitmap1.copyPixelsToBuffer(buffer1);
		
		ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
		bitmap2.copyPixelsToBuffer(buffer2);
		
		return Arrays.equals(buffer1.array(),  buffer2.array());
	}
	

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		String fileFullPath = urlToFileFullPath(surl);
		
		if( new File(fileFullPath).exists()){
			Bitmap myBitmap = BitmapFactory.decodeFile(fileFullPath);
			mImageView.setImageBitmap(myBitmap);
		}
		
	
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		String fileFullPath = urlToFileFullPath(surl);
		//String tempFilePath = fileFullPath+"_temp";
		
		//writeFile(result, new File(tempFilePath));
		//File downTempFile = new File(tempFilePath);
		//File newFile = new File(fileFullPath);
		//Log.v("pic", "file full path "+fileFullPath);
		
		if(new File(fileFullPath).exists()){
			/*
			Bitmap prevBitmap = BitmapFactory.decodeFile(fileFullPath);
			Log.v("pic", fileFullPath);
			Bitmap downBitmap = BitmapFactory.decodeFile(downTempFile.getAbsolutePath());
			
			Log.v("pic","before sameAs");
			if(sameAs(prevBitmap, downBitmap)){
				Log.v("same", "same");
				
			}
			else
			{
				writeFile(result, newFile);
				mImageView.setImageBitmap(result);
			}
			*/
			}
			else
			{
				Log.v("pic","else");
//				writeFile(result, new File(tempFilePath)newFile);
				writeFile(result, new File(fileFullPath));
				mImageView.setImageBitmap(result);
			}
	}
	@Override
	protected Bitmap doInBackground(Void... params){
		// TODO Auto-generated method stub
		
		String fileFullPath = urlToFileFullPath(surl);
		if(new File(fileFullPath).exists()){
			return null;
		}
		
		else
		{
			Log.v("new", surl);
			return downloadBitmap(surl);
		}
	}
	
	private String urlToFileFullPath(String _url){
		return context.getCacheDir().getAbsolutePath()+_url.substring(_url.lastIndexOf("/"), _url.length());
	}
	





	}