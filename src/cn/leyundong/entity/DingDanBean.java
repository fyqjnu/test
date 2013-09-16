package cn.leyundong.entity;

import java.util.ArrayList;
import java.util.List;

import cn.quickdevelp.anno.GenericType;

public class DingDanBean {
	public String ddid;//订单id
	public String ddxid;//订单项id
	public String cgmc;//场馆名称
	public String cgid;//场馆id
	public String cdlxmc;//场地类型显示名称:羽毛球
    public String cdlx;//场地类型:1羽毛球2网球场^具体根据下来列表
    public String ydrq;//预订日期：2013-9-12
    public String ddztmc;//预订状态显示名称:进行中
    public String ddzt;//预订状态：根据下来列表获取
    public String xdsj;//下单时间：2013-09-11 12:11:23
    public float ydzfy;//预订总费用
    @GenericType(genericClass=DingDanXiangBean.class)
    public List<DingDanXiangBean> ddxList=new ArrayList<DingDanXiangBean>();//订单项列表
    
    public boolean YxQxDd;//允许取消订单：true:允许取消订单；false：不允许取消订单
    
	@Override
	public String toString() {
		return "DingDanBean [ddid=" + ddid + ", ddxid=" + ddxid + ", cgmc="
				+ cgmc + ", cgid=" + cgid + ", cdlxmc=" + cdlxmc + ", cdlx="
				+ cdlx + ", ydrq=" + ydrq + ", ddztmc=" + ddztmc + ", ddzt="
				+ ddzt + ", xdsj=" + xdsj + ", ydzfy=" + ydzfy + ", ddxList="
				+ ddxList + "]";
	}

    
    
}