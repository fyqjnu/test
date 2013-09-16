package cn.leyundong.custom;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.leyundong.util.DimensionUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public abstract class AbstractItemView<T> extends LinearLayout implements
		OnClickListener {

	private Set<AbstractItem> cache = new HashSet<AbstractItem>();

	private List<AbstractItem> items = new ArrayList<AbstractItem>();

	protected OnClickListener clickListener;

	public AbstractItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AbstractItemView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setOrientation(1);
		int p = DimensionUtil.dip2px(getContext(), 5);
		setPadding(0, p, 0, p);
	}

	public void setClickListener(OnClickListener l) {
		this.clickListener = l;
	}

	@Override
	public void onClick(View v) {
		System.out.println("点击按钮-------------");
		if (clickListener != null) {
			clickListener.onClick(v);
		}
	}

	public void setData(List<T> data) {
		removeAllViews();
		items.clear();
		for (AbstractItem item : cache) {
			item.isUsed = false;
		}
		if (data == null || data.size() == 0) {
			return;
		}
		for (T t : data) {
			AbstractItem item = getItem();
			items.add(item);
			addView(item.view, -1, -2);
			bindData(t, item);
		}
	}

	public List<Button> getButtonByText(String text) {
		List<Button> btns = new ArrayList<Button>();
		try {
			for (AbstractItem item : items) {
				Class<? extends AbstractItem> clz = item.getClass();
				Field[] fs = clz.getFields();
				for (Field f : fs) {
					f.setAccessible(true);
					Class<?> type = f.getType();
					if (type.equals(Button.class)) {
						Button b;
						b = (Button) f.get(item);
						if (text.equals(b.getText())) {
							btns.add(b);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return btns;
	}

	public abstract void bindData(T t, AbstractItem item);

	private AbstractItem getItem() {
		AbstractItem item = null;
		for (AbstractItem i : cache) {
			if (!i.isUsed) {
				item = i;
				break;
			}
		}
		if (item == null) {
			item = createItem();
		}
		cache.add(item);
		item.isUsed = true;
		return item;
	}

	public abstract AbstractItem createItem();

}
