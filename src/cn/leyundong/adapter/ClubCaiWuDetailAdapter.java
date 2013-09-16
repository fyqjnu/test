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

public class ClubCaiWuDetailAdapter extends AutoAdapter<JuLeBuBean>{
	
	private Context ctx;
	
	public ClubCaiWuDetailAdapter(Context paramCtx){
		ctx = paramCtx;
		mData = new ArrayList<JuLeBuBean>();
	}

	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_club_caiwu_detail, new ViewHolder());
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		JuLeBuBean b = mData.get(position);
		//
		holder.tvName.setText(b.yhm);
		holder.tvDate.setText(b.szsj);
		holder.tvType.setText(b.szlxmc);
		holder.tvMoney1.setText("" + b.szje);
		
		holder.tvMoney2.setText("" + b.ye);
		
		return view;
	}
	
	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvName)TextView tvName;
		@ViewRef(id=R.id.tvDate)TextView tvDate;
		@ViewRef(id=R.id.tvType)TextView tvType;
		@ViewRef(id=R.id.tvMoney1)TextView tvMoney1;
		@ViewRef(id=R.id.tvMoney2)TextView tvMoney2;
	}
	
}
