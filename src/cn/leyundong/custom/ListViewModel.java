package cn.leyundong.custom;

import android.content.Context;
import android.widget.ListView;
import android.widget.ViewFlipper;
import cn.leyundong.impl.DataViewModeImpl;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.view.ProgressView;

public class ListViewModel extends ViewFlipper implements IDataViewModel {
	public interface IListViewFactory {
		ListView getListView();
	}
	
	private IListViewFactory facotry;
	private ProgressView pv;
	
	private IDataViewModel modelControl;
	private ListView lv;
	
	public ListViewModel(Context context, IListViewFactory f) {
		super(context);
		facotry = f;
		init();
	}
	
	private void init() {
		if (facotry != null) {
			lv = facotry.getListView();
		} else {
			//默认listview
			lv = new ListView(getContext());
		}
		//添加listview
		addView(lv);
		pv = new ProgressView(getContext());
		//添加进度层
		addView(pv);
		
		modelControl = new DataViewModeImpl(this, pv);
		
	}
	
	public void setEmptyText(String s) {
		pv.setEmptyText(s);
	}
	
	/**
	 * 刷新处理
	 * @param h
	 */
	public void setRefreshHandler(IRefresh h) {
		pv.setRefresh(h);
	}

	@Override
	public void showDataLayout() {
		modelControl.showDataLayout();
	}

	@Override
	public void showProgressLayout() {
		modelControl.showProgressLayout();
	}

	@Override
	public void showRefreshLayout() {
		modelControl.showRefreshLayout();
	}

	@Override
	public void showEmptyLayout() {
		modelControl.showEmptyLayout();
	}
	
	public ListView getListView() {
		return lv;
	}
	
}
