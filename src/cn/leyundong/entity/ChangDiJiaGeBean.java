package cn.leyundong.entity;

import java.util.List;

import cn.quickdevelp.anno.GenericType;

/**
 * 场地价格bean
 * @author zhongyq
 * @version 1.0, 2013-8-1
 * 
 */
public class ChangDiJiaGeBean {
	public String cdid;//场地id
    public String cdmc;//场地名称
    public String cdczmc;//场地材质名称
    public String cdzt;//场地状态:1正常2暂停使用3已停用
    
    @GenericType(genericClass=ChangDiJiaGeShiJianBean.class)
    public List<ChangDiJiaGeShiJianBean> cdsjjgList;//场地时间价格集合

	@Override
	public String toString() {
		return "ChangDiJiaGeBean [cdid=" + cdid + ", cdmc=" + cdmc
				+ ", cdczmc=" + cdczmc + ", cdzt=" + cdzt + ", cdsjjgList="
				+ cdsjjgList + "]";
	}
    
    
    
}
