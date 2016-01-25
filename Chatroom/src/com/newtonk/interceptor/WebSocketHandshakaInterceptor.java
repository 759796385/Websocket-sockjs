package com.newtonk.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.newtonk.entity.User;

public class WebSocketHandshakaInterceptor extends
		HttpSessionHandshakeInterceptor {
	/*
	 * ���ӷ����������û��� ֻ��ִ��һ�Σ�������Ϣ�ǲ�ִ��
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest()
					.getSession(false);
			if (session != null) {
				// ʹ��userName����WebSocketHandler���Ա㶨������Ϣ
				String userName = ((User) session
						.getAttribute(Constants.SESSION_USER)).getName();
				attributes.put(Constants.WEBSOCKET_USERNAME, userName);
			}
		}
		return super.beforeHandshake(request, response, wsHandler, attributes);

	}

	@Override
	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		super.afterHandshake(request, response, wsHandler, ex);
	}

}
