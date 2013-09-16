package cn.leyundong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.activity.yudingpage.YuDingTaskActivity;
import cn.leyundong.entity.ChangGuanBean;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

/**
 * 场馆详情
 * @author MSSOFT
 *
 */
public class ChangGuanDetailActivity extends BaseActivity {

	@ViewRef(id=R.id.header) Header header;
	//地址
	@ViewRef(id=R.id.tvDiZhi) TextView tvDiZhi;
	//电话
	@ViewRef(id=R.id.tvDianHuan) TextView tvTel;
	//数量 
	@ViewRef(id=R.id.tvShuLiang) TextView tvShuLiang;
	//价格
	@ViewRef(id=R.id.tvJiaGe) TextView tvPrice;
	//时间
	@ViewRef(id=R.id.tvShiJian) TextView tvTime;
	//公交
	@ViewRef(id=R.id.tvGongJiao) TextView tvBus;
	//详情
	@ViewRef(id=R.id.tvXiangQing) TextView tvDetail;
	//俱乐部
	@ViewRef(id=R.id.tvJuLeBu) TextView tvJuLeBu;
	@ViewRef(id=R.id.btnDingChang, onClick="onClick") Button btnDingChang;
	
	
	private ChangGuanBean info;
	
	private String cdlx;
	
	private void onClick(View v) {
		Intent i = new Intent(act, YuDingTaskActivity.class);
		i.putExtra(ChangGuanBean.class.getSimpleName(), info);
		i.putExtra("cdlx", cdlx);
		startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_changguan);
		
		info = (ChangGuanBean) getIntent().getSerializableExtra(ChangGuanBean.class.getName());
		cdlx = getIntent().getStringExtra("cdlx");
		if (info == null || cdlx == null) {
			finish();
			return ;
		}
		
		header.setTitle(info.cgmc);
		bindData();
	}
	
	private void bindData() {
		tvDiZhi.setText("场馆地址：" + info.cgdz);
		tvTel.setText("联系电话：" + info.lxdh);
		tvShuLiang.setText("场馆数量：" + info.cdsl);
		tvPrice.setText("场地价格：" + info.cgjg);
		tvTime.setText("营业时间：" + info.yysj);
		tvBus.setText("公交站点：" + info.gjzd);
		tvDetail.setText(info.cgxq);
		tvJuLeBu.setText(info.cgjlb);
	}
}
