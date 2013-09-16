package cn.leyundong.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.leyundong.entity.Result;
import cn.quickdevelp.json.JsonUtil;

public class JsonParser {
	public static Result getResultFromJson(String json) {
		Result result = new Result();
		if (json != null) {
			try {
				JSONObject obj = new JSONObject(json);
				result.success = obj.isNull("success") ? false : obj.getBoolean("success");
				result.error = obj.isNull("error") ? "" : obj.getString("error");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static Map<String, Object> getObject(String json, Map<String , Class> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject obj = new JSONObject(json);
			Set<Entry<String, Class>> entrySet = param.entrySet();
			
			for (Entry<String, Class> e : entrySet) {
				String key = e.getKey();
				Class clz = e.getValue();
				if (!obj.isNull(key)) {
					Object ary = JsonUtil.parseJsonArray(obj.getString(key), clz);
					result.put(key, ary);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param <T>
	 * @param d
	 * @param clz
	 * @return
	 */
	public static <T> T parseJson(String d, Class<? extends T> clz) {
		try {
			JSONObject json = new JSONObject(d);
			T obj = (T) clz.newInstance();
			Field[] fs = clz.getDeclaredFields();
			if (fs != null) {
				for (Field f : fs) {
					f.setAccessible(true);
					String name = f.getName();
					if (!json.isNull(name)) {
						Class<?> type = f.getType();
						if (type.equals(int.class)) {
							f.setInt(obj, json.getInt(name));
						} else if (type.equals(long.class)) {
							f.setLong(obj, json.getLong(name));
						} else if (type.equals(float.class)) {
							f.setFloat(obj, (float) json.getDouble(name));
						} else if (type.equals(String.class)) {
							f.set(obj, json.getString(name));
						} else if (type.equals(double.class)) {
							f.setDouble(obj, json.getDouble(name));
						} else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
							f.setBoolean(obj, json.getBoolean(name));
						}
					}
				} 
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject buildJson(Object obj) {
		try {
			Class<? extends Object> clz = obj.getClass();
			Field[] fs = clz.getDeclaredFields();
			JSONObject jo = new JSONObject();
			if (fs != null) {
				for (Field f : fs) {
					int m = f.getModifiers();
					if (Modifier.isStatic(m)) {
						continue;
					}
					f.setAccessible(true);
					String name = f.getName();
					Class<?> type = f.getType();
					if (type.equals(int.class)) {
						jo.put(name, f.getInt(obj));
					} else if (type.equals(long.class)) {
						jo.put(name, f.getLong(obj));
					} else if (type.equals(float.class)) {
						jo.put(name, f.getFloat(obj));
					} else if (type.equals(String.class)) {
						jo.put(name, (String)f.get(obj));
					} else if (type.equals(double.class)) {
						jo.put(name, f.getDouble(obj));
					}
				}
			}
			return jo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, Object> parseJsonMap(String json, Map<String, Class<?>> clzMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jo = new JSONObject(json);
			Set<Entry<String, Class<?>>> entrySet = clzMap.entrySet();
			for (Entry<String, Class<?>> e : entrySet) {
				String key = e.getKey();
				Class<?> clz = e.getValue();
				if (jo.isNull(key)) {
					continue ;
				}
				Object t = null;
				if (clz.isArray()) {
					JSONArray ja = jo.getJSONArray(key);
					t = parseJsonArray(ja.toString(), getClassFromArray(clz));
				} else {
					JSONObject obj = jo.getJSONObject(key);
					t = parseJson(obj.toString(), clz);
				}
				map.put(key, t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static <T> T[] parseJsonArray(String d, Class<? extends T> c) {
		try {
			JSONArray ary = new JSONArray(d);
			int len = ary.length();
			T[] result = (T[]) Array.newInstance(c, len);
			for (int i = 0; i < len; i++) {
				JSONObject jo = ary.getJSONObject(i);
				result[i] = parseJson(jo.toString(), c);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Class<?> getClassFromArray(Class<?> c) {
		Class<?> ret = null;
		if (c.isArray()) {
			String name = c.getName();
			int start = 0;
			int end = name.length();
			if (name.startsWith("[L")) {
				start = 2;
			} else if (name.startsWith("[")) {
				start = 1;
			}
			if (name.endsWith(";")) {
				end = name.length() - 1;
			}
			String clzName = name.substring(start, end);
			try {
				ret = Class.forName(clzName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			ret = c;
		}
		
		return ret;
	}
	
	public static JSONArray buildJsonArray(Object[] objs) {
		try {
			int len = objs.length;
			JSONArray ary = new JSONArray();
			for (int i = 0; i < len; i++) {
				JSONObject s = buildJson(objs[i]);
				if (s != null) {
					ary.put(i, s);
				}
			}
			return ary;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
