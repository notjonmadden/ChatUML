package com.chatuml.chatuml;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class ConnectingActivity extends Activity {

	private ConnectToServerTask mConnectToServerTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connecting);
		Intent creator = getIntent();
		String uname = creator.getStringExtra(MainActivity.USERNAME);
		String pw = creator.getStringExtra(MainActivity.PASSWORD);
		boolean iv = creator.getBooleanExtra(MainActivity.IS_VOLUNTEER, false);
		Log.i(MainActivity.TAG, "ConnectingActivity.onCreate()");
		mConnectToServerTask = new ConnectToServerTask(this);
		mConnectToServerTask.execute(uname, pw, iv ? "true" : "false");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connecting, menu);
		return true;
	}

}
