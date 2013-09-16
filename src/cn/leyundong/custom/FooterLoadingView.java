package cn.leyundong.custom;

import cn.leyundong.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FooterLoadingView implements OnClickListener {
	
	private Context ctx;
	
	private TextView tvLoadMore;

	private View view;
	
	public FooterLoadingView(Context ctx) {
		this.ctx = ctx;
		
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.footer_loading, null);
		tvLoadMore = (TextView) view.findViewById(R.id.tvLoadMore); 
		tvLoadMore.setOnClickListener(this);
	}
	
	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		
	}
}
