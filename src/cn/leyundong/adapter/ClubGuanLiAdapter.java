package cn.leyundong.adapter;

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
 * 俱乐部--俱乐部管理
 * @author Administrator
 *
 */
public class ClubGuanLiAdapter extends AutoAdapter<JuLeBuBean>{
	
	private Context ctx;
	
	public ClubGuanLiAdapter(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	protected View getAutoView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = createView(ctx, R.layout.item_managerclub, new ViewHolder());
		}
		ViewHolder vh = (ViewHolder) convertView.getTag();
		
		JuLeBuBean b = mData.get(position);
		vh.tvclubname.setText(b.jlbmc);
		vh.tvName1.setText(b.yhm);
		vh.tvName2.setText(b.zsxm);
		vh.tvPhone.setText(b.sjhm);
		vh.tvDesc.setText(b.yzms);
		vh.tvTime.setText(b.sqsj);
		
		vh.btnPass.setTag(position);
		vh.btnReject.setTag(position);
		vh.btnDeleteHuiyuan.setTag(position);
		vh.btnSetManager.setTag(position);
		vh.btnDeleteManager.setTag(position);
		vh.btnCancelYaoqing.setTag(position);
		
		//按钮显示
		if (b.yxQxyq) {
			vh.btnCancelYaoqing.setVisibility(0);
		} else {
			vh.btnCancelYaoqing.setVisibility(8);
		}
		
		if (b.yxSzgly) {
			vh.btnSetManager.setVisibility(0);
		} else {
			vh.btnSetManager.setVisibility(8);
		}
		
		if (b.yxScHy) {
			vh.btnDeleteHuiyuan.setVisibility(0);
		} else {
			vh.btnDeleteHuiyuan.setVisibility(8);
		}
		
		if (b.yxScgly) {
			vh.btnDeleteManager.setVisibility(0);
		} else {
			vh.btnDeleteManager.setVisibility(8);
		}
		
		if (b.yxJjjr) {
			vh.btnReject.setVisibility(0);
		} else {
			vh.btnReject.setVisibility(8);
		}
		
		if (b.yxYztg) {
			vh.btnPass.setVisibility(0);
		} else {
			vh.btnPass.setVisibility(8);
		}
		
		return convertView;
	}

	class ViewHolder extends AbstractViewHolder{
		@ViewRef(id=R.id.club_name) TextView tvclubname; 
		@ViewRef(id=R.id.btnPass,onClick="onClick") Button btnPass;
		@ViewRef(id=R.id.btnReject,onClick="onClick") Button btnReject;
		@ViewRef(id=R.id.btnSetManager,onClick="onClick") Button btnSetManager;
		@ViewRef(id=R.id.btnDeleteHuiYuan,onClick="onClick") Button btnDeleteHuiyuan;
		@ViewRef(id=R.id.btnDeleteManager,onClick="onClick") Button btnDeleteManager;
		@ViewRef(id=R.id.btnCancelYaoqing,onClick="onClick") Button btnCancelYaoqing;
		@ViewRef(id=R.id.tvName1) TextView tvName1; 
		@ViewRef(id=R.id.tvName2) TextView tvName2; 
		@ViewRef(id=R.id.tvPhone) TextView tvPhone; 
		@ViewRef(id=R.id.tvDesc) TextView tvDesc; 
		@ViewRef(id=R.id.tvTime) TextView tvTime;
		
		public void onClick(View v) {
			if (outerClickListener != null) {
				outerClickListener.onClick(v);
			}
		}
	}

}
