package com.chatuml.chatuml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
	
	//commands from parent
	private Queue<String> createChatList = new LinkedList<String>();
	
	Activity mParent;
	private LayoutInflater mInflater;
	private boolean killed = false;

	private ChatManager mChatManager;
	
	//active chat sessions
	private List<Chat> mChats = new ArrayList<Chat>();
	
	private Map<String, ChatBubbleListAdapter> mAdapterMap =
		new HashMap<String, ChatBubbleListAdapter>();
	
	//chat session id -> outgoing messages
	private Map<String, Queue<String>> mOutgoingMap = 
		new HashMap<String, Queue<String>>();
	
	//chat session id -> received messages
	private Map<String, Queue<String>> mIncomingMap =
		new HashMap<String, Queue<String>>();
	
	private Connection mConn;
	
	public XmppServerThread(ChatBubbleListAdapter adapter, Context context) {
		mAdapter = adapter;
		mParent = (Activity) context;
		mInflater = LayoutInflater.from(mParent);
		mConn = XMPPConnectionHolder.getInstance().getConnection();
		mChatManager = mConn.getChatManager();
	}
	
	@Override
	public void run() {
		if(mConn == null) {
			Log.e(MainActivity.TAG, "No connection to XMPP server!");
			return;
		}
		Log.i(MainActivity.TAG, "XmppServerThread running");
		while (!killed) {
			
			if(!mConn.isConnected()) {
				Log.e(MainActivity.TAG, "XmppConnection lost!");
				//TODO handle connection failure gracefully
				return;
			}
			
			while(!createChatList.isEmpty()) {
				String chatPartner = createChatList.remove();
				Chat chat = mChatManager.createChat( chatPartner , new MessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) {
						//mIncoming.add( message.getBody() );
						mIncomingMap.get(chat.getParticipant()).add(message.getBody());
					}
				});
				//save the chat
				mChats.add(chat);
				//create queues for messages to/from this chat
				mOutgoingMap.put(chat.getParticipant(), new LinkedList<String>());
				mIncomingMap.put(chat.getParticipant(), new LinkedList<String>());
			}
			
			//send any queued up messages,
			//and post any received messages
			for(final Chat c : mChats) {
				Queue<String> out = mOutgoingMap.get(c.getParticipant());
				Message m;
				while(!out.isEmpty()) {
					m = new Message();
					m.setBody(out.remove());
					try {
						c.sendMessage(m);
						Log.i(MainActivity.TAG, "sent message " + m.getBody());
					} catch (XMPPException e) {
						Log.e(MainActivity.TAG, "message failed to send");
						//TODO retry failed messages
						//     perhaps a special failed list?
					}
				}
				Queue<String> in = mIncomingMap.get(c.getParticipant());
				while(!in.isEmpty()) {
					addToAdapter(in.remove(), c.getParticipant());
					Log.i(MainActivity.TAG, "received message");
				}
			}
			
			//check for commands to create a new chat
			/*if (createChatList.isEmpty() == false) {
				String chatPartner = createChatList.remove();
				Chat chat = mChatManager.createChat( chatPartner , new MessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) {
						mIncoming.add( message.getBody() );
					}
				});
				//save the chat
				mChats.add(chat);
				//create queues for messages to & from this chat
				mOutgoingMap.put(chat.getParticipant(), new LinkedList<String>());
				mIncomingMap.put(chat.getParticipant(), new LinkedList<String>());
			}*/
			
			//send messages if there are any to send
			/*if (mOutgoing.isEmpty() == false && mChat != null) {
				Message newMessage = new Message();
				String messageText = mOutgoing.remove();
				newMessage.setBody(messageText);
				try {
					mChat.sendMessage(newMessage);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}*/
			
			/*if (mIncoming.isEmpty() == false) {
				String toAdapter = mIncoming.remove();
				final RelativeLayout rl = 
						(RelativeLayout) mInflater.inflate(R.layout.chat_msg_layout, null, false);
			}*/
			
		}
		
		Log.i(MainActivity.TAG, "XmppServerThread terminating");
		mConn.disconnect();
		
	}
	
	public void createChat(String participant) {
		synchronized (this) {
			createChatList.add(participant);
		}
		
	}
	
	public boolean destroyChat(String participant) {
		synchronized (this) {
			for(int i = 0 ; i < mChats.size() ; ++i) {
				if(mChats.get(i).getParticipant() == participant) {
					mChats.remove(i);
					return true;
				}
			}
			return false;
		}
	}
	
	public void sendMsg(String messageBody) throws XMPPException {
		//mOutgoing.add(messageBody);
	}
	
	public void sendMsg(String msg, String participant) throws NoSuchElementException {
		synchronized (this) {
			Queue<String> out = mOutgoingMap.get(participant);
			if(out == null) {
				throw new NoSuchElementException("No chat exists with user " + participant);				
			} else {
				out.add(msg);
			}
		}
	}
	
	public List<String> getChatParticipants() {
		List<String> rs = new ArrayList<String>();
		for(final Chat c : mChats) {
			rs.add(c.getParticipant());
		}
		return rs;
	}
	
	public void kill() {
		killed = true;
	}
	
	private void addToAdapter(String msg, final String participant) {
		final RelativeLayout rl = 
			(RelativeLayout) mInflater.inflate(R.layout.chat_msg_layout, null, false);
		ChatBubbleView bubble = (ChatBubbleView) rl.findViewById(R.id.chatBubble);
		bubble.setText(msg);
		bubble.setOrigin(ChatBubbleView.ORIGIN_RECV);
		mParent.runOnUiThread( new Runnable() {
			@Override
			public void run() {
				//mAdapter.addView(rl);
				mAdapterMap.get(participant).addView(rl);
			}
		});
	}

}
