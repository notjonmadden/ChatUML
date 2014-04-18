package com.chatuml.chatuml;

import java.util.LinkedList;
import java.util.Queue;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class XmppServerThread extends Thread {
	private ChatBubbleListAdapter mAdapter;
	private Queue<String> mOutgoing = new LinkedList<String>();
	private Queue<String> mIncoming = new LinkedList<String>();
	private Queue<String> createChatList = new LinkedList<String>();
	private Context mContext;
	private ChatManager mChatManager;
	private boolean killed = false;
	private Chat mChat;
	private Connection mConn;
	
	public XmppServerThread(ChatBubbleListAdapter adapter, Context context) {
		mAdapter = adapter;
		mContext = context;
		mConn = XMPPConnectionHolder.getInstance().getConnection();
	}
	
	@Override
	public void run() {
		if(mConn == null) {
			Log.e(MainActivity.TAG, "No connection to XMPP server!");
			return;
		}
		Log.i(MainActivity.TAG, "XmppServerThread running");
		while (!killed) {
			
			if (createChatList.isEmpty() == false) {
				mChatManager = mConn.getChatManager();
				String chatPartner = createChatList.remove();
				mChat = mChatManager.createChat( chatPartner , new MessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) {
						mIncoming.add( message.getBody() );
					}
				});
			}
			
			if (mOutgoing.isEmpty() == false && mChat != null) {
				Message newMessage = new Message();
				String messageText = mOutgoing.remove();
				newMessage.setBody(messageText);
				try {
					mChat.sendMessage(newMessage);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
			
			if (mIncoming.isEmpty() == false) {
				String toAdapter = mIncoming.remove();
				final RelativeLayout rl = 
						(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.chat_msg_layout, null, false);
				ChatBubbleView bubble = (ChatBubbleView) rl.findViewById(R.id.chatBubble);
				bubble.setText(toAdapter);
				bubble.setOrigin(ChatBubbleView.ORIGIN_RECV);
				( (Activity) mContext ).runOnUiThread( new Runnable() {
					@Override
					public void run() {
						mAdapter.addView(rl);
					}
				});
				
				
			}
			
		}
		
		Log.i(MainActivity.TAG, "XmppServerThread terminating");
		mConn.disconnect();
		
	}
	
	public void createChat(String id) {
		createChatList.add(id);	
	}
	
	public void sendMsg(String messageBody) throws XMPPException {
		//TODO implement sendMsgs

		mOutgoing.add(messageBody);
	}
	
	public void kill() {
		killed = true;
	}
	

}
