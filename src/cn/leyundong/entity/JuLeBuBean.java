package cn.leyundong.entity;

import java.io.Serializable;

public class JuLeBuBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8596352989866158539L;
	
	public long jlbid;//俱乐部id
	public String jlbmc;//俱乐部名称
	public String cdlxmc;//场地类型显示名称:羽毛球
	public String cdlx;//场地类型:1羽毛球2网球场^具体根据下来列表
	public String hddz;//活动地址
	public String hdcg;//活动场馆
	public String hdjg;//活动价格
	public String lxdh;//联系电话
	public String lxr;//联系人
    public String hdgz;//活动规则
    public String jlbsz;//俱乐部设置：1允许任何人；2需要身份验证；3不允许任何人
    public long qqqh;//QQ群号
    
    public int hyrs;//会员人数
    public int dyzrs;//待验证人数

	//yonghujulebu
	public String hysx;//会员属性
	public String hysxmc;//会员属性名称
	public String yzms;//验证描述
	public String yhjlbid;//用户俱乐部id
	
	//yonghu
	public String yhm;//用户名
	public String zsxm;//真实姓名
	public String yhid;//用户id
	public String sjhm;//手机号码
	
	//julebuhuiyuancaiwu
	public double szje;//收支金额
    public double ye;//余额
    public String szlx;//收支类型：
    public String szlxmc;//收支类型名称：1在线充值2支付宝充值3消费4退款5已结算
    public String szsj;//收支时间：2013-09-12 14:23:32
    
    public String sqsj;//申请时间：2013-09-12 14:23:32
    
    //以下是按钮显示不显示判断
    public boolean yxQxyq;//取消邀请:true:允许取消邀请；false：不允许取消邀请
    public boolean yxYztg;//验证通过:true:允许验证通过；false：不允许验证通过
    public boolean yxJjjr;//拒绝加入:true:允许拒绝加入；false：不允许拒绝加入
    public boolean yxSzgly;//设置管理员:true:允许设置管理员；false：不允许设置管理员
    public boolean yxScHy;//删除会员:true:允许删除会员；false：不允许删除会员
    public boolean yxScgly;//删除管理员:true:允许删除管理员；false：不允许删除管理员
    public boolean yxSqjr;//加入俱乐部：true：允许申请加入；false：不允许申请加入
    public boolean yxCz;//充值：true：允许充值；false：不允许充值
	public long getJlbid() {
		return jlbid;
	}
	public void setJlbid(long jlbid) {
		this.jlbid = jlbid;
	}
	public String getJlbmc() {
		return jlbmc;
	}
	public void setJlbmc(String jlbmc) {
		this.jlbmc = jlbmc;
	}
	public String getCdlxmc() {
		return cdlxmc;
	}
	public void setCdlxmc(String cdlxmc) {
		this.cdlxmc = cdlxmc;
	}
	public String getCdlx() {
		return cdlx;
	}
	public void setCdlx(String cdlx) {
		this.cdlx = cdlx;
	}
	public String getHddz() {
		return hddz;
	}
	public void setHddz(String hddz) {
		this.hddz = hddz;
	}
	public String getHdcg() {
		return hdcg;
	}
	public void setHdcg(String hdcg) {
		this.hdcg = hdcg;
	}
	public String getHdjg() {
		return hdjg;
	}
	public void setHdjg(String hdjg) {
		this.hdjg = hdjg;
	}
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	public String getLxr() {
		return lxr;
	}
	public void setLxr(String lxr) {
		this.lxr = lxr;
	}
	public String getHdgz() {
		return hdgz;
	}
	public void setHdgz(String hdgz) {
		this.hdgz = hdgz;
	}
	public String getJlbsz() {
		return jlbsz;
	}
	public void setJlbsz(String jlbsz) {
		this.jlbsz = jlbsz;
	}
	public long getQqqh() {
		return qqqh;
	}
	public void setQqqh(long qqqh) {
		this.qqqh = qqqh;
	}
	public int getHyrs() {
		return hyrs;
	}
	public void setHyrs(int hyrs) {
		this.hyrs = hyrs;
	}
	public int getDyzrs() {
		return dyzrs;
	}
	public void setDyzrs(int dyzrs) {
		this.dyzrs = dyzrs;
	}
	public String getHysx() {
		return hysx;
	}
	public void setHysx(String hysx) {
		this.hysx = hysx;
	}
	public String getHysxmc() {
		return hysxmc;
	}
	public void setHysxmc(String hysxmc) {
		this.hysxmc = hysxmc;
	}
	public String getYzms() {
		return yzms;
	}
	public void setYzms(String yzms) {
		this.yzms = yzms;
	}
	public String getYhjlbid() {
		return yhjlbid;
	}
	public void setYhjlbid(String yhjlbid) {
		this.yhjlbid = yhjlbid;
	}
	public String getYhm() {
		return yhm;
	}
	public void setYhm(String yhm) {
		this.yhm = yhm;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public double getSzje() {
		return szje;
	}
	public void setSzje(double szje) {
		this.szje = szje;
	}
	public double getYe() {
		return ye;
	}
	public void setYe(double ye) {
		this.ye = ye;
	}
	public String getSzlx() {
		return szlx;
	}
	public void setSzlx(String szlx) {
		this.szlx = szlx;
	}
	public String getSzlxmc() {
		return szlxmc;
	}
	public void setSzlxmc(String szlxmc) {
		this.szlxmc = szlxmc;
	}
	public String getSzsj() {
		return szsj;
	}
	public void setSzsj(String szsj) {
		this.szsj = szsj;
	}
	public String getSqsj() {
		return sqsj;
	}
	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}
	public boolean isYxQxyq() {
		return yxQxyq;
	}
	public void setYxQxyq(boolean yxQxyq) {
		this.yxQxyq = yxQxyq;
	}
	public boolean isYxYztg() {
		return yxYztg;
	}
	public void setYxYztg(boolean yxYztg) {
		this.yxYztg = yxYztg;
	}
	public boolean isYxJjjr() {
		return yxJjjr;
	}
	public void setYxJjjr(boolean yxJjjr) {
		this.yxJjjr = yxJjjr;
	}
	public boolean isYxSzgly() {
		return yxSzgly;
	}
	public void setYxSzgly(boolean yxSzgly) {
		this.yxSzgly = yxSzgly;
	}
	public boolean isYxScHy() {
		return yxScHy;
	}
	public void setYxScHy(boolean yxScHy) {
		this.yxScHy = yxScHy;
	}
	public boolean isYxScgly() {
		return yxScgly;
	}
	public void setYxScgly(boolean yxScgly) {
		this.yxScgly = yxScgly;
	}
	public boolean isYxSqjr() {
		return yxSqjr;
	}
	public void setYxSqjr(boolean yxSqjr) {
		this.yxSqjr = yxSqjr;
	}
	public boolean isYxCz() {
		return yxCz;
	}
	public void setYxCz(boolean yxCz) {
		this.yxCz = yxCz;
	}
	
    

}
