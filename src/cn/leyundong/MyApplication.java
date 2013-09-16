package cn.leyundong;

import java.util.ArrayList;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import cn.leyundong.constant.Constants;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.entity.City;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.message.MessageManager;
import cn.leyundong.receiver.NetReceiver;
import cn.leyundong.test.QuXianTable;
import cn.quickdevelp.db.DatabaseHelper;
import cn.quickdevelp.db.ITable;

public class MyApplication extends Application {
	
	public static MyApplication instance;
	
	/**
	 * 当前用户所在城市，默认为北京
	 */
	public static City mCity;
	/**
	 * 区县代码
	 */
	public static String quxiandama;
	
	/**
	 * 日期
	 */
	public static String riqi;
	
	private SQLiteDatabase db;
	
	public static YongHuBean mUser;
	public static YongHuBean tmpUser;
	
	public static SharedPreferences sp; 
	
	public Handler handler;

	public MaBiaoTask mmTask;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		instance = this;
		
		sp = getSharedPreferences(Constants.XML_FILE, 0);
		
		ArrayList<ITable> tables = new ArrayList<ITable>();
		tables.add(new QuXianTable());
		DatabaseHelper helper = new DatabaseHelper(this, "leyundong.db", null, 1, tables);
		db = helper.getWritableDatabase();
		
//		Data.putDataToDb(this, db);
		
		//程序启动默认一个地区
		mCity = new City();
		String name = "广州市";
		mCity.setCsmc(name);
		mCity.setCsdm("440100");
		
		/*String json = sp.getString(Constants.XML_KEY_USER, null);
		System.out.println("填充用户信息=" + json);
		if (json != null) {
			try {
				JSONObject obj = new JSONObject(json);
				if (obj.isNull(YongHuBean.class.getSimpleName())) {
					tmpUser = JsonUtil.parseJson(json, YongHuBean.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		boolean autoLogin = sp.getBoolean(Constants.XML_KEY_AUTOLOGIN, false);
		if (autoLogin) {
			mUser = tmpUser;
		}
		
		System.out.println("mUser=" + mUser); 
		if (mUser == null) {
			//默认没有用户
			mUser = YongHuBean.newNull();
		}
		if (tmpUser == null) {
			tmpUser = YongHuBean.newNull();
		}
		*/
		
		handler = new Handler();
		
		NetReceiver.init(this);
		mmTask = MaBiaoTask.getInstance(this);
		
		MessageManager.getInstance();
		
	}
	

}
