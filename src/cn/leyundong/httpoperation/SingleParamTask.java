package cn.leyundong.httpoperation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.leyundong.entity.BeanProxy;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.http.NetCheckHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

/**
 * 只有一个实体参数的查询 返回原始string数据
 * @author Administrator
 *
 * @param <T>
 */
public class SingleParamTask<T> implements IDataRequest {

	private Context ctx;
	private String url;
	private List<IJson> param;
	
	public SingleParamTask(Context ctx, String url, T t) {
		this.ctx = ctx;
		this.url = url;
		param = new ArrayList<IJson>();
		BeanProxy<T> p = new BeanProxy<T>(t);
		param.add(p);
	}
	
	@Override
	public Object doRequest() {
		IHttpDuty duty = new NetCheckHttpDuty(ctx, new HttpPostDuty(ctx, url, param));
		return duty.post();
	}

}
