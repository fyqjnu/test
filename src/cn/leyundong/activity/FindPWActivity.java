package cn.leyundong.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;
import cn.leyundong.R;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.YanZhengMaTask;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.http.NetCheckHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

public class FindPWActivity extends BaseActivity {
	
	private YongHuBean user = new YongHuBean();
	
	@ViewRef(id=R.id.vfLayout) ViewFlipper vfLayout;
	
	@ViewRef(id=R.id.header) Header header;
	@ViewRef(id=R.id.etUserName) EditText etUserName;
	@ViewRef(id=R.id.btnNext, onClick="onClick") Button btnNext;
	
	@ViewRef(id=R.id.etYanZhengMa) EditText etYanZhengMa;
	@ViewRef(id=R.id.etPW) EditText etPW;
	@ViewRef(id=R.id.etPWAgain) EditText etPWAgain;
	@ViewRef(id=R.id.btnOk, onClick="onClick") Button btnOk;
	
	private void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnNext:
			String name = etUserName.getEditableText().toString();
			if (TextUtils.isEmpty(name)) {
				toast("请输入用户名");
				return ;
			}
			System.out.println("name=" + name);
			//用户名
			user.yhm = name;
			System.out.println(vfLayout);
			int count = vfLayout.getChildCount();
			System.out.println("count=" + count);
			//发送类型
			user.fslx = 1;
			YanZhengMaTask task = new YanZhengMaTask(act, user);
			executeTask(R.id.btnNext, task);
			vfLayout.setDisplayedChild(1);
			System.out.println("aaaaaaaaaaaaa");
		break;
		case R.id.btnOk:
			String yzm = etYanZhengMa.getEditableText().toString();
			if (TextUtils.isEmpty(yzm)) {
				toast("请输入验证码");
				return ;
			}
			String pw = etPW.getEditableText().toString();
			if (TextUtils.isEmpty(pw)) {
				toast("请输入新密码");
				return ;
			}
			String pw2 = etPWAgain.getEditableText().toString();
			if (!pw.equals(pw2)) {
				toast("两次密码不一致");
				return ;
			}
			user.dxyzm = yzm;
			user.yhmm = pw;
			ResetPWTask task2 = new ResetPWTask(user);
			executeTask(R.id.btnOk, task2);
			break;
		}
		
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		System.out.println("result=" + result);
	}
	
	
	class ResetPWTask implements IDataRequest {
		
		YongHuBean user;
		
		public ResetPWTask(YongHuBean b) {
			user = b;
		}
		
		@Override
		public Object doRequest() {
			String url = Urls.getUrl(Urls.TYPE_GERENZHONGXIN, RequestId.PASSWORD);
			ArrayList<IJson> list = new ArrayList<IJson>();
			list.add(new BeanProxy<YongHuBean>(user));
			IHttpDuty duty = new NetCheckHttpDuty(act, new HttpPostDuty(act, url, list));
			return duty.post();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.findpw);
		header.setTitle("找回密码");
	}
	
}
