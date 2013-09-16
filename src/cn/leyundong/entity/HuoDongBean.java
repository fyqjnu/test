package cn.leyundong.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动bean
 * @author zhongyq
 * @version 1.0, 2013-8-1
 * 
 */
public class HuoDongBean implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public String hdid;//活动主键
	public String hdcg;//活动场馆
	public double hdjg;//活动价格
	public String hdsj;//活动时间2012-09-22
	public String hdlx;//活动类型：1单次活动2长定
	public int rsxz;//人数限制
	public int sbrs;//实报人数
	public int syrs;//剩余人数
	public int wbrs;//我报人数
	public String hdzt;//1进行中：2被取消；3已完成
	public String hdztmc;//1进行中：2被取消；3已完成
	public double hdjssjd;//活动结束时间点
    public double hdqssjd;//活动起始时间点
    public int jzbmsj;//截止报名时间
    public int zcqxsj;//最迟取消时间
    public String hdsm;//活动说明
    
    public String jlbmc;//俱乐部名称
    public long jlbid;//俱乐部编号
    public String jlbsz;//俱乐部设置：1允许任何人；2需要身份验证；3不允许任何人
    
    public String hdsjd;//活动时间段，显示用，格式：2012-09-22 12:00-14:00
    public double hdzsr;//活动总收入
    public String hysx;//会员属性：会员、非会员
    public String cdlxmc;//场地类型显示名称:羽毛球
    public String cdlx;//场地类型:1羽毛球2网球场^具体根据下来列表
    
    //以下是按钮显示不显示判断
    public boolean yxBm;//true:允许报名，false：不允许报名（已过截止报名时间）
    public boolean yxqx;//true：允许取消，false：不允许取消（已过最迟取消时间）
    
    public boolean yxbmqk;//true：允许报名情况，false：不允许报名情况
    public boolean yxbjhd;//true：允许编辑活动，false：不允许编辑活动
    public boolean yxqxhd;//true：允许取消活动，false：不允许取消活动
    public boolean yxcjxthd;//true：允许复制活动，false：不允许复制活动
    
    public boolean sfhy;//true:会员;false:非会员
    
    public String yhm;//用户名
    public String cjsj;//报名时间或者活动时间：2012-09-22 12:12:22
    public String bmztmc;//报名状态名称
	@Override
	public String toString() {
		return "HuoDongBean [hdid=" + hdid + ", hdcg=" + hdcg + ", hdjg="
				+ hdjg + ", hdsj=" + hdsj + ", hdlx=" + hdlx + ", rsxz=" + rsxz
				+ ", sbrs=" + sbrs + ", syrs=" + syrs + ", wbrs=" + wbrs
				+ ", hdzt=" + hdzt + ", hdztmc=" + hdztmc + ", hdjssjd="
				+ hdjssjd + ", hdqssjd=" + hdqssjd + ", jzbmsj=" + jzbmsj
				+ ", zcqxsj=" + zcqxsj + ", hdsm=" + hdsm + ", jlbmc=" + jlbmc
				+ ", jlbid=" + jlbid + ", jlbsz=" + jlbsz + ", hdsjd=" + hdsjd
				+ ", hdzsr=" + hdzsr + ", hysx=" + hysx + ", cdlxmc=" + cdlxmc
				+ ", cdlx=" + cdlx + ", yxBm=" + yxBm + ", yxqx=" + yxqx
				+ ", yxbmqk=" + yxbmqk + ", yxbjhd=" + yxbjhd + ", yxqxhd="
				+ yxqxhd + ", yxcjxthd=" + yxcjxthd + ", sfhy=" + sfhy
				+ ", yhm=" + yhm + ", cjsj=" + cjsj + ", bmztmc=" + bmztmc
				+ "]";
	}
    
}
