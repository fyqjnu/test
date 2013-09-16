package cn.leyundong.activity.yudingpage;

import android.app.Activity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import cn.leyundong.R;
import cn.leyundong.activity.bridge.ICommunicationBridge;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.activity.page.ITabManager;
import cn.leyundong.adapter.ChangGuanAdapter;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.ChangGuanBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
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
 * 显示场馆查询列表
 * @author MSSOFT
 *
 */
public class XuanZheChangeGuan extends AbstractAutoListViewPage<ChangGuanBean> implements IFindViewById, MessageObserver, IMaBiaoChangeDiListener {
	
	private Activity act;
	
	private View view;
	
	private String title;
	
	@ViewRef(id=R.id.spinner1) Spinner spinnerType;
	@ViewRef(id=R.id.spinner2) Spinner spinnerPla;
	@ViewRef(id=R.id.etName) EditText etName;
	@ViewRef(id=R.id.btnQuery,onClick="onClick") Button btnQuery;
	@ViewRef(id=R.id.tvDate) DateTextView tvDate;
	@ViewRef(id=R.id.llContainer) LinearLayout llContainer;
	
	private ChaXunBean mCurrentChaXun;
	
	private ITabManager mTabManager;
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		super.onTaskExecuted(what, result);
		((ChangGuanAdapter)adapter).setCdlx(mCurrentChaXun.cdlx);
	}
	
	@SuppressWarnings("unused")
	private void onClick(View v) {
		System.out.println(v);
		switch(v.getId()) {
		case R.id.btnQuery:
			loadData(ID_LOAD_FIRST);
			break;
		}
	}
	
	private int getPositionOfSpinner(Spinner sn, String s) {
		SpinnerAdapter adapter = sn.getAdapter();
		int count = adapter.getCount();
		int p = 0;
		for (int i = 0; i < count; i++) {
			Object item = adapter.getItem(i);
			if (item.equals(s)) {
				p = i;
				break;
			}
		}
		System.out.println("查询：" + s + " 位置 = " + p);
		return p;
	}
	
	private MaBiaoTask mbt;
	
	public XuanZheChangeGuan(Activity act, String title, ICommunicationBridge<ChaXunBean> chaxunSource, ITabManager m) {
		super(act, ChangGuanAdapter.class);
		this.act = act;
		this.title = title;
		this.mTabManager = m;
		
		MessageManager.getInstance().addObserver(this);
		mbt = MaBiaoTask.getInstance(act);
		mbt.registChangDiObserver(this);
		
		view = act.getLayoutInflater().inflate(R.layout.xuanzechangguan, null);
		ViewRefBinder binder = new ViewRefBinder(this);
		binder.init();
		
		BaseAdapter adapter = mbt.getChangDiLeiXingAdapter(act);
		setSpinnerChangDiLeiXing(adapter);
		
		setSpinnerPlaceAdapter();
	}
	
	@Override
	public void onDestroy() {
		MessageManager.getInstance().removeObserver(this);
		mbt.unregisterChangDiObserver(this);
	}
	
	private void setSpinnerChangDiLeiXing(BaseAdapter adapter) {
		if (adapter != null) {
			spinnerType.setAdapter(adapter);
		}
	}
	private void setSpinnerPlaceAdapter() {
	}

	@Override
	public View getView() {
		return view;
	}


	@Override
	public View getViewById(int id) {
		return view.findViewById(id);
	}
	
	private void setChangXunInfo(MyMessage mm) {
		etName.setText((String)mm.obj2);
		spinnerType.setSelection(mm.arg2);
	}

	@Override
	public boolean onReceiverMessage(MyMessage msg) {
		System.out.println("收到消息=" + msg);
		if (msg.type == MessageType.PAGE_TURN_TO) {
			//
			if (msg.arg1 == 1 && title.equals(msg.obj1)) {
				mTabManager.requestShowTitle(title);
				setChangXunInfo(msg);
				loadData(ID_LOAD_FIRST);
				//回调
				if (msg.needFeedbackResult) {
					msg.callback.onMessageHandle(msg.messageId, 1);
				}
				
				return true;
			}
		}
		return false;
	}

	@Override
	public void notifySuccess(MaBiaoTask mbt) {
		BaseAdapter adapter = mbt.getChangDiLeiXingAdapter(act);
		setSpinnerChangDiLeiXing(adapter);
	}

	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, ChangGuanBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, RequestId.CHAXUNCHANGGUAN);
		ChaXunBean b = Utils.createChaXunBean(adapter);
		mCurrentChaXun = b;
		//场地类型
		b.cdlx =  MaBiaoTask.getInstance(act).getChangDiKey((String) spinnerType.getSelectedItem());
		//场馆名称
		b.cgmc = etName.getText().toString();
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		llContainer.addView(lvm, -1, -1);
		lvm.setEmptyText("没有查询到相应场馆");
		lv.setOnItemClickListener((ChangGuanAdapter)adapter);
	}
}
