package com.newtonk.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.newtonk.entity.User;
import com.newtonk.interceptor.Constants;
import com.newtonk.service.ISpeakService;

public class SocketSessionUtil implements BeanFactoryAware {
	public static final int SYSTEM_MSG = 0;// 系统消息
	public static final int USER_MSG = 1;// 用户消息
	public static final String TO_ALL = "All";// 向所有人
	private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();
	private static BeanFactory beanFactory = null;

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
		sendUserComing(username);// 发送进入信息
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
		sendUserLeave(username);// 发送进入信息
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
	 * 用户进入聊天室消息0#message
	 * 
	 * @param username
	 */
	private static void sendUserComing(String username) {
		String message = SYSTEM_MSG + "#" + "用户" + username + "进入聊天室，welcome!";
		sendMessageToALL(username, message);
	}

	/**
	 * 用户离开聊天室消息0#message
	 * 
	 * @param username
	 */
	private static void sendUserLeave(String username) {
		String message = SYSTEM_MSG + "#" + "用户" + username + "离开聊天室，bye~";
		sendMessageToALL(username, message);
	}

	/**
	 * 给某个用户发消息
	 * 
	 * @param username
	 *            接受消息的用户名
	 * @param message
	 * @throws Exception
	 */
	public static void sendMessagetoUser(String toname, String message)
			throws Exception {
		if (!hasConnection(toname)) {
			throw new NullPointerException(getKey(toname)
					+ " connection does not exist");
		}

		WebSocketSession session = get(toname);
		try {
			session.sendMessage(new TextMessage(message));
		} catch (IOException e) {
			clients.remove(getKey(toname));
		}
	}

	/**
	 * 给所有用户发送消息
	 *
	 * @param userName
	 *            客户端用户名
	 * @param message
	 *            发给客户端的消息
	 */
	public static void sendMessageToALL(String fromName, String message) {
		Set<Entry<String, WebSocketSession>> users = clients.entrySet();
		for (Entry<String, WebSocketSession> user : users) {
			WebSocketSession session = user.getValue();
			if (session.isOpen()) {
				if (user.getKey().equals(fromName)) {// 不给自己发
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

	/**
	 * 获得客户端用户名
	 * 
	 * @param session
	 * @return
	 */
	public static String getName(WebSocketSession session) {
		String conntype = session.toString();
		String name = null;
		if (conntype.startsWith("SockJS")) {// sockjs
			HttpHeaders cookie = session.getHandshakeHeaders();
			name = analyzeNameGetCookie(cookie);
		} else {// websocket
			name = (String) session.getAttributes().get(
					Constants.WEBSOCKET_USERNAME);
		}
		return name;
	}

	/**
	 * 分析Sockjs获得客户端用户名
	 * 
	 * @param head
	 * @return
	 */
	private static String analyzeNameGetCookie(HttpHeaders head) {
		List<String> cookie = head.get("cookie");
		String name = null;
		// 比较奇葩的是 cookie并不是一个集合，而是一个字符串
		if (cookie.size() <= 1) {// 默认jsessionid
			String[] cookies = ((String) cookie.get(0)).split(";");
			for (String string : cookies) {
				if (string.trim().startsWith("username")) {
					name = string.substring(string.indexOf("=") + 1);
					name = unicode2String(name);
				}
			}
		}
		return name;
	}

	public static String unicode2String(String unicode) {
		List<String> list = new ArrayList<String>();
		String zz = "%u[0-9,a-z,A-Z]{4}";

		// 正则表达式用法参考API
		Pattern pattern = Pattern.compile(zz);
		Matcher m = pattern.matcher(unicode);
		while (m.find()) {
			list.add(m.group());
		}
		for (int i = 0, j = 2; i < list.size(); i++) {
			String st = list.get(i).substring(j, j + 4);

			// 将得到的数值按照16进制解析为十进制整数，再強转为字符
			char ch = (char) Integer.parseInt(st, 16);
			// 用得到的字符替换编码表达式
			unicode = unicode.replace(list.get(i), String.valueOf(ch));
		}
		return unicode;
	}

	/**
	 * @param name
	 *            客户端姓名
	 * @param string
	 *            客户端发来的消息
	 */
	public static void sendMessage(String name, String string) throws Exception {
		Map<String, String> result = analyzeMessage(string);
		String message = result.get("message");
		if (!canSpeak(name)) {
			return;
		}
		String toName = result.get("toName");
		if (toName.equals(TO_ALL)) {// 公聊1#name@message
			String msg = USER_MSG + "#" + TO_ALL + "@" + name + " : " + message;
			sendMessageToALL(name, msg);
		} else {// 私聊
			String msg = USER_MSG + "#" + toName + "@" + name + " : " + message;
			sendMessagetoUser(toName, msg);
		}
	}

	private static boolean canSpeak(String name) throws Exception {
		ISpeakService service = (ISpeakService) beanFactory
				.getBean("ISpeakService");
		boolean result = service.canSpeak(new User(name, null, null));
		if (!result) {// 被禁言
			String message = SYSTEM_MSG + "#" + "对不起您被禁言！";
			sendMessagetoUser(name, message);
			return false;
		}
		return true;
	}

	/**
	 * 解析客户端消息,返回消息方式
	 * 
	 * @param string
	 *            name@message
	 * @return
	 */
	private static Map<String, String> analyzeMessage(String string) {
		String[] result = string.trim().split("@");
		Map<String, String> back = new HashMap<String, String>();
		back.put("toName", result[0].trim());
		back.put("message", result[1]);
		return back;
	}

	@Override
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		beanFactory = arg0;
	}

}
