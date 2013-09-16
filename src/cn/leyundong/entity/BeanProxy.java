package cn.leyundong.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;

/**
 * 实体对象代理
 * @author Administrator
 *
 */
public class BeanProxy<T> implements IJson, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private T t;
	
	public BeanProxy(T t) {
		this.t = t;
	}
	
	public T getBean() {
		return t;
	}
	
	@Override
	public Object build() {
		return JsonUtil.buildJson(t);
	}

	@Override
	public void parseJson(String json) {
		JsonUtil.parseJson(json, t.getClass());
	}

	@Override
	public String getKey() {
		return t.getClass().getSimpleName();
	}

}
