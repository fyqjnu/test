package cn.leyundong.entity;

import java.io.Serializable;

/**
 * 城市代码名称
 * @author Administrator
 *
 */
public class City implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4320404095640256912L;
	
	private String csdm;
	private String csmc;
	
	public String getCsdm() {
		return csdm;
	}
	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}
	public String getCsmc() {
		return csmc;
	}
	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
	@Override
	public String toString() {
		return "City [csdm=" + csdm + ", csmc=" + csmc + "]";
	}
	
	
}
