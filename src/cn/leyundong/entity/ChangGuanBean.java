package cn.leyundong.entity;

import java.io.Serializable;
import java.util.List;

import cn.quickdevelp.anno.GenericType;

/**
 * 场馆
 * @author MSSOFT
 *
 */
public class ChangGuanBean implements Serializable {
	
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public String cgid;//场馆id
	public String cgmc;//场馆名称
	public String yysj;//营业时间：10:00 - 20:00
	public String cgdz;//场馆地址：  广东省广州市天河区黄村路口
	public String dtkcl;//当天空场率：20
	
	public String lxdh;//联系电话：020-32233223
	public String cgjg;//场馆价格：25-35元
	public String gjzd;//公交站点
	public String cgxq;//场馆详情
	public String cgjlb;//场馆俱乐部
	public String cdsl;//场地数量：2个羽毛球场，1个足球场
	
	public String cgzt;//场馆状态
	public String cgztmc;//场馆状态名称
	
	public int tckfsd;//退出扣费设定：退款95%
    public int tckfsj;//退出扣费时间设定：23小时
    public int tckfsd2;//退出扣费设定2：退款50%
    public int tckfsj2;//退出扣费时间设定：7小时
	
	public int tqydrq;//提前预订日期：以天为单位
	
	@GenericType(genericClass=RiQiBean.class)
	public List<RiQiBean> riQiBeanList;//日期bean
	
	@GenericType(genericClass=ShiJianBean.class)
	public List<ShiJianBean> shiJianBeanList;//时间bean
	
	@GenericType(genericClass=ChangDiJiaGeBean.class)
	public List<ChangDiJiaGeBean> cdjgBeanList;//场地价格bean List
	
	//下单时用
	public String hyzid;//会员组id

	@Override
	public String toString() {
		return "ChangGuanBean [cgid=" + cgid + ", cgmc=" + cgmc + ", yysj="
				+ yysj + ", cgdz=" + cgdz + ", dtkcl=" + dtkcl + ", lxdh="
				+ lxdh + ", cgjg=" + cgjg + ", gjzd=" + gjzd + ", cgxq=" + cgxq
				+ ", cgjlb=" + cgjlb + ", cdsl=" + cdsl + ", cgzt=" + cgzt
				+ ", cgztmc=" + cgztmc + ", tckfsd=" + tckfsd + ", tckfsj="
				+ tckfsj + ", tckfsd2=" + tckfsd2 + ", tckfsj2=" + tckfsj2
				+ ", tqydrq=" + tqydrq + ", riQiBeanList=" + riQiBeanList
				+ ", shiJianBeanList=" + shiJianBeanList + ", cdjgBeanList="
				+ cdjgBeanList + ", hyzid=" + hyzid + "]";
	}
	
	
	
}
