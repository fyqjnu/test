package cn.leyundong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyGallery extends Gallery {
	
	private boolean touching = false;
	
	private long lastScrollingTime = 0;

	private int interval = 400;
	private int initialPosition;
	private Runnable scrollerTask;
	
	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyGallery(Context context) {
		super(context);
		init();
	}

	private void init() {
		scrollerTask = new Runnable() {
			
			@Override
			public void run() {
				long time = System.currentTimeMillis() - lastScrollingTime;
				if (time > 2000) {
					//滑动停止
					System.out.println("滑动停止----------");
					if (listern != null) {
						listern.onScrollStop(MyGallery.this);
					}
				} else {
					postDelayed(scrollerTask, interval);
				}
			}
		};
	}
	
	public void startScrollTask() {
		initialPosition = getScrollX();
		postDelayed(scrollerTask, interval);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch(event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			touching = true;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			touching = false;
			startScrollTask();
			break;
		}
		
//		System.out.println("onTouchEvent-----------" + touching);
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		System.out.println("onScroll-" + distanceX + ", " + distanceY);
		lastScrollingTime = System.currentTimeMillis();
		return super.onScroll(e1, e2, distanceX, distanceY);
	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		lastScrollingTime = System.currentTimeMillis();
//		System.out.println("onScrollChanged= " + l + "," + t + "," + oldl + "," + oldt);
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	public boolean isTouched() {
		System.out.println(touching);
		return touching;
	}
	
	private OnGalleryScrollStop listern;
	
	public void setOnScrolllStopListener(OnGalleryScrollStop l) {
		this.listern = l;
	}
	
	public interface OnGalleryScrollStop {
		void onScrollStop(MyGallery gallery);
	}
	
}