package cn.leyundong.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.constant.Constants;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.Priority;
import cn.quickdevelp.json.JsonUtil;

public class MainActivity extends TabActivity implements OnTabChangeListener {
	
	public static MainActivity instance;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		instance = this;
		
		initGlobalField();
		
		setContentView(R.layout.home);
		
		initTab();
		
		/******************************************************/
		
		/*PriorityQueue<MyMessage> q = new PriorityQueue<MyMessage>();
		MyMessage m1 = new MyMessage();
		m1.arg1 = 1;
		m1.priority = Priority.MIDDLE;
		q.add(m1);
		MyMessage m2 = new MyMessage();
		m2.priority = Priority.LOW;
		m2.arg1 = 2;
		q.add(m2);
		MyMessage m3 = new MyMessage();
		m3.arg1 = 3;
		m3.priority = Priority.HIGH;
		q.add(m3);
		MyMessage m4 = new MyMessage();
		m4.arg1 = 4;
		m4.priority = Priority.HIGH;
		q.add(m3);
		MyMessage m5 = new MyMessage();
		m5.arg1 = 5;
		m5.priority = Priority.HIGH;
		q.add(m5);
		MyMessage m6 = new MyMessage();
		m6.arg1 = 6;
		m6.priority = Priority.LOW;
		q.add(m6);
		
		MyMessage p1 = q.poll();
		MyMessage p2 = q.poll();
		MyMessage p3 = q.poll();
		MyMessage p4 = q.poll();
		MyMessage p5 = q.poll();
		MyMessage p6 = q.poll();
		
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		System.out.println(p5);
		System.out.println(p6);*/
		
		
	/*	String s = "hellofjjjjjjjjj中中要地";
		byte[] bytes = s.getBytes();
		System.out.println(bytes.length);
		String encode = URLEncoder.encode(s);
		System.out.println(encode.getBytes().length);
		String decode = URLDecoder.decode(encode);
		System.out.println("encode=" + encode);
		System.out.println("decode=" + decode);
		
		JSONObject obj = new JSONObject();
		YongHuBean t = new YongHuBean();
		t.fslx = 1;
		t.sjhm = "18675828227";
		BeanProxy<YongHuBean> p = new BeanProxy<YongHuBean>(t);
		try {
			obj.put(p.getKey(), p.build());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("typeId", obj.toString()));
		try {
			HttpEntity entity = new UrlEncodedFormEntity(param);
			String format = URLEncodedUtils.format(param, HTTP.DEFAULT_CONTENT_CHARSET);
			
			System.out.println("format=" + format);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/******************************************************/
	}
	
	/**
	 * 初始化全局属性
	 */
	private void initGlobalField() {
		String json = MyApplication.sp.getString(Constants.XML_KEY_USER, null);
		System.out.println("填充用户信息=" + json);
		if (json != null) {
			try {
				JSONObject obj = new JSONObject(json);
				if (obj.isNull(YongHuBean.class.getSimpleName())) {
					MyApplication.tmpUser = JsonUtil.parseJson(json, YongHuBean.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		boolean autoLogin = MyApplication.sp.getBoolean(Constants.XML_KEY_AUTOLOGIN, false);
		if (autoLogin) {
			MyApplication.mUser = MyApplication.tmpUser;
		}
		
		System.out.println("mUser=" + MyApplication.mUser); 
		if (MyApplication.mUser == null) {
			//默认没有用户
			MyApplication.mUser = YongHuBean.newNull();
		}
		if (MyApplication.tmpUser == null) {
			MyApplication.tmpUser = YongHuBean.newNull();
		}
	}

	@Override
	public void onTabChanged(String tabId) {
		
	}
	
	private void initTab() {
		/*TabHost tabHost = getTabHost();
		Class<? extends TabHost> clz = tabHost.getClass();
		try {
			Field field = clz.getDeclaredField("mCurrentTab");
			field.setAccessible(true);
			field.setInt(tabHost, 4);
		} catch (Exception e) {
			e.printStackTrace();
		} */
		
		setupIntent(FirstActivity.class, getString(R.string.first_tab), R.drawable.selector_first);
		setupIntent(YuDingActivity.class, getString(R.string.second_tab), R.drawable.selector_second);
		setupIntent(ClubActivity.class, getString(R.string.third_tab), R.drawable.selector_third);
		setupIntent(HuiYuanZhuanQuActivity.class, getString(R.string.four_tab), R.drawable.selector_four);
//		setupIntent(FiveActivity.class, getString(R.string.five_tab), R.drawable.selector_five);
//		setupIntent(TalkActivity.class, getString(R.string.five_tab), R.drawable.selector_five);
		setupIntent(MoreActivity.class, getString(R.string.five_tab), R.drawable.selector_five);
		
//		getTabWidget().getChildAt(2).getLayoutParams().width = DimensionUtil.dip2px(getApplicationContext(), 30);
		
		getTabHost().setOnTabChangedListener(this);
		
//		getTabHost().setCurrentTab(2);
	}
	
	private void setupIntent(Class<?> clz, String s, int layoutId) {
		Intent intent = new Intent().setClass(this, clz);
		intent.putExtra("show_exit", true);
		if (layoutId != -1) {
			View widgetView = LayoutInflater.from(getApplicationContext())
					.inflate(R.layout.tabwidget, null);
			ImageView localImageView = (ImageView) widgetView
					.findViewById(R.id.icon);
			TextView localTextView = (TextView) widgetView
					.findViewById(R.id.text);
			localImageView.setImageResource(layoutId);
			localTextView.setText(s);
			TabHost.TabSpec tabSpec = getTabHost().newTabSpec(s)
					.setIndicator(widgetView).setContent(intent);
			getTabHost().addTab(tabSpec);
		} else {
			View widgetView = LayoutInflater.from(getApplicationContext())
					.inflate(R.layout.tabwidget_blank, null);
			TabHost.TabSpec tabSpec = getTabHost().newTabSpec(s)
					.setIndicator(widgetView).setContent(intent);
			getTabHost().addTab(tabSpec);
		}
	}
	
	public void setCurrentTab(int pos) {
		getTabHost().setCurrentTab(pos);
	}
	
	@Override
	protected void onDestroy() {
		instance = null;
		MyApplication.mUser = YongHuBean.newNull();
		MyApplication.tmpUser = YongHuBean.newNull();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
