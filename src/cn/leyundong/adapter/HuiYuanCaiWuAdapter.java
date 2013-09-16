package cn.leyundong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.adapter.ChangGuanAdapter.ViewHolder;
import cn.leyundong.entity.ChangGuanBean;
import cn.leyundong.entity.ChangGuangHuiYuanCaiWuBean;
import cn.leyundong.util.TextUtil;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class HuiYuanCaiWuAdapter extends AutoAdapter<ChangGuangHuiYuanCaiWuBean>  {
	private Context ctx;
	
	public HuiYuanCaiWuAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public void addData(List<ChangGuangHuiYuanCaiWuBean> data) {
		if (mData == null) {
			mData = new ArrayList<ChangGuangHuiYuanCaiWuBean>();
		}
		mData.addAll(data);
		notifyDataSetChanged();
	}
	
	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_huiyuancaiwu, new ViewHolder());
		}
		ViewHolder h = (ViewHolder) view.getTag();
		ChangGuangHuiYuanCaiWuBean b = mData.get(position);
		h.tvName.setText(b.cgmc);
		
		String title = "收支日期：";
//		h.tvDate.setText("收支日期：" + b.szsj);
		TextUtil.setText(h.tvDate, title + b.szsj, title.length());
		
		title = "收支类型：";
//		h.tvType.setText("收支类型：" + b.szlxmc);
		TextUtil.setText(h.tvType, title + b.szlxmc, title.length());
		
		title = "收支金额：";
//		h.tvMoney.setText("收支金额：" + b.szje);
		TextUtil.setText(h.tvMoney, title + b.szje + "元", title.length());
		
		title = "总余额：";
//		h.tvBalance.setText("总余额：" + b.ye);
		TextUtil.setText(h.tvBalance, title + b.ye + "元", title.length());
		
		return view;
	}

	
	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvName) TextView tvName; 
		@ViewRef(id=R.id.tvDate) TextView tvDate; 
		@ViewRef(id=R.id.tvType) TextView tvType; 
		@ViewRef(id=R.id.tvMoney) TextView tvMoney; 
		@ViewRef(id=R.id.tvBalance) TextView tvBalance; 
		
	}

}
