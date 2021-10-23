/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.util.regex.Pattern;
import mailscape.Credentials;
import java.util.*;
import java.util.Scanner;
import java.sql.*;
/**
 *
 * @author laxmikanth
 */
public class Get_Email {
    
    static String fromList[]=new String[5];
    static String subList[]=new String[5];
    static String bodyList[]=new String[5];
    static int legnth=0;
    public static void main(String args[]) throws MessagingException, IOException, ClassNotFoundException, SQLException{
        
        String protocol="imap";
        String host="imap.gmail.com";
        String user=Credentials.username;
        String password=Credentials.password;
        String port="993";
        downloadEmails(protocol,host,port,user,password);
    }
    public static Properties getServerProperties(String protocol,String host,String port){
        Properties properties=new Properties();
        properties.put(String.format("mail.%s.host",protocol),host);
        properties.put(String.format("mail.%s.port",protocol),port);
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),"javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socletFactory.fallback",protocol),"false");
        properties.setProperty(String.format("mail.%s.socketFactory.port", protocol),String.valueOf(port));
        properties.setProperty("mail.imaps.partialfetch","false");
        return properties;
    }
    
    public static void downloadEmails(String protocol,String host,String port,String userName,String password) throws MessagingException, IOException, ClassNotFoundException, SQLException{
       
        String result[]=new String[30];
        Properties properties =getServerProperties(protocol,host,port);
        Session session = Session.getDefaultInstance(properties);
        try{
        Store store=session.getStore(protocol);
        
        store.connect(userName,password);
        Folder folderInbox=store.getFolder("[Gmail]/All Mail");
        folderInbox.open(Folder.READ_WRITE);
        Message[] messages=folderInbox.getMessages();
        int n=messages.length;
        for(int i=n-1;i>=n-5;i--){
            Message msg=messages[i];
            InternetAddress sender = (InternetAddress)msg.getFrom()[0];
            String from=sender.getAddress();
            fromList[n-i-1]=from;
            String sub=msg.getSubject();
            subList[n-i-1]=sub;
            String bodypart="";
            if(msg.isMimeType("text/plain")){
                bodypart=msg.getContent().toString();
            }
            if(msg.isMimeType("multipart/*")){
                try{
                    MimeMultipart mimeMultipart=(MimeMultipart)msg.getContent();
                   
                        bodypart=getTextFromMimeMultipart(mimeMultipart);
                    
                    
                }
                catch(IOException e){
                
                }

                
            }
            bodyList[n-i-1]=bodypart;
            System.out.println();
            System.out.println(n-i+") ");
            System.out.println("From:- "+from);
            System.out.println("SUBJECT:- "+sub);
            System.out.println("BODY:- "+bodypart);
            System.out.println();}
            System.out.println("Enter Mail No to star");
            Scanner sc=new Scanner(System.in);
            int x=sc.nextInt();
            starMail(x-1);
            folderInbox.close(false);
            store.close();
            
        }
        
        catch(NoSuchProviderException ex){
              System.out.println("No provider for protocol "+protocol);
                ex.printStackTrace();
                }
        catch(MessagingException ex){
                System.out.println("Could not connect to the message store");
                ex.printStackTrace();
                }
        catch(IOException ex){}
        
        
    }
    public static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException{
        String result="";
        int count=mimeMultipart.getCount();
        try{
            for(int i=0;i<count;i++){
                BodyPart bodypart=mimeMultipart.getBodyPart(i);
                if(bodypart.isMimeType("text/plain")){
                    result+=bodypart.getContent();
                }
                else if(bodypart.isMimeType("text/html")){
                    String html=(String)bodypart.getContent();
                    result=result+org.jsoup.Jsoup.parse(html).text();
                }
                else if(bodypart.getContent() instanceof MimeMultipart){
                    result=result+getTextFromMimeMultipart((MimeMultipart)bodypart.getContent());
                }
            }
        }
        catch(IOException e){}
        return result;
    }
    public static void starMail(int x) throws ClassNotFoundException, SQLException{
        try{
          Class.forName("com.mysql.cj.jdbc.Driver");
          Connection con=DriverManager.getConnection(Credentials.DB_URL,Credentials.USER,Credentials.pass);
          Statement stmt=con.createStatement();
          bodyList[x]=bodyList[x].replace("'","\\'");
          String sqlInsert="INSERT INTO Star_Mail(name,sub,body)"+"VALUES('"+fromList[x]+"','"+subList[x]+"','"+bodyList[x]+"')";
          
          int countInserted=stmt.executeUpdate(sqlInsert);
          if(countInserted!=0){
              System.out.println("Email is starred Successfully");}
              
          
        con.close();
          
        }
        catch(Exception event){
            System.out.println(event);
        }
    
    
    }
}
