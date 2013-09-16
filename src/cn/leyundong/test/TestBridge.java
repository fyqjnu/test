package cn.leyundong.test;

import cn.leyundong.activity.bridge.ICommunicationBridge;
import cn.leyundong.entity.ChaXunBean;

public class TestBridge implements ICommunicationBridge<ChaXunBean> {

	@Override
	public ChaXunBean getValue() {
		ChaXunBean b = new ChaXunBean();
		b.yhm = "abc";
		return b;
	}


}
