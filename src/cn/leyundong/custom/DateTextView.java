package cn.leyundong.custom;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.SearchManager.OnDismissListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import cn.leyundong.R;
import cn.leyundong.util.DimensionUtil;

public class DateTextView extends TextView implements OnClickListener {
	
	public interface onDateSet {
		void onDateChanged(String riqi);
	}

	private onDateSet listener;
	
	public boolean canSetBefore = true;

	
	public void setOnDateSetListener(onDateSet l) {
		this.listener = l;
		listener.onDateChanged("" + getText());
	}
	
	public DateTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DateTextView(Context context) {
		super(context);
		init();
	}

	@Override
	public void onClick(View v) {
		final Calendar c = Calendar.getInstance();
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH);
		int d = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dlg = new DatePickerDialog(getContext(), new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				System.out.println("year=" + year + ", month=" + monthOfYear + ", day=" + dayOfMonth);
				Calendar ca = Calendar.getInstance();
				ca.set(Calendar.YEAR, year);
				ca.set(Calendar.MONTH, monthOfYear);
				ca.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				if (ca.getTimeInMillis() < c.getTimeInMillis() && !canSetBefore) {
					Toast.makeText(getContext(), "日期不能往后设置", Toast.LENGTH_LONG).show();
				} else {
					setDate(year, monthOfYear + 1, dayOfMonth);
				}
			}
		}, y, m, d);
		dlg.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", onClickListener);
		dlg.setOnCancelListener(onCancelListener);
		dlg.show();
	}
	
	private android.content.DialogInterface.OnClickListener onClickListener = new android.content.DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			setNull();
		}
	};
	
	private void setNull() {
		System.out.println("设置日期无限");
		int width = getWidth();
		int height = getHeight();
		LayoutParams lp = getLayoutParams();
		lp.width = width;
		lp.height = height;
		setText("");
	}
	
	private OnCancelListener onCancelListener = new OnCancelListener() {
		
		@Override
		public void onCancel(DialogInterface dialog) {
			setNull();
		}
	};
	
	private void setDate(int y, int m, int d) {
		StringBuilder sb = new StringBuilder();
		sb.append(y).append("-").append(m).append("-").append(d);
		String s = sb.toString();
		setText(s);
		
		if (listener != null) {
			listener.onDateChanged(s);
		}
	}
	
	public String getDate() {
		return "" + getText();
	}
	
	private void init() {
		setTextColor(Color.BLACK);
		int p = DimensionUtil.dip2px(getContext(), 5);
		setPadding(p, p, p, p);
//		Drawable d = getResources().getDrawable(R.drawable.time_bg);
		setBackgroundResource(R.drawable.time_bg);
		
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		
		final Calendar c = Calendar.getInstance();
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH);
		int d = c.get(Calendar.DAY_OF_MONTH);
		setDate(y, m + 1, d);
		setOnClickListener(this);
	}
	
	
}
