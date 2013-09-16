package cn.leyundong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.constant.Regulars;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.YanZhengMaTask;
import cn.leyundong.json.JsonParser;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

/**
 * 获取验证码
 * @author MSSOFT
 *
 */
public class YanZhengMaActivity extends BaseActivity implements OnCheckedChangeListener {
	
	@ViewRef(id=R.id.etNumber) EditText etNumber;
	@ViewRef(id=R.id.cbProtocal) CheckBox cbProtocal;
	@ViewRef(id=R.id.ok, onClick="onClick") Button btnSend;
	
	@ViewRef(id=R.id.header) Header header;
	
	private void onClick(View v) {
		switch(v.getId()) {
		case R.id.ok:
			String num = etNumber.getEditableText().toString();
			if (TextUtils.isEmpty(num)) {
				toast("请输入手机号码");
				return ;
			}
			if (!num.matches(Regulars.PHONE_NUMBER)) {
				toast("手机号码格式不正确");
				return ;
			}
			
			YongHuBean user = new YongHuBean();
			user.sjhm = num;
			//1为注册发送类型
			user.fslx = 1;
			//城市代码
			user.csdm = MyApplication.mCity.getCsdm();
			YanZhengMaTask task = new YanZhengMaTask(act, user);
			executeTask(R.id.ok, task);
			
			Intent i = new Intent(act, RegisterActivity.class);
			i.putExtra("phone", num);
			startActivityForResult(i, 0);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			//注册成功 
			setResult(RESULT_OK);
			finish();
		}
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		Result rs = JsonParser.getResultFromJson((String) result);
		if (rs.success) {
			//成功
		} else {
			//失败
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.yangzhengma);
		cbProtocal.setOnCheckedChangeListener(this);
		
		header.setTitle("免费注册");
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			btnSend.setClickable(true);
		} else {
			btnSend.setClickable(false);
		}
	}
}
