package com.chatuml.chatuml;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ConnectToServerTask extends AsyncTask<URL, Void, URLConnection> {

	Context mContext;
	
	public ConnectToServerTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected URLConnection doInBackground(URL... params) {
		URLConnection conn;
		try {
			conn = params[0].openConnection();
		} catch (IOException e) {
			return null;
		}
		return conn;
	}
	
	@Override
	protected void onPostExecute(URLConnection result) {
		if(result != null) {
			ConnectingActivity.setConnection(result);
			Intent intent = new Intent(mContext, ChatActivity.class);
			mContext.startActivity(intent);
		} else {
			((Activity)mContext).finish();
		}
	}
}
