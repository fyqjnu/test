package cn.leyundong.activity.yudingpage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.adapter.DingDanGuanLiAdapter;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.custom.TipsLoginView;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.dialog.MyDialog;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.DingDanBean;
import cn.leyundong.entity.DingDanXiangBean;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.LoginTask;
import cn.leyundong.httpoperation.RegisterTask;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.impl.DataViewModeImpl;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.interfaces.ITaskGenerater;
import cn.leyundong.interfaces.onLoginSuccess;
import cn.leyundong.interfaces.onRegisterSuccess;
import cn.leyundong.json.JsonParser;
import cn.leyundong.message.MessageObserver;
import cn.leyundong.message.MyMessage;
import cn.leyundong.test.Data;
import cn.leyundong.view.ProgressView;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.http.NetCheckHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;
import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;

/**
 * 订单
 * @author Administrator
 *
 */
public class DingDanGuanLiBak extends AbstractAsyncPage implements IRefresh, IFindViewById, onLoginSuccess, onRegisterSuccess, ITaskGenerater, IMaBiaoChangeDiListener, MessageObserver {
	
	static final int ID_QUERY = 1;
	static final int ID_CANCEL_DD = 2;
	static final int ID_CANCEL_DDX = 3;
	
	private Activity act;
	
	private View view;
	
	@ViewRef(id=R.id.spinnerType) Spinner spinnerType;
	@ViewRef(id=R.id.tvDate) DateTextView tvDate;
	@ViewRef(id=R.id.btnQuery, onClick="onClick") Button btnQuery;
	
	
	@ViewRef(id=R.id.lv) ListView lv;
	@ViewRef(id=R.id.vf) ViewFlipper vf;
	
	private IDataViewModel mDataViewModel;
	
	private DingDanGuanLiAdapter adapter;

	private MaBiaoTask mbt;
	
	private int cancelDDPos = -1;
	private int cancelDDXSubPos = -1;
	
	@Override
	public void doRefresh() {
		doQuery();
	}
	
	private void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnQuery:
			//查询
			doQuery();
			break;
		}
	}
	
	private void doQuery() {
		System.out.println("doQuery----------");
		mDataViewModel.showProgressLayout();
		IDataRequest task = generateTask();
		executeTask(ID_QUERY, task);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		switch(what) {
		case ID_QUERY:
			DingDanBean[] infos = parseJson((String) result);
			if (infos != null) {
				//成功
				mDataViewModel.showDataLayout();
				adapter.addData(Arrays.asList(infos));
				if (adapter.getCount() == 0) {
					mDataViewModel.showEmptyLayout();
				}
			} else {
				mDataViewModel.showRefreshLayout();
			}
			break;
		case ID_CANCEL_DD:
			dismissDialog();
			Result ret = JsonParser.getResultFromJson((String) result);
			if (ret.success) {
				toast(act, "取消订单成功");
				if (cancelDDPos != -1) {
					//
					adapter.removeItem(cancelDDPos);
				}
			} else {
				toast(act, "取消订单失败：" + ret.error);
			}
			cancelDDPos = -1;
			break;
		case ID_CANCEL_DDX:
			dismissDialog();
			Result ret2 = JsonParser.getResultFromJson((String) result);
			if (ret2.success) {
				toast(act, "取消订单成功");
				if (cancelDDPos != -1) {
					//
					adapter.removeDDX(cancelDDPos, cancelDDXSubPos);
				}
			} else {
				toast(act, "取消订单失败：" + ret2.error);
			}
			cancelDDPos = -1;
			cancelDDXSubPos = -1;
			break;
		}
	}
		
	private DingDanBean[] parseJson(String s) {
		if (s == null) {
			return null;
		}
		Result ret = JsonParser.getResultFromJson(s);
		if (ret.success) {
			try {
				JSONObject obj = new JSONObject(s);
				JSONObject page = obj.getJSONObject("page");
				JSONArray ary = page.getJSONArray("list");
				System.out.println("list = " + ary);
				DingDanBean[] infos = JsonUtil.parseJsonArray(ary.toString(), DingDanBean.class);
				return infos;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
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
	
	class QueryTask implements IDataRequest {

		@Override
		public Object doRequest() {
			/*try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, RequestId.DING_DAN_GUAN_LI);
			List<IJson> entitys = new ArrayList<IJson>();
			
			ChaXunBean chaxun = getChaXunBean(); 
			BeanProxy<ChaXunBean> beanProxy = new BeanProxy<ChaXunBean>(chaxun);
			entitys.add(beanProxy);
			IHttpDuty duty = new NetCheckHttpDuty(act, new HttpPostDuty(act, url, entitys));
			String post = duty.post();
			System.out.println("订单查询 = " + post);
			Result result = JsonParser.getResultFromJson(post);
			if (result.success) {
				//数据获取成功 
				try {
					JSONObject obj = new JSONObject(post);
					JSONObject page = obj.getJSONObject("page");
					JSONArray ary = page.getJSONArray("list");
					System.out.println("list = " + ary);
					DingDanBean[] infos = JsonUtil.parseJsonArray(ary.toString(), DingDanBean.class);
					System.out.println("订单个数=" + infos.length);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return Data.getDingDanBean();
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mbt.unregisterChangDiObserver(this);
		LoginTask.unregisterLoginSuccessListener(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
	}
	
	public DingDanGuanLiBak(Activity act) {
		System.out.println("DingDanGuanLi 构造方法");
		this.act = act;
		
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		
		view = act.getLayoutInflater().inflate(R.layout.dingdanguanli, null);
		new ViewRefBinder(this).init();
		
		
		ProgressView pv = new ProgressView(act);
		pv.setRefresh(this);
		pv.setEmptyText("尚没有订单");
		vf.addView(pv, -1, -1);
		mDataViewModel = new DataViewModeImpl(vf, pv);
		
		adapter = new DingDanGuanLiAdapter(act);
		adapter.setClickListener(itemButtonClickListener);
		mDataViewModel.showDataLayout();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(adapter);
		
		if (MyApplication.mUser.isNull()) {
			showNoLoginLayout();
		} else {
			showHasLoginLayout();
		}
		
		mbt = MaBiaoTask.getInstance(act);
		BaseAdapter adapter = mbt.getChangDiLeiXingAdapter(act);
		setChangGuanLeiXingAdapter(adapter);
		mbt.registChangDiObserver(this);
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
		return view.findViewById(id);
	}
	
	private void showNoLoginLayout() {
		System.out.println("showNoLoginLayout--------");
	}
	
	private void showHasLoginLayout() {
		System.out.println("showHasLoginLayout---------");
	}

	@Override
	public void onRegisterSuccess(YongHuBean user) {
		showHasLoginLayout();
	}

	@Override
	public void onLoginOrRegisterSuccess(YongHuBean user) {
		showHasLoginLayout();
		
	}

	@Override
	public IDataRequest generateTask() {
		String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, RequestId.DING_DAN_GUAN_LI);
		ChaXunBean chaxun = getChaXunBean(); 
		return new SingleParamTask<ChaXunBean>(act, url, chaxun);
	}

	@Override
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
	public boolean onReceiverMessage(MyMessage mm) {
		return false;
	}

	@Override
	public void onLoginout() {
		mDataViewModel.showRefreshLayout();
		showNoLoginLayout();
	}
	
}
