package com.newtonk.util;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {

	/**
	 * º”√‹
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64(String input) throws Exception {
		byte[] bytes = null;
		String result = null;
		try {
			bytes = input.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (bytes != null) {
			result = new BASE64Encoder().encode(bytes);
		}
		return result;
	}

	/**
	 * Ω‚√‹
	 *
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String decodeBase64(String input) throws Exception {
		byte[] bytes = null;
		String result = null;
		if (input != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				bytes = decoder.decodeBuffer(input);
				result = new String(bytes, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
