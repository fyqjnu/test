package cn.leyundong.activity.yudingpage;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import cn.leyundong.NeedLoginHelper;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.HuiYuanCaiWuAdapter;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.ChangGuangHuiYuanCaiWuBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.util.Utils;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 会员财务
 * @author Administrator
 *
 */
public class HuiYuanCaiWu  extends AbstractAutoListViewPage<ChangGuangHuiYuanCaiWuBean>  implements IFindViewById  {
	
	private Activity act;
	
	private View view;
	
	@ViewRef(id=R.id.etName) EditText etName;
	@ViewRef(id=R.id.snType) Spinner snType;
	@ViewRef(id=R.id.btnQuery, onClick="query") Button btnQuery;
	@ViewRef(id=R.id.llContainer) LinearLayout llContainer;

	private LinearLayout layout;

	private NeedLoginHelper helper;
	
	@SuppressWarnings("unused")
	private void query(View v) {
		//查询
		loadData(ID_LOAD_FIRST);
	}
	
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		super.onTaskExecuted(what, result);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.destroy();
	}
	
	public HuiYuanCaiWu(Activity act) {
		super(act, HuiYuanCaiWuAdapter.class);
		this.act = act;
		layout = (LinearLayout) act.getLayoutInflater().inflate(R.layout.huiyuancaiwu, null);
		new ViewRefBinder(this).init();
		
		helper = new NeedLoginHelper(act);
		helper.setAdapter(adapter);
		helper.setLayoutControl(layoutControl);
		helper.needLogin(layout, "登录才能查询数据");
		view = helper.getView();
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public View getViewById(int arg0) {
		return layout.findViewById(arg0);
	}
	
	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, ChangGuangHuiYuanCaiWuBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, RequestId.HUI_YUAN_CAI_WU);
		ChaXunBean b = Utils.createChaXunBean(adapter);
		b.cgmc = etName.getEditableText().toString();
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		llContainer.addView(lvm, -1, -1);
		lvm.setEmptyText("没有财务数据");
	}

}
