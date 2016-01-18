package com.newtonk.test;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class WebSocket {
	// 在线人数
	private static int online = 0;
	// 所有连接并发集合
	private static CopyOnWriteArraySet<WebSocket> set = new CopyOnWriteArraySet<WebSocket>();
	// 用户信息
	private Session session;

	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
		set.add(this);
		addOnline();
		System.out.println("服务器连接成功");
		System.out.println("当前在线人数：" + getOnline());
	}

	@OnMessage
	public String onMessage(String message) {
		System.out.println("服务器接受信息：" + message);
		for (WebSocket socket : set) {
			try {
				if (socket != this) {
					socket.sendMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return "发送成功";
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("服务器出现异常");
		t.printStackTrace();
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		set.remove(this);
		reduceOnline();
		System.out.println("服务器断开连接：" + reason);
		System.out.println("当前在线人数：" + getOnline());
	}

	public void sendMessage(String message) throws IOException {
		// this.session.getBasicRemote().sendText(message);
		this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized void addOnline() {
		online++;
	}

	public static synchronized void reduceOnline() {
		online--;
	}

	public int getOnline() {
		return online;
	}
}
