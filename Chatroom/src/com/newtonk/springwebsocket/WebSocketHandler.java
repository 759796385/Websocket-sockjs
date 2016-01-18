package com.newtonk.springwebsocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.newtonk.interceptor.Constants;

public class WebSocketHandler extends TextWebSocketHandler {
	public WebSocketHandler() {
	}

	/*
	 * ��������
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		String inquiryId = (String) session.getAttributes().get(
				Constants.WEBSOCKET_USERNAME);
		// HttpHeaders hy = session.getHandshakeHeaders();
		// int empNo = (int) session.getAttributes().get("empNo");
		// SocketSessionUtil.add(inquiryId, empNo, session);
		System.out.println(inquiryId);
	}

	/*
	 * �յ��ͻ�����Ϣ
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		// String inquiryId = (String) session.getAttributes().get("inquiryId");
		// int empNo = (int) session.getAttributes().get("empNo");
		// SocketSessionUtil.sendMessage(inquiryId, empNo, "�����Է������ĸ���������"
		// + message.getPayload().toString());
		System.out.println("test");
	}

	/*
	 * �����쳣
	 */
	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		// String inquiryId = (String) session.getAttributes().get("inquiryId");
		// int empNo = (int) session.getAttributes().get("empNo");
		// SocketSessionUtil.remove(inquiryId, empNo);
	}

	/*
	 * ���ӹر�
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		// String inquiryId = (String) session.getAttributes().get("inquiryId");
		// int empNo = (int) session.getAttributes().get("empNo");
		// SocketSessionUtil.remove(inquiryId, empNo);
	}

	/*
	 * �Ƿ�ֶη�����Ϣ
	 */
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
