package cn.leyundong.activity.huiyuanzhuanqupage;

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
import android.widget.Spinner;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.BaoMingQingKuanActivity;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.activity.page.ITabManager;
import cn.leyundong.adapter.BaoMingAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.custom.ListViewModel;
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
import cn.leyundong.interfaces.onLoginSuccess;
import cn.leyundong.interfaces.onRegisterSuccess;
import cn.leyundong.json.JsonParser;
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MessageObserver;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;
import cn.leyundong.util.Utils;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 报名
 * @author Administrator
 *
 */
public class BaoMingHuoDongPage extends AbstractAutoListViewPage<HuoDongBean> implements IFindViewById, 
								OnTabSelectedListener, MessageObserver, IMaBiaoChangeDiListener, 
								onLoginSuccess, onRegisterSuccess {
	
	static final int id_jiaru = 1;
	static final int id_baoming = 3;
	

	private String TAG = BaoMingHuoDongPage.class.getSimpleName() + ":";
	
	private View view;
	private Activity act;
	
	private String title;
	private ITabManager mTanManager;
	
	@ViewRef(id=R.id.container) LinearLayout container;
	@ViewRef(id=R.id.btnQuery, onClick="query") Button btnQuery;
	@ViewRef(id=R.id.tvDate) DateTextView tvDate;
	@ViewRef(id=R.id.spinnerType) Spinner spinnerType;
	
	private String queryName;
	
	
	private MaBiaoTask mbt;
	
	@SuppressWarnings("unused")
	private void query(View v) {
		System.out.println("查询--------");
		loadData(ID_LOAD_FIRST);
	}
	
	public BaoMingHuoDongPage(Activity a, String title, ITabManager m) {
		super(a, BaoMingAdapter.class);
		this.act = a;
		this.title = title;
		this.mTanManager = m;
		
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		
		view = act.getLayoutInflater().inflate(R.layout.baominghuodong, null);
		new ViewRefBinder(this).init();
		
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
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		if (what == ID_LOAD_FIRST || what == ID_LOAD_MORE) {
			super.onTaskExecuted(what, result);
		} else {
			switch(what) {
			case id_jiaru:
				handleJiaruResult((String) result);
				break;
			case id_baoming:
				handleBaomingResult((String)result);
				break;
			}
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
	public void onTabSelected(int which, String title) {
		System.out.println(which + ", " + title);
	}
	
	private void setChangXunInfo(MyMessage mm) {
		spinnerType.setSelection(mm.arg2);
		queryName = (String) mm.obj2;
	}
	
	@Override
	public boolean onReceiverMessage(MyMessage msg) {
		System.out.println("收到消息=" + msg);
		if (msg.type == MessageType.PAGE_TURN_TO) {
			//
			if (msg.arg1 == 3 && title.equals(msg.obj1)) {
				mTanManager.requestShowTitle(title);
				setChangXunInfo(msg);
				loadData(ID_LOAD_FIRST);
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

	@Override
	public void onLoginout() {
		reload();
	}

	@Override
	protected Result parseResult(String json) {
		Result result = Utils.getPageFromJson(json, HuoDongBean.class);
		System.out.println("解析结果=" + result);
		return result;
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		ChaXunBean cxb = Utils.createChaXunBean(adapter);
		//场馆名称
		cxb.cgmc = queryName;
		//场地类型
		cxb.cdlx = MaBiaoTask.getInstance(act).getChangDiKey((String) spinnerType.getSelectedItem());
		
		String url = Urls.getUrl(Urls.TYPE_HUIYUAN, 1);
		IDataRequest request = new SingleParamTask<ChaXunBean>(act, url, cxb);
		switch(what) {
		case ID_LOAD_FIRST:
			break;
		case ID_LOAD_MORE:
			break;
		}
		return request;
	}
	
	@Override
	protected void setupListView(ListViewModel lvm) {
		lvm.setEmptyText("没有报名活动项");
		container.addView(lvm, -1, -1);
		adapter.setOuterClickListener(itemButtonClick);
	}

}
