package com.chatuml.chatuml;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String TAG = "ChatUML";
	
	private Button mChatButton;
	private Button mVolunteerLoginButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChatButton = (Button) findViewById(R.id.buttonChat);
        mVolunteerLoginButton = (Button) findViewById(R.id.buttonVolunteerLogin);
        
        /* Start chat activity */
        mChatButton.setOnClickListener(
    		new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i(TAG, "entered Chat onClickListener()");
					Intent intent = new Intent(v.getContext(), ConnectingActivity.class);
					startActivity(intent);
				}
    		}
        );
        mVolunteerLoginButton.setOnClickListener(
        	new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i(TAG, "entered Login onClickListener()");
					Toast.makeText(v.getContext(), "Not yet implemented!", Toast.LENGTH_SHORT).show();
				}
        	}
        );
    }

    @Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
