package cn.leyundong.entity;

import java.io.Serializable;


/**
 * 订单项
 * @author MSSOFT
 *
 */
public class DingDanXiangBean implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	public DingDanXiangBean() {
		
	}
	
	public String ddxid;//订单项id
	public String ddid;//订单id
	public String cdlxmc;//场地类型显示名称:羽毛球
    public String cdlx;//场地类型:1羽毛球2网球场^具体根据下来列表
    public String ddxztmc;//预订项状态显示名称:进行中
    public String ddxzt;//预订项状态：根据下来列表获取
    
    public String cdmc;//场地名称
    public String ydsjd;//预订时间段：2013-03-22 12:00-14:00
    
    public double ydjg;//预订价格
    
    //以下用于下单时手机端传过来信息
    public String dwsjjgid;//单位时间价格id
    public String cdid;//场地id
    
    
    //为显示而添加
    public String dwsjd;//单位时间段，在下单时显示用：12:00-13:00
    
    public boolean YxQxDdx;//允许取消订单项：true:允许取消订单项；false：不允许取消订单项
    
	@Override
	public String toString() {
		return "DingDanXiangBean [ddxid=" + ddxid + ", ddid=" + ddid
				+ ", cdlxmc=" + cdlxmc + ", cdlx=" + cdlx + ", ddxztmc="
				+ ddxztmc + ", ddxzt=" + ddxzt + ", cdmc=" + cdmc + ", ydsjd="
				+ ydsjd + ", ydjg=" + ydjg + ", dwsjjgid=" + dwsjjgid
				+ ", cdid=" + cdid + "]";
	}
    
	@Override
	public int hashCode() {
		return dwsjjgid.hashCode() + cdid.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof DingDanXiangBean) {
			return dwsjjgid.equals(((DingDanXiangBean)o).dwsjjgid) && cdid.equals(((DingDanXiangBean)o).cdid);
		}
		return true;
	}

}