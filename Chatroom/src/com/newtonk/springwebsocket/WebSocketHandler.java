package com.newtonk.springwebsocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.newtonk.util.SocketSessionUtil;

public class WebSocketHandler extends TextWebSocketHandler {
	public WebSocketHandler() {
	}

	/*
	 * ��������
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		String name = SocketSessionUtil.getName(session);
		System.out.println("�û�" + name + "���������");
		SocketSessionUtil.add(name, session);
	}

	/*
	 * �յ��ͻ�����Ϣ
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		String name = SocketSessionUtil.getName(session);
		SocketSessionUtil.sendMessage(name, message.getPayload().toString());
	}

	/*
	 * �����쳣
	 */
	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		String user = SocketSessionUtil.getName(session);
		SocketSessionUtil.remove(user);
		exception.printStackTrace();
	}

	/*
	 * ���ӹر�
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		String name = SocketSessionUtil.getName(session);
		System.out.println("�û�" + name + "�Ͽ�������");
		SocketSessionUtil.remove(name);
	}

	/*
	 * �Ƿ�ֶη�����Ϣ
	 */
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
