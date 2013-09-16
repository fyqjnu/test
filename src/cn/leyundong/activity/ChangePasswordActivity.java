package cn.leyundong.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.renderscript.Program.TextureType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.constant.Constants;
import cn.leyundong.constant.Urls;
import cn.leyundong.dialog.MyDialog;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.Result;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.SingleParamTask;
import cn.leyundong.json.JsonParser;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.json.JsonUtil;

/**
 * 修改密码
 * @author Administrator
 *
 */
public class ChangePasswordActivity extends BaseActivity {

	
	@ViewRef(id=R.id.header)Header header;
	@ViewRef(id=R.id.et1)EditText et1;
	@ViewRef(id=R.id.et2)EditText et2;
	@ViewRef(id=R.id.et3)EditText et3;
	@ViewRef(id=R.id.btnOk, onClick="click")Button btnOk;
	
	@SuppressWarnings("unused")
	private void click(View v) {
		doChangePassword();
	}
	
	private void doChangePassword() {
		String oldPW = et1.getEditableText().toString();
		String newPW = et2.getEditableText().toString();
		String newPW2 = et3.getEditableText().toString();
		if (TextUtils.isEmpty(oldPW)) {
			toast("请输入旧密码");
			return ;
		}
		if (TextUtils.isEmpty(newPW)) {
			toast("请输入新密码");
			return ;
		}
		if (!newPW.endsWith(newPW2)) {
			toast("两次密码输入不一致");
			return ;
		}
		
		
		String url = Urls.getUrl(Urls.TYPE_GERENZHONGXIN, 4);
		YongHuBean b = new YongHuBean();
		b.yhid = MyApplication.mUser.yhid;
		b.yhmm = newPW;
		b.jmm = oldPW;
		IDataRequest request = new SingleParamTask<YongHuBean>(act, url, b);
		executeTask(1, request);
		showDialog("正在修改密码，请稍候...");
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		dismissDialog();
		System.out.println("result=" + result);
		Result ret = JsonParser.getResultFromJson((String) result);
		if (ret.success) {
			toast("修改密码成功");
			try {
				JSONObject obj = new JSONObject((String)result);
				String key = YongHuBean.class.getSimpleName();
				if (!obj.isNull(key)) {
					String json = obj.getString(key);
					YongHuBean yhb = JsonUtil.parseJson(json, YongHuBean.class);
					if (yhb != null) {
						MyApplication.mUser = yhb;
						MyApplication.tmpUser = yhb;
						MyApplication.sp.edit().putString(Constants.XML_KEY_USER, json).commit();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finish();
		} else {
			toast("修改密码失败：" + ret.error);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		
		header.setTitle("修改密码");
	}
}
