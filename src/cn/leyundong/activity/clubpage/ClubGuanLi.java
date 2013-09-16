package cn.leyundong.activity.clubpage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewFlipper;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.leyundong.adapter.ClubGuanLiAdapter;
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
 * 俱乐部管理
 * @author chenjunjun
 *
 */
public class ClubGuanLi extends AbstractAsyncPage implements IRefresh, IFindViewById{
	//第一次加载
	private static final int TASK_ID_FIRST_LOAD = 1;
	//加载更多
	private static final int TASK_ID_LOAD_MORE = 2;
	
	private Activity act;
	
	private View view;
	
	private int count;
	
	@ViewRef(id=R.id.btnQuery,onClick="onClick") Button btnQuery;
	@ViewRef(id=R.id.lv_managerclub) ListView lvManagerClub;
	@ViewRef(id=R.id.vf_managerclub) ViewFlipper vf;
	
	private ProgressView pbView;
	private ClubGuanLiAdapter mClubGuanLiAdapter;
	
	private IDataViewModel mDataViewMode;
	
	public ClubGuanLi(Activity act){
		this.act = act;
		
		view = act.getLayoutInflater().inflate(R.layout.julebuguanli, null);
		
		ViewRefBinder binder = new ViewRefBinder(this);
		binder.init();
		
		pbView = new ProgressView(act);
		vf.addView(pbView);
		pbView.setRefresh(this);
		
		mDataViewMode = new DataViewModeImpl(vf, pbView);
		
		loadData(TASK_ID_FIRST_LOAD);
		
		mClubGuanLiAdapter = new ClubGuanLiAdapter(act);
		
	}
	
	private void loadData(int taskId) {
		if (taskId == TASK_ID_FIRST_LOAD) {
			mDataViewMode.showProgressLayout();
		}
		ChaXunBean objInfo = new ChaXunBean();
		objInfo.yhid = MyApplication.mUser.yhid;
		GuanLiTask task = new GuanLiTask(act, objInfo);
		executeTask(taskId, task);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		if (result == null) {
			if (what == TASK_ID_FIRST_LOAD) {
				mDataViewMode.showRefreshLayout();
			}
			return ;
		}
		mDataViewMode.showDataLayout();
		JuLeBuBean[] infos = (JuLeBuBean[]) result;
		mClubGuanLiAdapter.addData(Arrays.asList(infos));
		lvManagerClub.setAdapter(mClubGuanLiAdapter);
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
	
	class GuanLiTask implements IDataRequest {
		
		private Context ctx;
		private ChaXunBean info;
		
		public GuanLiTask(Context ctx, ChaXunBean info){
			this.ctx = ctx;
			this.info = info;
		}

		@Override
		public Object doRequest() {
			System.out.println("ClubGuanLi--doRequest");
			String url = Urls.getUrl(Urls.TYPE_CLUB, RequestId.CLUB_CAIWU);
			List<IJson> list = new ArrayList<IJson>();
			list.add(new BeanProxy<ChaXunBean>(info));
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
