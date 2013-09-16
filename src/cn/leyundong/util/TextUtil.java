package cn.leyundong.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class TextUtil {
	/**
	 * 标题加粗
	 * @param tv
	 * @param s
	 * @param titleLen 标题长度
	 */
	public static void setText(TextView tv, String s, int titleLen) {
		Spannable ss = new SpannableString(s);
		ss.setSpan(new StyleSpan(Typeface.BOLD), 0, titleLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, titleLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(ss);
	}
	
	/**
	 * 标题加粗
	 * @param tv
	 * @param ss 内容
	 * @param titleLens 标题长度
	 */
	public static void setText(TextView tv, String[] ss, int[] titleLens) {
		StringBuilder sb = new StringBuilder();
		int spaceNum = 3;
		int i = 0;
		for (String s : ss) {
			sb.append(s);
			if (i != ss.length - 1) {
				for (int k = 0; k < spaceNum; k++) {
					sb.append(" ");
				}
			}
			i++;
		}
		
		Spannable sp = new SpannableString(sb.toString());
		int size = ss.length;
		int start = 0;
		int len = 0;
		for (i = 0; i < size; i++) {
			if (i > 0) {
				start = ss[i - 1].length() + spaceNum;
			}
		    len = start + titleLens[i];
			sp.setSpan(new ForegroundColorSpan(Color.BLUE), start, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			System.out.println("start=" + start + ",len=" + len);
		}
		tv.setText(sp);
	}
}
