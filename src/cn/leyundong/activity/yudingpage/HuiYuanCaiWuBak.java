package cn.leyundong.activity.yudingpage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.adapter.HuiYuanCaiWuAdapter;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.ChangGuangHuiYuanCaiWuBean;
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
import cn.leyundong.view.AutoListView;
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
 * 会员财务
 * @author Administrator
 *
 */
public class HuiYuanCaiWuBak  extends AbstractAsyncPage  implements IFindViewById, onLoginSuccess, onRegisterSuccess, ITaskGenerater {
	
	private int count;
	
	private Activity act;
	
	private View view;
	
	@ViewRef(id=R.id.vf) ViewFlipper vf;

	private ProgressView pbView;
	
	@ViewRef(id=R.id.etName) EditText etName;
	@ViewRef(id=R.id.snType) Spinner snType;
	@ViewRef(id=R.id.btnQuery, onClick="onClick") Button btnQuery;
	
	@ViewRef(id=R.id.lv) AutoListView lv;
	
	private HuiYuanCaiWuAdapter adapter;
	
	private IDataViewModel mDataViewMode;
	
	private IRefresh mRefresh = new IRefresh() {
		
		@Override
		public void doRefresh() {
			//刷新
			doQuery();
		}
	};
	
	private void onClick(View v) {
		//查询
		doQuery();
	}
	
	private void doQuery() {
		mDataViewMode.showProgressLayout();
		IDataRequest task = generateTask();
		executeTask(1, task);
	}
	
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		ChangGuangHuiYuanCaiWuBean[] ary = parseJson((String) result);
		if (ary == null) {
			if (adapter.getCount() == 0) {
				mDataViewMode.showRefreshLayout();
			}
			return ;
		}
		mDataViewMode.showDataLayout();
		adapter.addData(Arrays.asList(ary));
		if (adapter.getCount() == 0) {
			mDataViewMode.showEmptyLayout();
		}
	}
	
	private ChangGuangHuiYuanCaiWuBean[] parseJson(String s) {
		if (s == null) {
			return null;
		}
		Result result = JsonParser.getResultFromJson(s);
		if (result.success) {
			try {
				JSONObject obj = new JSONObject(s);
				JSONObject page = obj.getJSONObject("page");
				JSONArray ary = page.getJSONArray("list");
				ChangGuangHuiYuanCaiWuBean[] infos = JsonUtil.parseJsonArray(ary.toString(), ChangGuangHuiYuanCaiWuBean.class);
				System.out.println("会员财务个数=" + infos.length);
				return infos;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private ChaXunBean getChaXunBean() {
		ChaXunBean b = new ChaXunBean();
		//场馆名称
		b.cgmc = etName.getEditableText().toString();
		b.yhid = MyApplication.mUser.yhid;
		return b;
	}
	
	
	private class HuiYuanCaiWuTask implements IDataRequest {

		ChaXunBean info;
		
		public HuiYuanCaiWuTask(ChaXunBean info) {
			this.info = info;
		}
		
		@Override
		public Object doRequest() {
			String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, RequestId.HUI_YUAN_CAI_WU);
			List<IJson> list = new ArrayList<IJson>();
			BeanProxy<ChaXunBean> proxy = new BeanProxy<ChaXunBean>(info);
			list.add(proxy);
			IHttpDuty duty = new NetCheckHttpDuty(act, new HttpPostDuty(act, url, list));
			String post = duty.post();
			System.out.println("会员财务=" + post);
			Result result = JsonParser.getResultFromJson(post);
			if (result.success) {
				try {
					JSONObject obj = new JSONObject(post);
					JSONObject page = obj.getJSONObject("page");
					JSONArray ary = page.getJSONArray("list");
					ChangGuangHuiYuanCaiWuBean[] infos = JsonUtil.parseJsonArray(ary.toString(), ChangGuangHuiYuanCaiWuBean.class);
					System.out.println("会员财务个数=" + infos.length);
					return infos;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/*try {
				Thread.sleep(3000);
				if (count++ < 1) {
					return null;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ChangGuangHuiYuanCaiWuBean[] infos = new ChangGuangHuiYuanCaiWuBean[10];
			for (int i = 0; i < 10;i++) {
				infos[i] = new ChangGuangHuiYuanCaiWuBean();
			}*/
			return null;
		}
		
	}
	
	@Override
	public void onDestroy() {
		LoginTask.unregisterLoginSuccessListener(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
		super.onDestroy();
	}
	
	public HuiYuanCaiWuBak(Activity act) {
		this.act = act;
		view = act.getLayoutInflater().inflate(R.layout.huiyuancaiwu, null);
		new ViewRefBinder(this).init();
		
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		
		pbView = new ProgressView(act);
		pbView.setEmptyText("没有财务项");
		vf.addView(pbView);
		pbView.setRefresh(mRefresh);
		
		mDataViewMode = new DataViewModeImpl(vf, pbView);
		
		
		adapter = new HuiYuanCaiWuAdapter(act);
		lv.setAdapter(adapter);
		
		if (MyApplication.mUser.isNull()) {
			showNoLoginLayout();
		} else {
			showHasLoginLayout();
		}
		
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public View getViewById(int arg0) {
		return view.findViewById(arg0);
	}
	
	
	private void showHasLoginLayout() {
	}
	
	private void showNoLoginLayout() {
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
		String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, RequestId.HUI_YUAN_CAI_WU);
		ChaXunBean info = getChaXunBean();
		return new SingleParamTask<ChaXunBean>(act, url, info);
	}

	@Override
	public IDataRequest generateTask(Object param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoginout() {
		mDataViewMode.showRefreshLayout();
		showNoLoginLayout();
	}

}
