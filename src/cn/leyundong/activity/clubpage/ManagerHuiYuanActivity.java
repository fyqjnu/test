package cn.leyundong.activity.clubpage;

import android.os.Bundle;
import cn.leyundong.activity.BaseActivity;
import cn.leyundong.entity.JuLeBuBean;

public class ManagerHuiYuanActivity extends BaseActivity {

	private ManagerHuiYuanPage page;
	private JuLeBuBean extraJuLeBuBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		extraJuLeBuBean = (JuLeBuBean) getIntent().getSerializableExtra(JuLeBuBean.class.getSimpleName());
		if (extraJuLeBuBean == null) {
			finish();
			return ;
		}
		
		page = new ManagerHuiYuanPage(act, extraJuLeBuBean);
		setContentView(page.getView());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		page.onDestroy();
	}
	
}
