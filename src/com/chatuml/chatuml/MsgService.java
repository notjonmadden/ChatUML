package com.chatuml.chatuml;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

public class MsgService extends Service {
	
	/* Service implementation */
	
	private Binder mBinder = new MsgBinder();
	
	public class MsgBinder extends Binder {
		public MsgService getService() {
			return MsgService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	
	/* Service API */
	
	public void sendMsgs() {
		//TODO implement sendMsgs
	}
	
	public void receiveMsgs() {
		//TODO implement receiveMsgs
	}
	
	public void getReceivedMsgCount() {
		//TODO implement getReceivedMsgCount
	}	
}

IBinder binder = bindToService(MsgService.class);
binder.getService().sendMsg(...);
