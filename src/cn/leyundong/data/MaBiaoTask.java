package cn.leyundong.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import cn.leyundong.constant.Constants;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.MaBiaoBean;
import cn.leyundong.entity.Result;
import cn.leyundong.json.JsonParser;
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MessageObserver;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;
import cn.leyundong.receiver.NetReceiver;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;
import cn.quickdevelp.util.ThreadManager;

/**
 * 码表数据管理
 * @author Administrator
 *
 */
public class MaBiaoTask implements MessageObserver {
	
	public interface IMaBiaoChangeDiListener {
		void notifySuccess(MaBiaoTask mbt);
	}
	
	private static final String TAG = MaBiaoTask.class.getSimpleName() + ":";
	
	private static final int SUCCESS = 0;
	private static final int FAIL = -1;
	
	/**
	 * 数据是否存在
	 */
	public static boolean isMaBiaoDataAvailable;
	
	public String aboutMsg;
	
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			System.out.println(TAG + "请求数据结果=" + msg.what);
			if (msg.what == SUCCESS) {
				isDataSuccess = true;
				for (IMaBiaoChangeDiListener l : changeDiListener) {
					l.notifySuccess(MaBiaoTask.this);
				}
			}
			isGetingData = false;
			connectCounter++;
			if (msg.what == FAIL && connectCounter < 10) {
				//重新请求数据
				requestData();
			}
		};
	};

	private static MaBiaoTask instance;
	private Context ctx;
	
	/**
	 * 网络请求次数
	 */
	private int connectCounter;
	
	/**
	 * 原始json数据
	 */
	private String mJsonData;
	
	/**
	 * 场地类型
	 */
	private List<MaBiaoBean> mChangDiList = new ArrayList<MaBiaoBean>();
	
	private List<String> mChangLiNameList = new ArrayList<String>();
	

	/**
	 * 监听数据变化
	 */
	private List<IMaBiaoChangeDiListener> changeDiListener = new CopyOnWriteArrayList<IMaBiaoChangeDiListener>();
	
	/**
	 * 是否成功请求了数据
	 */
	private boolean isDataSuccess;
	
	/**
	 * 是否正在请求数据
	 */
	private boolean isGetingData;
	
	private MaBiaoTask(Context c) {
		this.ctx = c.getApplicationContext();
		//该方法只会线程启动调用一次 不需要去监听
		MessageManager.getInstance().addObserver(this);
		
		SharedPreferences sp = ctx.getSharedPreferences(Constants.XML_FILE, 0);
		mJsonData = sp.getString(Constants.XML_KEY_MABIAO, null);
		System.out.println("mJsonData=" + mJsonData);
		if (mJsonData != null) {
			parseJson(mJsonData);
		}
		requestData();
	}
	
	private boolean parseJson(String s) {
		System.out.println("码表数据-" + s);
		try {
			JSONObject jo = new JSONObject(s);
			if (!jo.isNull("InitBean")) {
				JSONObject initBean = jo.getJSONObject("InitBean");
				if (!initBean.isNull("gywmNr")) {
					aboutMsg = initBean.getString("gywmNr");
				}
				JSONArray cdAry = initBean.getJSONArray("cdlxmbList");
				MaBiaoBean[] mb = JsonUtil.parseJsonArray(cdAry.toString(), MaBiaoBean.class);
				if (mb != null) {
					mChangDiList.clear();
					mChangDiList.addAll(Arrays.asList(mb));
					
					for (MaBiaoBean b : mChangDiList) {
						mChangLiNameList.add(b.getValue());
					}
					
					isMaBiaoDataAvailable = true;
					
					return true;
				}
				System.out.println("=========================");
				System.out.println(mChangDiList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void registChangDiObserver(IMaBiaoChangeDiListener l) {
		if (!changeDiListener.contains(l)) {
			changeDiListener.add(l);
		}
	}
	
	public void unregisterChangDiObserver(IMaBiaoChangeDiListener l) {
		changeDiListener.remove(l);
	}
	
	private void requestData() {
		System.out.println(TAG + "请求码表数据------------" + NetReceiver.isNetworkAvailable);
		if (!NetReceiver.isNetworkAvailable) {
			return ;
		}
		isGetingData = true;
		ThreadManager.getInstance().execute(new Runnable() {
			
			@Override
			public void run() {
				String url = Urls.getUrl(Urls.TYPE_GONGGONG,  RequestId.MABIAO);
				List<IJson> entitys = new ArrayList<IJson>();
				IHttpDuty duty = new HttpPostDuty(ctx, url, entitys);
				String mabiao = duty.post();
				System.out.println("mabiao=" + mabiao);
				boolean isSueccess = false;
				if (mabiao != null) {
					Result result = JsonParser.getResultFromJson(mabiao);
					if (result.success) {
						SharedPreferences sp = ctx.getSharedPreferences(Constants.XML_FILE, 0);
						Editor edit = sp.edit();
						edit.putString(Constants.XML_KEY_MABIAO, mabiao).commit();
						isSueccess = parseJson(mabiao);
					}
				}
				if (isSueccess) {
					handler.obtainMessage(SUCCESS).sendToTarget();
				} else {
					handler.obtainMessage(FAIL).sendToTarget();
				}
			}
		});
	}
	
	public static MaBiaoTask getInstance(Context ctx) {
		if (instance == null) {
			instance = new MaBiaoTask(ctx);
		}
		return instance;
	}
	
	public List<String> getChangDi() {
		return mChangLiNameList;
	}
	
	public BaseAdapter getChangDiLeiXingAdapter(Context ctx) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, android.R.id.text1, mChangLiNameList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
	
	public String getChangDiKey(String value) {
		for (MaBiaoBean b : mChangDiList) {
			if (b.getValue().equals(value)) {
				return b.getKey();
			}
		}
		return "";
	}
	public String getChangDiNameByKey(String key) {
		for (MaBiaoBean b : mChangDiList) {
			if (b.getKey().equals(key)) {
				return b.getValue();
			}
		}
		return "";
	}
	
	public String getJsonData() {
		return mJsonData;
	}

	@Override
	public boolean onReceiverMessage(MyMessage mm) {
		System.out.println(TAG + "接收消息=	 " + mm);
		if (mm.type == MessageType.NETWORK_AVAILABLE) {
			System.out.println(TAG + "网络可用=" + mm.obj1);
			System.out.println(TAG + "是否请求成功=" + isDataSuccess + ",是否正在请求=" + isGetingData);
			if (isDataSuccess || isGetingData) {
				return true;
			}
			//请求数据
			requestData();
			return true;
		}
		return false;
	}
	
}
