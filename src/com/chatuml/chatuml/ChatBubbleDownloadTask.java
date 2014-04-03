package com.chatuml.chatuml;

import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class ChatBubbleDownloadTask extends AsyncTask<URLConnection, RelativeLayout, Void> {

	Context mContext;
	ChatBubbleListAdapter mAdapter;
	
	public ChatBubbleDownloadTask(Context context, ChatBubbleListAdapter adapter) {
		mContext = context;
		mAdapter = adapter;
	}
	
	@Override
	protected Void doInBackground(URLConnection... params) {
		while(!isCancelled()) {
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				Log.i(MainActivity.TAG, "MsgDownloadTask interrupted");
				break;
			}
			RelativeLayout rl = 
					(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.chat_msg_layout, null, false);
			ChatBubbleView bubble = (ChatBubbleView) rl.findViewById(R.id.chatBubble);
			bubble.setText("Hello!");
			bubble.setOrigin(ChatBubbleView.ORIGIN_RECV);
			publishProgress(rl);
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(RelativeLayout... progress) {
		mAdapter.addView(progress[0]);
	}

}
