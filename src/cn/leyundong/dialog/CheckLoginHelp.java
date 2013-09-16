package cn.leyundong.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import cn.leyundong.MyApplication;
import cn.leyundong.activity.LoginActivity;

/**
 * 检查用户是否登录的对话框
 * @author Administrator
 *
 */
public class CheckLoginHelp {
	
	/**
	 * 判断用户是否存在
	 * @return
	 */
	public static boolean isUserPresent(final Context context) {
		if (MyApplication.mUser.isNull()) {
			AlertDialog dlg = null;
			dlg = new AlertDialog.Builder(context).create();
			dlg.setTitle("温馨提示");
			dlg.setMessage("该操作需要先登录，按确定进入登录页面！");
			dlg.setButton(Dialog.BUTTON_POSITIVE, "确定", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//跳转到登录页面
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);
				}
				
			});
			dlg.show();
			return false;
		}
		return true;
	}

}
