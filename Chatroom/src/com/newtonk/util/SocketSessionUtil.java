package com.newtonk.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class SocketSessionUtil {
	private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

	/**
	 * 保存一个连接
	 * 
	 * @param inquiryId
	 * @param empNo
	 * @param session
	 */
	public static void add(String inquiryId, int empNo, WebSocketSession session) {
		clients.put(getKey(inquiryId, empNo), session);
	}

	/**
	 * 获取一个连接
	 * 
	 * @param inquiryId
	 * @param empNo
	 * @return
	 */
	public static WebSocketSession get(String inquiryId, int empNo) {
		return clients.get(getKey(inquiryId, empNo));
	}

	/**
	 * 移除一个连接
	 * 
	 * @param inquiryId
	 * @param empNo
	 */
	public static void remove(String inquiryId, int empNo) throws IOException {
		clients.remove(getKey(inquiryId, empNo));
	}

	/**
	 * 组装sessionId
	 * 
	 * @param inquiryId
	 * @param empNo
	 * @return
	 */
	public static String getKey(String inquiryId, int empNo) {
		return inquiryId + "_" + empNo;
	}

	/**
	 * 判断是否有效连接 判断是否存在 判断连接是否开启 无效的进行清除
	 * 
	 * @param inquiryId
	 * @param empNo
	 * @return
	 */
	public static boolean hasConnection(String inquiryId, int empNo) {
		String key = getKey(inquiryId, empNo);
		if (clients.containsKey(key)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取连接数的数量
	 * 
	 * @return
	 */
	public static int getSize() {
		return clients.size();
	}

	/**
	 * 发送消息到客户端
	 * 
	 * @param inquiryId
	 * @param empNo
	 * @param message
	 * @throws Exception
	 */
	public static void sendMessage(String inquiryId, int empNo, String message)
			throws Exception {
		if (!hasConnection(inquiryId, empNo)) {
			throw new NullPointerException(getKey(inquiryId, empNo)
					+ " connection does not exist");
		}

		WebSocketSession session = get(inquiryId, empNo);
		try {
			session.sendMessage(new TextMessage(message));
		} catch (IOException e) {
			clients.remove(getKey(inquiryId, empNo));
		}
	}
}
