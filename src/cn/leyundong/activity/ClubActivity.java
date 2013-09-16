package cn.leyundong.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.clubpage.ClubCaiWu;
import cn.leyundong.activity.clubpage.ClubGuanLi;
import cn.leyundong.activity.clubpage.ClubHuoDong;
import cn.leyundong.activity.clubpage.ClubPageFactory;
import cn.leyundong.activity.clubpage.CreateHuoDongActivity;
import cn.leyundong.activity.page.AbstractPageFactory;
import cn.leyundong.activity.page.IPage;
import cn.leyundong.custom.TabTitle;
import cn.leyundong.custom.TabTitle.OnTabSelectedListener;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

/**
 * 俱乐部页面
 * @author Administrator
 *
 */
public class ClubActivity extends BaseActivity implements OnTabSelectedListener{
	
	private String[] titles = {"管理","活动","财务"};
	
	@ViewRef(id=R.id.header) Header header;
	@ViewRef(id=R.id.btnTab1, onClick="onClick") Button btnTab1;
	@ViewRef(id=R.id.btnTab2, onClick="onClick") Button btnTab2;
	@ViewRef(id=R.id.btnTab3, onClick="onClick") Button btnTab3;
	@ViewRef(id=R.id.clubframelayout) FrameLayout clubframe;
	@ViewRef(id=R.id.tabTitle) TabTitle tt;
	
	private Button[] btns;
	
	private ClubGuanLi mClubGuanLi;
	private ClubHuoDong mClubHuoDong;
	private ClubCaiWu mClubCaiWu;
	
	private Map<String, IPage> mPageMap = new HashMap<String, IPage>();
	private AbstractPageFactory mPageFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.julebujiayuan);
		header.setTitle("俱乐部管理");
		
		LayoutParams lp = new LayoutParams(-2, -2);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		btnCreate = new Button(act);
		btnCreate.setText("创建");
		btnCreate.setId(R.id.id_btn_create_huodong);
		btnCreate.setOnClickListener(createHuoDong);
		//添加创建按钮
		header.addComponent(btnCreate, lp);
		
		btns = new Button[]{btnTab1, btnTab2, btnTab3};
		
		btnTab1.setTag("管理");
		btnTab2.setTag("活动");
		btnTab3.setTag("财务");
		
		mPageFactory = new ClubPageFactory(act);
		tt.setOnTabSelectedListener(this);
		tt.setTitles(titles, 0);
	}
	
	private OnClickListener createHuoDong = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("创建活动----------");
			if (MyApplication.mUser.isNull()) {
				toast("请先登录");
				return ;
			}
			Intent intent = new Intent(act, CreateHuoDongActivity.class);
			startActivity(intent);
		}
	};

	private Button btnCreate;
	
	@Override
	protected void onResume() {
		super.onResume();
		if (FirstActivity.instance.mChaXunBean != null) {
			onClick(btnTab1);
		}
		for (IPage p : mPageMap.values()) {
			p.onResume();
		}
	}
	
	private void onClick(View v) {
		for (Button b : btns) {
			b.setSelected(false);
		}
		
		switch(v.getId()) {
		case R.id.btnTab1:
			btnTab1.setSelected(true);
			break;
		case R.id.btnTab2:
			btnTab2.setSelected(true);
			break;
		case R.id.btnTab3:
			btnTab3.setSelected(true);
			break;
		}
		String tag = (String) v.getTag();
		IPage p = mPageMap.get(tag);
		if (p == null) {
			p = mPageFactory.createPage(tag);
			mPageMap.put(tag, p);
		}
		setPage(p);
	}
	
	private void setPage(IPage p) {
		System.out.println("显示=" + p);
		clubframe.removeAllViews();
		clubframe.addView(p.getView(), -1, -1);
	}

	@Override
	public void onTabSelected(int which, String title) {
		IPage p = mPageMap.get(title);
		if (p == null) {
			p = mPageFactory.createPage(title);
			mPageMap.put(title, p);
		}
		if (titles[1].equals(title)) {
			btnCreate.setVisibility(0);
		} else {
			btnCreate.setVisibility(8);
		}
		setPage(p);
	}
	
	
	
}
