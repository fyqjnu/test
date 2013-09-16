package cn.leyundong.activity.clubpage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.adapter.ClubCaiWuAdapter;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.entity.Result;
import cn.leyundong.impl.DataViewModeImpl;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.json.JsonParser;
import cn.leyundong.view.ProgressView;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.http.NetCheckHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IFindViewById;
import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;

/**
 * 会员财务
 * @author chenjunjun
 *
 */
public class ClubCaiWu extends AbstractAsyncPage implements IRefresh, IFindViewById, OnClickListener, OnItemClickListener{

	//第一次加载
	private static final int TASK_ID_FIRST_LOAD = 1;
	//加载更多
	private static final int TASK_ID_LOAD_MORE = 2;
	
	private Activity act;
	
	private View view;
	
	private int count;
	
	@ViewRef(id=R.id.tvHuiYuanMingCheng) EditText tvHuiYuanMingCheng;
	@ViewRef(id=R.id.tvClubMingCheng) EditText tvClubMingCheng;
	@ViewRef(id=R.id.query,onClick="onClick") Button btnQuery;
	@ViewRef(id=R.id.lv_guanlihuiyuancaiwu) ListView lvClubCaiWu;
	@ViewRef(id=R.id.vf_guanlihuiyuancaiwu) ViewFlipper vf;
	
	private ProgressView pbView;
	private ClubCaiWuAdapter mClubCaiWuAdapter;
	
	private IDataViewModel mDataViewMode;
	
	public ClubCaiWu(Activity act){
		this.act = act;
        view = act.getLayoutInflater().inflate(R.layout.guanlihuiyuancaiwu, null);
		
		ViewRefBinder binder = new ViewRefBinder(this);
		binder.init();
		
		pbView = new ProgressView(act);
		vf.addView(pbView);
		pbView.setRefresh(this);
		
		mDataViewMode = new DataViewModeImpl(vf, pbView);
		
		lvClubCaiWu.setOnItemClickListener(this);
	}
	
	private void loadData(int taskId) {
		if (taskId == TASK_ID_FIRST_LOAD) {
			mDataViewMode.showProgressLayout();
		}
		ChaXunBean objInfo = new ChaXunBean();
		objInfo.yhid = MyApplication.mUser.yhid;
		objInfo.yhm = tvHuiYuanMingCheng.getText().toString();
		objInfo.jlbmc = tvClubMingCheng.getText().toString();
		CaiWuTask task = new CaiWuTask(act, objInfo);
		executeTask(taskId, task);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println(what + ", " + result);
		if (result == null) {
			if (what == TASK_ID_FIRST_LOAD) {
				mDataViewMode.showRefreshLayout();
			}
			return ;
		}
		mDataViewMode.showDataLayout();
		JuLeBuBean[] infos = (JuLeBuBean[]) result;
		mClubCaiWuAdapter = new ClubCaiWuAdapter(act, Arrays.asList(infos));
		lvClubCaiWu.setAdapter(mClubCaiWuAdapter);
	}
	
	@Override
	public View getView() {
		return view;
	}

	@Override
	public View getViewById(int id) {
		return view.findViewById(id);
	}


	@Override
	public void doRefresh() {
		loadData(TASK_ID_FIRST_LOAD);
	}
	
	@Override
	public void onClick(View v) {
		System.out.println("ClubCaiWu--doRequest2");
		switch (v.getId()) {
		case R.id.query:
			loadData(TASK_ID_FIRST_LOAD);
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(act, "点击", 1).show();
		Intent intent = new Intent(act, ClubCaiWuDetailActivity.class);
		intent.putExtra("ClubDetail", (JuLeBuBean)mClubCaiWuAdapter.getItem(position));
		act.startActivity(intent);
	}
	
	class CaiWuTask implements IDataRequest {
		
		private Context ctx;
		private ChaXunBean info;
		
		public CaiWuTask(Context ctx, ChaXunBean info){
			this.ctx = ctx;
			this.info = info;
		}

		@Override
		public Object doRequest() {
			System.out.println("ClubCaiWu--doRequest");
			String url = Urls.getUrl(Urls.TYPE_CLUB, RequestId.CLUB_CAIWU);
			List<IJson> list = new ArrayList<IJson>();
			list.add(new BeanProxy<ChaXunBean>(info));
			System.out.println("url=" + url);
			IHttpDuty duty = new NetCheckHttpDuty(ctx, new HttpPostDuty(ctx, url, list));
			String post = duty.post();
			System.out.println("chaxun=" + post);
			Result result = JsonParser.getResultFromJson(post);
			if (result.success) {
				return doWithJson(post);
			}
			return null;
		}
		
	}
	
	private JuLeBuBean[] doWithJson(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			JSONObject page = obj.getJSONObject("page");
			JSONArray ary = page.getJSONArray("list");
			JuLeBuBean[] infos = JsonUtil.parseJsonArray(ary.toString(), JuLeBuBean.class);
			System.out.println("---------------------");
			System.out.println(Arrays.asList(infos));
			return infos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
