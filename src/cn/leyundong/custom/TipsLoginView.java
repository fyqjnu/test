package cn.leyundong.custom;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.activity.LoginActivity;

/**
 * 提示用户登录
 * @author MSSOFT
 *
 */
public class TipsLoginView extends LinearLayout implements OnClickListener {

	private TextView tvLogin;

	public TipsLoginView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	public TipsLoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TipsLoginView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.tips_login, null);
		tvLogin = (TextView) view.findViewById(R.id.tvLogin);
		tvLogin.setOnClickListener(this);
		addView(view, -1, -1);
	}
	
	public void setTipText(String s) {
		tvLogin.setText(s + "\n点击登录");
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getContext(), LoginActivity.class);
		getContext().startActivity(intent);
	}
}
