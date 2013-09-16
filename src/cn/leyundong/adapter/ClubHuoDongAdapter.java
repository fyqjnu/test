package cn.leyundong.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.HuoDongBean;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class ClubHuoDongAdapter extends AutoAdapter<HuoDongBean> {
	
	private Context ctx;
	
	public ClubHuoDongAdapter(Context ctx) {
		this.ctx = ctx;
		mData = new ArrayList<HuoDongBean>();
	}

	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_club_huodong, new ViewHolder());
		}
		
		HuoDongBean b = mData.get(position);
		ViewHolder h = (ViewHolder) view.getTag();
		
		h.tvTitle.setText(b.hdcg);
		h.tvClubName.setText(b.jlbmc);
		h.tvType.setText(b.cdlxmc);
		h.tvDate.setText(b.hdsjd);
		h.tvPrice.setText("" + b.hdjg);
		h.tvRenShuXiangZhi.setText("" + b.rsxz);
		h.tvShiBaoRenShu.setText("" + b.sbrs);
		h.tvState.setText(b.hdztmc);
		h.tvMoney.setText("" + b.hdzsr);
		
		h.btnCancel.setTag(position);
		h.btnCopy.setTag(position);
		h.btnDetail.setTag(position);
		h.btnEdit.setTag(position);
		
		return view;
	}
	
	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvTitle)TextView tvTitle;
		@ViewRef(id=R.id.tvClubName)TextView tvClubName;
		@ViewRef(id=R.id.tvType)TextView tvType;
		@ViewRef(id=R.id.tvDate)TextView tvDate;
		@ViewRef(id=R.id.tvPrice)TextView tvPrice;
		@ViewRef(id=R.id.tvRenShuXiangZhi)TextView tvRenShuXiangZhi;
		@ViewRef(id=R.id.tvShiBaoRenShu)TextView tvShiBaoRenShu;
		@ViewRef(id=R.id.tvState)TextView tvState;
		@ViewRef(id=R.id.tvMoney)TextView tvMoney;
		
		
		
		@ViewRef(id=R.id.btnEdit, onClick="click")Button btnEdit;
		@ViewRef(id=R.id.btnCancel, onClick="click")Button btnCancel;
		@ViewRef(id=R.id.btnCopy, onClick="click")Button btnCopy;
		@ViewRef(id=R.id.btnDetail, onClick="click")Button btnDetail;
		
		private void click(View v) {
			if (outerClickListener != null) {
				outerClickListener.onClick(v);
			}
		}
		
		
	}

}
