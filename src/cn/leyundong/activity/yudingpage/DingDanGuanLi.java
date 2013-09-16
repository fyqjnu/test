package cn.leyundong.activity.yudingpage;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import cn.leyundong.MyApplication;
import cn.leyundong.NeedLoginHelper;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.DingDanGuanLiAdapter;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.DingDanBean;
import cn.leyundong.entity.DingDanXiangBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.Utils;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 订单
 * @author Administrator
 *
 */
public class DingDanGuanLi extends AbstractAutoListViewPage<DingDanBean> implements IFindViewById, IMaBiaoChangeDiListener {
	
	static final int ID_CANCEL_DD = 2;
	static final int ID_CANCEL_DDX = 3;
	
	private Activity act;
	
	private View view;
	
	@ViewRef(id=R.id.spinnerType) Spinner spinnerType;
	@ViewRef(id=R.id.tvDate) DateTextView tvDate;
	@ViewRef(id=R.id.btnQuery, onClick="query") Button btnQuery;
	@ViewRef(id=R.id.llContainer) LinearLayout llContainer;
	
	LinearLayout layout;
	
	private MaBiaoTask mbt;
	
	@SuppressWarnings("unused")
	private void query(View v) {
		switch(v.getId()) {
		case R.id.btnQuery:
			//查询
			loadData(ID_LOAD_FIRST);
			break;
		}
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		super.onTaskExecuted(what, result);
		switch(what) {
		case ID_CANCEL_DD:
			dismissDialog();
			Result ret = JsonParser.getResultFromJson((String) result);
			if (ret.success) {
				toast(act, "取消订单成功");
				reload();
			} else {
				toast(act, "取消订单失败：" + ret.error);
			}
			break;
		case ID_CANCEL_DDX:
			dismissDialog();
			Result ret2 = JsonParser.getResultFromJson((String) result);
			if (ret2.success) {
				toast(act, "取消订单成功");
				reload();
			} else {
				toast(act, "取消订单失败：" + ret2.error);
			}
			break;
		}
	}
		
	private ChaXunBean getChaXunBean() {
		ChaXunBean cx = new ChaXunBean();
		//用户id
		cx.yhid = MyApplication.mUser.yhid;
//		String changdi = (String) snType.getSelectedItem();
//		cx.cdlx = MaBiaoTask.getInstance(act).getChangDiKey(changdi);
//		cx.ydrq = "" + tvDate.getText();
		return cx;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mbt.unregisterChangDiObserver(this);
		helper.destroy();
	}
	
	public DingDanGuanLi(Activity act) {
		super(act, DingDanGuanLiAdapter.class);
		this.act = act;
		mbt = MaBiaoTask.getInstance(act);
		mbt.registChangDiObserver(this);
		
		layout = (LinearLayout) act.getLayoutInflater().inflate(R.layout.dingdanguanli, null);
		new ViewRefBinder(this).init();
		
		helper = new NeedLoginHelper(act);
		helper.setAdapter(adapter);
		helper.setLayoutControl(layoutControl);
		helper.needLogin(layout, "需要登录才能查询订单");
		view = helper.getView();
		
		BaseAdapter adapter = mbt.getChangDiLeiXingAdapter(act);
		setChangGuanLeiXingAdapter(adapter);
	}
	
	
	private OnClickListener itemButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("外层监听=" + v.getTag());
			String tag = (String) v.getTag();
			if (tag.startsWith("0:")) {
				//订单
				String pos = tag.substring(2);
				cancelDingDan(Integer.valueOf(pos));
			} else if (tag.startsWith("1:")) {
				//订单项
				String pos = tag.substring(2);
				String[] split = pos.split(",");
				if (split.length == 2) {
					cancelDingDanXiang(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
				}
			}
		}
	};
	private NeedLoginHelper helper;
	
	/**
	 * 取消订单
	 */
	private void cancelDingDan(int pos) {
		DingDanBean dd = (DingDanBean) adapter.getItem(pos);
		showDialog(act, "正在取消订单，请稍候...");
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.cgid = dd.cgid;
		b.ddid = dd.ddid;
		executeTask(ID_CANCEL_DD, generateTask(b));
	}
	
	/**
	 * 取消订单项
	 */
	private void cancelDingDanXiang(int pos, int subPos) {
		DingDanBean dd = (DingDanBean) adapter.getItem(pos);
		DingDanXiangBean ddx = dd.ddxList.get(subPos);
		showDialog(act, "正在取消订单项，请稍候...");
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.cgid = dd.cgid;
		b.ddxid = ddx.ddxid;
		executeTask(ID_CANCEL_DDX, generateTask(b));
	}
	
	private void setChangGuanLeiXingAdapter(BaseAdapter adapter) {
		if (adapter != null) {
			spinnerType.setAdapter(adapter);
		}
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public View getViewById(int id) {
		return layout.findViewById(id);
	}
	
	public IDataRequest generateTask(Object param) {
		ChaXunBean b = (ChaXunBean) param;
		String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, 6);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	public void notifySuccess(MaBiaoTask mbt) {
		setChangGuanLeiXingAdapter(mbt.getChangDiLeiXingAdapter(act));
	}

	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, DingDanBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, RequestId.DING_DAN_GUAN_LI);
		ChaXunBean b = Utils.createChaXunBean(adapter);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		layout.addView(lvm, -1, -1);
		lvm.setEmptyText("尚没有订单");
		adapter.setOuterClickListener(itemButtonClickListener);
		lv.setOnItemClickListener((DingDanGuanLiAdapter)adapter);
	}
	
}
