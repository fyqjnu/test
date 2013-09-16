package cn.leyundong.activity.clubpage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import cn.leyundong.R;
import cn.leyundong.activity.BaseActivity;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.HuoDongBean;
import cn.leyundong.entity.Result;
import cn.leyundong.httpoperation.MultiParamTask;
import cn.leyundong.json.JsonParser;
import cn.leyundong.util.Utils;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

public class EditHuoDongActivity extends BaseActivity {
	
	static final int id_save = 1;
	
	private HuoDongBean extraHuoDongBean;
	
	
	@ViewRef(id=R.id.header)Header header;
	@ViewRef(id=R.id.vf)ViewFlipper vf;
	@ViewRef(id=R.id.tvClubId)TextView tvClubId;
	@ViewRef(id=R.id.btnSave, onClick="click")Button btnSave;
	@ViewRef(id=R.id.etHuodongchangguan)EditText etHuoDongChangGuan;
	@ViewRef(id=R.id.etRenshuxianzhi)EditText etRenShuXiangZhi;
	@ViewRef(id=R.id.tvPrice)TextView tvPrice;
	@ViewRef(id=R.id.etHuodongshuoming)EditText etHuoDongShuoMing;
	@ViewRef(id=R.id.tvDate)TextView tvDate;
	@ViewRef(id=R.id.tvTimeFrom)TextView tvTimeFrom;
	@ViewRef(id=R.id.tvTimeTo)TextView tvTimeTo;
	@ViewRef(id=R.id.etQuxiaoshijian)EditText etQuxiaoshijian;
	@ViewRef(id=R.id.etBaomingshijian)EditText etBaomingshijian;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		extraHuoDongBean = (HuoDongBean) getIntent().getSerializableExtra(HuoDongBean.class.getSimpleName());
		System.out.println("活动=" + extraHuoDongBean);
		if (extraHuoDongBean == null) {
			finish();
			return ;
		}
		
		setContentView(R.layout.editclubhuodong);
		header.setTitle("编辑活动");
		fillData();
		
	}
	
	private void click(View v) {
		switch(v.getId()) {
		case R.id.btnSave:
			doSave();
			break;
		}
	}
	
	private void doSave() {
		System.out.println("保存活动---------------");
		ChaXunBean cxb = Utils.createChaXunBean(null);
		String url = Urls.getUrl(Urls.TYPE_CLUB, 43);
		HuoDongBean hdb = createHuoDongBean();
		if (hdb == null) {
			return ;
		}
		IDataRequest t = new MultiParamTask(act, url, 
					new IJson[]{new BeanProxy<ChaXunBean>(cxb), 
							new BeanProxy<HuoDongBean>(hdb)});
		executeTask(id_save, t);
		showDialog("正在为你保存，请稍候...");
	}
	
	private HuoDongBean createHuoDongBean() {
		HuoDongBean b = new HuoDongBean();
		b.jlbid = extraHuoDongBean.jlbid;
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
		//价格
		b.hdjg = extraHuoDongBean.hdjg;
		
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
		b.hdsjd = extraHuoDongBean.hdsjd;
		return b;
	}

	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		switch(what) {
		case id_save:
			handleSaveResult((String)result);
			break;
		}
	}
	
	
	private void handleSaveResult(String result) {
		Result ret = JsonParser.getResultFromJson(result);
		if (ret.success) {
			toast("活动保存成功");
		} else {
			toast("保存失败:" + ret.error);
		}
		dismissDialog();
	}

	private void fillData() {
		tvClubId.setText("" + extraHuoDongBean.jlbid);
		tvPrice.setText("" + extraHuoDongBean.hdjg);
		tvDate.setText(extraHuoDongBean.hdsjd);
		
		etHuoDongChangGuan.setText(extraHuoDongBean.hdcg);
		etRenShuXiangZhi.setText("" + extraHuoDongBean.rsxz);
		etBaomingshijian.setText("" + extraHuoDongBean.jzbmsj);
		etQuxiaoshijian.setText("" + extraHuoDongBean.zcqxsj);
		etHuoDongShuoMing.setText(extraHuoDongBean.hdsm);
	}

	private void setViewDiseditable(View[] views) {
		for (View v : views) {
			int width = v.getWidth();
			int height = v.getHeight();
			System.out.println("width=" + width + ", height=" + height);
			ViewGroup p = (ViewGroup) v.getParent();
			p.removeView(v);
			FrameLayout layout = new FrameLayout(act);
			layout.addView(v, width, height);
			View front = new View(act);
			front.setOnClickListener(l);
			front.setBackgroundColor(0x77000000);
			layout.addView(front, width, height);
			p.addView(layout, width, height);
			
		}
	}
	
	private OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		}
	};

}
