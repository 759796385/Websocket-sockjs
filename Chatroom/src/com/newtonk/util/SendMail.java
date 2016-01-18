package com.newtonk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	private static String mailserver;// ����������ַ
	private static String from;// ������
	private static String password;// ��������
	private static String to;// �ռ���
	private static String subject;// ����
	private static String message;// �ʼ�����
	static {
		Properties pro = new Properties();
		try {
			String filename = "mail.properties";
			String path = SendMail.class.getClassLoader().getResource("")
					.toURI().getPath();
			pro.load(new FileInputStream(new File(path + filename)));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		mailserver = pro.getProperty("mailserver");
		from = pro.getProperty("from");
		password = pro.getProperty("password");
		subject = pro.getProperty("subject");
	}

	/**
	 * �ռ��� �����ַ
	 * 
	 * @param content
	 * @throws Exception
	 */
	public static void send(String emailaddress, String content)
			throws Exception {
		InternetAddress[] address = null;
		to = emailaddress; // �ռ���
		message = "<center><h2>��ӭ��ע��������</h2><p style='color:red'>����������ӽ���������֤,���ʼ���Сʱ����Ч</p><a href="
				+ content + ">�����յ��˶�������������</a></center>"; // �ʼ�����
		Properties pro = null;
		pro = System.getProperties();
		pro.put("mail.smtp.host", mailserver); // �趨Mail������
		pro.put("mail.smtp.auth", "true"); // ��ʹ�ô���Э��

		// �����û�
		MySecurity mses = new MySecurity(from, password);
		Session mailSession = Session.getDefaultInstance(pro, mses); // �����µ�session
		mailSession.setDebug(false); // ����Ҫ����
		Message msg = new MimeMessage(mailSession); // �����µ��ʼ���Ϣ
		try {
			msg.setFrom(new InternetAddress(from)); // �趨������
			address = InternetAddress.parse(to, false); // �趨�����ʼ��������˵�����

			msg.setRecipients(Message.RecipientType.TO, address); // �趨�ʼ����ͷ�ʽ
			msg.setSubject(subject); // �趨����
			msg.setSentDate(new Date()); // �趨����ʱ��
			// msg.setText(message);
			msg.setContent(message, "text/html;charset = gbk");// �趨��������
			Transport.send(msg, msg.getAllRecipients());
			System.out.println("�ʼ����ͳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
