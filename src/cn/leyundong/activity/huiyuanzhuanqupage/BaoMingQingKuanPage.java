package cn.leyundong.activity.huiyuanzhuanqupage;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.BaoMingQingKuanAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.HuoDongBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.util.Utils;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

public class BaoMingQingKuanPage extends AbstractAutoListViewPage<HuoDongBean> implements IFindViewById {
	
	private HuoDongBean extraHongDongBean;
	
	@ViewRef(id=R.id.ll)LinearLayout llContainer;
	@ViewRef(id=R.id.tv)TextView tv;
	@ViewRef(id=R.id.btnQuery, onClick="query")Button btnQuery;
	@ViewRef(id=R.id.header)Header header;
	@ViewRef(id=R.id.etName)EditText etName;
	
	private LinearLayout layout;
	
	@SuppressWarnings("unused")
	private void query(View v) {
		loadData(ID_LOAD_FIRST);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		super.onTaskExecuted(what, result);
	}
	
	public BaoMingQingKuanPage(Activity act, HuoDongBean b) {
		super(act, BaoMingQingKuanAdapter.class);
		extraHongDongBean = b;
		
		layout = (LinearLayout) act.getLayoutInflater().inflate(R.layout.baomingqingkuan, null);
		ViewRefBinder vb = new ViewRefBinder(this);
		vb.init();
		
		header.setTitle("报名情况");
		
		tv.setText(extraHongDongBean.hdcg + "  活动时间：" + extraHongDongBean.hdsjd);
		
		loadData(ID_LOAD_FIRST);
	}

	@Override
	public View getView() {
		return layout;
	}

	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, HuoDongBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 11);;
		ChaXunBean b = Utils.createChaXunBean(adapter);
		b.hdid = extraHongDongBean.hdid;
		b.yhm = etName.getEditableText().toString();
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		layout.addView(lvm, -1, -1);
		lvm.setEmptyText("没有查询到数据");
	}


	@Override
	public View getViewById(int arg0) {
		return layout.findViewById(arg0);
	}

}
