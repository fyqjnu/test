package cn.leyundong.test;

public class Printer {
	public static void print(String s, int count) {
		if (s == null) {
			return ;
		}
		if (count <= 0) {
			count = 1;
		}
		int l = s.length();
		int step = l / count;
		System.out.println("l=" + l);
		System.out.println("step=" + step);
		for (int i = 0; i < count; i++) {
			int start = 0;
			int end = 0;
			start = i * step;
			if (i != count - 1) {
				end = (i + 1) * step;
			} else {
				end = l;
			}
			System.out.println(s.substring(start, end));
		}
	}
}
