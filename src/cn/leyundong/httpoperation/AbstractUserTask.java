package cn.leyundong.httpoperation;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.leyundong.MyApplication;
import cn.leyundong.constant.Constants;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.json.JsonParser;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.http.NetCheckHttpDuty;
import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;

public abstract class AbstractUserTask {
	protected String TAG;
	
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			System.out.println(TAG + "handlermessage " + msg.obj);
			notifySuccess((YongHuBean) msg.obj);
		};
	};
	
	
	protected Context ctx;
	protected YongHuBean user;
	
	protected AbstractUserTask(Context ctx) {
		this.ctx = ctx.getApplicationContext();
		TAG = getClass().getName();
	}
	
	public void setUser(YongHuBean b) {
		this.user = b;
	}
	
	protected Object doPost(String url) {
		ArrayList<IJson> list = new ArrayList<IJson>();
		list.add(new BeanProxy<YongHuBean>(user));
		IHttpDuty duty = new NetCheckHttpDuty(ctx, new HttpPostDuty(ctx, url, list));
		String post = duty.post();
		System.out.println(TAG + post);
		Result result = JsonParser.getResultFromJson(post);
		if (result.success) {
			
			//成功
			try {
				JSONObject obj = new JSONObject(post);
				String key = YongHuBean.class.getSimpleName();
				if (!obj.isNull(key)) {
					String value = obj.getString(key);
					saveXml(value);
					
					YongHuBean bean = JsonUtil.parseJson(value, YongHuBean.class);
					this.user = bean;
					Message msg = handler.obtainMessage();
					msg.obj = bean;
					msg.sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return post;
	}
	
	
	private void saveXml(String json) {
		MyApplication.sp.edit().putString(Constants.XML_KEY_USER, json).commit();
	}

	protected abstract void notifySuccess(YongHuBean bean);
}
