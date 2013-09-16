package cn.leyundong.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.MaBiaoBean;
import cn.leyundong.entity.Result;
import cn.leyundong.json.JsonParser;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

/**
 * 获取场地类型 未使用
 * @author MSSOFT
 *
 */
public class ChangDiLeiXing implements IDataRequest {
	
	List<MaBiaoBean> data = new ArrayList<MaBiaoBean>();
	
	private static ChangDiLeiXing instance;
	
	private Context ctx;
	
	private ChangDiLeiXing(Context ctx) {
		this.ctx = ctx.getApplicationContext();
	}
	
	public static ChangDiLeiXing getInstance(Context ctx) {
		if (instance == null) {
			instance = new ChangDiLeiXing(ctx);
		}
		return instance;
	}
	
	public List<MaBiaoBean> getMaBianBean() {
		return data;
	}
	
	@Override
	public Object doRequest() {
		String url = Urls.getUrl(Urls.TYPE_GONGGONG, 4);
		ArrayList<IJson> list = new ArrayList<IJson>();
		IHttpDuty duty = new HttpPostDuty(ctx, url, list);
		String post = duty.post();
		if (post != null) {
			Result result = JsonParser.getResultFromJson(post);
			if (result.success) {
				Map<String, Class> params = new HashMap<String, Class>();
				params.put("cdlxmbList", MaBiaoBean.class);
				Map<String, Object> map = JsonParser.getObject(post, params);
				MaBiaoBean[] beans = (MaBiaoBean[]) map.get("cdlxmbList");
				data = Arrays.asList(beans);
				
			}
		}
		System.out.println("post=" + post);
		return post;
	}
	
	public String getKey(String value) {
		for (MaBiaoBean b : data) {
			if (b.getValue().equals(value)) {
				return b.getKey();
			}
		}
		return "";
	}

}
