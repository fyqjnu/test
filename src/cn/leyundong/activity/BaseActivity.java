package cn.leyundong.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import cn.leyundong.dialog.MyDialog;
import cn.quickdevelp.activity.QBaseActivity;

public class BaseActivity extends QBaseActivity {
	protected String TAG;
	
	protected BaseActivity act;
	
	protected MyDialog dlg;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		act = this;
		TAG = getClass().getName() + ":";
	}
	
	protected void showDialog(String s) {
		if (dlg == null) {
			dlg = new MyDialog(act);
		}
		dlg.setMessage(s);
		dlg.show();
	}
	
	protected void dismissDialog() {
		dlg.dismiss();
		dlg.obj1 = null;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		act = null;
	}
	
	protected void toast(String s) {
		Toast.makeText(act, s, Toast.LENGTH_LONG).show();
	}
	
}