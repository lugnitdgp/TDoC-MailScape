/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mailscape;
import javax.mail.*;
import javax.activation.*;
import java.util.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
import java.io.*;
import javax.mail.internet.MimeBodyPart;
import static mailscape.Get_Email.getServerProperties;
/**
 *
 * @author laxmikanth
 */
public class Send_Mail {
        String from="";
        String to="";
        String sub="";
        String body="";
        String path="";
        String pass="";
        boolean flag=true;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc=new Scanner(System.in);
    }
    Send_Mail(String a,String b,String c,String p,String user,String password){
        to=a;
        sub=b;
        body=c;
        path=p;
        pass=password;
        from=user;
        
    }
    public void send_mail(){
        String host="smtp.gmail.com";
        Properties props=new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",host);
        props.put("mail.smtp.port","587");
        
        Session session=Session.getInstance(props, 
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication(){
                        
                        return new PasswordAuthentication(from,pass);
                    }
                }
                );
        try{
//            file_picker ob=new file_picker();
//            path=ob.get_path();
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            message.setSubject(sub);
            if("".equals(path)){
               message.setContent(body,"text/html");
                
            }
            else{
                 
                 BodyPart messageBodyPart=new MimeBodyPart();
               messageBodyPart.setContent(body,"text/html");
               Multipart multipart=new MimeMultipart();
               multipart.addBodyPart(messageBodyPart);
               MimeBodyPart file=new MimeBodyPart();
               file.attachFile(path);
               multipart.addBodyPart(file);
               message.setContent(multipart,"text/html");
            }
            
           
            Transport.send(message);
            
            
        }
        catch(MessagingException ev){
            flag=false;
            throw new RuntimeException(ev);
            
        }
        catch(IOException e){
        
        }
    }
   
}
