package cn.leyundong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class SyncScrollView extends ScrollView {
	
	private String TAG = SyncScrollView.class.getSimpleName() + ":";
	
	private View syncView;

	public SyncScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SyncScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SyncScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		System.out.println(TAG + "onScrollChanged:" + l + "," + t);
		super.onScrollChanged(l, t, oldl, oldt);
//		if (syncView != null) {
//			syncView.scrollBy(l - oldl, t - oldt);
//		}
		
		if (syncView != null) {
			syncView.scrollTo(l, t);
		}
	}
	
	public void setSyncView(View v) {
		this.syncView = v;
	}
	
	@Override
	public void fling(int velocityY) {
		super.fling(velocityY);
	}
	
}
