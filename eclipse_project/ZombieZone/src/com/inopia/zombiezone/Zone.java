package com.inopia.zombiezone;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Zone {

	private Rect rect;
	private Paint p;
	private int type;
	
	public Zone(int left, int top, int right, int bottom, int color, int type) {
		
		rect = new Rect(left, top, right , bottom);
		p = new Paint();
		p.setColor(color);
		this.type = type;
	}
	
	public void onDraw(Canvas c) {		
		c.drawRect(rect, p);
	}
	
	public Rect getRect() {
		return rect;
	}
	public int getType() {
		return this.type;
	}
}
