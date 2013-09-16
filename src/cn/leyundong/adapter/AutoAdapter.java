package cn.leyundong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import cn.leyundong.entity.Page;
import cn.quickdevelp.adapter.QBaseAdapter;

public abstract class AutoAdapter<T> extends QBaseAdapter<T> implements OnScrollListener {

	protected Page<T> mCurrentPage;
	
	public interface HasMorePagesListener {
		void noMorePages();
		void mayHaveMorePages();
	}
	
	public interface RequestNextPageListener {
		void nextPage(int count);
	}
	
	protected boolean isBusy;

	private boolean automaticNextPageLoading = false;
	
	protected HasMorePagesListener hasMorePagesListener;
	
	protected RequestNextPageListener requestNextPageListener;
	
	private boolean isRequestingNextPage = false;
	
	protected OnClickListener outerClickListener;
	
	public void setOuterClickListener(OnClickListener l) {
		this.outerClickListener = l;
	}
	
	public void setHasMorePagesListener(HasMorePagesListener hasMorePagesListener) {
		this.hasMorePagesListener = hasMorePagesListener;
	}
	
	public void setRequestNextPage(RequestNextPageListener listener) {
		this.requestNextPageListener = listener;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View res = getAutoView(position, convertView, parent);
		
		if (position == getCount() - 1) {
			if (requestNextPageListener != null && automaticNextPageLoading && !isRequestingNextPage) {
				isRequestingNextPage = true;
				requestNextPageListener.nextPage(getCount());
			}
		}
		
		return res;
	}
	
	public void onNextPageCompleted() {
		isRequestingNextPage = false;
	}
	
	public void notifyNoMorePages() {
		automaticNextPageLoading = false;
		if (hasMorePagesListener != null) {
			hasMorePagesListener.noMorePages();
		}
	}
	
	public void notifyMayHaveMorePages() {
		automaticNextPageLoading = true;
		if (hasMorePagesListener != null) { 
			hasMorePagesListener.mayHaveMorePages();
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch(scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING:
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			isBusy = true;
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			isBusy = false;
			notifyDataSetChanged();
			break;
		}
	}
	
	public void addData(List<T> l) {
		if (mData == null) {
			mData = new ArrayList<T>();
		}
		mData.addAll(l);
		notifyDataSetChanged();
	}
	
	public void addPage(Page<T> p) {
		if (p == null) {
			return ;
		}
		addData(p.list);
		mCurrentPage = p;
		checkItemCount();
	}
	
	public void clearData() {
		if (mData != null) {
			mData.clear();
		}
		mCurrentPage = null;
		notifyDataSetChanged();
		notifyNoMorePages();
	}
	
	protected abstract View getAutoView(int position, View view, ViewGroup parent);
	public void checkItemCount() {
		if (mCurrentPage != null) {
			if (mCurrentPage.hasNextPage) {
				notifyMayHaveMorePages();
				System.out.println("还有更多数据-------");
			} else {
				notifyNoMorePages();
				System.out.println("没有更多数据-------");
			}
		}
	}

}
