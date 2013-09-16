package cn.leyundong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.activity.BaoMingQingKuanActivity;
import cn.leyundong.entity.HuoDongBean;
import cn.leyundong.util.TextUtil;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

/**
 * 报名页
 * @author Administrator
 *
 */
public class BaoMingAdapter extends AutoAdapter<HuoDongBean> implements OnItemClickListener {
	
	private Context ctx;
	
	public BaoMingAdapter(Context ctx) {
		this.ctx = ctx;
		mData = new ArrayList<HuoDongBean>();
	}
	
	public void addData(List<HuoDongBean> d) {
		mData.addAll(d);
		notifyDataSetChanged();
	}
	
	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_baoming, new ViewHolder());
		}
		//绑定数据
		ViewHolder h = (ViewHolder) view.getTag();
		HuoDongBean b = mData.get(position);
		h.tvTitle.setText(b.hdcg);
		
		
		String title = null;
		title = "俱乐部名称：";
//		h.tvName.setText("俱乐部名称：" + b.jlbmc);
		TextUtil.setText(h.tvName, title + b.jlbmc, title.length());
		
		title = "场地类型：";
//		h.tvType.setText("场地类型：" + b.cdlxmc);
		TextUtil.setText(h.tvType, title + b.cdlxmc, title.length());
		
		//时间
		title = "活动时间：";
//		h.tvTime.setText(b.hdsj);
		TextUtil.setText(h.tvTime, title + b.hdsjd, title.length());
		
		title = "活动价格：";
//		h.tvPrice.setText("活动价格：" + b.hdjg);
		TextUtil.setText(h.tvPrice, title + b.hdjg + "元", title.length());
		
		title = "人数限制：";
//		h.tvMost.setText("限制人数：" + b.rsxz);
		TextUtil.setText(h.tvMost, title + b.rsxz, title.length());
		
		title = "剩余人数：";
//		h.tvLeft.setText("剩余人数：" + b.syrs);
		TextUtil.setText(h.tvLeft, title + b.syrs, title.length());
		
		title = "我报人数：";
//		h.tvMy.setText("我报人数：" + b.wbrs);
		TextUtil.setText(h.tvMy, title + b.wbrs, title.length());
		
		title = "活动状态：";
		h.tvState.setText("活动状态：" + b.hdzt);
		TextUtil.setText(h.tvState, title + b.hdztmc, title.length());
		
		title = "活动收入：";
//		h.tvMoney.setText("活动收入：" + b.hdzsr);
		TextUtil.setText(h.tvMoney, title + b.hdzsr + "元", title.length());
		
		title = "会员属性：";
//		h.tvMember.setText("会员属性：" + b.hysx);
		TextUtil.setText(h.tvMember, title + b.hysx, title.length());
		
		h.btnBaoMing.setTag(position);
		h.btnJiaRu.setTag(position);
		h.btnDetail.setTag(position);
		
		//详情 报名情况
		if (b.yxbmqk) {
			h.btnDetail.setVisibility(0);
		} else {
			h.btnDetail.setVisibility(8);
		}
		
		//报名
		if (b.yxBm && b.sfhy) {
			h.btnBaoMing.setVisibility(0);
		} else {
			h.btnBaoMing.setVisibility(8);
		}
		
		// 加入按钮
		if (b.sfhy) {
			h.btnJiaRu.setVisibility(8);
		} else {
			h.btnJiaRu.setVisibility(0);
		}
		
		return view;
	}

	class ViewHolder extends AbstractViewHolder {
		@ViewRef(id=R.id.tvTitle)TextView tvTitle;
		@ViewRef(id=R.id.tvName)TextView tvName;
		@ViewRef(id=R.id.tvType)TextView tvType;
		@ViewRef(id=R.id.tvTime)TextView tvTime;
		@ViewRef(id=R.id.tvPrice)TextView tvPrice;
		@ViewRef(id=R.id.tvMost)TextView tvMost;
		@ViewRef(id=R.id.tvLeft)TextView tvLeft;
		@ViewRef(id=R.id.tvMy)TextView tvMy;
		@ViewRef(id=R.id.tvState)TextView tvState;
		@ViewRef(id=R.id.tvMoney)TextView tvMoney;
		@ViewRef(id=R.id.tvMember)TextView tvMember;
		@ViewRef(id=R.id.btnJiaRu, onClick="click")Button btnJiaRu;
		@ViewRef(id=R.id.btnBaoMing, onClick="click")Button btnBaoMing;
		@ViewRef(id=R.id.btnDetail, onClick="click")Button btnDetail;
		
		private void click(View v) {
			if (outerClickListener != null) {
				outerClickListener.onClick(v);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int index = position - ((ListView)parent).getHeaderViewsCount();
		HuoDongBean info = mData.get(index);
		Intent intent = new Intent(ctx, BaoMingQingKuanActivity.class);
		intent.putExtra(HuoDongBean.class.getSimpleName(), info);
		ctx.startActivity(intent);
	}

}
