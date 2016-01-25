package com.newtonk.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.newtonk.interceptor.Constants;

public class SocketSessionUtil {
	private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

	/**
	 * 保存一个连接
	 * 
	 * @param inquiryId
	 * @param session
	 * @throws Exception
	 */
	public static void add(String username, WebSocketSession session)
			throws Exception {
		if (hasConnection(getKey(username))) {// refresh
			try {
				get(getKey(username)).close();// close connection
				remove(getKey(username));// remove connection
			} catch (IOException e) {
				throw new Exception(getKey(username)
						+ "connection does not exit!");
			}
		}
		clients.put(getKey(username), session);
	}

	/**
	 * 获取一个连接
	 * 
	 * @param inquiryId
	 * @return
	 */
	public static WebSocketSession get(String username) {
		return clients.get(getKey(username));
	}

	/**
	 * 移除一个连接
	 * 
	 * @param inquiryId
	 */
	public static void remove(String username) throws IOException {
		clients.remove(getKey(username));
	}

	/**
	 * 组装sessionId
	 * 
	 * @param inquiryId
	 * @return
	 */
	public static String getKey(String username) {
		return username;
	}

	/**
	 * 判断是否有效连接 判断是否存在 判断连接是否开启 无效的进行清除
	 * 
	 * @param inquiryId
	 * @return
	 */
	public static boolean hasConnection(String username) {
		String key = getKey(username);
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
	 * 获得所有在线用户名
	 * 
	 * @return
	 */
	public static List<String> getUserName() {
		List<String> names = new ArrayList<String>(clients.keySet());
		return names;
	}

	/**
	 * 给某个用户发消息
	 * 
	 * @param username
	 * @param message
	 * @throws Exception
	 */
	public static void sendMessagetoUser(String username, String message)
			throws Exception {
		if (!hasConnection(username)) {
			throw new NullPointerException(getKey(username)
					+ " connection does not exist");
		}

		WebSocketSession session = get(username);
		try {
			session.sendMessage(new TextMessage(message));
		} catch (IOException e) {
			clients.remove(getKey(username));
		}
	}

	/**
	 * 给所有用户发送消息
	 *
	 * @param userName
	 * @param message
	 */
	public static void sendMessageToALL(String userName, String message) {
		Set<Entry<String, WebSocketSession>> users = clients.entrySet();
		for (Entry<String, WebSocketSession> user : users) {
			WebSocketSession session = user.getValue();
			if (session.isOpen()) {
				if (user.getKey().equals(userName)) {// 不给自己发
					continue;
				}
				try {
					session.sendMessage(new TextMessage(message));
					System.out.println("服务器发送信息" + message.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {// 连接关闭，移除无效链接
				String name = user.getKey();
				clients.remove(name);
			}
		}
	}

	public static String getName(WebSocketSession session) {
		String conntype = session.toString();
		String name = null;
		if (conntype.startsWith("SockJS")) {// sockjs
			List<String> cookie = session.getHandshakeHeaders().get("cookie");
			for (String string : cookie) {// 获取JSESSIONID=C039616D26280F997F401560D3736F17
				if (string.startsWith("JSESSIONID")) {
					name = string.substring(string.indexOf("=") + 1);
				}
			}
		} else {// websocket
			name = (String) session.getAttributes().get(
					Constants.WEBSOCKET_USERNAME);
		}
		return name;
	}
}
