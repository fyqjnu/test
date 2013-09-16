package cn.leyundong.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.util.DimensionUtil;

public class Header extends LinearLayout {
	private static final int HEIGHT_HEADER_DP = 60;
	private Context ctx;
	private TextView title;
	private RelativeLayout layout;
	
	public Header(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		init();
	}

	public Header(Context context) {
		super(context);
		ctx = context;
		init();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), DimensionUtil.dip2px(ctx, HEIGHT_HEADER_DP));
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}
	
	private void init() {
		
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (RelativeLayout) inflater.inflate(R.layout.header, null);
		title = (TextView) layout.findViewById(R.id.title);
		addView(layout, -1, DimensionUtil.dip2px(ctx, HEIGHT_HEADER_DP));
	}
	
	public void setTitle(String s) {
		title.setText(s);
	}
	
	public void removeTitle() {
		layout.removeView(title);
	}
	
	public void addComponent(View v, RelativeLayout.LayoutParams lp) {
		if (lp == null) {
			lp = new RelativeLayout.LayoutParams(-1, -1);
		}
		layout.addView(v, lp);
		System.out.println("add component-----------");
	}
	
}
