package cn.leyundong.activity.clubpage;

import java.util.Arrays;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.leyundong.NeedLoginHelper;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAutoListViewPage;
import cn.leyundong.adapter.ClubManagerAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.ListViewModel;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.util.Utils;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.json.JsonUtil;

public class ClubManagerPage extends AbstractAutoListViewPage<JuLeBuBean> implements OnItemClickListener {
	
	private ViewGroup layout;
	
	private View view;

	private NeedLoginHelper helper;

	public ClubManagerPage(Activity act) {
		super(act, ClubManagerAdapter.class);
		
		layout = (ViewGroup) act.getLayoutInflater().inflate(R.layout.club_manager_page, null);
		
		helper = new NeedLoginHelper(act);
		helper.setAdapter(adapter);
		helper.setLayoutControl(lvm);
		view = helper.needLogin(layout, "登录用户才能查看数据");
		
		loadData(ID_LOAD_FIRST);
	}
	
	@Override
	public void onDestroy() {
		helper.destroy();
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		IdTaskMap.remove(what);
		Result ret = parseResult((String)result);
		if (ret.success) {
			JuLeBuBean[] infos = (JuLeBuBean[]) ret.obj;
			adapter.addData(Arrays.asList(infos));
			layoutControl.showDataLayout();
		} else {
			layoutControl.showRefreshLayout();
		}
	}
	
	@Override
	protected Result parseResult(String json) {
		Result result = new Result(); 
		try {
			JSONObject obj = new JSONObject(json);
			String key = "success";
			if (!obj.isNull(key)) {
				result.success = obj.getBoolean(key);
			}
			if (!result.success) {
				key = "error";
				result.error = obj.getString(key);
			} else {
				key = "jlbList";
				String jlbList = obj.getString(key);
				JuLeBuBean[] infos = JsonUtil.parseJsonArray(jlbList, JuLeBuBean.class);
				result.obj = infos;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected IDataRequest getDataRequestTask(int what) {
		String url = Urls.getUrl(Urls.TYPE_CLUB, 2);
		ChaXunBean b = Utils.createChaXunBean(adapter);
		IDataRequest t = new SingleParamTask<ChaXunBean>(act, url, b);
		return t;
	}

	@Override
	protected void setupListView(ListViewModel lvm) {
		layout.addView(lvm, -1, -1);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		JuLeBuBean info = (JuLeBuBean) adapter.getItem(position - lv.getHeaderViewsCount());
		Intent intent = new Intent(act, ManagerHuiYuanActivity.class);
		intent.putExtra(JuLeBuBean.class.getSimpleName(), info);
		act.startActivity(intent);
	}
	
}
