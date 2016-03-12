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
	public static final int SYSTEM_MSG = 0;// ϵͳ��Ϣ
	public static final int USER_MSG = 1;// �û���Ϣ
	public static final String TO_ALL = "All";// ��������
	private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();
	private static BeanFactory beanFactory = null;

	/**
	 * ����һ������
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
		sendUserComing(username);// ���ͽ�����Ϣ
	}

	/**
	 * ��ȡһ������
	 * 
	 * @param inquiryId
	 * @return
	 */
	public static WebSocketSession get(String username) {
		return clients.get(getKey(username));
	}

	/**
	 * �Ƴ�һ������
	 * 
	 * @param inquiryId
	 */
	public static void remove(String username) throws IOException {
		clients.remove(getKey(username));
		sendUserLeave(username);// ���ͽ�����Ϣ
	}

	/**
	 * ��װsessionId
	 * 
	 * @param inquiryId
	 * @return
	 */
	public static String getKey(String username) {
		return username;
	}

	/**
	 * �ж��Ƿ���Ч���� �ж��Ƿ���� �ж������Ƿ��� ��Ч�Ľ������
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
	 * ��ȡ������������
	 * 
	 * @return
	 */
	public static int getSize() {
		return clients.size();
	}

	/**
	 * ������������û���
	 * 
	 * @return
	 */
	public static List<String> getUserName() {
		List<String> names = new ArrayList<String>(clients.keySet());
		return names;
	}

	/**
	 * �û�������������Ϣ0#message
	 * 
	 * @param username
	 */
	private static void sendUserComing(String username) {
		String message = SYSTEM_MSG + "#" + "�û�" + username + "���������ң�welcome!";
		sendMessageToALL(username, message);
	}

	/**
	 * �û��뿪��������Ϣ0#message
	 * 
	 * @param username
	 */
	private static void sendUserLeave(String username) {
		String message = SYSTEM_MSG + "#" + "�û�" + username + "�뿪�����ң�bye~";
		sendMessageToALL(username, message);
	}

	/**
	 * ��ĳ���û�����Ϣ
	 * 
	 * @param username
	 *            ������Ϣ���û���
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
	 * �������û�������Ϣ
	 *
	 * @param userName
	 *            �ͻ����û���
	 * @param message
	 *            �����ͻ��˵���Ϣ
	 */
	public static void sendMessageToALL(String fromName, String message) {
		Set<Entry<String, WebSocketSession>> users = clients.entrySet();
		for (Entry<String, WebSocketSession> user : users) {
			WebSocketSession session = user.getValue();
			if (session.isOpen()) {
				if (user.getKey().equals(fromName)) {// �����Լ���
					continue;
				}
				try {
					session.sendMessage(new TextMessage(message));
					System.out.println("������������Ϣ" + message.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {// ���ӹرգ��Ƴ���Ч����
				String name = user.getKey();
				clients.remove(name);
			}
		}
	}

	/**
	 * ��ÿͻ����û���
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
	 * ����Sockjs��ÿͻ����û���
	 * 
	 * @param head
	 * @return
	 */
	private static String analyzeNameGetCookie(HttpHeaders head) {
		List<String> cookie = head.get("cookie");
		String name = null;
		// �Ƚ�������� cookie������һ�����ϣ�����һ���ַ���
		if (cookie.size() <= 1) {// Ĭ��jsessionid
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

		// ������ʽ�÷��ο�API
		Pattern pattern = Pattern.compile(zz);
		Matcher m = pattern.matcher(unicode);
		while (m.find()) {
			list.add(m.group());
		}
		for (int i = 0, j = 2; i < list.size(); i++) {
			String st = list.get(i).substring(j, j + 4);

			// ���õ�����ֵ����16���ƽ���Ϊʮ�����������ُ�תΪ�ַ�
			char ch = (char) Integer.parseInt(st, 16);
			// �õõ����ַ��滻������ʽ
			unicode = unicode.replace(list.get(i), String.valueOf(ch));
		}
		return unicode;
	}

	/**
	 * @param name
	 *            �ͻ�������
	 * @param string
	 *            �ͻ��˷�������Ϣ
	 */
	public static void sendMessage(String name, String string) throws Exception {
		Map<String, String> result = analyzeMessage(string);
		String message = result.get("message");
		if (!canSpeak(name)) {
			return;
		}
		String toName = result.get("toName");
		if (toName.equals(TO_ALL)) {// ����1#name@message
			String msg = USER_MSG + "#" + TO_ALL + "@" + name + " : " + message;
			sendMessageToALL(name, msg);
		} else {// ˽��
			String msg = USER_MSG + "#" + toName + "@" + name + " : " + message;
			sendMessagetoUser(toName, msg);
		}
	}

	private static boolean canSpeak(String name) throws Exception {
		ISpeakService service = (ISpeakService) beanFactory
				.getBean("ISpeakService");
		boolean result = service.canSpeak(new User(name, null, null));
		if (!result) {// ������
			String message = SYSTEM_MSG + "#" + "�Բ����������ԣ�";
			sendMessagetoUser(name, message);
			return false;
		}
		return true;
	}

	/**
	 * �����ͻ�����Ϣ,������Ϣ��ʽ
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
