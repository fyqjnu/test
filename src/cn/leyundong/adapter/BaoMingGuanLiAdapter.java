package cn.leyundong.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.custom.BaoMingXiangView;
import cn.leyundong.entity.BaoMingBean;
import cn.leyundong.util.TextUtil;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class BaoMingGuanLiAdapter extends AutoAdapter<BaoMingBean> implements OnItemClickListener {
	
	private Map<Integer, Integer> visMap = new HashMap<Integer, Integer>();
	
	private Context ctx;
	
	public BaoMingGuanLiAdapter(Context c) {
		this.ctx = c;
		mData = new ArrayList<BaoMingBean>();
	}
	
	public void clear() {
		mData.clear();
		notifyDataSetChanged();
	}
	
	public void addData(List<BaoMingBean> d) {
		mData.addAll(d);
		notifyDataSetChanged();
	}

	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			ViewHold vh = new ViewHold();
			view = createView(ctx, R.layout.item_baomingguanli, vh);
			vh.ddx.setClickListener(outerClickListener);
		}
		ViewHold h = (ViewHold) view.getTag();
		BaoMingBean b = mData.get(position);
//		System.out.println("BaoMingBean=" + b);
		h.tvTitle.setText(b.hdcg);
		
		String title1 = "活动时间：";
//		h.tv1.setText("活动时间：" + b.hdsj);
		TextUtil.setText(h.tv1, title1 + b.hdsjd, title1.length());
		
//		h.tv2.setText("活动价格：" + b.hdjg + "元 " + "报名总费用：" + b.bmzfy + "元");
		title1 = "活动价格：";
		String s1 = title1 + b.hdjg + "元 ";
		String title2 = "报名总费用：";
		String s2 = title2 + b.bmzfy + "元";
		TextUtil.setText(h.tv2, new String[]{s1, s2}, new int[]{title1.length(), title2.length()});
		
		title1 = "报名状态：";
//		h.tv3.setText("报名状态：" + b.bmztmc);
		TextUtil.setText(h.tv3, title1 + b.bmztmc, title1.length());
		
		title1 = "报名时间：";
//		h.tv4.setText("报名时间：" + b.bmsj);
		TextUtil.setText(h.tv4, title1 + b.bmsj, title1.length());
		
		h.ddx.setData(b.bmxBeanList);
		

		h.btnCancel.setTag(position);
		h.btnPay.setTag(position);
		
		List<Button> btns = h.ddx.getButtonByText("取消");
		int i = 0;
//		System.out.println("btns count=" + btns.size());
		for (Button btn : btns) {
			btn.setTag(new Pair<Integer, Integer>(position, i++));
		}
		
		//支付按钮是否要显示
		if (b.yxQdzf) {
			h.btnPay.setVisibility(View.VISIBLE);
		} else {
			h.btnPay.setVisibility(View.GONE);
		}
		
		//取消按钮是否要显示
		if (b.yxQxbm) {
			h.btnCancel.setVisibility(0);
		} else {
			h.btnCancel.setVisibility(View.GONE);
		}
		
		Integer v = visMap.get(position);
		if (v != null && v == 0) {
			h.ddx.setVisibility(0);
		} else {
			h.ddx.setVisibility(View.GONE);
		}
		
		return view;
	}

	class ViewHold extends AbstractViewHolder {
		@ViewRef(id=R.id.tvTitle)TextView tvTitle;
		@ViewRef(id=R.id.tv1)TextView tv1;
		@ViewRef(id=R.id.tv2)TextView tv2;
		@ViewRef(id=R.id.tv3)TextView tv3;
		@ViewRef(id=R.id.tv4)TextView tv4;
		@ViewRef(id=R.id.my)BaoMingXiangView ddx;
		@ViewRef(id=R.id.btnPay, onClick="pay")Button btnPay;
		@ViewRef(id=R.id.btnCancel, onClick="cancel")Button btnCancel;
		
		private void pay(View v) {
			if (outerClickListener != null) {
				outerClickListener.onClick(v);
			}
		}
		
		private void cancel(View v) {
			if (outerClickListener != null) {
				outerClickListener.onClick(v);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListView lv = (ListView) parent;
		int p = position - lv.getHeaderViewsCount();
		ViewHold h = (ViewHold) view.getTag();
		int vis = h.ddx.getVisibility();
		int visTarget = 0;
		if (vis == 0) {
			//正在显示
			visTarget = View.GONE;
		}
		visMap.put(p, visTarget);
		h.ddx.setVisibility(visTarget);
	}

}
