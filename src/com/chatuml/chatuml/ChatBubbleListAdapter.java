package com.chatuml.chatuml;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ChatBubbleListAdapter extends BaseAdapter {

	private List<View> mMsgList = new ArrayList<View>();
	
	/*public ChatMsgListAdapter(Context context) {
		
	}*/

	@Override
	public int getCount() {
		return mMsgList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMsgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mMsgList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return (View) getItem(position);
	}
	
	public void addView(View child) {
		mMsgList.add(child);
		notifyDataSetChanged();
	}
}
