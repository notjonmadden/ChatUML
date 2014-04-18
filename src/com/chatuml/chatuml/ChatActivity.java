package com.chatuml.chatuml;

import org.jivesoftware.smack.XMPPException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChatActivity extends Activity {

	private LayoutInflater mInflater;
	
	private EditText mEditText;
	private Button mButtonSend;
	private ListView mListView;
	private ChatBubbleListAdapter mListAdapter;	
	
	private XmppServerThread mThread;
	
	private static final String ORIGINS_KEY = "saved_origins";
	private static final String MSGS_KEY = "saved_msgs";
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(MainActivity.TAG, "onCreate");
		mListAdapter = new ChatBubbleListAdapter();

		
		setContentView(R.layout.activity_chat);
		mInflater = LayoutInflater.from(this);


		
		mEditText = (EditText) findViewById(R.id.editTextMsg);
		mButtonSend = (Button) findViewById(R.id.buttonSend);
		mListView = (ListView) findViewById(R.id.listViewMsgs);
		mListView.setDivider(null);
		mListView.setDividerHeight(5);
		
		
		mListView.setAdapter(mListAdapter);
		mButtonSend.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					/* inflate the layout for insertion into list of chat bubbles */
					RelativeLayout rl = 
							(RelativeLayout) mInflater.inflate(R.layout.chat_msg_layout, mListView, false);
					ChatBubbleView bubble = (ChatBubbleView) rl.findViewById(R.id.chatBubble);
					
					
					String inputText = mEditText.getText().toString();
					
					
					bubble.setText(inputText);
					bubble.setOrigin(ChatBubbleView.ORIGIN_SENT);
					mListAdapter.addView(rl);
					try {
						mThread.sendMsg(inputText);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
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
		mThread = new XmppServerThread(mListAdapter, this);
		mThread.start();
		mThread.createChat("jmadden@chatuml.com");
		Toast.makeText(this, "Welcome! You're now connected to a crisis specialist.", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mThread.kill();
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

    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.chat_options_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.new_vol:
    		// TODO implement case
    		return true;
    	case R.id.help:
    		// TODO implement help/about us
    		return true;
    	}
    }
    */
}
