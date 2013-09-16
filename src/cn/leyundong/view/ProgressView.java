package cn.leyundong.view;

import cn.leyundong.R;
import cn.leyundong.interfaces.IRefresh;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.anno.ViewRefBinder;
import cn.quickdevelp.interfaces.IFindViewById;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ProgressView extends LinearLayout implements IFindViewById {
	
	private IRefresh mIRefresh;
	
	@ViewRef(id=R.id.vf) ViewFlipper vf;
	@ViewRef(id=R.id.tvTip, onClick="onClick") TextView tvTip;
	@ViewRef(id=R.id.tvEmpty) TextView tvEmpty;
	
	private void onClick(View v) {
		if (mIRefresh != null) {
			mIRefresh.doRefresh();
		}
	}
	
	public void setRefresh(IRefresh r) {
		this.mIRefresh = r;
	}


	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public ProgressView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
		init();
	}

	public ProgressView(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.progress, null);
		addView(view, -1, -1);
		new ViewRefBinder(this).init();
	}
	
	public void displayProgress() {
		vf.setDisplayedChild(1);
	}

	public void displayRefresh() {
		vf.setDisplayedChild(0);
	}
	
	public void displayEmpty() {
		//显示空数据
		vf.setDisplayedChild(2);
	}
	
	public void setEmptyText(String s) {
		tvEmpty.setText(s);
	}
	
	@Override
	public View getViewById(int id) {
		return findViewById(id);
	}
}
