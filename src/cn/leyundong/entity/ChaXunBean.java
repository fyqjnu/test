package cn.leyundong.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 查询场馆
 * @author Administrator
 *
 */
public class ChaXunBean implements Serializable {
	public String ydrq;//预订日期
	public String cgmc;//场馆名称
	public String cgid;//场馆id
	public String cdlx;//场地类型
	public String csdm;//城市代码
	public String qxdm;//区县代码
	public String yhid;//用户id
	public String yhm;//用户名
	public int pageStartRow;// 每页的起始行数
	public int pageSize;//每页默认10条数据(备用字段，不用传值)
	
	public long jlbid;//俱乐部编号
	public String jlbmc;//俱乐部名称
	public String hdcg;//活动场馆
	
	public String hdid;//活动id
	
	public String szlx;//收支类型
	public double szje;//收支金额
	
	public String ddid;//订单id
	public String zflx;//支付类型：1：直接扣款2在线支付
	public double ddzj;//订单总价
	public double syye;//剩余余额
	
	public String ddxid;//订单项id
	
	public String yhjlbid;//用户俱乐部id
	
	public DingDanBean dingDanBean;//下定单时和取消订单时要传入；
//	public List<DingDanXiangBean> ddxBeanList;//下定单时和取消订单时要传入；
	public HuoDongBean huoDongBean;//创建编辑活动时要传入；
	
	public String dlryid;//登录人员id
	
	public String jlbsz;//俱乐部设置
	public int bmrs;//报名人数
	
	public String bmid;//报名id
	public String bmxid;//报名项id
	
	public String hyzid;//会员组id

	@Override
	public String toString() {
		return "ChaXunBean [ydrq=" + ydrq + ", cgmc=" + cgmc + ", cgid=" + cgid
				+ ", cdlx=" + cdlx + ", csdm=" + csdm + ", qxdm=" + qxdm
				+ ", yhid=" + yhid + ", yhm=" + yhm + ", pageStartRow="
				+ pageStartRow + ", pageSize=" + pageSize + ", jlbid=" + jlbid
				+ ", jlbmc=" + jlbmc + ", hdcg=" + hdcg + ", hdid=" + hdid
				+ ", szlx=" + szlx + ", szje=" + szje + ", ddid=" + ddid
				+ ", zflx=" + zflx + ", ddzj=" + ddzj + ", syye=" + syye
				+ ", ddxid=" + ddxid + ", yhjlbid=" + yhjlbid
				+ ", dingDanBean=" + dingDanBean + ", huoDongBean="
				+ huoDongBean + ", dlryid=" + dlryid + ", jlbsz=" + jlbsz
				+ ", bmrs=" + bmrs + ", bmid=" + bmid + ", bmxid=" + bmxid
				+ ", hyzid=" + hyzid + "]";
	}

	
	
}