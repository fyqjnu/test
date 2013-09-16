package cn.leyundong.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractPageFactory;
import cn.leyundong.activity.page.IPage;
import cn.leyundong.activity.page.ITabManager;
import cn.leyundong.activity.yudingpage.YuDingPageFactory;
import cn.leyundong.custom.TabTitle;
import cn.leyundong.custom.TabTitle.OnTabSelectedListener;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

/**
 * 预订专区页面
 * @author MSSOFT
 *
 */
public class YuDingActivity extends BaseActivity implements OnTabSelectedListener, ITabManager {
	
	private String[] titles = {"场馆", "订单", "财务"};
	private String[] titlesOnCreate = {"场馆"};
	
	public static YuDingActivity instance;
	
	@ViewRef(id=R.id.header) Header header;
	@ViewRef(id=R.id.fl) FrameLayout fl;
	@ViewRef(id=R.id.tabTitle) TabTitle tt;
	
	
	
	private Button[] btns;
	
	private Map<String, IPage> mPageMap = new HashMap<String, IPage>();
	
	private AbstractPageFactory mPageFactory;
	
	private void onClick(View v) {
		for (Button b : btns) {
			b.setSelected(false);
		}
		
		String tag = (String) v.getTag();
		IPage p = mPageMap.get(tag);
		if (p == null) {
			p = mPageFactory.createPage(tag);
			mPageMap.put(tag, p);
		}
		setPage(p);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		for (IPage p : mPageMap.values()) {
			p.onResume();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		instance = this;
		
		setContentView(R.layout.yudingzhuangqu);
		
		header.setTitle("预订专区");
		
		mPageFactory = new YuDingPageFactory(act);
		initPage();
		
		tt.setOnTabSelectedListener(this);
		tt.setTitles(titles, 0);
		
//		onClick(btnXuanZheChangGuan);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Collection<IPage> ps = mPageMap.values();
		for (IPage p : ps) {
			p.onDestroy();
		}
	}
	
	private void initPage() {
		for (String t : titlesOnCreate) {
			mPageMap.put(t, mPageFactory.createPage(t));
		}
	}

	private void setPage(IPage p) {
		System.out.println("显示=" + p);
		fl.removeAllViews();
		fl.addView(p.getView(), -1, -1);
	}

	@Override
	public void onTabSelected(int which, String title) {
		IPage p = mPageMap.get(title);
		if (p == null) {
			p = mPageFactory.createPage(title);
			mPageMap.put(title, p);
		}
		setPage(p);
	}

	@Override
	public void requestShowTitle(String title) {
		System.out.println("请求显示页面=" + title);
		tt.setCurrentTab(title);
	}

	@Override
	public void requestShowTitle(int which) {
		System.out.println("请求显示页面=" + which);
		tt.setCurrentTab(which);
	}

	
}
