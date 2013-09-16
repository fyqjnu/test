package cn.leyundong.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;

public class BeanListProxy<T> implements IJson, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<T> data;
	
	public BeanListProxy(List<T> d) {
		this.data = d;
	}

	@Override
	public String getKey() {
		if (data.size() > 0) {
			return data.get(0).getClass().getSimpleName(); 
		}
		return "";
	}

	@Override
	public void parseJson(String arg0) {
	}

	@Override
	public Object build() {
		return JsonUtil.buildJsonArray(data.toArray());
	}

}
