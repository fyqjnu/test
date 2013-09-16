package cn.leyundong.entity;
/**
 * 时间bean
 * @author zhongyq
 * @version 1.0, 2013-8-1
 * 
 */
public class ShiJianBean {
	public String yysjdStr = "11:30";//营业时间显示：11:30
	public double yysjd = 11.5;//营业时间点：11.5
	
	public String getYysjdStr() {
		return yysjdStr;
	}
	public void setYysjdStr(String yysjdStr) {
		this.yysjdStr = yysjdStr;
	}
	public Double getYysjd() {
		return yysjd;
	}
	public void setYysjd(Double yysjd) {
		this.yysjd = yysjd;
	}
	@Override
	public String toString() {
		return "ShiJianBean [yysjdStr=" + yysjdStr + ", yysjd=" + yysjd + "]";
	}
	
	
	
}
