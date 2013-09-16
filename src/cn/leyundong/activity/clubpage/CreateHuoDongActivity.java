package cn.leyundong.activity.clubpage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import cn.leyundong.R;
import cn.leyundong.activity.BaseActivity;
import cn.leyundong.constant.Urls;
import cn.leyundong.custom.DateTextView;
import cn.leyundong.custom.TimeTextView;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.HuoDongBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.MultiParamTask;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.impl.DataViewModeImpl;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.IRefresh;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.Utils;
import cn.leyundong.view.Header;
import cn.leyundong.view.ProgressView;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

public class CreateHuoDongActivity extends BaseActivity implements IRefresh {
	
	static final int id_jlbid = 1;
	static final int id_create = 2;
	
	@ViewRef(id=R.id.header)Header header;
	@ViewRef(id=R.id.vf)ViewFlipper vf;
	@ViewRef(id=R.id.spinnerClubId)Spinner spinnerClubId;
	@ViewRef(id=R.id.btnCreateHuoDong, onClick="click")Button btnCreateHuoDong;
	@ViewRef(id=R.id.etHuodongchangguan)EditText etHuoDongChangGuan;
	@ViewRef(id=R.id.etRenshuxianzhi)EditText etRenShuXiangZhi;
	@ViewRef(id=R.id.etPrice)EditText etPrice;
	@ViewRef(id=R.id.etHuodongshuoming)EditText etHuoDongShuoMing;
	@ViewRef(id=R.id.tvDate)DateTextView tvDate;
	@ViewRef(id=R.id.tvTimeFrom)TimeTextView tvTimeFrom;
	@ViewRef(id=R.id.tvTimeTo)TimeTextView tvTimeTo;
	@ViewRef(id=R.id.etQuxiaoshijian)EditText etQuxiaoshijian;
	@ViewRef(id=R.id.etBaomingshijian)EditText etBaomingshijian;
	private IDataViewModel layouControl;
	
	private Map<String, String> jlbMap = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.clubhuodong);
		
		ProgressView pv = new ProgressView(act);
		pv.setRefresh(this);
		vf.addView(pv);
		layouControl = new DataViewModeImpl(vf, pv);
		
		header.setTitle("发起活动");
		
		loadJulebuId();
	}
	
	private void click(View v) {
		System.out.println("v id = " + v.getId());
		switch(v.getId()) {
		case R.id.btnCreateHuoDong:
			doCreateHuoDong();
			break;
		}
	}
	
	private void doCreateHuoDong() {
		System.out.println("创建活动--------------");
		IDataRequest task = getTask(id_create);
		if (task == null) {
			return ;
		}
		executeTask(id_create, task);
		showDialog("正在创建活动，请稍候...");
	}

	@Override
	public void doRefresh() {
		loadJulebuId();
	}
	
	private void loadJulebuId() {
		layouControl.showProgressLayout();
		executeTask(id_jlbid, getTask(id_jlbid));
		
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		switch(what) {
		case id_jlbid:
			handleJulebuResult((String)result);
			break;
		case id_create:
			handleCreateResult((String)result);
			break;
		}
	}
	
	private void handleCreateResult(String result) {
		dismissDialog();
		Result ret = JsonParser.getResultFromJson(result);
		if (ret.success) {
			toast("创建活动成功");
			finish();
		} else {
			toast("创建活动失败");
		}
	}

	private void handleJulebuResult(String result) {
		if (result == null) {
			layouControl.showRefreshLayout();
			return ;
		}
		try {
			JSONObject obj = new JSONObject(result);
			String key = "success";
			boolean success = false;
			if (!obj.isNull(key)) {
				success = obj.getBoolean(key);
			}
			if (!success) {
				layouControl.showRefreshLayout();
				return ;
			}
			layouControl.showDataLayout();
			key = "jlbList";
			if (!obj.isNull(key)) {
				JSONObject list = obj.getJSONObject(key);
				Iterator keys = list.keys();
				while(keys.hasNext()) {
					key = (String) keys.next();
					String value = list.getString(key);
					jlbMap.put(key, value);
				}
			}
			if (jlbMap.size() > 0) {
				ArrayAdapter adapter = new ArrayAdapter(act, android.R.layout.simple_spinner_item, jlbMap.values().toArray());
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerClubId.setAdapter(adapter);
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
	
	private HuoDongBean createHuoDongBean() {
		HuoDongBean b = new HuoDongBean();
		String key = getJlbId();
		b.jlbid = Long.valueOf(key);
		//活动场馆
		String hdcg = etHuoDongChangGuan.getText().toString();
		if (TextUtils.isEmpty(hdcg)) {
			toast("请输入活动场馆名称");
			return null;
		}
		b.hdcg = hdcg;
		try {
			//人数限制
			b.rsxz = Integer.valueOf(etRenShuXiangZhi.getText().toString());
		} catch (Exception e) {
			toast("人数限制请输入整数");
			return null;
		}
		try {
			//价格
			b.hdjg = Double.valueOf(etPrice.getText().toString());
		} catch (Exception e) {
			toast("价格请输入合法数字");
			return null;
		}
		
		//活动说明
		b.hdsm = etHuoDongShuoMing.getText().toString(); 
		
		try {
			//取消时间
			b.zcqxsj = Integer.valueOf(etQuxiaoshijian.getText().toString());
		} catch (Exception e) {
			toast("最迟取消时间只能是整数");
			return null;
		}
		
		try {
			//报名时间
			b.jzbmsj = Integer.valueOf(etBaomingshijian.getText().toString());
		} catch (Exception e) {
			toast("最迟取消时间只能是整数");
			return null;
		}
		
		//时间点
		/*StringBuilder sb = new StringBuilder();
		sb.append(tvDate.getDate())
			.append(" ")
			.append(tvTimeFrom.getTime())
			.append("-")
			.append(tvTimeTo.getTime());
		
		b.hdsjd = sb.toString();*/
		String date = tvDate.getDate();
		if (TextUtils.isEmpty(date)) {
			toast("请选择日期");
			return null;
		}
		b.hdsj = date;
		b.hdqssjd = tvTimeFrom.getTime();
		b.hdjssjd = tvTimeTo.getTime();
		
		return b;
	}

	private String getJlbId() {
		String key = null;
		String val = (String) spinnerClubId.getSelectedItem();
		Set<Entry<String, String>> es = jlbMap.entrySet();
		for (Entry<String, String> e : es) {
			if (e.getValue().endsWith(val)) {
				key = e.getKey();
				break;
			}
		}
		return key;
	}

	private IDataRequest getTask(int what) {
		ChaXunBean b = Utils.createChaXunBean(null);
		String url = null;
		IDataRequest t = null;
		if (what == id_jlbid) {
			url = Urls.getUrl(Urls.TYPE_CLUB, 41);
			t = new SingleParamTask<ChaXunBean>(act, url, b);
		} else if (what == id_create) {
			HuoDongBean hdb = createHuoDongBean();
			if (hdb == null) {
				return null;
			}
			url = Urls.getUrl(Urls.TYPE_CLUB, 4);
			t = new MultiParamTask(act, url, new IJson[]{new BeanProxy<ChaXunBean>(b), new BeanProxy<HuoDongBean>(hdb)});
		}
		return t;
	}

}
