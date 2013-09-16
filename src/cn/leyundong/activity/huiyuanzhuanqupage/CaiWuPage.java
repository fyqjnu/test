package cn.leyundong.activity.huiyuanzhuanqupage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import cn.leyundong.MyApplication;
import cn.leyundong.NeedLoginHelper;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.adapter.AutoAdapter.RequestNextPageListener;
import cn.leyundong.adapter.CaiWuAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.FooterLoadingView;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.custom.ListViewModel.IListViewFactory;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.entity.Page;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.LoginTask;
import cn.leyundong.httpoperation.RegisterTask;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.interfaces.ITaskGenerater;
import cn.leyundong.interfaces.onLoginSuccess;
import cn.leyundong.interfaces.onRegisterSuccess;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.TextUtil;
import cn.leyundong.util.Utils;
import cn.leyundong.view.AutoListView;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 财务
 * @author Administrator
 *
 */
public class CaiWuPage extends AbstractAsyncPage implements IListViewFactory, IFindViewById, 
						onLoginSuccess, onRegisterSuccess, RequestNextPageListener, IRefresh {
	
	/**
	 * 第一次加载
	 */
	private static final int ID_CAIWU_FIRST_LOAD = 1;
	
	/**
	 * 加载更多
	 */
	private static final int ID_CAIWU_LOAD_MORE = 3;

	private static final int ID_JULEBU = 2;

	private Activity act;
	
	public String title;
	
	private View view;
	private View layout;
	
	private @ViewRef(id=R.id.btnQuery, onClick="query")Button btnQuery;
	private @ViewRef(id=R.id.etJlb)EditText etJlb;
//	private @ViewRef(id=R.id.spinnerJuLeBu)Spinner spinnerJulebu;
	
	private Map<String, String> julebuMap = new HashMap<String, String>();
	
	private ListView lv;
	private CaiWuAdapter adapter;
	
	private IDataViewModel layoutControl;

	private NeedLoginHelper helper;
	
	private void query(View v) {
		System.out.println("查询---------");
		ChaXunBean b = getChaXunBean();
		if (b == null) {
			return ;
		}
		loadData(ID_CAIWU_FIRST_LOAD, b);
	}
	
	private ChaXunBean getChaXunBean() {
		ChaXunBean b = Utils.createChaXunBean(adapter);
//		b.yhid = MyApplication.mUser.yhid;
		
		String s = etJlb.getEditableText().toString();
		if (!TextUtils.isEmpty(s)) {
			long l = -1;
			try {
				l = Long.valueOf(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (l == -1) {
				toast(act, "请输入有效整数编号");
				return null;
			} else {
				b.jlbid = l;
			}
		}
		return b;
	}
	
	private void loadData(int type, ChaXunBean b) {
		switch(type) {
		case ID_CAIWU_FIRST_LOAD:
			layoutControl.showProgressLayout();
			adapter.clearData();
			break;
		case ID_CAIWU_LOAD_MORE:
			break;
		}
		
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 4);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(type, t);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		Result ret = null;
		Page<JuLeBuBean> page = null;
		switch(what) {
		case ID_CAIWU_FIRST_LOAD:
			ret = Utils.getPageFromJson((String)result, JuLeBuBean.class);
			
			if (!ret.success) {
				layoutControl.showRefreshLayout();
				return ;
			}
			
			layoutControl.showDataLayout();
			page  = (Page<JuLeBuBean>) ret.obj; 
			System.out.println("page=" + page);
			adapter.addPage(page);
			if (adapter.getCount() == 0) {
				layoutControl.showEmptyLayout();
			}
			break;
		case ID_CAIWU_LOAD_MORE:
			adapter.onNextPageCompleted();
			ret = Utils.getPageFromJson((String)result, JuLeBuBean.class);
			if (ret.success) {
				page  = (Page<JuLeBuBean>) ret.obj; 
				adapter.addPage(page);
			}
			
			break;
		}
	}
	
	private JuLeBuBean[] parseJson(String json) {
		return Utils.parsePageJson(json, JuLeBuBean.class);
	}
	
	public CaiWuPage(Activity a, String title) {
		this.act = a;
		this.title = title;
		
		LinearLayout layout = (LinearLayout) act.getLayoutInflater().inflate(R.layout.caiwu, null);
		this.layout = layout;
		new ViewRefBinder(this).init();
		
		ListViewModel lvm = new ListViewModel(act, this);
		layoutControl = lvm;
		lvm.setEmptyText("没有财务项");
		lvm.setRefreshHandler(this);
		layout.addView(lvm, -1, -1);
		
		
		adapter = new CaiWuAdapter(act);
		adapter.setRequestNextPage(this);
		lv.setAdapter(adapter);
		
		helper = new NeedLoginHelper(act);
		helper.setAdapter(adapter);
		helper.setLayoutControl(layoutControl);
		helper.needLogin(layout, "登录用户才有查看财务信息");
		view = helper.getView();
		
		
		layoutControl.showDataLayout();
		
		if (!MyApplication.mUser.isNull()) {
			getJulebu();
		}
		
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
	}
	
	@Override
	public void onDestroy() {
		helper.destroy();
		LoginTask.unregisterLoginSuccessListener(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
	}
	
	private void initSpinner() {
		
		Set<String> set = julebuMap.keySet();
		String[] names = new String[set.size() + 1];
		int index = 0;
		names[index++] = "无限";
		for(String s : set) {
			names[index++] = s;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, android.R.layout.simple_spinner_item, names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinnerJulebu.setAdapter(adapter);
		
//		spinnerJulebu.setVisibility(0);
	}

	@Override
	public View getView() {
		return view;
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
	public View getViewById(int arg0) {
		return layout.findViewById(arg0);
	}
	
	/**
	 * 获取俱乐部信息
	 */
	private void getJulebu() {
		System.out.println("请求俱乐部列表数据------------");
//		executeTask(ID_JULEBU, generateTask(null));
	}

	@Override
	public void onRegisterSuccess(YongHuBean user) {
		getJulebu();
	}

	@Override
	public void onLoginOrRegisterSuccess(YongHuBean user) {
		getJulebu();
	}

	@Override
	public void onLoginout() {
	}

	@Override
	public void nextPage(int count) {
		System.out.println("请求下一页数据-----------");
		ChaXunBean b = getChaXunBean();
		if (b == null) {
			return ;
		}
		loadData(ID_CAIWU_LOAD_MORE, b);
	}

	@Override
	public void doRefresh() {
		loadData(ID_CAIWU_FIRST_LOAD, getChaXunBean());
	}

}
