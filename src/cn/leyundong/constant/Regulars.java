package cn.leyundong.constant;

/**
 * 正则表达式
 * @author MSSOFT
 *
 */
public class Regulars {
	//用户名
	public static String REG_NAME = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
	//邮箱
	public static String MAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	//密码
	public static String PASSWORD = ".{6,}";
	//手机号码
	public static String PHONE_NUMBER = "^[1][3-8]\\d{9}$";
}
