package cn.leyundong.test;

import cn.leyundong.entity.QuXianInfo;
import cn.quickdevelp.db.DbUtil;
import cn.quickdevelp.db.ITable;

public class QuXianTable implements ITable {

	@Override
	public String getCreateTableSql() {
		return DbUtil.getCreateTableSql("quxian", DbUtil.getFieldForEntiry(QuXianInfo.class));
	}

	@Override
	public String getDeleteTableSql() {
		return DbUtil.getDeleteTableSql("quxian");
	}

}
