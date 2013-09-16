package cn.leyundong.httpoperation;

import java.util.ArrayList;

import android.app.Activity;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.YongHuBean;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.http.NetCheckHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

/**
 * 获取验证码网络操作
 * @author MSSOFT
 *
 */
public class YanZhengMaTask implements IDataRequest {
	YongHuBean user;
	Activity act;
	
	public YanZhengMaTask(Activity act, YongHuBean b) {
		this.user = b;
		this.act = act;
	}

	@Override
	public Object doRequest() {
		String url = Urls.getUrl(1, 1);
		System.out.println("url=" + url);
		ArrayList<IJson> d = new ArrayList<IJson>();
		d.add(new BeanProxy<YongHuBean>(user));
		IHttpDuty duty = new NetCheckHttpDuty(act, new HttpPostDuty(act, url, d));
		return duty.post();
	}
	
}