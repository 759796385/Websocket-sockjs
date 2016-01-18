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
	private static String mailserver;// 邮箱主机地址
	private static String from;// 发件人
	private static String password;// 邮箱密码
	private static String to;// 收件人
	private static String subject;// 主题
	private static String message;// 邮件内容
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
	 * 收件人 邮箱地址
	 * 
	 * @param content
	 * @throws Exception
	 */
	public static void send(String emailaddress, String content)
			throws Exception {
		InternetAddress[] address = null;
		to = emailaddress; // 收件人
		message = "<center><h2>欢迎您注册聊天室</h2><p style='color:red'>点击以下连接进行邮箱验证,本邮件半小时内有效</p><a href="
				+ content + ">棒棒哒的人都点我设置密码</a></center>"; // 邮件内容
		Properties pro = null;
		pro = System.getProperties();
		pro.put("mail.smtp.host", mailserver); // 设定Mail服务器
		pro.put("mail.smtp.auth", "true"); // 所使用传输协议

		// 创建用户
		MySecurity mses = new MySecurity(from, password);
		Session mailSession = Session.getDefaultInstance(pro, mses); // 产生新的session
		mailSession.setDebug(false); // 不需要调试
		Message msg = new MimeMessage(mailSession); // 创建新的邮件信息
		try {
			msg.setFrom(new InternetAddress(from)); // 设定发件人
			address = InternetAddress.parse(to, false); // 设定传送邮件到收信人的信箱

			msg.setRecipients(Message.RecipientType.TO, address); // 设定邮件发送方式
			msg.setSubject(subject); // 设定主题
			msg.setSentDate(new Date()); // 设定发送时间
			// msg.setText(message);
			msg.setContent(message, "text/html;charset = gbk");// 设定发送内容
			Transport.send(msg, msg.getAllRecipients());
			System.out.println("邮件发送成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
