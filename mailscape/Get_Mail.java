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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import mailscape.credentials;
public class Get_Mail
{
    static String fromList[]=new String[5];
    static String subList[]=new String[5];
    static String bodyList[]=new String[5];
    static int length=0;
    public static void main(String args[])
    {
        String protocol="imap";
        String host="imap.gmail.com";
        String username=credentials.username;
        String password=credentials.password;
        String port="993";
        //downloadEmails(protocol, host, port, username, password);
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
    public String[] downloadEmails(String protocol, String host, String port, String username, String password)
    {
        String result[]=new String[30];
        Scanner in=new Scanner(System.in);
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
            for(int i=n-1; i>=n-30; i--)
            {
                Message msg=messages[i];
                /*InternetAddress sender=(InternetAddress)msg.getFrom()[0];
                String from=sender.getAddress();
                fromList[n-1-i]=from;
                String subject=msg.getSubject();
                subList[n-1-i]=subject;
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
                bodyList[n-1-i]=bodypart;
                System.out.println();
                System.out.println(n-i+")");
                System.out.println("FROM: "+from);
                System.out.println("SUBJECT: "+subject);
                System.out.println("BODY: "+bodypart);
                System.out.println(); */
                result[n-1-i]=msg.getSubject();
            }
            //System.out.print("Enter mail no. to star: ");
            //int x=in.nextInt();
            //starMail(x-1);
            //System.out.print("Enter mail no. to download attachment: ");
            //int x=in.nextInt();
            //getAttach(protocol, host, port, username, password, x-1);
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
        //catch(IOException e){}
        return result;
    }
    public String[] getMail(String protocol, String host, String port, String username, String password, int index)
    {
        String result[]=new String[3];
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
            Message msg=messages[n-1-index];
            Object content=msg.getContent();
            if(content instanceof Multipart)
            {
                Multipart mp=(Multipart)content;
                for(int j=0; j<mp.getCount(); j++)
                {
                    BodyPart bp=mp.getBodyPart(j);
                    if(Pattern.compile(Pattern.quote("text/html"), Pattern.CASE_INSENSITIVE).matcher(bp.getContentType()).find())
                    {
                        result[2]=((String)bp.getContent());
                    }
                    else{}
                }
            }
            
            InternetAddress sender=(InternetAddress)msg.getFrom()[0];
            String from=sender.getAddress();
            String body="";
            String subject=msg.getSubject();
            if(msg.isMimeType("text/plain") && result[1]==null)
                result[2]=msg.getContent().toString();
            if(msg.isMimeType("multipart/*"))
            {
                try
                {
                    MimeMultipart mimemultipart=(MimeMultipart)msg.getContent();
                    if(result[2]==null)
                    result[2]=getTextFromMimeMultipart(mimemultipart);
                }
                catch(IOException e){}
            }
            result[0]=from;
            result[1]=subject;
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
        return result;
    }
    public String getTextFromMimeMultipart(MimeMultipart mimeMultipart)throws MessagingException
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
                    //System.out.println(result);
                    break;
                }
                else if(bodyPart.isMimeType("text/html"))
                {
                    //System.out.println("text html");
                    String html=(String)bodyPart.getContent();
                    //System.out.println(html);
                    result=result+org.jsoup.Jsoup.parse(html).text();
                }
                else if(bodyPart.getContent()instanceof MimeMultipart)
                {
                    //System.out.println("None");
                    result=result+getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
                    //System.out.println(result);
                }
            }
        }
        catch(IOException e){}
        return result;
    }
   public static void starMail(int x)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection(credentials.DB_URL, credentials.USER, credentials.PASS);
            Statement stmt=con.createStatement();
            bodyList[x]=bodyList[x].replace("'", "\\'");
            String sqlInsert="INSERT INTO Star(name, sub, body)"+"VALUES('"+fromList[x]+"', '"+subList[x]+"', '"+bodyList[x]+"')";
            int countInserted=stmt.executeUpdate(sqlInsert);
            if(countInserted!=0)
            System.out.println("Email starred successfully");
            con.close();
        }
        catch(Exception event){System.out.println(event);}
    }
    public void getAttach(String protocol, String host, String port, String userName, String password, int index)
    {
        String save="C:/Users/aruno/Downloads/";
        String result[]=new String[2];
        Properties properties=getServerProperties(protocol, host, port);
        Session session=Session.getDefaultInstance(properties);
        try
        {
            Store store=session.getStore(protocol);
            store.connect(userName, password);
            Folder folderInbox=store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
            Message[] messages=folderInbox.getMessages();
            int n=messages.length;
            Message msg=messages[n-1-index];
            InternetAddress sender=(InternetAddress)msg.getFrom()[0];
            String from=sender.getAddress();
            if(msg.isMimeType("text/plain")){}
            //System.out.println("No attachment exists in mail");
            if(msg.isMimeType("multipart/*"))
            {
                try
                {
                    //String filename="";
                    MimeMultipart mimeMultipart=(MimeMultipart)msg.getContent();
                    for(int j=0; j<mimeMultipart.getCount(); j++)
                    {
                        MimeBodyPart part=(MimeBodyPart)mimeMultipart.getBodyPart(j);
                        if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
                        {
                            if(part.getFileName()!=null)
                            {
                                //filename+=part.getFileName();
                                part.saveFile(save+part.getFileName());
                                System.out.println("Saved");
                            }
                        }
                    }
                    //if(filename=="")
                    //System.out.println("No attachment exists in mail");
                }
                catch(IOException e){}
                folderInbox.close(false);
                store.close();
            }
        }
        catch(NoSuchProviderException ex)
        {
            System.out.println("No provider for protocol "+protocol);
            ex.printStackTrace();;
        }
        catch(MessagingException ex)
        {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
    }
    public boolean attachName(String protocol, String host, String port, String username, String password, int index)
    {
        String save="C:/Users/aruno/Downloads/";
        String res="";
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
            Message msg=messages[n-1-index];
            InternetAddress sender=(InternetAddress)msg.getFrom()[0];
            String from=sender.getAddress();
            if(msg.isMimeType("text/plain")){}
            if(msg.isMimeType("multipart/*"))
            {
                try
                {
                    //String filename="";
                    MimeMultipart mimeMultipart=(MimeMultipart)msg.getContent();
                    for(int j=0; j<mimeMultipart.getCount(); j++)
                    {
                        MimeBodyPart part=(MimeBodyPart)mimeMultipart.getBodyPart(j);
                        if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
                        {
                            /*if(part.getFileName()!=null)
                            {
                                filename+=part.getFileName();
                                part.saveFile(save+part.getFileName());
                                System.out.println("Saved");
                            }*/
                        }
                        res=part.getFileName();                        
                    }
                    //if(filename=="")
                    //System.out.println("No attachment exists in mail");
                }
                catch(IOException e){}
                folderInbox.close(false);
                store.close();
            }
                
        }
        catch(NoSuchProviderException ex)
        {
            System.out.println("No provider for protocol "+protocol);
            ex.printStackTrace();;
        }
        catch(MessagingException ex)
        {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
        if(res==null)
        return false;
        else
        return true;
    }
}
