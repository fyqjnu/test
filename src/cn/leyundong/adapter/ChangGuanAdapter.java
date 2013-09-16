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
import cn.leyundong.activity.ChangGuanDetailActivity;
import cn.leyundong.activity.yudingpage.YuDingTaskActivity;
import cn.leyundong.entity.ChangGuanBean;
import cn.leyundong.util.TextUtil;
import cn.quickdevelp.adapter.AbstractViewHolder;
import cn.quickdevelp.anno.ViewRef;

public class ChangGuanAdapter extends AutoAdapter<ChangGuanBean> implements OnItemClickListener {
	
	private Context ctx;
	
	private String cdlx;
	
	public ChangGuanAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public void clearData() {
		if (mData != null) {
			mData.clear();
		}
	}
	
	public void setCdlx(String s) {
		cdlx = s;
	}
	

	public void addData(List<ChangGuanBean> data) {
		if (mData == null) {
			mData = new ArrayList<ChangGuanBean>();
		}
		mData.addAll(data);
		notifyDataSetChanged();
	}
	
	public void setData(List<ChangGuanBean> data) {
		mData = data;
		notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int p,
			long id) {
		ListView lv = (ListView) parent;
		int hc = lv.getHeaderViewsCount();
		System.out.println("header view count = " + hc);
		int position = p - hc;
		System.out.println("p = " + position);
		ChangGuanBean info = mData.get(position);
		Intent i = new Intent();
		i.setClass(ctx, ChangGuanDetailActivity.class);
		i.putExtra(ChangGuanBean.class.getName(), info);
		i.putExtra("cdlx", cdlx);
		ctx.startActivity(i);
	}
	
	@Override
	protected View getAutoView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(ctx, R.layout.item_changguan, new ViewHolder());
		}
		ChangGuanBean b = mData.get(position);
		ViewHolder h = (ViewHolder) view.getTag();
		h.btn1.setTag(position);
		h.tv1.setText(b.cgmc);
		
		String title = "当天空场率:";
//		h.tv2.setText("当天空场率:" + b.dtkcl + "%");
		TextUtil.setText(h.tv2, title + b.dtkcl + "%", title.length());
		
		title = "营业时间：";
		h.tv3.setText("营业时间：" + b.yysj);
		TextUtil.setText(h.tv3, title + b.yysj, title.length());
		
		title = "场馆地址：";
//		h.tv4.setText("场馆地址：" + b.cgdz);
		TextUtil.setText(h.tv4, title + b.cgdz, title.length());
		
		return view;
	}

	class ViewHolder extends AbstractViewHolder {
		//标题
		@ViewRef(id=R.id.tv1) TextView tv1; 
		//空场率
		@ViewRef(id=R.id.tv2) TextView tv2; 
		//营业时间
		@ViewRef(id=R.id.tv3) TextView tv3; 
		//地址
		@ViewRef(id=R.id.tv4) TextView tv4; 
		//预订
		@ViewRef(id=R.id.btn1,onClick="onClick") Button btn1; 
		
		public void onClick(View v) {
			Object tag = v.getTag();
			System.out.println("tag=" + tag);
			if (tag instanceof Integer) {
				int p = (Integer) tag;
				ChangGuanBean info = mData.get(p);
				Intent i = new Intent(ctx, YuDingTaskActivity.class);
				i.putExtra(ChangGuanBean.class.getSimpleName(), info);
				i.putExtra("cdlx", cdlx);
				ctx.startActivity(i);
			}
		}
	}

}
