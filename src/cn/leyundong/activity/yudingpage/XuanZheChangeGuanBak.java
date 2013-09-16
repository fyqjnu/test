package cn.leyundong.activity.yudingpage;

import java.util.Arrays;

import android.app.Activity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ViewFlipper;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.bridge.ICommunicationBridge;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.activity.page.ITabManager;
import cn.leyundong.adapter.ChangGuanAdapter;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.ChangGuanBean;
import cn.leyundong.impl.DataViewModeImpl;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MessageObserver;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;
import cn.leyundong.view.ProgressView;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 显示场馆查询列表
 * @author MSSOFT
 *
 */
public class XuanZheChangeGuanBak extends AbstractAsyncPage implements IFindViewById, MessageObserver, IMaBiaoChangeDiListener {
	//第一次加载
	private static final int TASK_ID_FIRST_LOAD = 1;
	//加载更多
	private static final int TASK_ID_LOAD_MORE = 2;
	
	private Activity act;
	
	private View view;
	
	private String title;
	
	@ViewRef(id=R.id.spinner1) Spinner spinnerType;
	@ViewRef(id=R.id.spinner2) Spinner spinnerPla;
	@ViewRef(id=R.id.etName) EditText etName;
	@ViewRef(id=R.id.btnQuery,onClick="onClick") Button btnQuery;
	@ViewRef(id=R.id.lv) ListView lv;
	@ViewRef(id=R.id.tvDate) DateTextView tvDate;
	
	@ViewRef(id=R.id.vf) ViewFlipper vf;

	private ProgressView pbView;
	private ChangGuanAdapter adapter;
	
	private IDataViewModel mDataViewMode;
	
	private ChaXunBean mCurrentChaXun;
	
	private ITabManager mTabManager;
	
	private IRefresh mRefresh = new IRefresh() {
		
		@Override
		public void doRefresh() {
			loadData(TASK_ID_FIRST_LOAD, getChaXunBean());
		}
	};
	
	private void loadData(int taskId, ChaXunBean b) {
		if (taskId == TASK_ID_FIRST_LOAD) {
			mDataViewMode.showProgressLayout();
		}
		mCurrentChaXun = b;
//		ChaXunTask task = new ChaXunTask(act, b);
//		executeTask(taskId, task);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		if (result == null) {
			if (what == TASK_ID_FIRST_LOAD) {
				mDataViewMode.showRefreshLayout();
			}
			return ;
		}
		mDataViewMode.showDataLayout();
		ChangGuanBean[] infos = (ChangGuanBean[]) result;
		if (what == TASK_ID_FIRST_LOAD) {
			adapter.clearData();
		}
		adapter.addData(Arrays.asList(infos));
		adapter.setCdlx(mCurrentChaXun.cdlx);
		if (adapter.getCount() == 0) {
			System.out.println("没有数据");
			mDataViewMode.showEmptyLayout();
		}
	}
	
	private ChaXunBean getChaXunBean() {
		ChaXunBean info = new ChaXunBean();
		info.ydrq = "" + tvDate.getText();
		info.cdlx =  MaBiaoTask.getInstance(act).getChangDiKey((String) spinnerType.getSelectedItem());
		info.csdm = MyApplication.mCity.getCsdm();
//		info.qxdm = QuXianData.getInstance(act).queryQuXianDaMa((String) spinnerPla.getSelectedItem());
		info.cgmc = etName.getText().toString();
		info.yhid = MyApplication.mUser.isNull() ? "" : MyApplication.mUser.yhid;
		return info;
	}
	
	private void onClick(View v) {
		System.out.println(v);
		switch(v.getId()) {
		case R.id.btnQuery:
			loadData(TASK_ID_FIRST_LOAD, getChaXunBean());
			break;
		}
	}
	
	@Override
	public void onResume() {
		/*ChaXunBean cxb = null;
		if (mChaxunSource != null) {
			cxb = mChaxunSource.getValue();
		}
		System.out.println("从查询源端过来的数据=" + cxb);
		if (cxb != null) {
			mTabManager.requestShowTitle(title);
			setChaXunBean(cxb);
			loadData(TASK_ID_FIRST_LOAD, cxb);
		}*/
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
	
	private ICommunicationBridge<ChaXunBean> mChaxunSource;
	private MaBiaoTask mbt;
	
	public XuanZheChangeGuanBak(Activity act, String title, ICommunicationBridge<ChaXunBean> chaxunSource, ITabManager m) {
		this.act = act;
		this.title = title;
		this.mChaxunSource = chaxunSource;
		this.mTabManager = m;
		
		view = act.getLayoutInflater().inflate(R.layout.xuanzechangguan, null);
		
		ViewRefBinder binder = new ViewRefBinder(this);
		binder.init();
		
		adapter = new ChangGuanAdapter(act);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(adapter);
		
		pbView = new ProgressView(act);
		vf.addView(pbView);
		pbView.setRefresh(mRefresh);
		
		mDataViewMode = new DataViewModeImpl(vf, pbView);
		
		mbt = MaBiaoTask.getInstance(act);
		BaseAdapter adapter = mbt.getChangDiLeiXingAdapter(act);
		setSpinnerChangDiLeiXing(adapter);
		mbt.registChangDiObserver(this);
		
		setSpinnerPlaceAdapter();
		
		MessageManager.getInstance().addObserver(this);
		
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
				loadData(TASK_ID_FIRST_LOAD, getChaXunBean());
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
}
