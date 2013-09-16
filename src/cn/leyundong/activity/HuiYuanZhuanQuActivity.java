package cn.leyundong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import cn.leyundong.R;
import cn.leyundong.activity.huiyuanzhuanqupage.HuiYuanZhuanQuPageFactory;
import cn.leyundong.activity.page.AbstractPageFactory;
import cn.leyundong.activity.page.IPage;
import cn.leyundong.activity.page.ITabManager;
import cn.leyundong.custom.TabTitle;
import cn.leyundong.custom.TabTitle.OnTabSelectedListener;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

public class HuiYuanZhuanQuActivity extends BaseActivity implements OnTabSelectedListener, ITabManager {
	
	public static HuiYuanZhuanQuActivity instance;
	
	private String[] titles = {"俱乐部", "报名", "活动", "财务"};
	private String[] titlesOnCreate = {"报名"};

	@ViewRef(id=R.id.header) Header header;
	
	
	@ViewRef(id=R.id.tt) TabTitle tt;
	
	private Button[] btns;
	
	@ViewRef(id=R.id.fl) FrameLayout fl;
	
	private AbstractPageFactory mPageFactory;
	
	private Map<String, IPage> pages = new HashMap<String, IPage>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		instance = this;
		
		setContentView(R.layout.huiyuanzhuangqu);
		header.setTitle("会员专区");
		
		mPageFactory = new HuiYuanZhuanQuPageFactory(act);
		
		initPages();
		
		tt.setOnTabSelectedListener(this);
		tt.setTitles(titles, 0);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (IPage p : pages.values()) {
			p.onDestroy();
		}
	}
	
	private void initPages() {
		for (String s : titlesOnCreate) {
//			pages.add(mPageFactory.createPage(s));
			pages.put(s, mPageFactory.createPage(s));
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println(TAG + "onresume------------");
		for (IPage p : pages.values()) {
			p.onResume();
		}
	}
	
	private void setPage(IPage p) {
		fl.removeAllViews();
		fl.addView(p.getView(), -1, -1);
	}

	@Override
	public void onTabSelected(int which, String title) {
		IPage p = mPageFactory.createPage(title);
		pages.put(title, p);
		setPage(p);
	}

	@Override
	public void requestShowTitle(String title) {
		System.out.println("请求显示页=" + title);
		tt.setCurrentTab(title);
	}

	@Override
	public void requestShowTitle(int which) {
		System.out.println("请求显示页=" + which);
		tt.setCurrentTab(which);
	}
}
