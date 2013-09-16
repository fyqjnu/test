package cn.leyundong.entity;

public class Result {
	public boolean success = false;
	public String error = "";
	public Object obj;
	@Override
	public String toString() {
		return "Result [success=" + success + ", error=" + error + ", obj="
				+ obj + "]";
	}
	
}
