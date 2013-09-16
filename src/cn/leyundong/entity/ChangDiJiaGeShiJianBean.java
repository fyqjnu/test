package cn.leyundong.entity;

/**
 * 场地价格时间bean
 * @author zhongyq
 * @version 1.0, 2013-8-17
 * 
 */
public class ChangDiJiaGeShiJianBean {
	public String dwsjjgid;//单位时间价格id
    public String cdid;//场地id
    public double dj;//单价：30.00
    public String dwsjd;//单位时间段，在下单时显示用：12:00-13:00
    public boolean sfyxs;//是否一小时，true是1小时，false是0.5小时
    public int ydzt;//预订状态:1空闲2被选择3已出售4被长定5：未定价
    public String ydztsm;//预订状态说明，只有当预订状态为1时才有值，其值为：未定价
    public double yysjd;//营业时间点：11.5
    
	@Override
	public String toString() {
		return "ChangDiJiaGeShiJianBean [dwsjjgid=" + dwsjjgid + ", cdid="
				+ cdid + ", dj=" + dj + ", dwsjd=" + dwsjd + ", sfyxs=" + sfyxs
				+ ", ydzt=" + ydzt + ", ydztsm=" + ydztsm + ", yysjd=" + yysjd
				+ "]";
	}
	
    
}
