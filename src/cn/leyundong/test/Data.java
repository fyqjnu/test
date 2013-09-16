package cn.leyundong.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import cn.leyundong.entity.DingDanBean;
import cn.leyundong.entity.DingDanXiangBean;
import cn.leyundong.entity.QuXianInfo;
import cn.quickdevelp.db.DbUtil;

public class Data {
	public static DingDanBean[] getDingDanBean() {
		int len = 20;
		DingDanBean[] infos = new DingDanBean[len];
		for (int i = 0; i < len; i++) {
			DingDanBean d = new DingDanBean();
			infos[i] = d;
			
			d.ddid = "111";
			d.ddxid = "1111";
			d.cgmc = "奥体";
			d.cgid = "11133";
			d.cdlxmc = "羽毛球";
			d.cdlx = "篮球";
			d.ydrq = "2013-8-9";
			d.ddztmc = "进行中";
			d.xdsj = "2013-8-9 11:33:30";
			d.ydzfy = 44.6f;
			d.ddxList = getDingDanXiangBean();
		}
		return infos;
	}
	
	public static List<DingDanXiangBean> getDingDanXiangBean() {
		int size = 3;
		List<DingDanXiangBean> list = new ArrayList<DingDanXiangBean>();
		for (int i = 0; i < size; i++) {
			DingDanXiangBean d = new DingDanXiangBean();
			d.ddxid = "222";
			d.ddid = "2444";
			d.cdlxmc = "篮球";
			d.ddxztmc = "进行中";
			d.cdmc = "奥体";
			d.ydjg = 50f;
			d.ydsjd = "2013-3-4 20:00-22:00";
			list.add(d);
		}
		return list;
	}
	
	public static void putDataToDb(Context ctx, SQLiteDatabase db) {
		InputStream is = null;
		try {
			is = ctx.getAssets().open("qx.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				insertRecord(db, line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void insertRecord(SQLiteDatabase db, String line) {
		String[] ary = line.split(",");
		QuXianInfo info = new QuXianInfo();
		info.QXDM = ary[0];
		info.CSDM = ary[1];
		info.QXMC = ary[2];
		info.QXJC = ary[3];
		System.out.println(info);
		
		ContentValues cv = DbUtil.deconstructFromEntity(info);
		db.insert("quxian", null, cv);
	}
	
}
