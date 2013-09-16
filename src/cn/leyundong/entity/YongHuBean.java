package cn.leyundong.entity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.leyundong.interfaces.Nullable;
import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;

/**
 * 用户信息
 * @author MSSOFT
 *
 */
public class YongHuBean implements Nullable {
	public String yhid;//用户id
	public String yhm;//用户名
	public String yhmm;//用户密码
	public String sjhm = "18688888888";//手机号码
	public String dxyzm;//短信验证码
	public String yx;//邮箱
	public String csdm;//城市代码
	public int fslx = 1;//发送类型：1注册、2找回密码
	
	public String jmm;//旧密码
	
	@Override
	public boolean isNull() {
		return false;
	}
	
	public static YongHuBean newNull() {
		return new NullYongHuBean();
	}

	@Override
	public String toString() {
		return "YongHuBean [yhid=" + yhid + ", yhm=" + yhm + ", yhmm=" + yhmm
				+ ", sjhm=" + sjhm + ", dxyzm=" + dxyzm + ", yx=" + yx
				+ ", csdm=" + csdm + ", fslx=" + fslx + "]";
	}
	
	
	
}
