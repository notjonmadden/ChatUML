package com.chatuml.chatuml;

import org.jivesoftware.smack.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ConnectToServerTask extends AsyncTask<String, Void, Boolean> {

	Context mContext;
	
	public ConnectToServerTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		XMPPConnection conn;
		try {
			SmackAndroid.init(mContext);
			AndroidConnectionConfiguration config = 
				new AndroidConnectionConfiguration("129.63.16.140", 5222);
			if(params[2] == "false") {
				config.setServiceName("chatuml.com");
			} else {
				config.setServiceName("volunteer.chatuml.com");
			}
			config.setSendPresence(false);
			conn = new XMPPConnection(config);
			conn.connect();
			Log.i(MainActivity.TAG, "Connected");
			conn.login(params[0], params[1] != null ? params[1] : "password");
			Log.i(MainActivity.TAG, "Logged in");
		} catch (Exception e) {
			Log.e(MainActivity.TAG, e.getMessage());
			return false;
		}
		
		XMPPConnectionHolder.getInstance().setConnection(conn);
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(result != null && result == true) {
			Intent intent = new Intent(mContext, ChatActivity.class);
			mContext.startActivity(intent);
		} else {
			Toast.makeText(mContext, "Failed to connect", Toast.LENGTH_LONG).show();
		}
		((Activity)mContext).finish();
	}
}
