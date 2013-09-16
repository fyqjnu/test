package cn.leyundong.activity;

import android.os.Bundle;
import cn.leyundong.activity.huiyuanzhuanqupage.BaoMingQingKuanPage;
import cn.leyundong.entity.HuoDongBean;

public class BaoMingQingKuanActivity extends BaseActivity {

	private HuoDongBean extraHongDongBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		extraHongDongBean = (HuoDongBean) getIntent().getSerializableExtra(HuoDongBean.class.getSimpleName());
		System.out.println("传过来信息=" + extraHongDongBean);
		if(extraHongDongBean == null) {
			finish();
			return ;
		}
		
		BaoMingQingKuanPage p = new BaoMingQingKuanPage(act, extraHongDongBean);
		
		setContentView(p.getView());
		
	}
	
}
