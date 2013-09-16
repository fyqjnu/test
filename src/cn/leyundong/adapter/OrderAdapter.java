package cn.leyundong.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.DingDanXiangBean;
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class OrderAdapter extends AutoAdapter<DingDanXiangBean> {
	
	private Context ctx;
	
	public OrderAdapter(Context ctx) {
		this.ctx = ctx;
		mData = new ArrayList<DingDanXiangBean>();
	}
	
	
	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_order, new ViewHolder());
		}
		ViewHolder h = (ViewHolder) view.getTag();
		DingDanXiangBean info = mData.get(position);
		h.tvText1.setText(info.dwsjd);
		h.tvText2.setText(info.cdmc);
		h.tvText3.setText("" + info.ydjg);
		h.ibCanel.setTag(position);
		return view;
	}
	
	public void clearAllDingDang() {
		if (mData.size() > 0) {
			notifyDataChanged(mData.toArray(new DingDanXiangBean[mData.size()]));
		}
		clearData();
	}

	private void notifyDataChanged(DingDanXiangBean[] ddxb) {
		MessageManager mm = MessageManager.getInstance();
		MyMessage m = mm.obtainMyMessage();
		m.type = MessageType.DINGDANGXIANG_CHANGED;
		m.obj1 = ddxb;
		mm.sendMyMessage(m);
	}

	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvText1)TextView tvText1;
		@ViewRef(id=R.id.tvText2)TextView tvText2;
		@ViewRef(id=R.id.tvText3)TextView tvText3;
		@ViewRef(id=R.id.ibCancel, onClick="cancel")ImageButton ibCanel;
		
		private void cancel(View v) {
			System.out.println("取消--------" + v.getTag());
			int pos = (Integer) v.getTag();
			DingDanXiangBean dd = mData.get(pos);
			DingDanXiangBean[] bs = new DingDanXiangBean[]{dd};
			notifyDataChanged(bs);
			mData.remove(pos);
			notifyDataSetChanged();
		}
	}

	
}
