/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

import java.util.Properties;
import java.util.*;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Multipart;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetAddress;
import java.io.*;
import javax.mail.Part;
import java.util.regex.Pattern;
 
public class getmailgui {
    
    public static void main(String[] args) 
	{
		String protocol="imap",host="imap.gmail.com",username=credentialsstar.username,password=credentialsstar.password,port="993";
		//downloadEmails(protocol,host,port,username,password);
		}
    
    public Properties getServerProperties(String protocol, String host,
            String port) {
        Properties properties = new Properties();
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),"javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol),"false");
        properties.setProperty(String.format("mail.%s.socketFactory.port", protocol),String.valueOf(port));
        properties.setProperty("mail.imaps.partialfetch", "false");
 
        return properties;
    }
    public String[] getMail(String protocol, String host, String port,
            String userName, String password,int index) {
         String result[] = new String[2];
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
 
        try {
            Store store = session.getStore(protocol);
            store.connect(userName, password);
            Folder folderInbox = store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
 
            // fetches new messages from server
            Message[] messages = folderInbox.getMessages(); 
            
             int n = messages.length;
            /*int c=-1;
            for (int i = n-1; i>=n-30; i--) {
                c++;
                if(c==index){*/
                    Message msg = messages[n-1-index];
                    
                    Object content = msg.getContent();
if (content instanceof Multipart) {
    Multipart mp = (Multipart) content;
    for (int j = 0; j < mp.getCount(); j++) {
        BodyPart bp = mp.getBodyPart(j);
        if (Pattern.compile(Pattern.quote("text/html"),Pattern.CASE_INSENSITIVE).matcher(bp.getContentType()).find()) {
             result[1]=((String) bp.getContent());
        } else {
             
            
        }
    }
}
                    
                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
                String from = sender.getAddress();
                        String body="";
                        String subject = msg.getSubject();  
                  if (msg.isMimeType("text/plain")&& result[1]==null) {
                      result[1] = msg.getContent().toString();
                    } 
                  if (msg.isMimeType("multipart/*")) {
                       try{
                   MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                      if(result[1]==null)
                  result[1]= getTextFromMimeMultipart(mimeMultipart);
                       }catch(IOException e){}
                       }
                    result[0]=subject;
                   
         
            
            folderInbox.close(false);
            store.close();
    
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException ex){
        }
        
       return result;

    }
    public String[] downloadEmails(String protocol, String host, String port,
            String userName, String password) {
         String result[] = new String[30];
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties); 
        try {
            Store store = session.getStore(protocol);
            store.connect(userName, password);
            
            Folder folderInbox = store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
            Message[] messages = folderInbox.getMessages(); 
             int n = messages.length;
            
            for (int i = n-1; i>=n-30; i--) {
                Message msg = messages[i];
                
//                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
//                String from = sender.getAddress();
                result[n-i-1]=msg.getSubject();
               
            }
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
        return result;
    }
 
        public String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException{
    String result = "";
    int count = mimeMultipart.getCount();
  try{
        for (int i = 0; i < count; i++) {
        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
       
        if (bodyPart.isMimeType("text/plain")) 
        {
           result  += bodyPart.getContent();
            System.out.println(result);
            break; 
        } 
        else if (bodyPart.isMimeType("text/html")) 
        {
             System.out.println("text html");
            String html = (String) bodyPart.getContent();
            System.out.println(html);
            result += org.jsoup.Jsoup.parse(html).text();
        } 
        else if (bodyPart.getContent() instanceof MimeMultipart)
        {
             System.out.println("none");
            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
             System.out.println(result);
        }
    }
  }catch(IOException e){}
    return result;
}
    
      public void getAttach(String protocol, String host, String port, String userName, String password,int index)
      {
          String save = "/home/jayshree/Downloads/";
          String result[] = new String[2];
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
 
        try {
            Store store = session.getStore(protocol);
            store.connect(userName, password);
            Folder folderInbox = store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
            Message[] messages = folderInbox.getMessages(); 
             int n = messages.length;
       
                    Message msg = messages[n-1-index];
                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
                String from = sender.getAddress();
                          
                  if (msg.isMimeType("text/plain")) {
                       
                  
                    } 
                  if (msg.isMimeType("multipart/*")) {
                       try{
                   MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                   for (int j = 0; j < mimeMultipart.getCount(); j++) {
                         MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(j);
                          if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                  
                           part.saveFile(save + part.getFileName());
                             System.out.println("Saved");
                         }
                   } 
                   
                   
                       }catch(IOException e){}
                  }
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }  
       
       
    }
      public String parseAddresses(Address[] address) {
        String listAddress = "";
 
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                listAddress += address[i].toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }
 
        return listAddress;
    }
      public boolean attachName(String protocol, String host, String port,
            String userName, String password,int index) {
          String save = "/home/jayshree/Downloads/";
          String res ="";
      
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
 
        try {
             
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(userName, password);
               
            // opens the inbox folder
            Folder folderInbox = store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
 
            // fetches new messages from server
            Message[] messages = folderInbox.getMessages(); 
//            msg.mes = messages;
             int n = messages.length;
       
                    Message msg = messages[n-1-index];
                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
                String from = sender.getAddress();
                          
                  if (msg.isMimeType("text/plain")) {
                       
                  
                    } 
                  if (msg.isMimeType("multipart/*")) {
                       try{
                   MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                   for (int j = 0; j < mimeMultipart.getCount(); j++) {
                         MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(j);
                          if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                  
                            
                         }
                          res=part.getFileName();
                   } 
                   
                       }catch(IOException e){}
                       }
            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }  
       if(res==null)
       return false;
       else
           return true;
    }
      
     
}
