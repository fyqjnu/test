package cn.leyundong.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {

	private Paint paint;
	
	public MyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if (paint == null) {
			paint = new Paint();
			paint.setColor(Color.BLACK);
//			paint.setStyle(Style.STROKE);
			paint.setTextSize(30);
		}
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();
		System.out.println("w=" + w + ",h=" + h);
		canvas.drawLine(0, 0, w, h, paint);
		
		
		canvas.drawText("时", w/2, h/2, paint);
		canvas.drawText("场", 10, h - 10, paint);
		
		super.onDraw(canvas);
	}
}
