package cn.leyundong.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class MyDialog extends ProgressDialog {
	
	public Object obj1;

	public MyDialog(Context context) {
		super(context);
	}
	
	
	@Override
	public void onBackPressed() {
	}
	
}
