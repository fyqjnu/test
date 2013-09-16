package cn.leyundong.httpoperation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.os.Handler;
import cn.leyundong.MyApplication;
import cn.leyundong.constant.Constants;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.interfaces.onLoginSuccess;
import cn.quickdevelp.interfaces.IDataRequest;

public class LoginTask extends AbstractUserTask implements IDataRequest {
	
	private static LoginTask instance;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			notifySuccess((YongHuBean) msg.obj);
		};
	};
	
	private static List<onLoginSuccess> listeners = new CopyOnWriteArrayList<onLoginSuccess>();
	
	private boolean autoLoginNextTime;
	
	private LoginTask(Context ctx) {
		super(ctx);
	}
	
	public void setAutoLoginNextTime(boolean b) {
		this.autoLoginNextTime = b;
	}
	
	public static LoginTask getInstance(Context ctx) {
		if (instance == null) {
			instance = new LoginTask(ctx);
		}
		return instance;
	}
	
	@Override
	public Object doRequest() {
		String url = Urls.getUrl(Urls.TYPE_GERENZHONGXIN, RequestId.LOGIN);
		return doPost(url);
	}
	
	protected void notifySuccess(YongHuBean user) {
		System.out.println("登录成功------------" + user);
		MyApplication.mUser = user;
		System.out.println("登录成功------------" + MyApplication.mUser);
		
		MyApplication.sp.edit().putBoolean(Constants.XML_KEY_AUTOLOGIN, autoLoginNextTime).commit();
		
		for (onLoginSuccess l : listeners) {
			l.onLoginOrRegisterSuccess(user);
		}
	}
	
	public void notifyLoginout() {
		System.out.println("登录退出------------");
		
		MyApplication.mUser = YongHuBean.newNull();
		MyApplication.sp.edit().putBoolean(Constants.XML_KEY_AUTOLOGIN, false).commit();
		for (onLoginSuccess l : listeners) {
			l.onLoginout();
		}
	}
	
	public static void registerLoginSuccessListener(onLoginSuccess l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
		System.out.println(listeners);
	}
	
	public static void unregisterLoginSuccessListener(onLoginSuccess l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}
	
}