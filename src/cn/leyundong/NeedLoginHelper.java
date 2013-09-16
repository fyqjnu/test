package cn.leyundong;

import android.content.Context;
import android.view.View;
import android.widget.ViewFlipper;
import cn.leyundong.adapter.AutoAdapter;
import cn.leyundong.custom.TipsLoginView;
import cn.leyundong.entity.YongHuBean;
import cn.leyundong.httpoperation.LoginTask;
import cn.leyundong.httpoperation.RegisterTask;
import cn.leyundong.interfaces.IDataViewModel;
import cn.leyundong.interfaces.onLoginSuccess;
import cn.leyundong.interfaces.onRegisterSuccess;

public class NeedLoginHelper implements onLoginSuccess, onRegisterSuccess {
	
	private ViewFlipper vf;
	private Context ctx;
	private TipsLoginView tvLogin;
	
	private boolean hasDateBeforeLoginout;
	
	private IDataViewModel layoutControl;
	
	@SuppressWarnings("rawtypes")
	private AutoAdapter adapter;
	
	public NeedLoginHelper(Context c)  {
		this.ctx = c;
	}
	
	public void setAdapter(AutoAdapter adapter) {
		this.adapter = adapter;
	}
	
	public void setLayoutControl(IDataViewModel m) {
		layoutControl = m;
	}
	
	public View needLogin(View layout) {
		return needLogin(layout, null);
	}
	
	public View needLogin(View layout, String tips) {
		LoginTask.registerLoginSuccessListener(this);
		RegisterTask.registerRegisterSuccessListener(this);
		vf = new ViewFlipper(ctx);
		tvLogin = new TipsLoginView(ctx);
		if (tips != null) {
			tvLogin.setTipText(tips);
		}
		vf.addView(tvLogin);
		vf.addView(layout);
		
		if (MyApplication.mUser.isNull()) {
			showNoLoginLayout();
		} else {
			showHasLoginLayout();
		}
		return vf;
	}
	
	public void destroy() {
		LoginTask.unregisterLoginSuccessListener(this);
		RegisterTask.unregisterRegisterSuccessListener(this);
	}

	private void showHasLoginLayout() {
		vf.setDisplayedChild(1);
		if(adapter != null && adapter.getCount() > 0 && layoutControl != null) {
			layoutControl.showRefreshLayout();
		}
	}
	
	private void showNoLoginLayout() {
		vf.setDisplayedChild(0);
	}

	@Override
	public void onRegisterSuccess(YongHuBean user) {
		showHasLoginLayout();
	}


	@Override
	public void onLoginOrRegisterSuccess(YongHuBean user) {
		showHasLoginLayout();
	}
	
	public View getView() {
		return vf;
	}

	@Override
	public void onLoginout() {
		showNoLoginLayout();
		if (adapter != null) {
			System.out.println("清空数据-------" + adapter);
		}
	}
}
