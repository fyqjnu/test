package cn.leyundong.interfaces;

import cn.leyundong.entity.YongHuBean;

/**
 * 登录成功
 * @author Administrator
 *
 */
public interface onLoginSuccess {
	void onLoginOrRegisterSuccess(YongHuBean user);
	void onLoginout();
}
