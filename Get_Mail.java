/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.util.regex.Pattern;
import mailscape.credentials;
public class Get_Mail
{
    public static void main(String args[])
    {
        String protocol="imap";
        String host="imap.gmail.com";
        String username=credentials.username;
        String password=credentials.password;
        String port="993";
        downloadEmails(protocol, host, port, username, password);
    }
    public static Properties getServerProperties(String protocol, String host, String port)
    {
        Properties properties=new Properties();
        properties.put(String.format("mail.%s.host", protocol),host);
        properties.put(String.format("mail.%s.port", protocol), port);
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
        properties.setProperty(String.format("mail.%s.socketFactory.class", port), String.valueOf(port));
        properties.setProperty("mail.imaps.partialfetch", "false");
        return properties;
    }
    public static void downloadEmails(String protocol, String host, String port, String username, String password)
    {
        String result[]=new String[30];
        Properties properties=getServerProperties(protocol, host, port);
        Session session=Session.getDefaultInstance(properties);
        try
        {
            Store store=session.getStore(protocol);
            store.connect(username, password);
            Folder folderInbox=store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
            Message[] messages=folderInbox.getMessages();
            int n=messages.length;
            for(int i=n-1; i>=n-5; i--)
            {
                Message msg=messages[i];
                InternetAddress sender=(InternetAddress)msg.getFrom()[0];
                String from=sender.getAddress();
                String subject=msg.getSubject();
                String bodypart="";
                if(msg.isMimeType("text/plain") && result[1]==null)
                {
                    bodypart=msg.getContent().toString();
                }
                if(msg.isMimeType("multipart/*"))
                {
                    try
                    {
                        MimeMultipart mimeMultipart=(MimeMultipart)msg.getContent();
                        if(result[1]==null)
                        bodypart=getTextFromMimeMultipart(mimeMultipart);
                    }
                    catch(IOException e)
                    {
                    }
                }
                System.out.println();
                System.out.println(n-i+")");
                System.out.println("FROM: "+from);
                System.out.println("SUBJECT: "+subject);
                System.out.println("BODY: "+bodypart);
                System.out.println();
            }
            folderInbox.close(false);
            store.close();
        }
        catch(NoSuchProviderException ex)
        {
            System.out.println("No provider for Protocol "+protocol);
            ex.printStackTrace();
        }
        catch(MessagingException ev)
        {
            System.out.println("Could not connect to message store");
            ev.printStackTrace();
        }
        catch(IOException e){}
    }
    public static String getTextFromMimeMultipart(MimeMultipart mimeMultipart)throws MessagingException
    {
        String result="";
        int count=mimeMultipart.getCount();
        try
        {
            for(int i=0;i<count;i++)
            {
                BodyPart bodyPart=mimeMultipart.getBodyPart(i);
                if(bodyPart.isMimeType("text/plain"))
                {
                    result=result+bodyPart.getContent();
                    break;
                }
                else if(bodyPart.isMimeType("text/html"))
                {
                    String html=(String)bodyPart.getContent();
                    result=result+org.jsoup.Jsoup.parse(html).text();
                }
                else if(bodyPart.getContent()instanceof MimeMultipart)
                {
                    result=result+getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
                }
            }
        }
        catch(IOException e){}
        return result;
    }
}
