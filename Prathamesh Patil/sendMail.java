package project;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class sendMail {

	public static void send_now(String from, String to, String sub, String body) throws IOException {
		
		String host = "smtp.gmail.com";
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port", "587");
		
		String pass= "gani2267bhai";
		
		Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator() {
			 protected PasswordAuthentication getPasswordAuthentication() {  
				    return new PasswordAuthentication(from,"dxfssndbqeohyoyb");  
			 }
		});
		
		try {
			file_picker ob = new file_picker();
			String path = ob.get_path();
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(sub);
			
			if(path != "") {
				//attachment file to be sent
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(body,"text/html");
				
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);    //first half of multipart is text
				MimeBodyPart file = new MimeBodyPart();
				file.attachFile(path);
				multipart.addBodyPart(file);               //second half of multipart is attachment
				message.setContent(multipart,"text/html");
				
			}else {
				message.setContent(body,"text/html");
			}
			
			Transport.send(message);
			System.out.println("Mail successful");
		}
		catch(MessagingException mex) {
			System.out.println("not sent");
			
		}
		catch(IOException e) {
			
		}
		
	}

}
