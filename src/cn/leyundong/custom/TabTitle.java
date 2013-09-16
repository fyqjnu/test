package cn.leyundong.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.util.DimensionUtil;

public class TabTitle extends LinearLayout implements OnClickListener {
	
	private static final int HEIGHT_HEADER_DP = 42;
	
	public interface OnTabSelectedListener {
		void onTabSelected(int which, String title);
	}

	private OnTabSelectedListener listener;
	
	private String[] titles;
	private TextView[] tvs;
	
	private int currentTab = -1;

	public TabTitle(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TabTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TabTitle(Context context) {
		super(context);
	}
	
	public void setOnTabSelectedListener(OnTabSelectedListener l) {
		this.listener = l;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), DimensionUtil.dip2px(getContext(), HEIGHT_HEADER_DP));
	}
	
	public void setTitles(String[] titles, int firstSelectedPos) {
		setBackgroundColor(Color.GRAY);

		int p = firstSelectedPos;
		
		int size = titles.length;
		if (p < 0 || p >= size) {
			p = 0;
		}
		this.titles = titles;
		tvs = new TextView[size];
		
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		
		for (int i = 0; i < size; i++) {
			TextView t = new TextView(getContext());
			tvs[i] = t;
			t.setText(titles[i]);
			t.setTextSize(15);
			t.setGravity(Gravity.CENTER);
			t.setTextColor(getResources().getColorStateList(R.color.tab_title_font));
			t.setBackgroundResource(R.drawable.tab_title_bg);
			t.setOnClickListener(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1);
			lp.gravity = Gravity.CENTER;
			lp.height = DimensionUtil.dip2px(getContext(), HEIGHT_HEADER_DP);
			lp.weight = 1;
			addView(tvs[i], lp);
			if (i != size - 1) {
				//添加一竖线
				View line = new View(getContext());
				line.setBackgroundColor(Color.BLACK);
				LinearLayout.LayoutParams lpLine = new LinearLayout.LayoutParams(-2, -2);
				lpLine.width = 1;
				lpLine.height = DimensionUtil.dip2px(getContext(), HEIGHT_HEADER_DP);
				addView(line, lpLine);
			}
		}
		
		onClick(tvs[p]);
	}

	@Override
	public void onClick(View v) {
		System.out.println(v);
		int which = 0;
		int index = 0;
		for (TextView t : tvs) {
			t.setSelected(false);
			if (t == v) {
				which = index;
			}
			index++;
		}
		v.setSelected(true);
		
		if (which == currentTab) {
			return ;
		}
		currentTab = which;
		
		if (listener != null) {
			listener.onTabSelected(which, titles[which]);
		}
	}
	
	public void setCurrentTab(int pos) {
		if (pos < 0 || pos >= tvs.length) {
			return ;
		}
		onClick(tvs[pos]);
	}
	
	public void setCurrentTab(String title) {
		int pos = -1;
		for(int i = 0; i < titles.length; i++) {
			if (title.equals(titles[i])) {
				pos = i;
				break ;
			}
		}
		setCurrentTab(pos);
	}
	

}
