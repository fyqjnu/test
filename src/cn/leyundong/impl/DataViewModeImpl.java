package cn.leyundong.impl;

import android.widget.ViewFlipper;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.view.ProgressView;

public class DataViewModeImpl implements IDataViewModel {
	
	private ViewFlipper mFlipper;
	private ProgressView mProgressView;
	
	
	
	/**
	 * viewflipper 0层放data， 1层放progressview
	 * @param vf
	 * @param pv
	 */
	public DataViewModeImpl(ViewFlipper vf, ProgressView pv) {
		this.mFlipper = vf;
		this.mProgressView = pv;
	}

	@Override
	public void showDataLayout() {
		mFlipper.setDisplayedChild(0);
	}

	@Override
	public void showProgressLayout() {
		mFlipper.setDisplayedChild(1);
		mProgressView.displayProgress();
	}

	@Override
	public void showRefreshLayout() {
		mFlipper.setDisplayedChild(1);
		mProgressView.displayRefresh();
	}

	@Override
	public void showEmptyLayout() {
		//空数据
		mFlipper.setDisplayedChild(1);
		mProgressView.displayEmpty();
	}

}
