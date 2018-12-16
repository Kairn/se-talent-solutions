package com.revature.sets.utility;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UtilityManager {

	// Get JDBC connection
	public static Connection getConnection() {

		Connection conn = null;

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream in = cl.getResourceAsStream("connection.properties");
		Properties props = new Properties();
		try {
			props.load(in);
			conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"),
					props.getProperty("password"));
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}

		return conn;

	}

	// Send an email via Yahoo server
	public static void sendEmail(String recipient, String subject, String content) {

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream in = cl.getResourceAsStream("email.properties");
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.mail.yahoo.com");
		props.put("mail.smtp.port", "587");
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final String username = props.getProperty("username");
		final String password = props.getProperty("password");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(username));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(subject);
			message.setText(content);
			Transport.send(message);
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	// Transform a raw password string into a SHA-256 hash string
	public static String digestSHA256(String message) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		StringBuilder digest = new StringBuilder("");
		md.update(message.getBytes());
		byte[] bytes = md.digest();
		for (byte b : bytes) {
			digest.append(String.format("%02X", b));
		}

		return new String(digest);

	}

}
