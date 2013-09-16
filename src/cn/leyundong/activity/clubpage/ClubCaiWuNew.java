package cn.leyundong.activity.clubpage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import cn.leyundong.MyApplication;
import cn.leyundong.NeedLoginHelper;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.ClubCaiWuAdapter;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.impl.DataViewModeImpl;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.Utils;
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
 * @author chenjunjun
 *
 */
public class ClubCaiWuNew extends AbstractAutoListViewPage<JuLeBuBean> implements IFindViewById {
	
	private Activity act;
	
	private View view;
	

	private ViewGroup layout;
	
	@ViewRef(id=R.id.tvHuiYuanMingCheng) EditText tvHuiYuanMingCheng;
	@ViewRef(id=R.id.tvClubMingCheng) EditText tvClubMingCheng;
	@ViewRef(id=R.id.query,onClick="onClick") Button btnQuery;
	@ViewRef(id=R.id.llContainer) LinearLayout llContainer;
	
	public ClubCaiWuNew(Activity act){
		super(act, ClubCaiWuAdapter.class);
		this.act = act;
        layout = (ViewGroup) act.getLayoutInflater().inflate(R.layout.guanlihuiyuancaiwu_new, null);
		ViewRefBinder binder = new ViewRefBinder(this);
		binder.init();
		
		NeedLoginHelper helper = new NeedLoginHelper(act);
		view = helper.needLogin(layout, "需要登录才能查看");
		helper.setAdapter(adapter);
		helper.setLayoutControl(lvm);
	}
	
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println(what + ", " + result);
		super.onTaskExecuted(what, result);
	}
	
	@Override
	public View getView() {
		return view;
	}

	public View getViewById(int id) {
		return layout.findViewById(id);
	}

	public void onClick(View v) {
		System.out.println("ClubCaiWu--doRequest2");
		switch (v.getId()) {
		case R.id.query:
			loadData(ID_LOAD_FIRST);
			break;
		}
	}
	
	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, JuLeBuBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		ChaXunBean b = Utils.createChaXunBean(adapter);
		b.yhm = tvHuiYuanMingCheng.getEditableText().toString();
		b.jlbmc = tvClubMingCheng.getEditableText().toString();
		String url = Urls.getUrl(Urls.TYPE_CLUB, RequestId.CLUB_CAIWU);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		llContainer.addView(lvm, -1, -1);
		lvm.setEmptyText("没有财务数据");
		adapter.setOuterClickListener(itemButtonClick);
	}


	private OnClickListener itemButtonClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("v id " + v.getId());
			Integer pos = (Integer) v.getTag();
			switch(v.getId()) {
			case R.id.btnDetail:
				//详情
				System.out.println("详情---------");
				doDetail(pos);
				break;
			}
		}
	};

	
	/**
	 * 去到详情
	 * @param p
	 */
	private void doDetail(int p) {
		JuLeBuBean b = (JuLeBuBean) adapter.getItem(p);
		Intent intent = new Intent(act, ClubCaiWuDetailActivityNew.class);
		intent.putExtra(JuLeBuBean.class.getSimpleName(), b);
		act.startActivity(intent);
	}
	
}
