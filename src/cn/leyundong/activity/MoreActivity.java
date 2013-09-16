package cn.leyundong.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.constant.Constants;
import cn.leyundong.data.MaBiaoTask;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.LoginTask;
import cn.leyundong.httpoperation.RegisterTask;
import cn.leyundong.interfaces.onLoginSuccess;
import cn.leyundong.interfaces.onRegisterSuccess;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

/**
 * 更多
 * @author chenjunjun
 *
 */
public class MoreActivity extends BaseActivity implements onLoginSuccess, onRegisterSuccess {
	
	static final int id_check_update = 1;

	@ViewRef(id=R.id.header) Header header;
	@ViewRef(id=R.id.tvLogin) TextView tvLogin;
	@ViewRef(id=R.id.btnLogin, onClick="click") TextView btnLogin;
	@ViewRef(id=R.id.vf) ViewFlipper vf;
	@ViewRef(id=R.id.btnLoginOut, onClick="click") TextView btnLoginOut;
	@ViewRef(id=R.id.btnChangePassword, onClick="click") TextView btnChangePassword;
	@ViewRef(id=R.id.btnAbout, onClick="click") TextView btnAbout;
	
	private Dialog aboutDlg;
	private String aboutMsg;
	
	@SuppressWarnings("unused")
	private void click(View v) {
		switch(v.getId()) {
		case R.id.btnLogin:
			if (MyApplication.mUser.isNull()) {
				Intent i = new Intent(act, LoginActivity.class);
				startActivity(i);
			}
			break;
		case R.id.btnLoginOut:
			//退出登录
			doLoginOut();
			break;
		case R.id.btnChangePassword:
			doChangePassword();
			break;
		case R.id.btnAbout:
			//关于
			about();
			break;
		case R.id.btnCheckUpdate:
			doCheckUpdate();
			break;
		}
	}
	
	private void doCheckUpdate() {
		System.out.println("检查更新----------");
	}

	private void about() {
		aboutMsg = MaBiaoTask.getInstance(act).aboutMsg;
		if (aboutMsg != null) {
			if (aboutDlg == null) {
				aboutDlg = new AlertDialog.Builder(act)
				.setTitle("关于")
				.setMessage(aboutMsg)
				.create();
			}
			aboutDlg.show();
		}
	}
	
	/**
	 * 修改密码
	 */
	private void doChangePassword() {
		Intent intent = new Intent(act, ChangePasswordActivity.class);
		startActivity(intent);
	}

	private void doLoginOut() {
		LoginTask.getInstance(act).notifyLoginout();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		
		setContentView(R.layout.more);
		
		header.setTitle("更多");
		if (MyApplication.mUser.isNull()) {
			setupNoLoginState();
		} else {
			setupLoginState();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LoginTask.unregisterLoginSuccessListener(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
	}
	
	/**
	 * 设置登录的状态
	 */
	private void setupLoginState() {
		btnLoginOut.setVisibility(0);
		tvLogin.setText(MyApplication.mUser.yhm);
		btnChangePassword.setVisibility(0);
		vf.setDisplayedChild(0);
	}
	
	/**
	 * 设置未登录状态
	 */
	private void setupNoLoginState() {
		btnLoginOut.setVisibility(8);
		btnChangePassword.setVisibility(8);
		vf.setDisplayedChild(1);
	}
	
	@Override
	public void onRegisterSuccess(YongHuBean user) {
		setupLoginState();
	}

	@Override
	public void onLoginOrRegisterSuccess(YongHuBean user) {
		setupLoginState();
	}

	@Override
	public void onLoginout() {
		setupNoLoginState();
	}
	
}
