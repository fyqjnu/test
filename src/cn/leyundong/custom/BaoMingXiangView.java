package cn.leyundong.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.BaoMingXiangBean;

public class BaoMingXiangView extends AbstractItemView<BaoMingXiangBean> {
	
	
	public BaoMingXiangView(Context context) {
		super(context);
	}

	public BaoMingXiangView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void bindData(BaoMingXiangBean b, AbstractItem item) {
		Item it = (Item) item;

		it.tv1.setText(b.hdcg);
		it.tv2.setText(b.hdjg + "元");
		it.tv3.setText(b.bmxztmc);
		it.tv4.setText(b.hdsjd);
		
		
		if (b.yxQxbm) {
			it.btn.setVisibility(0);
		} else {
			it.btn.setVisibility(View.GONE);
		}
	}
	
	class Item extends AbstractItem {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public Button btn;
	}
	@Override
	public AbstractItem createItem() {
		Item item = new Item();
		LinearLayout ll = new LinearLayout(getContext());
		ll.setGravity(Gravity.CENTER_VERTICAL);
		ll.setOrientation(0);
		TextView tv1 = createTextView();
		LinearLayout.LayoutParams lp1 = new LayoutParams(-2, -2);
		lp1.weight = 1;
		ll.addView(tv1, lp1);
		item.tv1 = tv1;
		
		TextView tv2 = createTextView();
		LinearLayout.LayoutParams lp2 = new LayoutParams(-2, -2);
		lp2.weight = 1;
		ll.addView(tv2, lp2);
		item.tv2 = tv2;
		
		TextView tv3 = createTextView();
		LinearLayout.LayoutParams lp3 = new LayoutParams(-2, -2);
		lp3.weight = 1;
		ll.addView(tv3, lp3);
		item.tv3 = tv3;
		
		TextView tv4 = createTextView();
		LinearLayout.LayoutParams lp4 = new LayoutParams(-2, -2);
		lp4.weight = 1;
		ll.addView(tv4, lp4);
		item.tv4 = tv4;
		
		Button btn = new Button(getContext());
		btn.setText("取消");
		btn.setId(R.id.id_btn_cancel);
		btn.setOnClickListener(this);
		ll.addView(btn);
		item.view = ll;
		item.btn = btn;
		return item;
	}

	private TextView createTextView() {
		TextView tv = new TextView(getContext());
		return tv;
	}
}
