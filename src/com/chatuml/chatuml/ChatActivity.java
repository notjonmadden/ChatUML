package com.chatuml.chatuml;

import java.net.URLConnection;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChatActivity extends Activity {

	private LayoutInflater mInflater;
	private ChatBubbleDownloadTask mMsgDownloader;
	private URLConnection mMsgConnection;
	
	private EditText mEditText;
	private Button mButtonSend;
	private ListView mListView;
	private ChatBubbleListAdapter mListAdapter;	
	
	private static final String ORIGINS_KEY = "saved_origins";
	private static final String MSGS_KEY = "saved_msgs";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mInflater = LayoutInflater.from(this);
		mMsgConnection = ConnectingActivity.getConnection();
		
		mEditText = (EditText) findViewById(R.id.editTextMsg);
		mButtonSend = (Button) findViewById(R.id.buttonSend);
		mListView = (ListView) findViewById(R.id.listViewMsgs);
		mListView.setDivider(null);
		mListView.setDividerHeight(5);
		mListAdapter = new ChatBubbleListAdapter();//(this);
		
		mListView.setAdapter(mListAdapter);
		mButtonSend.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					/* inflate the layout for insertion into list of chat bubbles */
					RelativeLayout rl = 
							(RelativeLayout) mInflater.inflate(R.layout.chat_msg_layout, mListView, false);
					ChatBubbleView bubble = (ChatBubbleView) rl.findViewById(R.id.chatBubble);
					bubble.setText(mEditText.getText().toString());
					bubble.setOrigin(ChatBubbleView.ORIGIN_SENT);
					mListAdapter.addView(rl);
					
					/* clear input for next msg */
					mEditText.setText("");
				}
			}
		);
		
		if(savedInstanceState != null) {
			/* recreate previously sent messages */
			int[] origins = savedInstanceState.getIntArray(ORIGINS_KEY);
			String[] msgs = savedInstanceState.getStringArray(MSGS_KEY);
			for(int i = 0 ; i < origins.length ; ++i) {
				RelativeLayout rl = 
						(RelativeLayout) mInflater.inflate(R.layout.chat_msg_layout, mListView, false);
				ChatBubbleView bubble = (ChatBubbleView) rl.findViewById(R.id.chatBubble);
				bubble.setText(msgs[i]);
				bubble.setOrigin(origins[i]);
				mListAdapter.addView(rl);				
			}
		}
		
		Toast.makeText(this, "Welcome! You're now connected to a crisis specialist.", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//TODO consider whether msg downloading should be done with a more advanced thread class
		mMsgDownloader = new ChatBubbleDownloadTask(this, mListAdapter);
		mMsgDownloader.execute(mMsgConnection);
	}
	
	@Override 
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mMsgDownloader.cancel(true);
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
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		/* create arrays of everything we need to represent a message */
		String[] msgs = new String[mListView.getChildCount()];
		int[] origins = new int[mListView.getChildCount()];
		for(int i = 0 ; i < mListView.getChildCount() ; ++i) {
			RelativeLayout rl = (RelativeLayout) mListView.getChildAt(i);
			ChatBubbleView bubble = (ChatBubbleView) rl.findViewById(R.id.chatBubble);
			msgs[i] = (bubble.getText());
			origins[i] = (bubble.getOrigin());
		}
		savedInstanceState.putIntArray(ORIGINS_KEY, origins);
		savedInstanceState.putStringArray(MSGS_KEY, msgs);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

}
