package cn.leyundong.activity.clubpage;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import cn.leyundong.activity.page.AbstractPageFactory;
import cn.leyundong.activity.page.IPage;
import cn.leyundong.activity.yudingpage.DingDanGuanLi;
import cn.leyundong.activity.yudingpage.HuiYuanCaiWu;
import cn.leyundong.activity.yudingpage.XuanZheChangeGuan;

public class ClubPageFactory extends AbstractPageFactory{

	private Map<String, IPage> mPageCache = new HashMap<String, IPage>();
	
	public ClubPageFactory(Activity act) {
		this.act = act;
	}
	
	private Activity act;
	
	@Override
	public IPage createPage(String tag) {
		IPage p = mPageCache.get(tag);
		if (p != null) {
			return p;
		}
		if ("管理".equals(tag)) {
			p = new ClubManagerPage(act);
		} else if ("活动".equals(tag)) {
			p = new ClubHuoDongPage(act);
		} else if ("财务".equals(tag)) {
			p = new ClubCaiWuNew(act);
		}
		mPageCache.put(tag, p);
		return p;
	}

}
