package cn.leyundong.activity.page;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;
import cn.leyundong.adapter.AutoAdapter;
import cn.leyundong.adapter.AutoAdapter.RequestNextPageListener;
import cn.leyundong.custom.FooterLoadingView;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.custom.ListViewModel.IListViewFactory;
import cn.leyundong.entity.Page;
import cn.leyundong.entity.Result;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.view.AutoListView;
import cn.quickdevelp.interfaces.IDataRequest;

public abstract class AbstractAutoListViewPage<T> extends AbstractAsyncPage implements IListViewFactory, 
							RequestNextPageListener, IRefresh {

	/**
	 * 第一次加载
	 */
	protected static final int ID_LOAD_FIRST = 100;
	/**
	 * 加载更多
	 */
	protected static final int ID_LOAD_MORE = 101;
	
	protected AutoListView lv;
	protected Activity act;
	protected AutoAdapter<T> adapter;
	
	protected IDataViewModel layoutControl;
	protected ListViewModel lvm;
	private boolean callSetupListView;
	
	protected Map<Integer, IDataRequest> IdTaskMap = new HashMap<Integer, IDataRequest>();
	
	protected AbstractAutoListViewPage(Activity act, Class<? extends AutoAdapter<T>> adapterClz) {
		this.act = act;
		try {
			Constructor<? extends AutoAdapter<T>> c = adapterClz.getConstructor(Context.class);
			adapter = c.newInstance(this.act);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		adapter.setRequestNextPage(this);
		
		lvm = new ListViewModel(this.act, this);
		layoutControl = lvm;
		lvm.setRefreshHandler(this);
		
		lv.setAdapter(adapter);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		IdTaskMap.remove(what);
		if (what != ID_LOAD_FIRST && what != ID_LOAD_MORE) {
			return ;
		}
		Result ret = parseResult((String) result);
		if (ret.success) {
			//成功
			Page<T> page = (Page<T>) ret.obj; 
			adapter.addPage(page);
		}
		layoutControl.showDataLayout();
		switch(what) {
		case ID_LOAD_FIRST:
			if (!ret.success) {
				//显示刷新
				layoutControl.showRefreshLayout();
			} else if (adapter.getCount() == 0){
				layoutControl.showEmptyLayout();
			}
			break;
		case ID_LOAD_MORE:
			//通知加载完成
			adapter.onNextPageCompleted();
			break;
		}
	}
	
	protected abstract Result parseResult(String json);
	
	/**
	 * 加载数据
	 * @param what 
	 */
	protected void loadData(int what) {
		if (!callSetupListView) {
			setupListView(lvm);
			callSetupListView = true;
		}
		IDataRequest task = IdTaskMap.get(what);
		if (task != null) {
			return ;
		}
		switch(what) {
		case ID_LOAD_FIRST:
			adapter.clearData();
			layoutControl.showProgressLayout();
			task = getDataRequestTask(ID_LOAD_FIRST);
			executeTask(ID_LOAD_FIRST, task);
			break;
		case ID_LOAD_MORE:
			task = getDataRequestTask(ID_LOAD_MORE);
			executeTask(ID_LOAD_MORE, task);
			break;
		}
		IdTaskMap.put(what, task);
	}
	
	protected abstract IDataRequest getDataRequestTask(int what);
	
	/**
	 * 配置listviewmodle, 将lisview添加到布局中
	 * @param lvm 
	 */
	protected abstract void setupListView(ListViewModel lvm);

	@Override
	public void nextPage(int count) {
		loadData(ID_LOAD_MORE);
	}

	@Override
	public ListView getListView() {
		if (lv == null) {
			AutoListView tmp = new AutoListView(act);
			FooterLoadingView footer = new FooterLoadingView(act);
			tmp.setLoadingView(footer.getView());
			lv = tmp;
		}
		return lv;
	}

	@Override
	public void doRefresh() {
		System.out.println("刷新--------");
		loadData(ID_LOAD_FIRST);
	}
	
	protected void reload() {
		System.out.println("重新加载--------");
		loadData(ID_LOAD_FIRST);
	}
	
}
