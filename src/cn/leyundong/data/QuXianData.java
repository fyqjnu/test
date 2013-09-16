package cn.leyundong.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.leyundong.entity.QuXianInfo;
import cn.quickdevelp.db.DbUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 区县数据
 * @author MSSOFT
 *
 */
public class QuXianData {
	
	private static final String TABLE_NAME = "quxian";

	private static final String DB_NAME = "qx.db";
	
	private String fileName;
	
	private Context ctx;
	private static QuXianData instance;

	private SQLiteDatabase db;
	
	private QuXianData(Context ctx) {
		this.ctx = ctx.getApplicationContext();
		fileName = "/data/data/" + ctx.getPackageName() + "/databases/" + DB_NAME;
		if (!checkDbExits()) {
			//不存在
			copyDatabase();
		}
		db = SQLiteDatabase.openDatabase(fileName, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	private boolean checkDbExits() {
		if (new File(fileName).exists()) {
			return true;
		}
		return false;
	}
	
	private void copyDatabase() {
		InputStream is = null;
		OutputStream out = null;
		try {
			is = ctx.getAssets().open(DB_NAME);
			out = new FileOutputStream(fileName);
			byte[] buf = new byte[2048];
			int len = 0;
			while((len = is.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static QuXianData getInstance(Context ctx) {
		if (instance == null) {
			instance = new QuXianData(ctx);
		}
		return instance;
	}
	
	/**
	 * 通过市来查其区县
	 * @param shi
	 * @return
	 */
	public List<String> queryQuXianByShi(String shi) {
		List<String> ary = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_NAME, null, "QXJC=?", new String[]{shi}, null, null, null);
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				//读取数据
				QuXianInfo info = DbUtil.buildEntityFromCursor(cursor, QuXianInfo.class);
				ary.add(info.QXMC);
				cursor.moveToNext();
			}
		}
		cursor.close();
		System.out.println("查询市=" + shi + ", 返回区县数据=" + ary);
		return ary;
	}
	
	public String queryQuXianDaMa(String quxian) {
		Cursor cursor = db.query(TABLE_NAME, null, "QXMC=?", new String[]{quxian}, null, null, null);
		String ret = "";
		if (cursor.moveToFirst()) {
			if (!cursor.isAfterLast()) {
				//读取数据
				QuXianInfo info = DbUtil.buildEntityFromCursor(cursor, QuXianInfo.class);
				ret = info.QXDM;
			}
		}
		cursor.close();
		return ret;
	}
}
