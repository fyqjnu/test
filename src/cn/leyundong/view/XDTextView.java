package cn.leyundong.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.ChangDiJiaGeShiJianBean;

public class XDTextView extends TextView {
	private static final int text_size = 15;

	private ChangDiJiaGeShiJianBean info;
	
	private boolean canSelected;
	
	public XDTextView(Context context, ChangDiJiaGeShiJianBean info) {
		super(context);
		this.info = info;
		init();
	}
	
	private void init() {
		setTextSize(text_size);
		setTextColor(Color.GRAY);
		setGravity(Gravity.CENTER);
		setBackgroundResource(R.drawable.box_bg);
		
		initState();
	}
	
	private void initState() {
//		System.out.println("场地状态=" + info.ydzt);
		switch(info.ydzt) {
		case 1:
			//空闲
			canSelected = true;
			setText("" + info.dj);
			break;
		case 2:
			//被选择
			setText("已被订");
			break;
		case 3:
			//已出售
			setText("已出售");
			break;
		case 4:
			//被长定
			setText("被长定");
			break;
		case 5:
			//未定价
			setText("未定价");
			break;
		}
	}
	
	public boolean canSelected() {
		return canSelected;
	}

	private void onSelected() {
		//用户选中
//		setText("选中");
	}
	
	private void onUnSelected() {
		//用户没有选中
//		setText("" + info.dj);
	}
	
	public ChangDiJiaGeShiJianBean getJiaGeBean() {
		return info;
	}
	
	public void isClicked() {
		if (!canSelected) {
			return ;
		}
		boolean b = !isSelected();
		setSelected(b);
	}
	
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		if (selected) {
			onSelected();
		} else {
			onUnSelected();
		}
	}
	
	
}
