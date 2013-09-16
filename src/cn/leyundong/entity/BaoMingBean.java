package cn.leyundong.entity;

import java.util.List;

import cn.quickdevelp.anno.GenericType;

public class BaoMingBean {
    public String bmid;//报名id
    public String zffs;//支付方式
    public String bmzt;//报名状态：'1'未支付,'2'已支付,'3'被取消,'4已完成,'5'已过期
    public String bmztmc;//报名状态：'1'未支付,'2'已支付,'3'被取消,'4已完成,'5'已过期
    public String hdsj;//活动时间
    public double hdjssjd;//活动结束时间点
    public double hdqssjd;//活动起始时间点
    public double hdjg;//活动价格
    public String jlbmc;//俱乐部名称
    public String hdcg;//活动场馆
    public String bmsj;//报名时间2013-09-11 12:11:23
    public String hdsjd;//活动时间段2013-03-22 12:00-14:00
    public double bmzfy;//报名总费用
    public boolean yxQxbm;//允许取消报名：true:允许取消报名；false：不允许取消报名
    public boolean yxQdzf;//允许确定支付：true:允许确定支付；false：不允许确定支付
    
    @GenericType(genericClass=BaoMingXiangBean.class)
    public List<BaoMingXiangBean> bmxBeanList;//报名项列表

	@Override
	public String toString() {
		return "BaoMingBean [bmid=" + bmid + ", zffs=" + zffs + ", bmzt="
				+ bmzt + ", bmztmc=" + bmztmc + ", hdsj=" + hdsj + ", hdjssjd="
				+ hdjssjd + ", hdqssjd=" + hdqssjd + ", hdjg=" + hdjg
				+ ", jlbmc=" + jlbmc + ", hdcg=" + hdcg + ", bmsj=" + bmsj
				+ ", hdsjd=" + hdsjd + ", bmzfy=" + bmzfy + ", yxQxbm="
				+ yxQxbm + ", yxQdzf=" + yxQdzf + ", bmxBeanList="
				+ bmxBeanList + "]";
	}
    

}
