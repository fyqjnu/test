package cn.leyundong.activity.huiyuanzhuanqupage;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.BaoMingQingKuanActivity;
import cn.leyundong.activity.bridge.ICommunicationBridge;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.activity.page.ITabManager;
import cn.leyundong.adapter.BaoMingAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.custom.ListViewModel.IListViewFactory;
import cn.leyundong.custom.TabTitle.OnTabSelectedListener;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.dialog.CheckLoginHelp;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.HuoDongBean;
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
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MessageObserver;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;
import cn.quickdevelp.json.JsonUtil;

/**
 * 报名
 * @author Administrator
 *
 */
public class BaoMingHuoDongPage2 extends AbstractAsyncPage implements IFindViewById, 
								IListViewFactory, IRefresh, OnTabSelectedListener, ITaskGenerater, 
								MessageObserver, IMaBiaoChangeDiListener, onLoginSuccess, onRegisterSuccess {
	
	static final int id_jiaru = 1;
	static final int id_query = 2;
	static final int id_baoming = 3;
	

	private String TAG = BaoMingHuoDongPage2.class.getSimpleName() + ":";
	
	private View view;
	private Activity act;
	
	private String title;
	private ITabManager mTanManager;
	private ICommunicationBridge<ChaXunBean> mChaXunSource;
	
	@ViewRef(id=R.id.container) LinearLayout container;
	@ViewRef(id=R.id.btnQuery, onClick="query") Button btnQuery;
	@ViewRef(id=R.id.tvDate) DateTextView tvDate;
	@ViewRef(id=R.id.spinnerType) Spinner spinnerType;
	
	
	private IDataViewModel layoutControl;
	
	private ListView lv;
	private BaoMingAdapter adapter;

	private MaBiaoTask mbt;
	
	
	private void query(View v) {
		System.out.println("查询--------");
		ChaXunBean b = getChaXunBean();
		loadData(b); 
	}
	
	public BaoMingHuoDongPage2(Activity a, String title, ICommunicationBridge<ChaXunBean> chaxunSourve, ITabManager m) {
		this.act = a;
		this.title = title;
		this.mTanManager = m;
		this.mChaXunSource = chaxunSourve;
		
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		
		view = act.getLayoutInflater().inflate(R.layout.baominghuodong, null);
		new ViewRefBinder(this).init();
		
		ListViewModel lvm = new ListViewModel(act, this);
		lvm.setEmptyText("没有报名活动项");
		container.addView(lvm, -1, -1);
		lvm.setRefreshHandler(this);
		
		layoutControl = lvm;
		
		adapter = new BaoMingAdapter(act);
		adapter.setOuterClickListener(itemButtonClick);
		lv.setAdapter(adapter);
//		lv.setOnItemClickListener(adapter);
		
		mbt = MaBiaoTask.getInstance(act);
		BaseAdapter adapter = mbt.getChangDiLeiXingAdapter(act);
		setChangDiLeiXingAdapter(adapter);
		mbt.registChangDiObserver(this);
		
		MessageManager.getInstance().addObserver(this);
	}
	
	private OnClickListener itemButtonClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("view id=" + v.getId());
			boolean up = CheckLoginHelp.isUserPresent(act);
			if (!up) {
				return ;
			}
			Integer p = (Integer) v.getTag();
			HuoDongBean b = (HuoDongBean) adapter.getItem(p);
			switch(v.getId()) {
			case R.id.btnBaoMing:
				//报名
				if (!b.sfhy) {
					//不是会员 提示用户先按加入按钮
					toast(act, "您还不是会员，请点击加入按钮申请成为会员！");
					return ;
				}
				doBaoming(b);
				break;
			case R.id.btnJiaRu:
				//加入
				doJiaru(b);
				break;
			case R.id.btnDetail:
				//报名详情
				Intent intent = new Intent(act, BaoMingQingKuanActivity.class);
				intent.putExtra(HuoDongBean.class.getSimpleName(), b);
				act.startActivity(intent);
				break;
			}
		}
	};
	
	private View inputPeopleView;
	private Button btnOk;
	private Button btnCancel;
	private EditText etPeople;
	
	private void doBaoming(HuoDongBean b) {
		System.out.println("执行报名------");
		DialogA dlg = new DialogA(act);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlg.b = b;
		dlg.show();
		inputPeopleView = act.getLayoutInflater().inflate(R.layout.baoming_dialog, null);
		etPeople = (EditText) inputPeopleView.findViewById(R.id.etPeople);
		btnOk = (Button) inputPeopleView.findViewById(R.id.btnOk);
		btnCancel = (Button) inputPeopleView.findViewById(R.id.btnCancel);
		btnOk.setOnClickListener(dialogButtonClick);
		btnCancel.setOnClickListener(dialogButtonClick);
		btnOk.setTag(dlg);
		btnCancel.setTag(dlg);
		dlg.setContentView(inputPeopleView);
	}
	
	class DialogA extends Dialog {
		HuoDongBean b;

		public DialogA(Context context) {
			super(context);
		}
		
	}
	
	private OnClickListener dialogButtonClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			DialogA dlg = (DialogA) v.getTag();
			switch(v.getId()) {
			case R.id.btnOk:
				String input = etPeople.getText().toString();
				int num = -1;
				try {
					num = Integer.valueOf(input.trim());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (num <= 0) {
					toast(act, "请输入有效人数");
				} else {
					dlg.cancel();
					//
					baoming(dlg.b, num);
				}
				break;
			case R.id.btnCancel:
				dlg.cancel();
				break;
			}
		}
	};

	private void baoming(HuoDongBean hd, int people) {
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 13);
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.hdid = hd.hdid;
		b.bmrs = people;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		executeTask(id_baoming, t);
		showDialog(act, "正在为您报名，请稍候...");
	}
	
	private void doJiaru(HuoDongBean b) {
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 12);
		ChaXunBean cx = new ChaXunBean();
		cx.yhid = MyApplication.mUser.yhid;
		cx.jlbid = b.jlbid;
		cx.jlbsz = b.jlbsz;
		IDataRequest request = new SingleParamTask<ChaXunBean>(act, url, cx);
		executeTask(id_jiaru, request);
		showDialog(act, "正在申请加入俱乐部，请稍候...");
	}
	
	
	@Override
	public void onDestroy() {
		System.out.println(TAG + "destory----------");
		mbt.unregisterChangDiObserver(this);
		MessageManager.getInstance().removeObserver(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
		LoginTask.unregisterLoginSuccessListener(this);
	}
	
	private void loadData(ChaXunBean b) {
		System.out.println("加载查询=" + b);
		adapter.clearData();
		layoutControl.showProgressLayout();
		executeTask(id_query, generateTask(b));
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		switch(what) {
		case id_query:
			handleQueryResult((String) result);
			break;
		case id_jiaru:
			handleJiaruResult((String) result);
			break;
		case id_baoming:
			handleBaomingResult((String)result);
			break;
		}
	}
	
	//报名完成
	private void handleBaomingResult(String json) {
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			toast(act, "报名成功");
			reload();
		} else {
			toast(act, "报名失败：" + result.error);
		}
		dismissDialog();
	}

	private void handleJiaruResult(String json) {
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			//加入成功
			toast(act, "加入成功");
			reload();
		} else {
			//失败
			toast(act, "加入失败:" + result.error);
		}
		dismissDialog();
	}

	private void handleQueryResult(String json) {
		HuoDongBean[] infos = parseJson((String) json);
		if (infos == null) {
			//加左边失败
			layoutControl.showRefreshLayout();
			return ;
		}
		layoutControl.showDataLayout();
		List<HuoDongBean> d = Arrays.asList(infos);
		adapter.addData(d);
		if (adapter.getCount() == 0) {
			//
			System.out.println("没有查询到数据------");
			layoutControl.showEmptyLayout();
		}
	}
	
	
	private HuoDongBean[] parseJson(String s) {
		Result result = JsonParser.getResultFromJson(s);
		if (result.success) {
			try {
				JSONObject obj = new JSONObject(s);
				String key = "page";
				if (!obj.isNull(key)) {
					JSONObject page = obj.getJSONObject(key);
					key = "list";
					if (!page.isNull(key)) {
						JSONArray list = page.getJSONArray(key);
						HuoDongBean[] infos = JsonUtil.parseJsonArray(list.toString(), HuoDongBean.class);
						return infos;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return new HuoDongBean[0];
		}
		return null;
	}
	
	@Override
	public void onResume() {
		/*ChaXunBean b = null;
		if (mChaXunSource != null) {
			b = mChaXunSource.getValue();
		}
		System.out.println("从数据源数据=" + b);
		if (b != null) {
			mTanManager.requestShowTitle(title);
			loadData(b);
		}*/
	}
	
	private void setChangDiLeiXingAdapter(BaseAdapter adapter) {
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

	@Override
	public ListView getListView() {
		System.out.println("生成listview---------");
		if (lv == null) {
			lv = new ListView(act);
		}
		return lv;
	}

	@Override
	public void doRefresh() {
		System.out.println("刷新--------------");
	}

	@Override
	public void onTabSelected(int which, String title) {
		System.out.println(which + ", " + title);
	}
	
	private ChaXunBean getChaXunBean() {
		ChaXunBean b = new ChaXunBean();
		b.ydrq = "" + tvDate.getText();
		b.cdlx =  MaBiaoTask.getInstance(act).getChangDiKey((String) spinnerType.getSelectedItem());
		b.yhid = MyApplication.mUser.yhid;
		return b;
	}

	@Override
	public IDataRequest generateTask() {
		return null;
	}

	@Override
	public IDataRequest generateTask(Object param) {
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 1);
		ChaXunBean b = (ChaXunBean) param; 
		return new SingleParamTask<ChaXunBean>(act, url, b);
	}
	
	private void setChangXunInfo(MyMessage mm) {
		spinnerType.setSelection(mm.arg2);
	}
	
	@Override
	public boolean onReceiverMessage(MyMessage msg) {
		System.out.println("收到消息=" + msg);
		if (msg.type == MessageType.PAGE_TURN_TO) {
			//
			if (msg.arg1 == 3 && title.equals(msg.obj1)) {
				mTanManager.requestShowTitle(title);
				setChangXunInfo(msg);
				loadData(getChaXunBean());
				if (msg.needFeedbackResult) {
					msg.callback.onMessageHandle(msg.messageId, 2);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void notifySuccess(MaBiaoTask mbt) {
		setChangDiLeiXingAdapter(mbt.getChangDiLeiXingAdapter(act));
	}

	@Override
	public void onRegisterSuccess(YongHuBean user) {
		reload();
	}

	@Override
	public void onLoginOrRegisterSuccess(YongHuBean user) {
		reload();
	}

	//重新加载
	private void reload() {
		System.out.println("用户登入成功后报名活动页面重新加载数据------------");
		loadData(getChaXunBean());
	}

	@Override
	public void onLoginout() {
		reload();
	}

}
