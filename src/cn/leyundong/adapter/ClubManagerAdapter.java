package cn.leyundong.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.JuLeBuBean;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class ClubManagerAdapter extends AutoAdapter<JuLeBuBean> {

	private Context ctx;
	
	public ClubManagerAdapter(Context ctx) {
		this.ctx = ctx;
		mData = new ArrayList<JuLeBuBean>();
	}
	
	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_club_manager, new ViewHolder());
		}
		ViewHolder h = (ViewHolder) view.getTag();
		JuLeBuBean b = mData.get(position);
		//俱乐部名称
		h.tvTitle.setText(b.jlbmc);
		//场地类型
		h.tvType.setText(b.cdlxmc);
		//俱乐部id
		h.tvClubId.setText("" + b.jlbid);
		//会员数
		h.tvMemNum.setText("" + b.hyrs);
		//待验证
		h.tvDaiYanNum.setText("" + b.dyzrs);
		return view;
	}
	
	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvTitle)TextView tvTitle;
		@ViewRef(id=R.id.tvType)TextView tvType;
		@ViewRef(id=R.id.tvClubId)TextView tvClubId;
		@ViewRef(id=R.id.tvMemNum)TextView tvMemNum;
		@ViewRef(id=R.id.tvDaiYanNum)TextView tvDaiYanNum;
	}

}
