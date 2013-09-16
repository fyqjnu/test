package cn.leyundong.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.leyundong.R;
import cn.quickdevelp.anno.ViewRef;

public class ModifyPWActivity extends BaseActivity {
	
	@ViewRef(id=R.id.etOldPW) EditText etOldPW;
	@ViewRef(id=R.id.etNewPW) EditText etNewPW;
	@ViewRef(id=R.id.etNewPW2) EditText etNewPW2;
	@ViewRef(id=R.id.btnOk, onClick="onClick") Button btnOk;
	
	private void onClick(View v) {
		String oldPW = etOldPW.getEditableText().toString();
		if (TextUtils.isEmpty(oldPW)) {
			return ;
		}
		
		String newPW = etNewPW.getEditableText().toString();
		if (TextUtils.isEmpty(newPW)) {
			return ;
		}
		if (!newPW.equals(etNewPW2.getEditableText().toString())) {
			return ;
		}
		
		
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.modifypw);
	}
}
