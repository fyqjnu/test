package cn.leyundong.entity;

/**
 * 日期bean
 * @author zhongyq
 * @version 1.0, 2013-8-1
 * 
 */
public class RiQiBean {
	public String dqxq = "星期一";//当前星期: 星期一
	public String dqrq = "2013-7-22";//当前日期：2013-7-22

	public String getDqxq() {
		return dqxq;
	}
	public void setDqxq(String dqxq) {
		this.dqxq = dqxq;
	}
	public String getDqrq() {
		return dqrq;
	}
	public void setDqrq(String dqrq) {
		this.dqrq = dqrq;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof RiQiBean) {
			return dqrq.equals(((RiQiBean)o).dqrq);
		}
		return super.equals(o);
	}
	
	@Override
	public int hashCode() {
		return dqrq.hashCode();
	}
	

}
