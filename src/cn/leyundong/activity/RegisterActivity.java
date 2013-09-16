package cn.leyundong.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.leyundong.R;
import cn.leyundong.constant.Regulars;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.RegisterTask;
import cn.leyundong.json.JsonParser;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

/**
 * 注册页面
 * @author MSSOFT
 *
 */
public class RegisterActivity extends BaseActivity {
	
	private String mPhoneNum;
	
	@ViewRef(id=R.id.etUserName) EditText etUserName;
	@ViewRef(id=R.id.etMail) EditText etMail;
	@ViewRef(id=R.id.etMiMa) EditText etMiMa;
	@ViewRef(id=R.id.etQueRenMiMa) EditText etQueRenMiMa;
	@ViewRef(id=R.id.etYanZhengMa) EditText etYanZhenMa;
	
	@ViewRef(id=R.id.header) Header header;
	
	@ViewRef(id=R.id.btnRegister,onClick="onClick") Button btnRegister;
	
	private void onClick(View v) {
		doRegist();
	}
	
	private void doRegist() {
		String name = etUserName.getEditableText().toString();
		if (TextUtils.isEmpty(name)) {
			toast("用户名不能为空");
			return ;
		}
		if (!name.matches(Regulars.REG_NAME)) {
			toast("用户名只能是汉字，英文字母或者数字下划线");
			return ;
		}
		
		String mail = etMail.getEditableText().toString();
		if (TextUtils.isEmpty(mail)) {
			toast("邮箱不能为空");
			return ;
		}
		if (!mail.matches(Regulars.MAIL)) {
			toast("邮箱格式不正确");
			return ;
		}
		
		String pw = etMiMa.getEditableText().toString();
		if (TextUtils.isEmpty(pw)) {
			toast("密码不能为空");
			return ;
		}
		if (!pw.matches(Regulars.PASSWORD)) {
			toast("密码必须6位任意字符");
			return ;
		}
		String pw2 = etQueRenMiMa.getEditableText().toString();
		if (!pw.equals(pw2)) {
			toast("两次密码输入不一致");
			return ;
		}
		
		String num = etYanZhenMa.getEditableText().toString();
		if (TextUtils.isEmpty(num)) {
			toast("请输入验证码");
			return ;
		}
		
		YongHuBean b = new YongHuBean();
		b.yhm = name;
		b.yhmm = pw;
		b.fslx = 1;
		b.yx = mail;
		b.dxyzm = num;
		b.sjhm = mPhoneNum;
		
		RegisterTask task = RegisterTask.getInstance(act);
		task.setUser(b);
		executeTask(R.id.btnRegister, task);
		showDialog("正在注册，请稍候...");
		
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
		dismissDialog();
		boolean success = false;
		if (result != null) {
			Result res = JsonParser.getResultFromJson((String) result);
			success = res.success;
		}
		if (success) {
			onRegisterSuccess();
			toast("注册成功");
		} else {
			toast("注册失败");
		}
	}
	
	private void onRegisterSuccess() {
		setResult(RESULT_OK);
		finish();
	}
	
	
	/*class RegisterTask implements IDataRequest {
		YongHuBean user;
		
		public RegisterTask(YongHuBean b) {
			user = b;
		}

		@Override
		public Object doRequest() {
			String url = Urls.getUrl(2, 1);
			ArrayList<IJson> list = new ArrayList<IJson>();
			list.add(user);
			IHttpDuty duty = new NetCheckHttpDuty(act, new HttpPostDuty(act, url, list));
			return duty.post();
		}
		
	}
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPhoneNum = getIntent().getStringExtra("phone");
		System.out.println("传递过来手机号码=" + mPhoneNum);
		setContentView(R.layout.register);
		
		header.setTitle("注册");
	}
}
