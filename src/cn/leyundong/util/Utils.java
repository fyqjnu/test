package cn.leyundong.util;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.BaseAdapter;

import cn.leyundong.MyApplication;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.Page;
import cn.leyundong.entity.Result;
import cn.leyundong.json.JsonParser;
import cn.quickdevelp.json.JsonUtil;

public class Utils {

	/**
	 * 解析page 下的list数组
	 * @param json
	 * @param clz
	 * @return
	 */
	public static <T> T[] parsePageJson(String json, Class<? extends T> clz) {
		if (json == null) {
			return null;
		}
		Result result = JsonParser.getResultFromJson(json);
		if (result.success) {
			try {
				JSONObject obj = new JSONObject(json);
				if (!obj.isNull("page")) {
					JSONObject page = obj.getJSONObject("page");
					if (!page.isNull("list")) {
						JSONArray list = page.getJSONArray("list");
						T[] infos = JsonUtil.parseJsonArray(list.toString(), clz);
						return infos;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static <T> Result getPageFromJson(String json, Class<? extends T> clz) {
		Result result = new Result();
		if (json == null) {
			return result;
		}
		try {
			JSONObject obj = new JSONObject(json);
			String key = "success";
			if (!obj.isNull(key)) {
				result.success = obj.getBoolean(key);
			}
			if (result.success) {
				//解析page
				Page<T> page = new Page<T>();
				result.obj = page;
				key = "page";
				if (!obj.isNull(key)) {
					JSONObject joPage = obj.getJSONObject(key);
					key = "list";
					if (!joPage.isNull(key)) {
						JSONArray list = joPage.getJSONArray(key);
						T[] ary = JsonUtil.parseJsonArray(list.toString(), clz);
						page.list = Arrays.asList(ary);
					}
					key = "hasNextPage";
					if (!joPage.isNull(key)) {
						page.hasNextPage = joPage.getBoolean(key);
					}
				}
			} else {
				key = "error";
				if (!obj.isNull(key)) {
					result.error = obj.getString(key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static ChaXunBean createChaXunBean(BaseAdapter adapter) {
		ChaXunBean b = new ChaXunBean();
		b.yhid = MyApplication.mUser.yhid;
		b.csdm = MyApplication.mCity.getCsdm();
		b.qxdm = MyApplication.quxiandama;
		b.ydrq = MyApplication.riqi;
		if (adapter != null) {
			//已经显示的数据个数
			b.pageStartRow = adapter.getCount();
		}
		return b;
	}
	
}
