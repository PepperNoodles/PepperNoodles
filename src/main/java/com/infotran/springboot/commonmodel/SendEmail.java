package com.infotran.springboot.commonmodel;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class SendEmail {
	
	public String getRandom() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}
	
	public boolean sendEmail(UserAccount user) {
		boolean test = false;
		String toEmail = user.getAccountIndex();
		System.out.println(toEmail);
		String fromEmail = "chrislo5311@gmail.com";
		String password = "yxkvbvbjasbfooya";
		try {
			Properties pr = new Properties();
			pr.setProperty("mail.smtp.host", "smtp.gmail.com");
			pr.setProperty("mail.smtp.port", "587");
			pr.setProperty("mail.smtp.auth", "true");
			pr.setProperty("mail.smtp.starttls.enable", "true");
			//for Google Gmail
			pr.put("mail.smtp.socketFactory.port", "587");
			pr.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			//get session
			Session session = Session.getInstance(pr, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			});
			Message mess = new MimeMessage(session);
			mess.setFrom(new InternetAddress(fromEmail));
			System.out.println("1");
			mess.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail) );
			System.out.println("2");
			mess.setSubject("User Email Varification");
			mess.setText("假的驗證碼已寄出" + user.getCode());
			Transport.send(mess);
			test=true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return test;
	}
}
