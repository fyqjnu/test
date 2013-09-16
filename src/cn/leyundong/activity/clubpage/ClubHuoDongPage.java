package cn.leyundong.activity.clubpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import cn.leyundong.NeedLoginHelper;
import cn.leyundong.R;
import cn.leyundong.activity.BaoMingQingKuanActivity;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.ClubHuoDongAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.HuoDongBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.Utils;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 俱乐部活动页
 * @author MSSOFT
 *
 */
public class ClubHuoDongPage extends AbstractAutoListViewPage<HuoDongBean> implements IFindViewById, IMaBiaoChangeDiListener {
	
	private static final int id_cancel = 1;
	private static final int id_copy = 2;
	
	private View view;
	
	private ViewGroup layout;
	
	@ViewRef(id=R.id.llContainer)LinearLayout llContainer;
	@ViewRef(id=R.id.btnQuery, onClick="query")Button btnQuery;
	@ViewRef(id=R.id.tvDate)DateTextView tvDate;
	@ViewRef(id=R.id.spinnerType)Spinner spinnerType;

	private MaBiaoTask mbTask;
	
	private void query(View v) {
		System.out.println("查询--------");
		loadData(ID_LOAD_FIRST);
	}
	

	public ClubHuoDongPage(Activity act) {
		super(act, ClubHuoDongAdapter.class);
		
		mbTask = MaBiaoTask.getInstance(act);
		mbTask.registChangDiObserver(this);
		
		layout = (ViewGroup) act.getLayoutInflater().inflate(R.layout.club_huodong, null);
		ViewRefBinder binder = new ViewRefBinder(this);
		binder.init();
		
		helper = new NeedLoginHelper(act);
		helper.setAdapter(adapter);
		helper.setLayoutControl(lvm);
		view = helper.needLogin(layout, "需要登录才能查看");
		
		BaseAdapter cdAdapter = mbTask.getChangDiLeiXingAdapter(act);
		setupSpinnerType(cdAdapter);
	}
	
	
	@Override
	public void onDestroy() {
		mbTask.unregisterChangDiObserver(this);
		helper.destroy();
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		super.onTaskExecuted(what, result);
		switch(what) {
		case id_cancel:
		case id_copy:
			Result ret = JsonParser.getResultFromJson((String) result);
			if (what == id_cancel) {
				handleCancelResult(ret);
			} else {
				handleCopyResult(ret);
			}
			dismissDialog();
			break;
		}
	}
	
	private void handleCancelResult(Result ret) {
		if (ret.success) {
			toast(act, "取消成功");
			reload();
		} else {
			toast(act, "取消失败：" + ret.error);
		}
	}
	
	private void handleCopyResult(Result ret) {
		if (ret.success) {
			toast(act, "复制成功");
			reload();
		} else {
			toast(act, "复制失败：" + ret.error);
		}
	}


	/**
	 * 场地类型
	 */
	private void setupSpinnerType(BaseAdapter adapter) {
		if (adapter != null) {
			spinnerType.setAdapter(adapter);
		}
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, HuoDongBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		ChaXunBean b = Utils.createChaXunBean(adapter);
		IDataRequest t = null;
		String url = null;
		switch (what) {
		case ID_LOAD_FIRST:
		case ID_LOAD_MORE:
			url = Urls.getUrl(Urls.TYPE_CLUB, 42);
			b.ydrq = tvDate.getDate();
			b.cdlx = mbTask.getChangDiKey((String)spinnerType.getSelectedItem());
			t = new SingleParamTask<ChaXunBean>(act, url, b);
			break;
		}
		return t;
	}
	
	private OnClickListener itemButtonClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("v id = " + v.getId());
			Integer pos = (Integer) v.getTag();
			switch(v.getId()) {
			case R.id.btnEdit:
				doEdit(pos);
				break;
			case R.id.btnCancel:
				doCancel(pos);
				break;
			case R.id.btnCopy:
				doCopy(pos);
				break;
			case R.id.btnDetail:
				doDetail(pos);
				break;
			}
		}
	};

	private NeedLoginHelper helper;
	
	private void doEdit(int pos) {
		System.out.println("编辑------------");
		Intent intent = new Intent(act, EditHuoDongActivity.class);
		intent.putExtra(HuoDongBean.class.getSimpleName(), (HuoDongBean)adapter.getItem(pos));
		act.startActivity(intent);
	}
	private void doCancel(int pos) {
		System.out.println("取消活动----------------");
		ChaXunBean b = Utils.createChaXunBean(null);
		HuoDongBean info = (HuoDongBean) adapter.getItem(pos);
		b.jlbid = info.jlbid;
		b.hdid = info.hdid;
		String url = Urls.getUrl(Urls.TYPE_CLUB, 44);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_cancel, t);
		showDialog(act, "正在取消，请稍候...");
	}
	private void doCopy(int pos) {
		System.out.println("复制活动----------------");
		ChaXunBean b = Utils.createChaXunBean(null);
		HuoDongBean info = (HuoDongBean) adapter.getItem(pos);
		b.hdid = info.hdid;
		String url = Urls.getUrl(Urls.TYPE_CLUB, 45);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_cancel, t);
		showDialog(act, "正在复制，请稍候...");
	}
	private void doDetail(int pos) {
		System.out.println("详情-------------");
		Intent intent = new Intent(act, BaoMingQingKuanActivity.class);
		intent.putExtra(HuoDongBean.class.getSimpleName(), (HuoDongBean)adapter.getItem(pos));
		act.startActivity(intent);
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		llContainer.addView(lvm, -1, -1);
		lvm.setEmptyText("没有查询到活动数据");
		adapter.setOuterClickListener(itemButtonClick);
	}

	@Override
	public View getViewById(int arg0) {
		return layout.findViewById(arg0);
	}


	@Override
	public void notifySuccess(MaBiaoTask mbt) {
		setupSpinnerType(mbt.getChangDiLeiXingAdapter(act));
	}
	
}
