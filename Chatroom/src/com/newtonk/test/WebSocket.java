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
	// ��������
	private static int online = 0;
	// �������Ӳ�������
	private static CopyOnWriteArraySet<WebSocket> set = new CopyOnWriteArraySet<WebSocket>();
	// �û���Ϣ
	private Session session;

	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
		set.add(this);
		addOnline();
		System.out.println("���������ӳɹ�");
		System.out.println("��ǰ����������" + getOnline());
	}

	@OnMessage
	public String onMessage(String message) {
		System.out.println("������������Ϣ��" + message);
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
		return "���ͳɹ�";
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("�����������쳣");
		t.printStackTrace();
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		set.remove(this);
		reduceOnline();
		System.out.println("�������Ͽ����ӣ�" + reason);
		System.out.println("��ǰ����������" + getOnline());
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
