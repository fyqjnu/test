package cn.leyundong.httpoperation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.http.NetCheckHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

public class MultiParamTask implements IDataRequest  {

	private Context ctx;
	private List<IJson> params = new ArrayList<IJson>();
	private String url;
	
	public MultiParamTask(Context ctx, String url, IJson[] params) {
		this.ctx = ctx;
		this.url = url;
		for (IJson p : params) {
			this.params.add(p);
		}
	}
	
	@Override
	public Object doRequest() {
		System.out.println("多参数请求----------");
		IHttpDuty duty = new NetCheckHttpDuty(ctx, new HttpPostDuty(ctx, url, params));
		return duty.post();
	}

}
