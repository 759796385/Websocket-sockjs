package com.newtonk.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class SocketSessionUtil {
	private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

	/**
	 * ����һ������
	 * 
	 * @param inquiryId
	 * @param empNo
	 * @param session
	 */
	public static void add(String inquiryId, int empNo, WebSocketSession session) {
		clients.put(getKey(inquiryId, empNo), session);
	}

	/**
	 * ��ȡһ������
	 * 
	 * @param inquiryId
	 * @param empNo
	 * @return
	 */
	public static WebSocketSession get(String inquiryId, int empNo) {
		return clients.get(getKey(inquiryId, empNo));
	}

	/**
	 * �Ƴ�һ������
	 * 
	 * @param inquiryId
	 * @param empNo
	 */
	public static void remove(String inquiryId, int empNo) throws IOException {
		clients.remove(getKey(inquiryId, empNo));
	}

	/**
	 * ��װsessionId
	 * 
	 * @param inquiryId
	 * @param empNo
	 * @return
	 */
	public static String getKey(String inquiryId, int empNo) {
		return inquiryId + "_" + empNo;
	}

	/**
	 * �ж��Ƿ���Ч���� �ж��Ƿ���� �ж������Ƿ��� ��Ч�Ľ������
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
	 * ��ȡ������������
	 * 
	 * @return
	 */
	public static int getSize() {
		return clients.size();
	}

	/**
	 * ������Ϣ���ͻ���
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
