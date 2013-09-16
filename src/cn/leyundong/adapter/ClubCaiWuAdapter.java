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
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

/**
 * 俱乐部管理--财务
 * @author chenjunjun
 *
 */
public class ClubCaiWuAdapter extends AutoAdapter<JuLeBuBean>{

	private Context ctx;
	
	public ClubCaiWuAdapter(Context ctx, List<JuLeBuBean> l) {
		this(ctx);
		mData.addAll(l);
	}
	
	public ClubCaiWuAdapter(Context ctx) {
		this.ctx = ctx;
		mData = new ArrayList<JuLeBuBean>();
	}
	

	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_club_caiwu, new ViewHolder());
		}
		ViewHolder vh = (ViewHolder) view.getTag();
		JuLeBuBean b = mData.get(position);
		vh.tvHuiYuanMing.setText(b.getYhm());
		vh.tvClubXingMing.setText(b.getZsxm());
		vh.tvClubHuiYuanShuXing.setText(b.getHysxmc());
		vh.tvclubphone.setText(b.getSjhm());
		vh.tvClubJuLeBuMingCheng.setText(b.getJlbmc());
		vh.tvClubZhongYuE.setText(String.valueOf(b.getYe()));
		vh.btnChongZhi.setTag(position);
		vh.btnKouKuan.setTag(position);
		vh.btnDetail.setTag(position);
		return view;
	}
	
	class ViewHolder extends AbstractViewHolder{
		@ViewRef(id=R.id.club_huiyuanming) TextView tvHuiYuanMing; 
		@ViewRef(id=R.id.club_ChongZhi,onClick="onClick") Button btnChongZhi;
		@ViewRef(id=R.id.club_KouKuan,onClick="onClick") Button btnKouKuan;
		@ViewRef(id=R.id.club_XingMing) TextView tvClubXingMing; 
		@ViewRef(id=R.id.club_HuiYuan_ShuXing) TextView tvClubHuiYuanShuXing; 
		@ViewRef(id=R.id.club_phone) TextView tvclubphone; 
		@ViewRef(id=R.id.club_JuLeBu_MingCheng) TextView tvClubJuLeBuMingCheng; 
		@ViewRef(id=R.id.club_zhongyue) TextView tvClubZhongYuE; 
		@ViewRef(id=R.id.btnDetail, onClick="onClick") Button btnDetail; 
		
		public void onClick(View v) {
			if (outerClickListener != null) {
				outerClickListener.onClick(v);
			}
		}
	}

}
