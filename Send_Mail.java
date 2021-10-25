/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mailscape;

/**
 *
 * @author aruno
 */
import javax.mail.*;
import javax.activation.*;
import java.util.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
import java.io.*;
public class Send_Mail
{

    /**
     * @param args the command line arguments
     */
    String from="ac.20u10162@btech.nitdgp.ac.in";
    String to="";
    String sub="";
    String body="";
    String path="";
    public Send_Mail(String a, String b, String c, String d)
    {
        to=a;
        sub=b;
        body=c;
        path=d;
    }
    public static void main(String[] args)
    {    // TODO code application logic here
        Scanner in=new Scanner(System.in);
        
    }
    public void send_mail()
    {
        String host="smtp.gmail.com";
        Properties props=new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        
        Session session=Session.getInstance(props, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("ac.20u10162@btech.nitdgp.ac.in", "wzutpyjldqcybafe");
            }
        }
        );
        try
        {
            //file_picker ob=new file_picker();
            //String path=ob.getPath();
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(sub);
            if(path!="")
            {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(body, "text/html");
                Multipart multipart=new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                MimeBodyPart file=new MimeBodyPart();
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
        catch(IOException e)
        {
        }
    }
}
