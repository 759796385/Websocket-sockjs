package com.newtonk.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		System.out.println(unicode2String("%u5510%u5F3Aa%u5F3A"));
	}

	public static String unicode2String(String s) {
		List<String> list = new ArrayList<String>();
		String zz = "%u[0-9,a-z,A-Z]{4}";

		// ������ʽ�÷��ο�API
		Pattern pattern = Pattern.compile(zz);
		Matcher m = pattern.matcher(s);
		while (m.find()) {
			list.add(m.group());
		}
		for (int i = 0, j = 2; i < list.size(); i++) {
			String st = list.get(i).substring(j, j + 4);

			// ���õ�����ֵ����16���ƽ���Ϊʮ�����������ُ�תΪ�ַ�
			char ch = (char) Integer.parseInt(st, 16);
			// �õõ����ַ��滻������ʽ
			s = s.replace(list.get(i), String.valueOf(ch));
		}
		return s;
	}
}
