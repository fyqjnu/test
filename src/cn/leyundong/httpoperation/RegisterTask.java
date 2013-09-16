package cn.leyundong.httpoperation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import cn.leyundong.MyApplication;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.interfaces.onRegisterSuccess;
import cn.quickdevelp.interfaces.IDataRequest;

public class RegisterTask extends AbstractUserTask  implements IDataRequest {
	
	private static List<onRegisterSuccess> listeners = new CopyOnWriteArrayList<onRegisterSuccess>();
	
	private static RegisterTask instance;
	
	private RegisterTask(Context ctx) {
		super(ctx);
	}
	
	public static RegisterTask getInstance(Context ctx) {
		if (instance == null) {
			instance = new RegisterTask(ctx);
		}
		return instance;
	}
	
	@Override
	public Object doRequest() {
		String url = Urls.getUrl(Urls.TYPE_GERENZHONGXIN, RequestId.REGISTER);
		return doPost(url);
	}
	
	protected void notifySuccess(YongHuBean user) {
		System.out.println("注册成功-------------" + listeners.size());
		MyApplication.mUser = user;
		for (onRegisterSuccess l : listeners) {
			l.onRegisterSuccess(user);
		}
	}
	
	public static void registerRegisterSuccessListener(onRegisterSuccess l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public static void unregisterRegisterSuccessListener(onRegisterSuccess l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}
	
}