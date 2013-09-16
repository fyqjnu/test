package cn.leyundong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.util.TextUtil;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class CaiWuAdapter extends AutoAdapter<JuLeBuBean>  {

	private Context ctx;
	
	public CaiWuAdapter(Context ctx) {
		this.ctx = ctx;
		mData = new ArrayList<JuLeBuBean>();
	}
	
	public void addData(List<JuLeBuBean> d) {
		mData.addAll(d);
		notifyDataSetChanged();
	}
	
	
	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_caiwu, new ViewHolder());
		}
		ViewHolder h = (ViewHolder) view.getTag();
		JuLeBuBean b = mData.get(position);
		h.tvTitle.setText(b.jlbmc);
		
		String title = null;
		title = "编号：";
//		h.tvId.setText("编号：" + b.jlbid);
		TextUtil.setText(h.tvId, title + b.jlbid, title.length());
		
		title = "收支日期：";
//		h.tvDate.setText("收支日期：" + b.szsj);
		TextUtil.setText(h.tvDate, title + b.szsj, title.length());
		
		title = "收支类型：";
//		h.tvType.setText("收支类型：" + b.szlxmc);
		TextUtil.setText(h.tvType, title + b.szlxmc, title.length());
		
		title = "收支金额：";
//		h.tvMoney.setText("收支金额：" + b.szje);
		TextUtil.setText(h.tvMoney, title + b.szje + "元", title.length());
		
		title = "总余额：";
//		h.tvAccount.setText("总余额：" + b.ye);
		TextUtil.setText(h.tvAccount, title + b.ye + "元", title.length());
		return view;
	}

	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvTitle)TextView tvTitle;
		@ViewRef(id=R.id.tvId)TextView tvId;
		@ViewRef(id=R.id.tvType)TextView tvType;
		@ViewRef(id=R.id.tvDate)TextView tvDate;
		@ViewRef(id=R.id.tvMoney)TextView tvMoney;
		@ViewRef(id=R.id.tvAccount)TextView tvAccount;
	}

}
