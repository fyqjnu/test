package cn.leyundong.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.HuoDongBean;
import cn.leyundong.util.TextUtil;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class BaoMingQingKuanAdapter extends AutoAdapter<HuoDongBean> {
	
	private Context ctx;
	
	public BaoMingQingKuanAdapter(Context ctx) {
		this.ctx = ctx;
		mData = new ArrayList<HuoDongBean>();
	}

	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_baomingqingkuan, new ViewHolder());
		}
		ViewHolder h = (ViewHolder) view.getTag();
		HuoDongBean b = mData.get(position);
		h.tv1.setText(b.hdcg);
		String title = "报名项状态：";
		h.tv2.setText("报名项状态：" + b.bmztmc);
		TextUtil.setText(h.tv2, title + b.bmztmc, title.length());
		
		title = "报名时间：";
//		h.tv3.setText("报名时间：" + b.cjsj);
		TextUtil.setText(h.tv3, title + b.cjsj, title.length());
		
		return view;
	}

	
	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tv1)TextView tv1;
		@ViewRef(id=R.id.tv2)TextView tv2;
		@ViewRef(id=R.id.tv3)TextView tv3;
	}

}
