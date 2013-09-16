package cn.leyundong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.leyundong.adapter.AutoAdapter;
import cn.leyundong.adapter.AutoAdapter.HasMorePagesListener;

public class AutoListView extends ListView implements HasMorePagesListener {

	private View listFooter;
	private boolean footerViewAttached = false;

	private AutoAdapter adapter;
	private LinearLayout headerLayout;

	public AutoListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AutoListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoListView(Context context) {
		super(context);
	}
	
	 public void setLoadingView(View listFooter) {
			this.listFooter = listFooter;
		}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (!(adapter instanceof AutoAdapter)) {
			throw new IllegalArgumentException(
					AutoListView.class.getSimpleName()
							+ " must use adapter of type "
							+ AutoAdapter.class.getSimpleName());
		}

		// previous adapter
		if (this.adapter != null) {
			this.adapter.setHasMorePagesListener(null);
			this.setOnScrollListener(null);
		}

		this.adapter = (AutoAdapter) adapter;
		((AutoAdapter) adapter).setHasMorePagesListener(this);

		View dummy = new View(getContext());
		super.addFooterView(dummy);
		headerLayout = new LinearLayout(getContext());
		headerLayout.setOrientation(LinearLayout.VERTICAL);
		super.addHeaderView(headerLayout);
		super.setAdapter(adapter);
		super.removeFooterView(dummy);
		
		setOnScrollListener(this.adapter);
		this.adapter.checkItemCount();
	}

	@Override
	public void noMorePages() {
		if (listFooter != null) {
			this.removeFooterView(listFooter);
		}
		footerViewAttached = false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public void mayHaveMorePages() {
		if (!footerViewAttached && listFooter != null) {
			this.addFooterView(listFooter);
			footerViewAttached = true;
		}
	}

	public boolean isLoadingViewVisible() {
		return footerViewAttached;
	}
	
	public LinearLayout getHeaderLayout() {
		return headerLayout;
	}

}
