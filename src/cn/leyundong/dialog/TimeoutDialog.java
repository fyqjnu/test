package cn.leyundong.dialog;

import cn.leyundong.MyApplication;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * 超时对话框
 * @author MSSOFT
 *
 */
public class TimeoutDialog extends ProgressDialog {

	//超时
	private int timeout = 10 * 1000;
	
	private long id;
	
	private String text;
	
	public interface OnTaskTimeout {
		void onTimeout(long id);
	}
	
	private OnTaskTimeout timeoutListener;
	
	public void setTimeoutListener(OnTaskTimeout l) {
		timeoutListener = l;
	}
	
	public TimeoutDialog(Context context, int timeout) {
		this(context);
		this.timeout = timeout;
	}
	
	public TimeoutDialog(Context context, String text) {
		this(context);
		this.text = text;
	}
	
	public TimeoutDialog(Context context, String text, int timeout) {
		this(context);
		this.text = text;
		this.timeout = timeout;
	}
	
	public void show(long id) {
		this.id = id;
		super.show();
	}
	
	public TimeoutDialog(Context context) {
		super(context);
		Handler h = MyApplication.instance.handler;
		h.postDelayed(timeoutTask, timeout);
	}
	
	private Runnable timeoutTask = new Runnable() {
		
		@Override
		public void run() {
			if (isShowing()) {
				//还没消失
				dismiss();
				if (timeoutListener != null) {
					timeoutListener.onTimeout(id);
				}
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
}
