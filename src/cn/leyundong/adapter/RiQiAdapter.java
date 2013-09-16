package cn.leyundong.adapter;

import java.util.List;

import cn.leyundong.R;
import cn.leyundong.entity.RiQiBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RiQiAdapter extends BaseAdapter {
	
	private Context ctx;
	
	private List<RiQiBean> mData;
	
	public RiQiAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public void setData(List<RiQiBean> l) {
		mData = l;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView();
		}
		ViewHolder h = (ViewHolder) view.getTag();
		RiQiBean info = mData.get(position);
		h.tv1.setText(info.dqxq);
		h.tv2.setText(info.dqrq);
		
		return view;
	}
	
	private View createView() {
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_riqi, null);
		ViewHolder h = new ViewHolder();
		h.tv1 = (TextView) view.findViewById(R.id.tv1);
		h.tv2 = (TextView) view.findViewById(R.id.tv2);
		view.setTag(h);
		return view;
	}
	
	class ViewHolder {
		TextView tv1;
		TextView tv2;
		
	}

}
