/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */      
package mailscape;

import javax.mail.*;
import javax.activation.*;
import java.util.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import javax.swing.JFileChooser;

public class sendmail {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc= new Scanner(System.in);
        String from = "maurya2026@gmail.com";
        String to = sc.nextLine();
        String sub = sc.nextLine();
        String body = sc.nextLine();
        sendmail(from, to, sub, body);
    }
    public static void sendmail(String from, String to, String sub, String body){
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication("maurya2026@gmail.com","rurtkddzjjzdgjaz");
                    }
                });
            try{
                file_picker ob = new file_picker();
                String path = ob.get_path();
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
                message.setSubject(sub);
                message.setContent(body, "text/html");
                if(path!="") {
                    //attachment to be sent
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(body, "text/html");
                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);
                    MimeBodyPart file = new MimeBodyPart();
                    file.attachFile(path);
                    multipart.addBodyPart(file);
                    message.setContent(multipart, "text/html");
                    
                }
                else
                message.setContent(body, "text/html");
                Transport.send(message);
            }
            catch(MessagingException ev)
            {
                throw new RuntimeException(ev);
            }
            catch(IOException e){
                
            }
    }
    
}
