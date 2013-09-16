package cn.leyundong.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.entity.ChangDiJiaGeBean;
import cn.leyundong.entity.ChangDiJiaGeShiJianBean;
import cn.leyundong.entity.DingDanXiangBean;
import cn.leyundong.entity.ShiJianBean;

public class BoxView extends RelativeLayout implements OnClickListener {
	
	public interface OnUserOperationChanged {
		void onItemStateChanged(BoxView bv, Map<String, DingDanXiangBean> dingdanxiang);
	}
	
	private OnUserOperationChanged mStateChanged;
	
	private static final int id_myview = 1000;
	private static final int id_top_sv = 1001;
	private static final int id_letf_sv = 1002;
	
	private static final int size_top_title = 15;
	private static final int size_left_title = 15;
	private static final int size_body = 15;
	
	
	private int width_left_title = 150;
	private int width_top_title = 120;
	private int height_top_title = 80;
	private int height_left_title = 80;
	
	private Context ctx;
	private LinearLayout llBody;
	private LinearLayout llLeft;
	private LinearLayout llTop;

	
	private List<ShiJianBean> topTitles;
	private List<ChangDiJiaGeBean> leftTitles;
	
	private XDTextView[][] items;
	private Set<String> selectionItemPostions = new HashSet<String>();
	
	private Set<XDTextView> mSelectedItems = new HashSet<XDTextView>();
	
	private Map<String, DingDanXiangBean> dingDanXiangMap = new HashMap<String, DingDanXiangBean>();
	
	public BoxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BoxView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		ctx = getContext();
		
		//左上角
		MyView v = new MyView(ctx);
		RelativeLayout.LayoutParams lpMyView = new RelativeLayout.LayoutParams(-2, -2);
		lpMyView.width = width_left_title;
		lpMyView.height = height_top_title;
		addView(v, lpMyView);
		v.setId(id_myview);
		v.setBackgroundResource(R.drawable.shape_1);
		
		//上标题
		SyncHorizonScrollView hsvTop = new SyncHorizonScrollView(ctx);
		hsvTop.setId(id_top_sv);
		hsvTop.setHorizontalScrollBarEnabled(false);
		llTop = new LinearLayout(ctx);
		llTop.setOrientation(0);
		llTop.setGravity(Gravity.CENTER_VERTICAL);
		android.view.ViewGroup.LayoutParams lpTop = new android.view.ViewGroup.LayoutParams(-2 , -2);
		lpTop.height = height_top_title;
		hsvTop.addView(llTop, lpTop);
		RelativeLayout.LayoutParams lpHsvTop = new RelativeLayout.LayoutParams(-2, -2);
		lpHsvTop.addRule(RelativeLayout.RIGHT_OF, id_myview);
		addView(hsvTop, lpHsvTop);
		
		//左标题
		SyncScrollView vsvLeft = new SyncScrollView(ctx);
		vsvLeft.setId(id_letf_sv);
		vsvLeft.setVerticalScrollBarEnabled(false);
		llLeft = new LinearLayout(ctx);
		llLeft.setOrientation(1);
		llLeft.setGravity(Gravity.CENTER_HORIZONTAL);
		android.view.ViewGroup.LayoutParams lpLeft = new android.view.ViewGroup.LayoutParams(-2 , -2);
		lpLeft.width = width_left_title;
		vsvLeft.addView(llLeft, lpLeft);
		RelativeLayout.LayoutParams lpVsvLeft = new RelativeLayout.LayoutParams(-2, -2);
		lpVsvLeft.addRule(RelativeLayout.BELOW, id_myview);
		addView(vsvLeft, lpVsvLeft);
		
		//内容
		SyncScrollView vsvBody = new SyncScrollView(ctx);
		SyncHorizonScrollView hsvBody = new SyncHorizonScrollView(ctx);
		vsvBody.setVerticalScrollBarEnabled(false);
		hsvBody.setHorizontalScrollBarEnabled(false);
		vsvBody.addView(hsvBody);
		llBody = new LinearLayout(ctx);
		llBody.setOrientation(1);
		hsvBody.addView(llBody);
		
		RelativeLayout.LayoutParams lpVsvBody = new RelativeLayout.LayoutParams(-2, -2);
		lpVsvBody.addRule(RelativeLayout.ALIGN_LEFT, id_top_sv);
		lpVsvBody.addRule(RelativeLayout.ALIGN_TOP, id_letf_sv);
		addView(vsvBody, lpVsvBody);
		
		//互相绑定滑动
		hsvTop.setSyncView(hsvBody);
		vsvLeft.setSyncView(vsvBody);
		hsvBody.setSyncView(hsvTop);
		vsvBody.setSyncView(vsvLeft);
		
//		fillData();
	}
	
	public void fillData(List<ShiJianBean> shiJian, List<ChangDiJiaGeBean> changDi) {
		System.out.println("---------------boxview fill data--------------");
//		System.out.println(shiJian);
//		System.out.println(changDi);
		clearData();
		
		if (shiJian.size() == 0 || changDi.size() == 0) {
			return ;
		}
		
		System.out.println("时间列数=" + shiJian.size());
		
		topTitles = shiJian;
		leftTitles = changDi;
		int rows = changDi.size();
		int columns = shiJian.size();
		items = new XDTextView[rows][columns];
		
		setTopTitle(shiJian);
		setLeftTitle(changDi);
	}
	
	
	private void clearData() {
		llTop.removeAllViews();
		llLeft.removeAllViews();
		llBody.removeAllViews();
		topTitles = null;
		leftTitles = null;
		items = null;
		selectionItemPostions = new HashSet<String>();
		mSelectedItems.clear();
	}

	public void setTopTitle(List<ShiJianBean> shiJian) {
		int size = shiJian.size();
		for (int i = 0; i < size; i++) {
			ShiJianBean bean = shiJian.get(i);
			addTopTitle(bean.getYysjdStr());
		}
	}
	
	private void addTopTitle(String title) {
		TextView tv = getTopTextView();
		tv.setText(title);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width_top_title, height_top_title);
		llTop.addView(tv, lp);
	}
	
	
	private TextView getTopTextView() {
		TextView tv = new TextView(ctx);
		tv.setTextSize(size_top_title);
		tv.setGravity(Gravity.CENTER);
		tv.setBackgroundResource(R.drawable.shape_1);
		return tv;
	}
	
	public void setLeftTitle(List<ChangDiJiaGeBean> changDi) {
		int size = changDi.size();
		for (int i = 0; i < size; i++) {
			ChangDiJiaGeBean b = changDi.get(i);
			addLeftTitle(b.cdmc);
			
//			View v = new View(ctx);
//			v.setBackgroundColor(Color.BLACK);
//			llLeft.addView(v, width_left_title, 1);
			
			//
			addRow(i, b.cdsjjgList);
		}
	}
	
	private void addRow(int rowIndex, List<ChangDiJiaGeShiJianBean> infos) {
		if (infos == null) {
			return ;
		}
		System.out.println("第 " + rowIndex + " 行列数=" + infos.size()); 
		int columns = infos.size();
		LinearLayout row = new LinearLayout(ctx);
		row.setOrientation(0);
		llBody.addView(row);
		for (ShiJianBean sj : topTitles) {
			for (int i = 0; i < columns; i++) {
				ChangDiJiaGeShiJianBean b = infos.get(i);
				//确定营业时间点
				if (isEqual(sj.yysjd, b.yysjd)) {
					XDTextView item = new XDTextView(ctx, b);
					item.setOnClickListener(this);
					items[rowIndex][i] = item;
					item.setTag(rowIndex + "," + i);
					
					TableRow.LayoutParams params = new TableRow.LayoutParams(-2, -2);
					System.out.println("是不是1个小时" + b.sfyxs);
					if (b.sfyxs) {
						params.width = 2 * width_top_title;
					} else {
						params.width = width_top_title;
					}
					params.height = height_left_title;
					row.addView(item, params);
					break;
				}
			}
		}
		
	}
	
	private boolean isEqual(double a , double b) {
		if (Math.abs(a - b) < 0.01) {
			return true;
		}
		return false;
	}
	
	private void addLeftTitle(String title) {
		TextView tv = getLeftTextView();
		tv.setText(title);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width_left_title, height_left_title);
		llLeft.addView(tv, lp);
	}
	
	
	private TextView getLeftTextView() {
		TextView tv = new TextView(ctx);
		tv.setTextSize(size_left_title);
		tv.setGravity(Gravity.CENTER);
		tv.setBackgroundResource(R.drawable.shape_1);
		return tv;
	}
	
/*	public void fillData() {
		int ts = 20;
		
		//填充上标题
		for (int i = 0; i < 10; i++) {
			TextView tv = new TextView(ctx);
			tv.setText("上标题" + i);
			tv.setTextSize(ts);
			tv.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width_top_title, height_top_title);
			llTop.addView(tv, lp);
		}
		
		//左标题
		for (int i = 0; i < 20; i++) {
			TextView tv = new TextView(ctx);
			tv.setText("左标题" + i);
			tv.setTextSize(ts);
			tv.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width_left_title, height_left_title);
			llLeft.addView(tv, lp);
		}
		
		//内容
		for (int i = 0; i < 20; i++) {
			TableRow tr = new TableRow(ctx);
			for (int k = 0; k < 10; k++) {
				TextView tv = new TextView(ctx);
				tv.setTextSize(ts);
				tv.setGravity(Gravity.CENTER);
				tv.setText("body" + i + "," + k);
				tv.setGravity(Gravity.CENTER);
				TableRow.LayoutParams lp = new TableRow.LayoutParams(width_top_title, height_left_title);
				tr.addView(tv, lp);
				tv.setOnClickListener(clickL);
				tv.setTag(i + "," + k);
				tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.box_bg));
			}
			llBody.addView(tr);
		}
		
	}
	
	private OnClickListener clickL = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			boolean b = v.isSelected();
			v.setSelected(!b);
			
			
		}
	};*/
	
	public Set<XDTextView> getSelectedItems() {
		return mSelectedItems;
	}
	
	public void setOnUserOperationChangedListener(OnUserOperationChanged l) {
		this.mStateChanged = l;
	}
	
	public Set<String> getSelectedItemPosition() {
		return selectionItemPostions;
	}
	
	public void setItemSelectedPosition(Set<String> d) {
		System.out.println("恢复选择状态-----------" + d);
		for (String p : d) {
			System.out.println("位置=" + p);
			String[] split = p.split(",");
			if (split.length == 2) {
				int row = Integer.valueOf(split[0]);
				int column = Integer.valueOf(split[1]);
				if (items.length > row && items[0].length > column) {
					onClick(items[row][column]);
				}
			}
		}
	}
	
	private Pair<Integer, Integer> parsePosition(String pos) {
		if (pos == null) {
			return null;
		}
		String[] split = pos.split(",");
		if (split.length == 2) {
			int r = Integer.valueOf(split[0]);
			int c = Integer.valueOf(split[1]);
			return new Pair<Integer, Integer>(r, c);
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		XDTextView tv = (XDTextView) v;
		String pos = (String) v.getTag();
		Pair<Integer, Integer> pair = parsePosition(pos);
		if (pair == null) {
			return ;
		}
		tv.isClicked();
		boolean b = v.isSelected();
		if (b) {
			mSelectedItems.add(tv);
			selectionItemPostions.add((String) v.getTag());
			DingDanXiangBean ddx = creatDingDanXiang(pair);
			dingDanXiangMap.put(pos, ddx);
		} else {
			mSelectedItems.remove(v);
			selectionItemPostions.remove(v.getTag());
			dingDanXiangMap.remove(pos);
		}
		
		System.out.println("选中的item个数=" + mSelectedItems.size());
		if (mStateChanged != null) {
			mStateChanged.onItemStateChanged(this, dingDanXiangMap);
		}
	}
	
	public void deselectItem(DingDanXiangBean[] infos) {
		System.out.println("去掉订单项=" + infos);
		if (infos == null || infos.length == 0) {
			return ;
		}
		String pos = null;
		for (DingDanXiangBean b : infos) {
			pos = null;
			Set<Entry<String, DingDanXiangBean>> entrySet = dingDanXiangMap.entrySet();
			for (Entry<String, DingDanXiangBean> entry : entrySet) {
				System.out.println("比较订单项：" + b.hashCode() + ", " + entry.getValue().hashCode());
				if (b.equals(entry.getValue())) {
					pos = entry.getKey();
					break;
				}
			}
			if (pos != null) {
				deselectItemByPosition(pos);
			}
		}
	}
	
	private void deselectItemByPosition(String pos) {
		System.out.println("去掉item位置="+ pos);
		Pair<Integer, Integer> pair = parsePosition(pos);
		if (pair != null) {
			onClick(items[pair.first][pair.second]);
		}
	}
	
	private DingDanXiangBean creatDingDanXiang(Pair<Integer, Integer> pos) {
		DingDanXiangBean b = new DingDanXiangBean();
		ChangDiJiaGeBean cdjg = leftTitles.get(pos.first);
		ChangDiJiaGeShiJianBean sj = cdjg.cdsjjgList.get(pos.second);
		
		b.ydjg = (float) sj.dj;
		b.cdmc = cdjg.cdmc;
//		b.ydsjd = sj.dwsjd;
		b.cdid = cdjg.cdid;
		b.dwsjjgid = sj.dwsjjgid;
		b.dwsjd = sj.dwsjd;
		
		return b;
	}
	
}
