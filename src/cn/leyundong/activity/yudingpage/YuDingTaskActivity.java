package cn.leyundong.activity.yudingpage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.BaseActivity;
import cn.leyundong.activity.LoginActivity;
import cn.leyundong.adapter.RiQiAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.ChangDiJiaGeBean;
import cn.leyundong.entity.ChangDiJiaGeShiJianBean;
import cn.leyundong.entity.ChangGuanBean;
import cn.leyundong.entity.DingDanXiangBean;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.RiQiBean;
import cn.leyundong.entity.ShiJianBean;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.LoginTask;
import cn.leyundong.httpoperation.RegisterTask;
import cn.leyundong.impl.DataViewModeImpl;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.interfaces.onLoginSuccess;
import cn.leyundong.interfaces.onRegisterSuccess;
import cn.leyundong.json.JsonParser;
import cn.leyundong.message.IMessageCallback;
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MessageObserver;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;
import cn.leyundong.test.Printer;
import cn.leyundong.view.BoxView;
import cn.leyundong.view.BoxView.OnUserOperationChanged;
import cn.leyundong.view.Header;
import cn.leyundong.view.MyGallery;
import cn.leyundong.view.MyGallery.OnGalleryScrollStop;
import cn.leyundong.view.ProgressView;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;

public class YuDingTaskActivity extends BaseActivity implements OnItemSelectedListener, OnGalleryScrollStop, IRefresh, OnUserOperationChanged, onLoginSuccess, onRegisterSuccess, IMaBiaoChangeDiListener, MessageObserver {

	@ViewRef(id=R.id.header) Header header;
	@ViewRef(id=R.id.gallery) MyGallery gallery;
	@ViewRef(id=R.id.vf1) ViewFlipper vf;
	@ViewRef(id=R.id.tvMoney) TextView tvMoney;
	@ViewRef(id=R.id.llContainer) LinearLayout content;
	@ViewRef(id=R.id.btnOrder, onClick="order") Button btnOrder;
	Spinner spinnerType;
	private ChangGuanBean extraChangGuanBean;
	private ChangGuanBean changGuanBean;
	
	private Set<Integer> currentTask = new HashSet<Integer>();
	
	
	private String cdlx;
	private BoxView bx;
	private RiQiAdapter adapter;
	
	private int gallerySelectionPos = 0;
	private IDataViewModel layoutControl;
	
	private Set<String> selectedItemPosition;
	
	private double mCurrentMoney;
	
	private Map<String, DingDanXiangBean> mCurrentDingDanXiang;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		};
	};
	
	
	private void order(View v) {
		System.out.println("下单----------" + mCurrentDingDanXiang);
		if (changGuanBean == null) {
			toast("数据有误，无法下单");
			return ;
		}
		
		if (mCurrentDingDanXiang == null || mCurrentDingDanXiang.size() == 0) {
			toast("请先选中场号");
			return ;
		}
		
		//判断用户是否登录
		if (MyApplication.mUser.isNull()) {
			Intent intent = new Intent();
			intent.setClass(act, LoginActivity.class);
			startActivity(intent);
			return ;
		}
		
		//跳转到我的预订页面
		Intent intent = new Intent(act, MyOrderActivity.class);
		
		ArrayList<DingDanXiangBean> infos = new ArrayList<DingDanXiangBean>();
		for (DingDanXiangBean b : mCurrentDingDanXiang.values()) {
			infos.add(b);
		}
		ChaXunBean b = new ChaXunBean();
		//用户id
		b.yhid = MyApplication.mUser.yhid;
		RiQiBean riqi = (RiQiBean) gallery.getSelectedItem();
		//日期
		b.ydrq = riqi.dqrq;
		//场地类型
		b.cdlx = mbt.getChangDiKey((String) spinnerType.getSelectedItem());
		//场馆id
		b.cgid = changGuanBean.cgid;
		//订单项
//		b.ddxBeanList = infos;
		//会员组id
		b.hyzid = changGuanBean.hyzid;
		System.out.println("会员组id=" + b.hyzid);
		intent.putExtra(ChaXunBean.class.getSimpleName(), b);
		intent.putExtra(DingDanXiangBean.class.getSimpleName(), infos);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			//下单成功 刷新
			reload();
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		extraChangGuanBean = (ChangGuanBean) getIntent().getSerializableExtra(ChangGuanBean.class.getSimpleName());
		System.out.println("extraChangGuanBean=" + extraChangGuanBean);
		cdlx = getIntent().getStringExtra("cdlx");
		System.out.println("cdlx=" + cdlx);
		if (extraChangGuanBean == null || cdlx == null) {
			finish();
			return ;
		}
		MessageManager.getInstance().addObserver(this);
		
		setContentView(R.layout.yudingtask);
		
		ProgressView pv = new ProgressView(act);
		pv.setRefresh(this);
		vf.addView(pv);
		layoutControl = new DataViewModeImpl(vf, pv);
		
		header.setTitle(extraChangGuanBean.cgmc);
		LayoutParams lp = new LayoutParams(-2, -2);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		spinnerType = new Spinner(act);
		header.addComponent(spinnerType, lp);
		
		//添加自定义控件
		bx = new BoxView(act);
		bx.setOnUserOperationChangedListener(this);
		content.addView(bx, -1, -1);
		
		fillGallery();
		
		mbt = MaBiaoTask.getInstance(act);
		mbt.registChangDiObserver(this);
		setChangDiLeiXingAdapter(mbt.getChangDiLeiXingAdapter(act));
		setChangDiLeiXing(cdlx);
		spinnerType.setOnItemSelectedListener(spinnerItemSelectedListener);
		
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		
		RiQiBean today = getRiqiBean(Calendar.getInstance());
		loadData(today);
	}
	
	private OnItemSelectedListener spinnerItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			loadData((RiQiBean) gallery.getSelectedItem());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	private MaBiaoTask mbt;
	
	private void setChangDiLeiXingAdapter(BaseAdapter adapter) {
		if (adapter != null) {
			spinnerType.setAdapter(adapter);
		}
	}
	
	private void setChangDiLeiXing(String lx) {
		if (lx == null || spinnerType.getAdapter() == null) {
			return ;
		}
		String name = mbt.getChangDiNameByKey(lx);
		int size = spinnerType.getAdapter().getCount();
		int i = 0; 
		boolean find = false;
		for (; i < size; i++) {
			if (name.equals(spinnerType.getItemAtPosition(i))) {
				find = true;
				break ;
			}
		}
		if (find) {
			spinnerType.setSelection(i);
		}
	}
	
	@Override
	public void notifySuccess(MaBiaoTask mbt) {
		setChangDiLeiXingAdapter(mbt.getChangDiLeiXingAdapter(act));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		MessageManager.getInstance().removeObserver(this);
		MaBiaoTask.getInstance(act).unregisterChangDiObserver(this);
		LoginTask.unregisterLoginSuccessListener(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
	}
	
	private void fillData(List<ShiJianBean> shiJian, List<ChangDiJiaGeBean> changDi) {
//		ChangDiJiaGeBean b = changDi.get(0);
//		ChangDiJiaGeShiJianBean b2 = b.cdsjjgList.get(0);
//		b2.sfyxs = true;
		System.out.println("显示数据----------");
		bx.fillData(shiJian, changDi);
		layoutControl.showDataLayout();
		if (selectedItemPosition != null) {
			bx.setItemSelectedPosition(selectedItemPosition);
		}
		selectedItemPosition = null;
		/*List<ChangDiJiaGeBean> changDi = new ArrayList<ChangDiJiaGeBean>();
		for (int i = 0; i < 20; i++) {
			changDi.add(new ChangDiJiaGeBean());
		}
		List<ShiJianBean> shiJian = new ArrayList<ShiJianBean>();
		for (int i = 0; i < 20; i++) {
			shiJian.add(new ShiJianBean());
		}
		bx.fillData(shiJian, changDi);*/
	}

	private void loadData(RiQiBean rq) {
		if (rq == null || currentTask.contains(rq.hashCode())) {
			return ;
		}
		currentTask.add(rq.hashCode());
		System.out.println("加载数据--------------");
		showMoney(0);
		layoutControl.showProgressLayout();
		ChaXunBean cx = getChaXunBean(rq);
		int id = rq.dqrq.hashCode();
		executeTask(id, new QueryTask(cx));
	}
	
	
	class QueryTask implements IDataRequest {
		ChaXunBean b;
		public QueryTask(ChaXunBean b) {
			this.b = b;
		}

		@Override
		public Object doRequest() {
			String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, 3);
			List<IJson> list = new ArrayList<IJson>();
			BeanProxy<ChaXunBean> obj = new BeanProxy<ChaXunBean>(b);
			list.add(obj);
			IHttpDuty duty = new HttpPostDuty(act, url, list);
			return duty.post();
		}
		
	}
	
	private void save(String s) {
		File dir = new File(Environment.getExternalStorageDirectory(), "lyd");
		if (!dir.exists()){
			dir.mkdir();
		}
	}
	
	private void onloadFail() {
		layoutControl.showRefreshLayout();
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		currentTask.remove(what);
		RiQiBean rq = (RiQiBean) gallery.getSelectedItem();
		if (what != rq.dqrq.hashCode()) {
			//不是当次请求
			return ;
		}
		
		if (result != null) {
			gallerySelectionPos = gallery.getSelectedItemPosition();
			String s = (String) result;
			Result ret = JsonParser.getResultFromJson(s);
			if (ret.success) {
				//成功
				parseJson(s);
			} else {
				//失败
				toast("请求数据失败");
				onloadFail();
				
			}
			
		} else {
			onloadFail();
		}
	}
	
	private void parseJson(String s) {
		//
		try {
			JSONObject obj = new JSONObject(s);
//			System.out.println("=========================");
//			Printer.print(s, 10);
//			System.out.println("=========================");
			
			
			if (!obj.isNull("ChangGuanBean")) {
				List<ShiJianBean> shiJianList = null;
				List<ChangDiJiaGeBean> changDiJiaGeList = null;
				
				changGuanBean = JsonUtil.parseJson(obj.getString(ChangGuanBean.class.getSimpleName()), ChangGuanBean.class);
				if (changGuanBean != null) {
					System.out.println("会员组id=" + changGuanBean.hyzid);
					shiJianList = changGuanBean.shiJianBeanList;
					changDiJiaGeList = changGuanBean.cdjgBeanList;
				}
				
				/*JSONObject changGuan = obj.getJSONObject("ChangGuanBean");
				if (!changGuan.isNull("shiJianBeanList")) {
					//时间表
					JSONArray ary = changGuan.getJSONArray("shiJianBeanList");
					ShiJianBean[] shiJian = JsonUtil.parseJsonArray(ary.toString(), ShiJianBean.class);
					shiJianList = Arrays.asList(shiJian);
					System.out.println(shiJianList);
				}
				if (!changGuan.isNull("cdjgBeanList")) {
					//场地价格
					JSONArray ary = changGuan.getJSONArray("cdjgBeanList");
					
					changDiJiaGeList = new ArrayList<ChangDiJiaGeBean>();
					
					int size = ary.length();
					for (int i = 0; i < size; i++) {
						ChangDiJiaGeBean b = new ChangDiJiaGeBean();
						changDiJiaGeList.add(b);
						
						JSONObject jo = ary.getJSONObject(i);
						String key = "cdid";
						if (!jo.isNull("cdid")) {
							b.cdid = jo.getString("cdid");
						}
						key = "cdmc";
						if (!jo.isNull(key)) {
							b.cdmc = jo.getString(key);
						}
						key = "cdczmc";
						if (!jo.isNull(key)) {
							b.cdczmc = jo.getString(key);
						}
						key = "cdzt";
						if (!jo.isNull(key)) {
							b.cdzt = jo.getString(key);
						}
						if (!jo.isNull("cdsjjgList")) {
							//时间格式列表
							JSONArray cdsjjgList = jo.getJSONArray("cdsjjgList");
							System.out.println("cdsjjgList=" + cdsjjgList);
							ChangDiJiaGeShiJianBean[] jiaGeShijian = JsonUtil.parseJsonArray(cdsjjgList.toString(), ChangDiJiaGeShiJianBean.class);
							if (jiaGeShijian != null) {
								b.cdsjjgList = Arrays.asList(jiaGeShijian);
							}
						}
					}
					
				}*/
				if (shiJianList != null && changDiJiaGeList != null) {
					fillData(shiJianList, changDiJiaGeList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ChaXunBean getChaXunBean(RiQiBean rq) {
		ChaXunBean info = new ChaXunBean();
		//日期
		info.ydrq = rq.dqrq;
		//场馆id
		info.cgid = extraChangGuanBean.cgid;
		//场地类型
		info.cdlx =  mbt.getChangDiKey((String) spinnerType.getSelectedItem());
		if (!MyApplication.mUser.isNull()) {
			info.yhid = MyApplication.mUser.yhid;
		}
		return info;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("pos selected=" + position + gallery.isTouched());
	}
	
	private void fillGallery() {
		adapter = new RiQiAdapter(act);
		List<RiQiBean> data = new ArrayList<RiQiBean>();
		
		int days = extraChangGuanBean.tqydrq;
		if (days < 1) {
			days = 7;
		}
		
		for (int i = 0; i < days; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, i);
			data.add(getRiqiBean(c));
		}
		
		adapter.setData(data);
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(this);
//		gallery.setCallbackDuringFling(false);
		gallery.setOnScrolllStopListener(this);
		//默认第一个位置
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				gallery.getSelectedView().setSelected(true);
			}
		}, 1500);
	}
	
	private RiQiBean getRiqiBean(Calendar c) {
		RiQiBean info = new RiQiBean();
		info.dqxq = getWeekDay(c);
		info.dqrq = getDate(c); 
		return info;
	}
	
	private String getDate(Calendar c) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		date.setTime(c.getTimeInMillis());
		return fmt.format(date);
	}
	
	private String getWeekDay(Calendar c) {
		int i = c.get(Calendar.DAY_OF_WEEK);
		String day = "";
		switch(i) {
		case Calendar.MONDAY:
			day = "星期一";
			break;
		case Calendar.TUESDAY:
			day = "星期二";
			break;
		case Calendar.WEDNESDAY:
			day = "星期三";
			break;
		case Calendar.THURSDAY:
			day = "星期四";
			break;
		case Calendar.FRIDAY:
			day = "星期五";
			break;
		case Calendar.SATURDAY:
			day = "星期六"	;
			break;
		case Calendar.SUNDAY:
			day = "星期日";
			break;
		default:
				day = "";
				break;
		}
		return day;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		System.out.println("没有选中-----------");
	}

	@Override
	public void onScrollStop(MyGallery gallery) {
		System.out.println("gallery 停止滑动----------");
		int newPos = gallery.getSelectedItemPosition();
		if (newPos == gallerySelectionPos) {
			System.out.println("位置没变-----------");
			return ;
		}
		
		RiQiBean rq = (RiQiBean) gallery.getSelectedItem();
		loadData(rq);
	}

	@Override
	public void doRefresh() {
		RiQiBean rq = (RiQiBean) gallery.getSelectedItem();
		loadData(rq);
	}

	@Override
	public void onItemStateChanged(BoxView bv, Map<String, DingDanXiangBean> ddx) {
		System.out.println("onItemStateChanged订单项=" + ddx);
		double money = 0;
		mCurrentDingDanXiang = ddx;
		RiQiBean riqi = (RiQiBean) gallery.getSelectedItem();
		for (DingDanXiangBean b : ddx.values()) {
			money += b.ydjg;
//			if (!b.ydsjd.startsWith("2013")) {
//				b.ydsjd = riqi.dqrq + " " + b.ydsjd;
//			}
		}
		System.out.println("当前价格=" + money);
		showMoney(money);
	}
	
	private void showMoney(double d) {
		tvMoney.setText("总价：" + d + "元");
		mCurrentMoney = d;
	}

	@Override
	public void onRegisterSuccess(YongHuBean user) {
		reload();
	}

	@Override
	public void onLoginOrRegisterSuccess(YongHuBean user) {
		reload();
	}
	
	private void reload() {
		System.out.println("刷新价格数据-----------");
		RiQiBean rq = (RiQiBean) gallery.getSelectedItem();
		selectedItemPosition = bx.getSelectedItemPosition();
		loadData(rq);
	}

	@Override
	public boolean onReceiverMessage(MyMessage mm) {
		if (mm.type == MessageType.DINGDANGXIANG_CHANGED) {
			DingDanXiangBean[] ddx = (DingDanXiangBean[]) mm.obj1;
			bx.deselectItem(ddx);
			return true;
		}
		return false;
	}

	@Override
	public void onLoginout() {
		reload();
	}
	
}
