package com.chatuml.chatuml;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author J. Madden
 * ChatBubbleView represents a message sent/received to/from 
 * the XMPP server. 
 */

public class ChatBubbleView extends LinearLayout {

	private static int MAX_LINE_LENGTH;
	public static final int ORIGIN_SENT = -1;
	public static final int ORIGIN_RECV = -2;
	
	private Context mContext;
	private DisplayMetrics mDisplayMetrics;
	
	private Paint mBubblePaint;
	private RectF mBubbleRect;
	
	private int mOrigin;
	private Time mCreationTime;
	
	private float mTextSize = 24;
	private float mWidth = 0;
	private float mHeight = 0;
	private RelativeLayout.LayoutParams mParams;

	public ChatBubbleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mDisplayMetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		MAX_LINE_LENGTH = (int) (mDisplayMetrics.widthPixels * 0.65);
		
		mCreationTime = new Time("EST");
		mCreationTime.setToNow();
		
		mBubblePaint = new Paint();
		mBubblePaint.setAntiAlias(true);
		mBubblePaint.setStyle(Paint.Style.FILL);
		mBubbleRect = new RectF();
		
		//Layout params relative to parent RelativeLayout
		mParams = new RelativeLayout.LayoutParams(0, 0);
		mParams.setMargins(5, 5, 5, 5);
		
		setWillNotDraw(false);
		this.setOrientation(VERTICAL);
		
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO chat bubble onclick
			}
		});
	}
	
	
	/* setters */
	
	/**
	 * Set the origin of this bubble.
	 * @param origin Either ORIGIN_SENT or ORIGIN_RECV
	 */
	public void setOrigin(int origin) {
		mOrigin = origin;
		if(mOrigin == ORIGIN_RECV) {
			mBubblePaint.setColor(Color.rgb(0xd9, 0xd9, 0xd9));
		} else {
			mBubblePaint.setColor(Color.rgb(0x90, 0xff, 0x8a));
		}
	}
	
	/**
	 * Set the creation time of this bubble.
	 * @param time Time of creation
	 */
	public void setCreationTime(Time time) {
		mCreationTime = time;
	}
	
	/**
	 * Set the text of this bubble.
	 * @param text Text to display
	 */
	public void setText(String text) {
		TextView tv;
		String line;
		Rect bounds = new Rect();
		/* cut long text up into shorter lines */
		do {
			tv = new TextView(mContext);
			tv.setPadding(20, 0, 20, 0);
			tv.setTextSize(mTextSize);
			line = new String();
			int i = 0;
			/* fit as many chars as we can onto a line 
			 * unless we hit a newline first */
			while(i < text.length() &&
				  tv.getPaint().measureText(line) < MAX_LINE_LENGTH) { 
				line += text.charAt(i);
				++i;
				if(line.charAt(i-1) == '\n') {
					break;
				}
			}
			tv.setText(line.trim());
			addView(tv);

			/* increase height of bubble */
			tv.getPaint().getTextBounds(line, 0, line.length(), bounds);
			mHeight += bounds.height() ;
			
			if(i < text.length()) {
				text = text.substring(i);
			} else {
				break;
			}
		} while(true);
		
		for(int i = 0 ; i < getChildCount() ; ++i) {
			String l = ((TextView) getChildAt(i)).getText().toString();
			float w = tv.getPaint().measureText(l);
			if(w > mWidth) {
				mWidth = w;
			}
		}
		double hBubble =  mHeight + 25 * getChildCount();
		float wBubble = mWidth + tv.getPaddingLeft() + tv.getPaddingRight();
		mBubbleRect.set(0, 0, wBubble, (float) hBubble);
		mParams.height = (int) mBubbleRect.height();
		mParams.width = (int) mBubbleRect.width();
	}
	
	
	/* getters */
	
	public int getOrigin() {
		return mOrigin;
	}
	
	public Time getCreationTime() {
		return mCreationTime;
	}
	
	public String getText() {
		StringBuilder sb = new StringBuilder();
		/* reconstruct single string from multiple lines of text */
		for(int i = 0 ; i < getChildCount() ; ++i) {
			sb.append(((TextView)getChildAt(i)).getText() + " ");
		}
		return sb.toString();
	}
	
	
	/* overrides */
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRoundRect(mBubbleRect, 5.0f, 5.0f, mBubblePaint);
		if(mOrigin == ORIGIN_SENT) {
			mParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		} else {
			mParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		}
		setLayoutParams(mParams);
		super.onDraw(canvas);
	}
	
	
	
	
	/* helpers */

	public ChatBubbleView(Context context) {
		this(context, null);
	}

	public ChatBubbleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}	
}
