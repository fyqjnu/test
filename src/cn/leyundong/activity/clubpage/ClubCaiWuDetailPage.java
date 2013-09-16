package cn.leyundong.activity.clubpage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.ClubCaiWuDetailAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.util.Utils;
import cn.leyundong.view.Header;
import cn.quickdevelp.interfaces.IDataRequest;

/**
 * 会员财务详细信息页面
 * @author MSSOFT
 *
 */
public class ClubCaiWuDetailPage extends AbstractAutoListViewPage<JuLeBuBean> {

	
	private ViewGroup view;
	private Header header;
	
	private JuLeBuBean extraJuLeBuBean;

	public ClubCaiWuDetailPage(Activity act, JuLeBuBean extraJuLeBuBean) {
		super(act, ClubCaiWuDetailAdapter.class);
		
		this.extraJuLeBuBean = extraJuLeBuBean;
		
		view = (ViewGroup) act.getLayoutInflater().inflate(R.layout.club_caiwu_detail, null);
		header = (Header) view.findViewById(R.id.header);
		header.setTitle("会员财务明细");
		
		loadData(ID_LOAD_FIRST);
	}

	@Override
	public View getView() {
		return view;
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		super.onTaskExecuted(what, result);
	}

	@Override
	protected Result parseResult(String json) {
		return Utils.getPageFromJson(json, JuLeBuBean.class);
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 32);
		ChaXunBean b = Utils.createChaXunBean(adapter);
		b.dlryid = extraJuLeBuBean.yhid;
		b.jlbid = extraJuLeBuBean.jlbid;
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		view.addView(lvm, -1, -1);
		lvm.setEmptyText("没有相应数据");
	}
	
}
