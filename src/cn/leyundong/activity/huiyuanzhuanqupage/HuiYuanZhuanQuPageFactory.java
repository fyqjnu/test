package cn.leyundong.activity.huiyuanzhuanqupage;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import cn.leyundong.activity.FirstActivity;
import cn.leyundong.activity.HuiYuanZhuanQuActivity;
import cn.leyundong.activity.page.AbstractPageFactory;
import cn.leyundong.activity.page.IPage;

/**
 * 会员专区Page工厂
 * @author Administrator
 *
 */
public class HuiYuanZhuanQuPageFactory extends AbstractPageFactory {
	
	
	private Map<String, IPage> mPageCache = new HashMap<String, IPage>();
	
	public HuiYuanZhuanQuPageFactory(Activity act) {
		this.act = act;
	}
	
	private Activity act;
	
	@Override
	public IPage createPage(String tag) {
		IPage p = mPageCache.get(tag);
		if (p != null) {
			return p;
		}
		if ("俱乐部".equals(tag)) {
			p = new JuLeBuPage(act);
		} else if ("报名".equals(tag)) {
			p = new BaoMingHuoDongPage(act, tag, HuiYuanZhuanQuActivity.instance);
		} else if ("活动".equals(tag)) {
			p = new BaoMingGuanLiPage(act, tag);
		} else if ("财务".equals(tag)) {
			p = new CaiWuPage(act, tag);
		}
		mPageCache.put(tag, p);
		return p;
	}

}
