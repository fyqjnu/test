package cn.leyundong.entity;
/**
 * 码表bean
 * @author zhongyq
 * @version 1.0, 2013-8-1
 * 
 */
public class MaBiaoBean {
	private String key;
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "MaBiaoBean [key=" + key + ", value=" + value + "]";
	}
	
	
	
}
