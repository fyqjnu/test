package cn.leyundong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class SyncHorizonScrollView extends HorizontalScrollView {
	private String TAG = SyncHorizonScrollView.class.getSimpleName() + ":";
	
	private View syncView;	

	public SyncHorizonScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public SyncHorizonScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SyncHorizonScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		System.out.println(TAG + "onScrollChanged--" + l + ", " + t);
		super.onScrollChanged(l, t, oldl, oldt);
		
		if (syncView != null) {
			syncView.scrollTo(l, t);
		}
		
//		if (syncView != null) {
//			syncView.scrollBy(l - oldl, t - oldt);
//		}
		
	}
	
	public void setSyncView(View v) {
		this.syncView = v;
	}
	
	@Override
	public void fling(int velocityX) {
		super.fling(velocityX);
	}
	
}
