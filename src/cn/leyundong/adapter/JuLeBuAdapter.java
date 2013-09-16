package cn.leyundong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.util.TextUtil;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class JuLeBuAdapter extends AutoAdapter<JuLeBuBean> {
	
	private Context ctx;
	
	public JuLeBuAdapter(Context ctx) {
		this.ctx = ctx;
		mData = new ArrayList<JuLeBuBean>();
	}
	
	public void clear() {
		mData.clear();
		notifyDataSetChanged();
	}
	
	public void addData(List<JuLeBuBean> d) {
		mData.addAll(d);
		notifyDataSetChanged();
	}

	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_julebu, new ViewHolder());
		}
		ViewHolder h = (ViewHolder) view.getTag();
		
		JuLeBuBean b = mData.get(position);
		
		h.tvTitle.setText(b.jlbmc);
		
		String title = null;
		title = "俱乐部编号：";
//		h.tvId.setText("俱乐部编号：" + b.jlbid);
		TextUtil.setText(h.tvId, title + b.jlbid, title.length());
		
		title = "会员属性：";
//		h.tvHuiYuan.setText("会员属性：" + b.hysxmc);
		TextUtil.setText(h.tvHuiYuan, title + b.hysxmc, title.length());
		
		title = "运动类型：";
		h.tvType.setText("运动类型：" + b.cdlxmc);
		TextUtil.setText(h.tvType, title + b.cdlxmc, title.length());
		
		title = "活动场管：";
		h.tvChangGuan.setText("活动场管：" + b.hdcg);
		TextUtil.setText(h.tvChangGuan, title + b.hdcg, title.length());
		
		title = "活动地址：";
		h.tvAddress.setText("活动地址：" + b.hddz);
		TextUtil.setText(h.tvAddress, title + b.hddz, title.length());
		
		h.btnCharge.setTag(position);
		h.btnJiaRu.setTag(position);
		h.btnJuJie.setTag(position);
		h.btnPass.setTag(position);
		
		//允许验证通过
		if (b.yxYztg) {
			h.btnPass.setVisibility(0);
		} else {
			h.btnPass.setVisibility(8);
		}
		
		//允许加入俱乐部
		if (b.yxSqjr) {
			h.btnJiaRu.setVisibility(0);
		} else {
			h.btnJiaRu.setVisibility(8);
		}
		
		//允许充值
		if (b.yxCz) {
			h.btnCharge.setVisibility(0);
		} else {
			h.btnCharge.setVisibility(8);
		}
		
		//允许拒绝加入
		if (b.yxJjjr) {
			h.btnJuJie.setVisibility(0);
		} else {
			h.btnJuJie.setVisibility(8);
		}
		
		return view;
	}
	
	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvTitle)TextView tvTitle;
		@ViewRef(id=R.id.tvId)TextView tvId;
		@ViewRef(id=R.id.tvHuiYuan)TextView tvHuiYuan;
		@ViewRef(id=R.id.tvType)TextView tvType;
		@ViewRef(id=R.id.tvChangGuan)TextView tvChangGuan;
		@ViewRef(id=R.id.tvAddress)TextView tvAddress;
		
		@ViewRef(id=R.id.btnPass, onClick="click")Button btnPass;
		@ViewRef(id=R.id.btnJuJie, onClick="click")Button btnJuJie;
		@ViewRef(id=R.id.btnCharge, onClick="click")Button btnCharge;
		@ViewRef(id=R.id.btnJiaRu, onClick="click")Button btnJiaRu;
		
		private void click(View v) {
			if (outerClickListener != null) {
				outerClickListener.onClick(v);
			}
		}
	}

}
