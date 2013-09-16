package cn.leyundong.activity.clubpage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import cn.leyundong.NeedLoginHelper;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.ClubGuanLiAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.Utils;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

public class ManagerHuiYuanPage extends AbstractAutoListViewPage<JuLeBuBean> implements IFindViewById {
	
	static final int id_pass = 1;
	static final int id_reject = 2;
	static final int id_deleteHuiyuan = 3;
	static final int id_deleteManager = 4;
	static final int id_cancelYaoqing = 5;
	static final int id_setManager = 6;
	
	
	private ViewGroup layout;
	private View view;
	
	private JuLeBuBean extraJuLeBuBean;
	
	@ViewRef(id=R.id.llContainer)LinearLayout llContainer;
	@ViewRef(id=R.id.etName)EditText etName;
	@ViewRef(id=R.id.header)Header header;
	@ViewRef(id=R.id.btnQuery, onClick="query")Button btnQuery;
	private NeedLoginHelper helper;
	
	private void query(View v) {
		System.out.println("查询------------");
		loadData(ID_LOAD_FIRST);
	}
	
	public ManagerHuiYuanPage(Activity act, JuLeBuBean b) {
		super(act, ClubGuanLiAdapter.class);
		extraJuLeBuBean = b;
		
		layout = (ViewGroup) act.getLayoutInflater().inflate(R.layout.club_manager_huiyuan, null);
		new ViewRefBinder(this).init();
		
		header.setTitle("管理会员");
		
		helper = new NeedLoginHelper(act);
		view = helper.needLogin(layout, "登录才能查看");
		helper.setAdapter(adapter);
		helper.setLayoutControl(lvm);
		
		loadData(ID_LOAD_FIRST);
		
	}
	
	private android.view.View.OnClickListener itemButtonClick = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("v id=" + v.getId());
			Integer pos = (Integer) v.getTag();
			JuLeBuBean jlb = (JuLeBuBean) adapter.getItem(pos);
			switch(v.getId()) {
			case R.id.btnPass:
				doPass(jlb);
				break;
			case R.id.btnReject:
				doReject(jlb);
				break;
			case R.id.btnDeleteHuiYuan:
				doDeleteHuiyuan(jlb);
				break;
			case R.id.btnDeleteManager:
				doDeleteManager(jlb);
				break;
			case R.id.btnSetManager:
				doSetManager(jlb);
				break;
			case R.id.btnCancelYaoqing:
				doCancelYaoqing(jlb);
				break;
			}
		}
	};
	
	protected void onTaskExecuted(int what, Object result) {
		super.onTaskExecuted(what, result);
		switch(what) {
		case id_pass:
			handlePassResult((String)result);
			break;
		case id_cancelYaoqing:
			handleCancelYaoqingResult((String) result);
			break;
		case id_deleteHuiyuan:
			handleDeleteHuiyuanResult((String) result);
			break;
		case id_deleteManager:
			handleDeleteManagerResult((String) result);
			break;
		case id_reject:
			handleRejectResult((String) result);
			break;
		case id_setManager:
			handleSetManagerResult((String) result);
			break;
		}
	};
	
	private void handlePassResult(String result) {
		dismissDialog();
		Result ret = JsonParser.getResultFromJson(result);
		if (ret.success) {
			toast(act, "通过成功");
			reload();
		} else {
			toast(act, "通过失败：" + ret.error);
		}
	}
	private void handleRejectResult(String result) {
		dismissDialog();
		Result ret = JsonParser.getResultFromJson(result);
		if (ret.success) {
			toast(act, "拒绝成功");
			reload();
		} else {
			toast(act, "拒绝失败：" + ret.error);
		}
	}
	private void handleCancelYaoqingResult(String result) {
		dismissDialog();
		Result ret = JsonParser.getResultFromJson(result);
		if (ret.success) {
			toast(act, "取消邀请成功");
			reload();
		} else {
			toast(act, "取消邀请失败：" + ret.error);
		}
	}
	private void handleDeleteHuiyuanResult(String result) {
		dismissDialog();
		Result ret = JsonParser.getResultFromJson(result);
		if (ret.success) {
			toast(act, "删除会员成功");
			reload();
		} else {
			toast(act, "删除会员失败：" + ret.error);
		}
	}
	private void handleDeleteManagerResult(String result) {
		dismissDialog();
		Result ret = JsonParser.getResultFromJson(result);
		if (ret.success) {
			toast(act, "删除管理员成功");
			reload();
		} else {
			toast(act, "删除管理员失败：" + ret.error);
		}
	}
	private void handleSetManagerResult(String result) {
		dismissDialog();
		Result ret = JsonParser.getResultFromJson(result);
		if (ret.success) {
			toast(act, "设置管理员成功");
			reload();
		} else {
			toast(act, "设置管理员失败：" + ret.error);
		}
	}

	@Override
	public void onDestroy() {
		helper.destroy();
	}

	/**
	 * 通过
	 * @param jlb
	 */
	protected void doPass(JuLeBuBean jlb) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 11);
		ChaXunBean b = Utils.createChaXunBean(null);
		b.yhjlbid = jlb.yhjlbid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_pass, t);
		showDialog(act, "执行通过，请稍候...");
	}
	/**
	 * 拒绝
	 * @param jlb
	 */
	protected void doReject(JuLeBuBean jlb) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 12);
		ChaXunBean b = Utils.createChaXunBean(null);
		b.yhjlbid = jlb.yhjlbid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_reject, t);
		showDialog(act, "执行拒绝，请稍候...");
	}
	/**
	 * 设置管理员
	 * @param jlb
	 */
	protected void doSetManager(JuLeBuBean jlb) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 14);
		ChaXunBean b = Utils.createChaXunBean(null);
		b.yhjlbid = jlb.yhjlbid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_setManager, t);
		showDialog(act, "设置管理员，请稍候...");
	}
	/**
	 * 取消邀请
	 * @param jlb
	 */
	protected void doCancelYaoqing(JuLeBuBean jlb) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 13);
		ChaXunBean b = Utils.createChaXunBean(null);
		b.yhjlbid = jlb.yhjlbid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_cancelYaoqing, t);
		showDialog(act, "执行取消邀请，请稍候...");
	}
	/**
	 * 删除会员
	 * @param jlb
	 */
	protected void doDeleteHuiyuan(JuLeBuBean jlb) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 16);
		ChaXunBean b = Utils.createChaXunBean(null);
		b.yhjlbid = jlb.yhjlbid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_deleteHuiyuan, t);
		showDialog(act, "删除会员，请稍候...");
	}
	/**
	 * 删除管理员
	 * @param jlb
	 */
	protected void doDeleteManager(JuLeBuBean jlb) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 15);
		ChaXunBean b = Utils.createChaXunBean(null);
		b.yhjlbid = jlb.yhjlbid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_deleteManager, t);
		showDialog(act, "删除管理员，请稍候...");
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, JuLeBuBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 21);
		ChaXunBean b = Utils.createChaXunBean(adapter);
		b.yhm = etName.getText().toString();
		b.jlbid = extraJuLeBuBean.jlbid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		llContainer.addView(lvm, -1, -1);
		adapter.setOuterClickListener(itemButtonClick);
	}

	@Override
	public View getViewById(int arg0) {
		return layout.findViewById(arg0);
	}

}
