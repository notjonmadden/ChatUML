package com.chatuml.chatuml;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static final String TAG = "ChatUML";
	public static final String USERNAME = "uname";
	public static final String PASSWORD = "pw";
	public static final String IS_VOLUNTEER = "isvol";
	
	private Button mChatButton;
	private Button mVolunteerLoginButton;
	private SharedPreferences mPrefs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChatButton = (Button) findViewById(R.id.buttonChat);
        mVolunteerLoginButton = (Button) findViewById(R.id.buttonVolunteerLogin);
        mPrefs = getPreferences(0);
        
        /* Start chat activity */
        mChatButton.setOnClickListener(
    		new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i(TAG, "entered Chat onClickListener()");
					final Context context = v.getContext();
					final Intent intent = new Intent(context, ConnectingActivity.class);
					final View view = LayoutInflater.from(context).inflate(R.layout.login_user, null);
					final EditText et = (EditText) view.findViewById(R.id.user_uname);
					et.setText(mPrefs.getString(USERNAME, ""));
					AlertDialog.Builder b = new AlertDialog.Builder(context);
					b.setView(view)
					 .setTitle("Enter username")
					 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(TAG, "Starting ConnectingActivity as user");
							mPrefs.edit().putString(USERNAME, et.getText().toString());
							intent.putExtra(USERNAME, et.getText().toString());
							intent.putExtra(IS_VOLUNTEER, false);
							context.startActivity(intent);
						}
					})
					 .setNegativeButton("Cancel", null)
					 .show();
				}
    		}
        );
        mVolunteerLoginButton.setOnClickListener(
        	new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i(TAG, "entered Login onClickListener()");
					final Context context = v.getContext();
					final Intent intent = new Intent(context, ConnectingActivity.class);
					final View view = 
						LayoutInflater.from(context).inflate(R.layout.login_volunteer, null);
					final EditText uname = (EditText) view.findViewById(R.id.volunteer_uname);
					final EditText pw = (EditText) view.findViewById(R.id.volunteer_pw);
					uname.setText(mPrefs.getString(USERNAME, ""));
					pw.setText(mPrefs.getString(PASSWORD, ""));
					final CheckBox cb = (CheckBox) view.findViewById(R.id.volunteer_remember_pw);
					AlertDialog.Builder b = new AlertDialog.Builder(context);
					b.setView(view)
					 .setTitle("Enter username and password")
					 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(TAG, "Starting ConnectingActivity as Volunteer");
							mPrefs.edit().putString(USERNAME, uname.getText().toString()).commit();
							if(cb.isChecked()) {
								mPrefs.edit().putString(PASSWORD, pw.getText().toString()).commit();
							}
							intent.putExtra(USERNAME, uname.getText().toString());
							intent.putExtra(PASSWORD, pw.getText().toString());
							intent.putExtra(IS_VOLUNTEER, true);
							context.startActivity(intent);
						}
					 })
					 .setNegativeButton("Cancel", null)
					 .show();
				}
        	}
        );
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
