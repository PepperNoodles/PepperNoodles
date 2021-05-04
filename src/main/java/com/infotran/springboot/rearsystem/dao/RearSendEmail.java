package com.infotran.springboot.rearsystem.dao;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

import com.infotran.springboot.commonmodel.UserAccount;

@Component
public class RearSendEmail {
	
//    private static final String lowStr = "abcdefghijklmnopqrstuvwxyz";
//    private static final String specialStr = "~!@#$%^&*()_+/-=[]{};:'<>?.";
//    private static final String numStr = "0123456789";
//    
//    // 随机获取字符串字符
//    private static char getRandomChar(String str) {
//        SecureRandom random = new SecureRandom();
//        return str.charAt(random.nextInt(str.length()));
//    }
//
//    // 随机获取小写字符
//    private static char getLowChar() {
//        return getRandomChar(lowStr);
//    }
//
//    // 随机获取大写字符
//    private static char getUpperChar() {
//        return Character.toUpperCase(getLowChar());
//    }
//
//    // 随机获取数字字符
//    private static char getNumChar() {
//        return getRandomChar(numStr);
//    }
//
//    // 随机获取特殊字符
//    private static char getSpecialChar() {
//        return getRandomChar(specialStr);
//    }
//
//    //指定调用字符函数
//    private static char getRandomChar(int funNum) {
//        switch (funNum) {
//            case 0:
//                return getLowChar();
//            case 1:
//                return getUpperChar();
//            case 2:
//                return getNumChar();
//            default:
//                return getSpecialChar();
//        }
//    }
//
//    // 指定长度，随机生成复杂密码
//    public  String getRandomPwd() {
//
//        List<Character> list = new ArrayList<>(8);
//        list.add(getLowChar());
//        list.add(getUpperChar());
//        list.add(getNumChar());
//        list.add(getSpecialChar());
//
//        for (int i = 4; i < 8; i++) {
//            SecureRandom random = new SecureRandom();
//            int funNum = random.nextInt(4);
//            list.add(getRandomChar(funNum));
//        }
//
//        Collections.shuffle(list);   // 打乱排序
//        StringBuilder stringBuilder = new StringBuilder(list.size());
//        for (Character c : list) {
//            stringBuilder.append(c);
//        }
//
//        return stringBuilder.toString();
//    }
//
//
//	
//	public String getRandom() {
//		Random rnd = new Random();
//		int number = rnd.nextInt(999999);
//		return String.format("%06d", number);
//	}
//	
//	
//	public String getRandompassword() {
//		final int maxnum = 45;//45是因為數組是0開始，26個字母+10个數字+9個特殊字元
//		int i; //生成的隨機數
//		int count = 0; //生成的密碼的長度
//		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
//		'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
//		'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' ,'~', '!','@','#','$','%','^','&','*'};
//		StringBuffer pwd = new StringBuffer("");
//		Random r = new Random();
//		while(count < 6){
//			//生成隨機數，取絕對值，防止生成負數，
//			i = Math.abs(r.nextInt(maxnum)); //生成的数最大为36-1
//			if (i >= 0 && i < str.length) {
//			pwd.append(str[i]);
//			count ++;
//			}
//			
//			}
//		
//			System.out.println(pwd.toString());
//			return pwd.toString();
//
//	}


	
	
	public boolean sendRightEmail(UserAccount user) { //停權人信箱
		boolean test = false;
		String toEmail = user.getAccountIndex();
		System.out.println(toEmail);
		String fromEmail = "chrislo5311@gmail.com"; //系統信箱
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
			//get session 驗證系統
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
			mess.setSubject("User right notiffication");
			if(user.isEnabled()) {
				mess.setText("停權通知: STOP(停止使用權限)"); //信件寄出的內容

			}else {
				mess.setText("停權通知: REVIVE(開通使用權限)"); //信件寄出的內容

			}
			Transport.send(mess);
			test=true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return test;
	}
}
