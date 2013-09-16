package cn.leyundong.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.constant.Constants;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.LoginTask;
import cn.leyundong.json.JsonParser;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.json.JsonUtil;

public class LoginActivity extends BaseActivity {
	
	private static final int REQUESTCODE_REGISTER = 0;
	private static final int REQUESTCODE_FINDPW = 1;
	@ViewRef(id=R.id.btnLogin,onClick="onClick") Button btnLogin;
	@ViewRef(id=R.id.etAccount) EditText etAccount;
	@ViewRef(id=R.id.etMiMa) EditText etMiMa;
	@ViewRef(id=R.id.header) Header header;
	@ViewRef(id=R.id.cbAutoLogin) CheckBox cbAutoLogin;
	@ViewRef(id=R.id.tvFindPW,onClick="onClick") TextView tvFindPW;
	@ViewRef(id=R.id.tvRegister,onClick="onClick") TextView tvRegister;
	
	private void onClick(View v) {
		Intent i = new Intent();
		switch(v.getId()) {
		case R.id.btnLogin:
			doLogin();
			break;
		case R.id.tvFindPW:
			i.setClass(act, FindPWActivity.class);
			startActivityForResult(i, REQUESTCODE_FINDPW);
			break;
		case R.id.tvRegister:
			i.setClass(act, YanZhengMaActivity.class);
			startActivityForResult(i, REQUESTCODE_REGISTER);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case REQUESTCODE_REGISTER:
			if (resultCode == RESULT_OK) {
				finish();
			}
			break;
		}
	}

	private void doLogin() {
		String account = etAccount.getEditableText().toString();
		if (TextUtils.isEmpty(account)) {
			toast("请输入账号");
			return ;
		}
		String pw = etMiMa.getEditableText().toString();
		if (TextUtils.isEmpty(pw)) {
			toast("请输入密码");
			return ;
		}
		
		YongHuBean user = new YongHuBean();
		user.yhm = account;
		user.yhmm = pw;
		showDialog("正在登录，请稍候...");
//		LoginTask task = new LoginTask(act, user, cbAutoLogin.isChecked());
		LoginTask task = LoginTask.getInstance(act);
		task.setAutoLoginNextTime(cbAutoLogin.isChecked());
		task.setUser(user);
		executeTask(R.id.btnLogin, task);
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		dismissDialog();
		Result rs = JsonParser.getResultFromJson((String) result);
		if (rs.success) {
			//成功
			toast("登录成功");
			finish();
		} else {
			toast("登录失败: " + rs.error);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
		
		header.setTitle("登录");
		
		fillUserName();
	}
	
	private void fillUserName() {
		/*String json = MyApplication.sp.getString(Constants.XML_KEY_USER, null);
		System.out.println("填充用户信息=" + json);
		if (json != null) {
			YongHuBean user = null;
			Map<String, Class<?>> map = new HashMap<String, Class<?>>();
			map.put(YongHuBean.class.getSimpleName(), YongHuBean.class);
			Map<String, Object> ret = JsonParser.parseJsonMap(json, map);
			user = (YongHuBean) ret.get(YongHuBean.class.getSimpleName());
			System.out.println("user=" + user);
			if (user != null) {
				etAccount.setText(user.yhm);
				etMiMa.setText(user.yhmm);
			}
		}*/
		if (!MyApplication.mUser.isNull()) {
			etAccount.setText(MyApplication.mUser.yhm);
			etMiMa.setText(MyApplication.mUser.yhmm);
		} else if (!MyApplication.tmpUser.isNull()) {
			etAccount.setText(MyApplication.tmpUser.yhm);
			etMiMa.setText(MyApplication.tmpUser.yhmm);
		}
	}
	
}
