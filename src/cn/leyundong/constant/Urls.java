package cn.leyundong.constant;

/**
 * @author MSSOFT
 *
 */
public class Urls {
	
	/**
	 * 公共
	 */
	public static final int TYPE_GONGGONG = 1;
	
	/**
	 * 个人中心
	 */
	public static final int TYPE_GERENZHONGXIN = 2;
	/**
	 * 预定专区
	 */
	public static final int TYPE_YUDINGZHUANGQU = 3;
	
	//俱乐部
	public static final int TYPE_CLUB = 4;
	
	/**
	 * 会员
	 */
	public static final int TYPE_HUIYUAN = 5;
	
	//公共链接
	static final String URL_GONGGONG = "http://42.121.145.198:8080/lydwz/az/gg?mod=";
	//个人中心
	static final String URL_GERENZHENGXIN = "http://42.121.145.198:8080/lydwz/az/grzx?mod=";
	
	/**
	 * 预定专区
	 */
	static final String URL_YUDINGZHUANGQU = "http://42.121.145.198:8080/lydwz/az/ydzq?mod=";
	
	//俱乐部-财务
	static final String URL_CLUB_CAIWU = "http://42.121.145.198:8080/lydwz/az/jlbjy?mod=";
	
	/**
	 * 会员区
	 */
	static final String URL_HUIYUAN = "http://42.121.145.198:8080/lydwz/az/hyzq?mod=";
	
	public static String getUrl(int type, int mod) {
		switch (type) {
		case TYPE_GONGGONG:
			//公共
			return URL_GONGGONG + mod;
		case TYPE_GERENZHONGXIN:
			//个人中心
			return URL_GERENZHENGXIN + mod;
		case TYPE_YUDINGZHUANGQU:
			return URL_YUDINGZHUANGQU + mod;
		case TYPE_CLUB:
			return URL_CLUB_CAIWU + mod;
		case TYPE_HUIYUAN:
			return URL_HUIYUAN + mod;
		}
			
		return "";
	}
}
