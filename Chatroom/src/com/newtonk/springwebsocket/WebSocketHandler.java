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
	 * 建立连接
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		String name = SocketSessionUtil.getName(session);
		System.out.println("用户" + name + "连入服务器");
		SocketSessionUtil.add(name, session);
	}

	/*
	 * 收到客户端消息
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		String name = SocketSessionUtil.getName(session);
		SocketSessionUtil.sendMessage(name, message.getPayload().toString());
	}

	/*
	 * 出现异常
	 */
	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		String user = SocketSessionUtil.getName(session);
		SocketSessionUtil.remove(user);
		exception.printStackTrace();
	}

	/*
	 * 连接关闭
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		String name = SocketSessionUtil.getName(session);
		System.out.println("用户" + name + "断开服务器");
		SocketSessionUtil.remove(name);
	}

	/*
	 * 是否分段发送消息
	 */
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
