package cn.leyundong.activity.yudingpage;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import cn.leyundong.activity.FirstActivity;
import cn.leyundong.activity.YuDingActivity;
import cn.leyundong.activity.page.AbstractPageFactory;
import cn.leyundong.activity.page.IPage;

public class YuDingPageFactory extends AbstractPageFactory {

	private Map<String, IPage> mPageCache = new HashMap<String, IPage>();
	
	public YuDingPageFactory(Activity act) {
		this.act = act;
	}
	
	private Activity act;

	@Override
	public IPage createPage(String tag) {
		IPage p = mPageCache.get(tag);
		if (p != null) {
			return p;
		}
		if ("场馆".equals(tag)) {
			p = new XuanZheChangeGuan(act, tag, FirstActivity.instance, YuDingActivity.instance);
		} else if ("订单".equals(tag)) {
			p = new DingDanGuanLi(act);
		} else if ("财务".equals(tag)) {
			p = new HuiYuanCaiWu(act);
		}
		mPageCache.put(tag, p);
		return p;
	}
}
