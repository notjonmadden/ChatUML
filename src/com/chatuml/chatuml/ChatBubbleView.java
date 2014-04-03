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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	private int mLineCount = 0;
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
		mBubblePaint.setAlpha(128);
		mBubbleRect = new RectF();
		
		mParams = new RelativeLayout.LayoutParams(0, 0);
		
		setWillNotDraw(false);
		this.setOrientation(VERTICAL);
	}
	
	
	/* setters */
	
	public void setOrigin(int origin) {
		mOrigin = origin;
		if(mOrigin == ORIGIN_RECV) {
			mBubblePaint.setColor(Color.GRAY);
		} else {
			mBubblePaint.setColor(Color.GREEN);
		}
	}
	
	public void setCreationTime(Time time) {
		mCreationTime = time;
	}
	
	public void setText(String text) {
		TextView tv;
		String line;
		Rect bounds = new Rect();
		/* cut long text up into shorter lines */
		do {
			tv = new TextView(mContext);
			tv.setPadding(10, 0, 10, 0);
			tv.setTextSize(mTextSize);
			line = text;
			int i = 0;
			for( ; tv.getPaint().measureText(line) > MAX_LINE_LENGTH ; ++i) { 
				line = text.substring(0, text.length() - i);
			}
			if(tv.getPaint().measureText(text) > MAX_LINE_LENGTH) {
				text = text.substring(text.length() - i);
			}
			tv.setText(line);
			addView(tv);
			

			tv.getPaint().getTextBounds(tv.getText().toString(), 0, tv.length(), bounds);
			mHeight += bounds.height() + 5;
			++mLineCount;
		} while(tv.getPaint().measureText(text) > MAX_LINE_LENGTH);
		
		//this is a somewhat messy implementation, fix it maybe
		if(mLineCount > 1) {
			tv = new TextView(mContext);
			tv.setText(text);
			tv.setPadding(10, 0, 10, 0);
			tv.setTextSize(mTextSize);
			addView(tv);
			tv.getPaint().getTextBounds(tv.getText().toString(), 0, tv.length(), bounds);
			mHeight += bounds.height() + 5;
		}
		
		mWidth = tv.getPaint().measureText(((TextView)getChildAt(0)).getText().toString());
		this.setLayoutParams(new RelativeLayout.LayoutParams(2 * (int)mWidth, 2 * (int)mHeight));
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
		for(int i = 0 ; i < mLineCount ; ++i) {
			sb.append(((TextView)getChildAt(i)).getText() + " ");
		}
		return sb.toString();
	}
	
	
	/* overrides */
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawBubble(canvas);
		mParams.height = (int) mBubbleRect.height() + 10;
		mParams.width = (int) mBubbleRect.width() + 15;
		if(mOrigin == ORIGIN_SENT) {
			mParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		} else {
			mParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		}
		this.setLayoutParams(mParams);
		super.onDraw(canvas);
	}
	
	
	/* helpers */
	
	private void drawBubble(Canvas canvas) {
		mBubbleRect.set(0, 0, mWidth + 20, mHeight + 15);
		canvas.drawRoundRect(mBubbleRect, 10.0f, 10.0f, mBubblePaint);
	}

	public ChatBubbleView(Context context) {
		this(context, null);
	}

	public ChatBubbleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}	
}
