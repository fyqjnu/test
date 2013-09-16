package cn.leyundong.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.custom.DingDanXiangView;
import cn.leyundong.entity.DingDanBean;
import cn.leyundong.util.TextUtil;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class DingDanGuanLiAdapter extends AutoAdapter<DingDanBean> implements OnItemClickListener {
	private Context ctx;
	
	private Map<Integer, Integer> mViewVisiability = new HashMap<Integer, Integer>();
	
	private OnClickListener outClickListener;
	
	public DingDanGuanLiAdapter(Context ctx) {
		this.ctx = ctx;
	}
	
	public void removeItem(int pos) {
		mData.remove(pos);
		notifyDataSetChanged();
	}
	
	public void removeDDX(int pos, int subPos) {
		DingDanBean dd = mData.get(pos);
		dd.ddxList.remove(subPos);
		notifyDataSetChanged();
	}
	
	public void addData(List<DingDanBean> d) {
		System.out.println("订单项数据=" + d);
		if (mData == null) {
			mData = new ArrayList<DingDanBean>();
		}
		mData.addAll(d);
		notifyDataSetChanged();
	}
	
	public void setClickListener(OnClickListener l) {
		this.outClickListener = l;
	}
	
	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_dingdanguanli, new ViewHolder());
		}
		ViewHolder h = (ViewHolder) view.getTag();
		DingDanBean info = mData.get(position);
		h.btnCancelAll.setTag(position);
		h.tvTitle.setText(info.cgmc);
		
		String title = "预订总费用（元）：";
//		h.tvMoney.setText("预订总费用（元）：" + info.ydzfy);
		TextUtil.setText(h.tvMoney, title + info.ydzfy, title.length());
		
		title = "预订日期:";
//		h.tvDate.setText("预订日期:" + info.ydrq);
		TextUtil.setText(h.tvDate, title + info.ydrq, title.length());
		
		title = "订单状态：";
//		h.tvState.setText("订单状态：" + info.ddztmc);
		TextUtil.setText(h.tvState, title + info.ddztmc, title.length());
		
		title = "订单时间：";
		h.tvTime.setText("订单时间：" + info.xdsj);
		TextUtil.setText(h.tvTime, title + info.xdsj, title.length());
		
		title = "运动类型：";
//		h.tvType.setText("运动类型：" + info.cdlxmc);
		TextUtil.setText(h.tvType, title + info.cdlxmc, title.length());
		
		h.dingDanXian.setData(info.ddxList);
		h.dingDanXian.setClickListener(outClickListener);
		h.btnCancelAll.setTag("0:" + position);
		
		if (info.YxQxDd) {
			h.btnCancelAll.setVisibility(0);
		} else {
			h.btnCancelAll.setVisibility(8);
		}
		
		if (isItemVisible(position)) {
			h.dingDanXian.setVisibility(0);
		} else {
			h.dingDanXian.setVisibility(8);
		}
		
		List<Button> btns = h.dingDanXian.getButtonByText("取消");
		int size = btns.size();
		for (int i = 0; i < size; i++) {
			btns.get(i).setTag("1:" + position + "," + i);
		}
		
		return view;
	}
	
	private boolean isItemVisible(int position) {
		Integer state = mViewVisiability.get(position);
		if (state != null && state == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int p, long arg3) {
		System.out.println(p);
		ViewHolder h = (ViewHolder) view.getTag();
		ListView lv = (ListView) arg0;
		int index = p - lv.getHeaderViewsCount();
		boolean v = isItemVisible(index);
		Integer target = 0;
		if (v) {
			target = 8;
		} else {
			target = 0;
		}
		h.dingDanXian.setVisibility(target);
		mViewVisiability.put(index, target);
	}
	
	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvTitle) TextView tvTitle;
		@ViewRef(id=R.id.tvDate) TextView tvDate;
		@ViewRef(id=R.id.tvTime) TextView tvTime;
		@ViewRef(id=R.id.tvState) TextView tvState;
		@ViewRef(id=R.id.tvMoney) TextView tvMoney;
		@ViewRef(id=R.id.tvType) TextView tvType;
		@ViewRef(id=R.id.btnCancelAll, onClick="cancelDD") Button btnCancelAll;
		@ViewRef(id=R.id.dingDanXian) DingDanXiangView dingDanXian;
		
		
		//取消订单
		private void cancelDD(View v) {
			System.out.println(v.getTag());
			if (outClickListener != null) {
				outClickListener.onClick(v);
			}
		}
	}


}
