package cn.leyundong.activity.huiyuanzhuanqupage;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.JuLeBuAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.dialog.CheckLoginHelp;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.LoginTask;
import cn.leyundong.httpoperation.RegisterTask;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.interfaces.onLoginSuccess;
import cn.leyundong.interfaces.onRegisterSuccess;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.Utils;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 俱乐部
 * @author Administrator
 *
 */
public class JuLeBuPage extends AbstractAutoListViewPage<JuLeBuBean> implements IFindViewById, onLoginSuccess, onRegisterSuccess, IMaBiaoChangeDiListener {
	
	static final int id_jiaru = 2;
	static final int id_charge = 3;
	static final int id_pass = 4;
	static final int id_jujie = 5;

	private View view;
	private Activity act;
	
	@ViewRef(id=R.id.btnQuery, onClick="query") Button btnQuery;
	
	//场地类型
	@ViewRef(id=R.id.spinnerType) Spinner spinnerType;
	
	
	
	@ViewRef(id=R.id.etJuLeBuMingChen) EditText etJuLeBuMingChen;
	@ViewRef(id=R.id.etHuoDongChangGuan) EditText etHuoDongChangGuan;
	@ViewRef(id=R.id.etJuLeBuBianHao) EditText etJuLeBuBianHao;
	@ViewRef(id=R.id.llContainer) LinearLayout llContainer;
	
	private MaBiaoTask mbt;
	
	@SuppressWarnings("unused")
	private void query(View v) {
		System.out.println("查询-----------");
		loadData(ID_LOAD_FIRST);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		super.onTaskExecuted(what, result);
		switch(what) {
		case id_jiaru:
			handleJiaruResult((String) result);
			break;
		case id_pass:
			handlePassResult((String) result);
			break;
		case id_jujie:
			handleJujieResult((String) result);
			break;
		}
		
	}
	
	private void handleJujieResult(String json) {
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			toast(act, "拒绝成功");
			reload();
		} else {
			toast(act, "拒绝失败：" + result.error);
		}
		dismissDialog();
	}

	private void handlePassResult(String json) {
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			toast(act, "通过成功");
			reload();
		} else {
			toast(act, "通过失败：" + result.error);
		}
		dismissDialog();
	}

	private void handleJiaruResult(String json) {
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			toast(act, "加入成功");
			reload();
		} else {
			toast(act, "加入失败：" + result.error);
		}
		dismissDialog();
	}
	
	private void setTypeAdapter(BaseAdapter adapter) {
		spinnerType.setAdapter(adapter);
	}
	
	public JuLeBuPage(Activity a) {
		super(a, JuLeBuAdapter.class);
		this.act = a;
		
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		
		//view初始化
		view = act.getLayoutInflater().inflate(R.layout.julebu, null);
		new ViewRefBinder(this).init();
		
		mbt = MaBiaoTask.getInstance(act);
		BaseAdapter adapter = mbt.getChangDiLeiXingAdapter(act);
		if (adapter != null) {
			setTypeAdapter(adapter);
		}
		mbt.registChangDiObserver(this);
	}
	
	private OnClickListener itemClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("view id=" + v.getId());
			Integer pos = (Integer) v.getTag();
			JuLeBuBean jlb = (JuLeBuBean) adapter.getItem(pos);
			//未登入判断
			if (!CheckLoginHelp.isUserPresent(act)) {
				return ;
			}
			
			switch(v.getId()) {
			case R.id.btnJuJie:
				//拒绝
				doJujie(jlb);
				break;
			case R.id.btnJiaRu:
				//加入
				doJiaru(jlb);
				break;
			case R.id.btnCharge:
				//充值
				doCharge(jlb);
				break;
			case R.id.btnPass:
				//通过
				doPass(jlb);
				break;
			}
		}
	};
	
	/**
	 * 通过
	 * @param jlb
	 */
	private void doPass(JuLeBuBean jlb) {
		System.out.println("执行通过--------" + jlb);
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 16);
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.yhjlbid = jlb.yhjlbid;
		IDataRequest request = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_pass, request);
		showDialog(act, "正在执行通过，请稍候...");
	}
	
	/**
	 * 拒绝
	 * @param jlb
	 */
	protected void doJujie(JuLeBuBean jlb) {
		System.out.println("执行拒绝----------" + jlb);
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.yhjlbid = jlb.yhjlbid;
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 15);
		IDataRequest request = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_jujie, request);
		showDialog(act, "正在执行拒绝，请稍候...");
	}

	private void doJiaru(JuLeBuBean jlb) {
		System.out.println("执行加入操作--------" + jlb);
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.jlbid = jlb.jlbid;
		b.jlbsz = jlb.jlbsz;
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 12);
		IDataRequest request = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_jiaru, request);
		showDialog(act, "正在为你申请加入，请稍候...");
	}

	private void doCharge(JuLeBuBean jlb) {
		System.out.println("执行充值---------");
		
	}
	
	@Override
	public void onDestroy() {
		mbt.unregisterChangDiObserver(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
		LoginTask.unregisterLoginSuccessListener(this);
	}
	
	@Override
	public View getView() {
		return view;
	}

	@Override
	public View getViewById(int id) {
		return view.findViewById(id);
	}

	@Override
	public void onRegisterSuccess(YongHuBean user) {
		reload();
	}

	@Override
	public void onLoginOrRegisterSuccess(YongHuBean user) {
		reload();
	}
	

	private ChaXunBean getChaXunBean() {
		ChaXunBean b = Utils.createChaXunBean(adapter);
		//俱乐部名称
		b.jlbmc = etJuLeBuMingChen.getEditableText().toString();
		//场地类型
		b.cdlx = MaBiaoTask.getInstance(act).getChangDiKey((String)spinnerType.getSelectedItem());
		return b;
	}

	@Override
	public void notifySuccess(MaBiaoTask mbt) {
		BaseAdapter adapter = mbt.getChangDiLeiXingAdapter(act);
		if (adapter != null) {
			setTypeAdapter(adapter);
		}
	}

	@Override
	public void onLoginout() {
		reload();
	}

	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, JuLeBuBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 2);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, getChaXunBean());
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		llContainer.addView(lvm, -1, -1);
		lvm.setEmptyText("没有相应俱乐部");
		adapter.setOuterClickListener(itemClickListener);
	}

}
