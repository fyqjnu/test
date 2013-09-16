package cn.leyundong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.bridge.ICommunicationBridge;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.custom.DateTextView.onDateSet;
import cn.leyundong.custom.TabTitle;
import cn.leyundong.custom.TabTitle.OnTabSelectedListener;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.data.MaBiaoTask.IMaBiaoChangeDiListener;
import cn.leyundong.data.QuXianData;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.City;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.LoginTask;
import cn.leyundong.httpoperation.RegisterTask;
import cn.leyundong.interfaces.onLoginSuccess;
import cn.leyundong.interfaces.onRegisterSuccess;
import cn.leyundong.message.IMessageCallback;
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

public class FirstActivity extends BaseActivity implements onLoginSuccess, onRegisterSuccess,
								IMaBiaoChangeDiListener, OnTabSelectedListener, 
								ICommunicationBridge<ChaXunBean>, onDateSet {
	
	private String[] ts = {"我要订场", "我要报名"};
	
	public static FirstActivity instance;
	
	protected static final int REQUSTCODE_CITY = 0;
	
	private static final int TASK_ID_MA_BIAO = 1;
	private static final int TASK_ID_DINGCHAGE = 2;
	private static final int TASK_ID_BAOMING = 3;
	
	private int COUNTER_FOR_TASK_MA_BIAO = 3;
	
	private MaBiaoTask mMaBiaoTask;
	
	private int mCurrentPage;
	
	public ChaXunBean mChaXunBean;
	
	private boolean bridgeDataAvaliable;
	
	//类型
	@ViewRef(id=R.id.spinnerType) Spinner spinnerType;
	//区域
	@ViewRef(id=R.id.spinnerArea) Spinner spinnerArea;
	
	
	@ViewRef(id=R.id.header) Header header;
	@ViewRef(id=R.id.btnDingChange, onClick="onClick") Button btnDingChange;
	@ViewRef(id=R.id.btnBaoMing, onClick="onClick") Button btnBaoMing;
	@ViewRef(id=R.id.tt) TabTitle tt;
	
	
	
	private TextView tvLogin;
	private TextView tvCity;
	
	@ViewRef(id=R.id.tvDate) DateTextView tvDate;
	
	//场馆名称
	@ViewRef(id=R.id.etName) EditText etName;
	
	@ViewRef(id=R.id.tvName) TextView tvName;
	
	@ViewRef(id=R.id.btnOk, onClick="onClickQuery") Button btnQuery;
	
	private void onClick(View v) {
		btnDingChange.setSelected(false);
		btnBaoMing.setSelected(false);
		
		switch(v.getId()) {
		case R.id.btnDingChange:
			btnDingChange.setSelected(true);
			tvName.setText("场馆名称：");
			mCurrentPage = 0;
			break;
		case R.id.btnBaoMing:
			mCurrentPage = 1;
			tvName.setText("俱乐部名称：");
			btnBaoMing.setSelected(true);
			break;
		}
	}
	
	private void onClickQuery(View v) {
		bridgeDataAvaliable = true;
		mChaXunBean = getChaXunBean();
		
		MessageManager mm = MessageManager.getInstance();
		MyMessage msg = mm.obtainMyMessage();
		msg.type = MessageType.PAGE_TURN_TO;
		msg.needFeedbackResult = true;
		int selectedItemPosition = spinnerType.getSelectedItemPosition();
		//场地类型选择位置
		msg.arg2 = selectedItemPosition;
		msg.obj2 = etName.getText().toString();
		if (mCurrentPage == 0) {
			MainActivity.instance.setCurrentTab(1);
			msg.messageId = 1;
			msg.arg1 = 1;
			msg.callback = msgHandle;
			msg.obj1 = "场馆";
		} else if (mCurrentPage == 1) {
			MainActivity.instance.setCurrentTab(3);
			msg.arg1 = 3;
			msg.messageId = 2;
			msg.obj1 = "报名";
		}
		
		mm.sendMyMessage(msg);
	}
	
	private IMessageCallback msgHandle = new IMessageCallback() {
		
		@Override
		public void onMessageHandle(long messageId, Object result) {
			System.out.println("消息回调结果= id=" + messageId +", result=" + result);
		}
	};
	
	private ChaXunBean getChaXunBean() {
		ChaXunBean info = new ChaXunBean();
		//日期
		info.ydrq = "" + tvDate.getText();
		//场地
		info.cdlx =  MaBiaoTask.getInstance(act).getChangDiKey((String) spinnerType.getSelectedItem());
		//城市
		info.csdm = MyApplication.mCity.getCsdm();
		//区县
		info.qxdm = QuXianData.getInstance(act).queryQuXianDaMa((String) spinnerArea.getSelectedItem());
		//场馆名称
		info.cgmc = etName.getText().toString();
		//用户id
		info.yhid = MyApplication.mUser.isNull() ? "" : MyApplication.mUser.yhid;
		System.out.println("查询=" + info);
		
		return info;
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		switch (what) {
		case TASK_ID_DINGCHAGE:
			System.out.println("订场=" + result);
			break;
		case TASK_ID_BAOMING:
			System.out.println("报名=" + result);
			break;
		}
	}
	
	/**
	 * 设置区县
	 * @param shi
	 */
	private void setAreaData(String shi) {
		List<String> data = new ArrayList<String>();
		data.add("无限");
		List<String> data2 = QuXianData.getInstance(act).queryQuXianByShi(shi);
		data.addAll(data2);
		ArrayAdapter<String> mAdapterArea = new ArrayAdapter<String>(act, android.R.layout.simple_spinner_item, android.R.id.text1, data);
		mAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerArea.setAdapter(mAdapterArea);
	}
	
	@Override
	protected void onDestroy() {
		instance = null;
		mMaBiaoTask.unregisterChangDiObserver(this);
		LoginTask.unregisterLoginSuccessListener(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
		
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mChaXunBean = null;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		setContentView(R.layout.main);
		
		
		tt.setOnTabSelectedListener(this);
		tt.setTitles(ts, 0);
		
		tvDate.setOnDateSetListener(this);
		
		View v = getLayoutInflater().inflate(R.layout.first_header, null);
		header.removeTitle();
		header.addComponent(v, null);
		tvLogin = (TextView) v.findViewById(R.id.tvLogin);
		setupLoginButton();
		tvCity = (TextView) v.findViewById(R.id.tvCity);
		tvCity.setOnClickListener(clickListener);
		
		spinnerArea.setOnItemSelectedListener(itemSelectedListener);
		
		setCity(MyApplication.mCity);
		mMaBiaoTask = MaBiaoTask.getInstance(act);
		List<String> changDi = mMaBiaoTask.getChangDi();
		if (changDi.size() > 0) {
			spinnerType.setAdapter(mMaBiaoTask.getChangDiLeiXingAdapter(act));
		} else {
			mMaBiaoTask.registChangDiObserver(this);
		}
		
		btnDingChange.setSelected(true);
	}
	
	private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			System.out.println("地区选择--" + position);
			//区县
			MyApplication.quxiandama = QuXianData.getInstance(act).queryQuXianDaMa((String) spinnerArea.getSelectedItem());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	private void setupLoginButton() {
		System.out.println(TAG + "setupLoginButton=" + MyApplication.mUser);
		if (MyApplication.mUser.isNull()) {
			tvLogin.setText("登录");
			tvLogin.setOnClickListener(clickListener);
		} else {
			//显示用户名
			tvLogin.setText(MyApplication.mUser.yhm);
		}
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			switch(v.getId()) {
			case R.id.tvLogin:
				if (MyApplication.mUser.isNull()) {
					i.setClass(act, LoginActivity.class);
					startActivity(i);
				}
				break;
			case R.id.tvCity:
				i.setClass(act, CitysActivity.class);
				startActivityForResult(i, REQUSTCODE_CITY);
				break;
			}
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case REQUSTCODE_CITY:
			if (resultCode == RESULT_OK) {
				City city = (City) data.getSerializableExtra(City.class.getName());
				System.out.println("city=" + city);
				MyApplication.mCity = city;
				setCity(city);
			}
			break;
		}
	};
	
	private void setCity(City city) {
		setCity(city.getCsmc());
	}
	
	private void setCity(String s) {
		String city = s;
		if (!city.endsWith("市")) {
			city = city + "市";
		}
		tvCity.setText(city + "[切换城市]");
		
		setAreaData(city);
	}

	@Override
	public void onRegisterSuccess(YongHuBean user) {
		setupLoginButton();
	}

	@Override
	public void onLoginOrRegisterSuccess(YongHuBean user) {
		setupLoginButton();
	}

	@Override
	public void notifySuccess(MaBiaoTask mbt) {
		System.out.println(TAG + " 被通知码表数据");
		spinnerType.setAdapter(mMaBiaoTask.getChangDiLeiXingAdapter(act));
	}

	@Override
	public void onTabSelected(int which, String title) {
		
		switch(which) {
		case 0:
			tvName.setText("场馆名称：");
			mCurrentPage = 0;
			break;
		case 1:
			tvName.setText("俱乐部名称：");
			mCurrentPage = 1;
			break;
		}
	}

	@Override
	public ChaXunBean getValue() {
		if (bridgeDataAvaliable) {
			bridgeDataAvaliable = false;
			return getChaXunBean();
		}
		return null;
	}

	@Override
	public void onLoginout() {
		setupLoginButton();
	}

	@Override
	public void onDateChanged(String riqi) {
		MyApplication.riqi = riqi;
	}

}
