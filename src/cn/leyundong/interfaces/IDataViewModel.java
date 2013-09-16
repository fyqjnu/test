package cn.leyundong.interfaces;

/**
 * 包括数据、进度、刷新3层view的模块接口
 * @author Administrator
 *
 */
public interface IDataViewModel {
	/**
	 * 显示数据层
	 */
	void showDataLayout();
	/**
	 * 显示进度转圈层
	 */
	void showProgressLayout();
	/**
	 * 显示刷新层
	 */
	void showRefreshLayout();
	
	/**
	 * 显示空数据层
	 */
	void showEmptyLayout();
}
