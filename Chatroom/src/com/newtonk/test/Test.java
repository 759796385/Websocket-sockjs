package com.newtonk.test;


public class Test {
	public static void main(String[] args) {
		System.out.println(unicode2String("%u5510%u5F3A"));
	}

	public static String unicode2String(String unicode) {

		StringBuffer string = new StringBuffer();

		String[] hex = unicode.split("%u");

		for (int i = 1; i < hex.length; i++) {

			// ת����ÿһ�������
			int data = Integer.parseInt(hex[i], 16);

			// ׷�ӳ�string
			string.append((char) data);
		}

		return string.toString();
	}
}
