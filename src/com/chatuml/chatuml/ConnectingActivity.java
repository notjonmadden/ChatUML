package com.chatuml.chatuml;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ConnectingActivity extends Activity {

	private ConnectToServerTask mConnectToServerTask;
	private static URLConnection mConnection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connecting);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			mConnectToServerTask = new ConnectToServerTask(this);
			mConnectToServerTask.execute(new URL("http://www.cs.uml.edu"));
		} catch (MalformedURLException e) {
			
		}
	}
	
	public static void setConnection(URLConnection conn) {
		mConnection = conn;
	}

	public static URLConnection getConnection() {
		return mConnection;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connecting, menu);
		return true;
	}

}
