package cn.leyundong.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TimeTextView extends Spinner {
	private Map<Double, String> timeMap = new HashMap<Double, String>();
	

	
	private int hour;
	private int minute;
	
	
	public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TimeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimeTextView(Context context) {
		super(context);
		init();
	}

	private void setTime(int h, int m) {
		StringBuilder sb = new StringBuilder();
		sb.append(h).append(":").append(m);
		String s = sb.toString();
		this.hour = h;
		this.minute = m;
	}
	
	private String getValue(double d) {
		StringBuilder sb = new StringBuilder();
		int i = (int) d;
		if (i <= 9) {
			sb.append("0");
		}
		sb.append(i);
		int a = (int) ((d - i) * 60);
		sb.append(":");
		if (a == 0) {
			sb.append("00");
		} else {
			sb.append(a);
		}
		return sb.toString();
	}
	
	private void init() {
		double key = 0;
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 48; i++) {
			String value = getValue(key);
			timeMap.put(key, value);
			data.add(value);
			System.out.println("key=" + key + ",value=" + value);
			key += 0.5;
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getContext(), 
						android.R.layout.simple_spinner_item, 
						data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		setAdapter(adapter);
		
	}
	
	private double getKey(String value) {
		Set<Entry<Double, String>> es = timeMap.entrySet();
		for (Entry<Double, String> e : es) {
			if (e.getValue().equals(value)) {
				return e.getKey();
			}
		}
		return 0;
	}
	
	public double getTime() {
		String value = (String) getSelectedItem();
		return getKey(value);
	}
	
	public int compareTo(TimeTextView other) {
		return hour * 60 + minute - other.hour * 60 - other.minute;
	}
	
}
