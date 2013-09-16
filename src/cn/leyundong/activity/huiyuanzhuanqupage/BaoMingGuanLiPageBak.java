package cn.leyundong.activity.huiyuanzhuanqupage;

import java.util.Arrays;

import android.app.Activity;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.leyundong.MyApplication;
import cn.leyundong.NeedLoginHelper;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.adapter.BaoMingGuanLiAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.custom.ListViewModel.IListViewFactory;
import cn.leyundong.dialog.MyDialog;
import cn.leyundong.entity.BaoMingBean;
import cn.leyundong.entity.BaoMingXiangBean;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.interfaces.ITaskGenerater;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.Utils;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 活动(报名管理列表)
 * @author Administrator
 *
 */
public class BaoMingGuanLiPageBak extends AbstractAsyncPage implements IFindViewById, ITaskGenerater, IListViewFactory, IRefresh {

	static final int id_cancel_baomingxiang = 1;
	static final int id_cancel_baoming = 2;
	static final int id_pay = 3;
	static final int id_query = 4;
	
	private Activity act;
	private String title;
	private LinearLayout layout;
	private View view;
	
	private IDataViewModel layoutControl;
	
	private ListView lv;
	private BaoMingGuanLiAdapter adapter;
	
	@ViewRef(id=R.id.btnQuery, onClick="query") Button btnQuery;
	
	private void query(View v) {
		System.out.println("查询-----------");
		loadData();
	}
	
	private void loadData() {
		layoutControl.showProgressLayout();
		adapter.clear();
		executeTask(id_query, generateTask());
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		switch (what) {
		case id_query:
			BaoMingBean[] infos = parseJson((String) result);
			if (infos == null) {
				layoutControl.showRefreshLayout();
				return;
			}
			layoutControl.showDataLayout();
			adapter.addData(Arrays.asList(infos));
			if (adapter.getCount() == 0) {
				layoutControl.showEmptyLayout();
			}
			break;
		case id_pay:
			handlePayResult((String) result);
			break;
		case id_cancel_baoming:
			handleCancelBaoMingResult((String) result);
			break;
		case id_cancel_baomingxiang:
			handleCancelBaoMingXiangResult((String) result);
			break;
		}
	}
	
	private void handlePayResult(String json) {
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			//成功
			toast(act, "支付成功 ");
			reload();
		} else {
			//失败
			toast(act, "支付失败：" + result.error);
		}
		dismissDialog();
	}
	private void handleCancelBaoMingResult(String json) {
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			//成功
			toast(act, "取消成功 ");
			reload();
		} else {
			//失败
			toast(act, "取消失败：" + result.error);
		}
		dismissDialog();
	}
	private void handleCancelBaoMingXiangResult(String json) {
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			//成功
			toast(act, "取消成功 ");
			reload();
		} else {
			//失败
			toast(act, "取消失败：" + result.error);
		}
		dismissDialog();
	}
	
	private void reload() {
		loadData();
	}
	
	private BaoMingBean[] parseJson(String json) {
		return Utils.parsePageJson(json, BaoMingBean.class);
	}
	
	
	public BaoMingGuanLiPageBak(Activity act, String title) {
		this.act = act;
		this.title = title;
		layout = (LinearLayout) act.getLayoutInflater().inflate(R.layout.baomingguanli, null);
		new ViewRefBinder(this).init();
		
		
		ListViewModel lvm = new ListViewModel(act, this);
		lvm.setEmptyText("没有相应查询项");
		layoutControl = lvm;
		layout.addView(lvm, -1, -1);
		lvm.setRefreshHandler(this);
		
		adapter = new BaoMingGuanLiAdapter(act);
		lv.setAdapter(adapter);
		adapter.setOuterClickListener(clickListener);
		lv.setOnItemClickListener(adapter);
		
		NeedLoginHelper helper = new NeedLoginHelper(act);
		helper.needLogin(layout, "需要登录才能查看活动");
		view = helper.getView();
		
		loadData();
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("v id=" + v.getId());
			Object tag = v.getTag();
			switch(v.getId()) {
			case R.id.btnCancel:
				cancelBaoMing(tag);
				break;
			case R.id.btnPay:
				payBaoMing(tag);
				break;
			case R.id.id_btn_cancel:
				cancelBaoMingXiang(tag);
				break;
			}
		}
	};
	
	private void cancelBaoMing(Object tag) {
		System.out.println("取消报名--------");
		Integer p = (Integer) tag;
		BaoMingBean bm = (BaoMingBean) adapter.getItem(p);
		
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 31);
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.bmid = bm.bmid;
		System.out.println("报名id=" + b.bmid);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_cancel_baoming, t);
		showDialog(act, "取消报名，请稍候...");
		dlg.obj1 = tag;
	}
	
	/**
	 * 支付
	 * @param tag
	 */
	private void payBaoMing(Object tag) {
		System.out.println("支付报名---------------");
		Integer p = (Integer) tag;
		BaoMingBean bm = (BaoMingBean) adapter.getItem(p);
		
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 32);
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.bmid = bm.bmid;
		System.out.println("报名项id=" + b.bmxid);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_pay, t);
		showDialog(act, "正在支付，请稍候...");
		dlg.obj1 = tag;
	}
	
	private void cancelBaoMingXiang(Object tag) {
		System.out.println("取消报名项--------");
		Pair<Integer, Integer> p = (Pair<Integer, Integer>) tag;
		BaoMingBean bm = (BaoMingBean) adapter.getItem(p.first);
		BaoMingXiangBean bmx = bm.bmxBeanList.get(p.second);
		
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 31);
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.bmxid = bmx.bmxid;
		System.out.println("报名项id=" + b.bmxid);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_cancel_baomingxiang, t);
		showDialog(act, "取消报名项，请稍候...");
		dlg.obj1 = tag;
	}
	
	@Override
	public View getView() {
		return view;
	}

	@Override
	public View getViewById(int id) {
		return layout.findViewById(id);
	}

	@Override
	public IDataRequest generateTask() {
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 3);
		//只需要用户id
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	public IDataRequest generateTask(Object param) {
		return null;
	}

	@Override
	public ListView getListView() {
		if (lv == null) {
			lv = new ListView(act);
		}
		return lv;
	}

	@Override
	public void doRefresh() {
		loadData();
	}

}
