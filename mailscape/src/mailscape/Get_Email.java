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
    public void main(String args[]) throws MessagingException, IOException, ClassNotFoundException, SQLException{
        
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
    
    public String[] downloadEmails(String protocol,String host,String port,String userName,String password) throws MessagingException, IOException, ClassNotFoundException, SQLException{
       
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
        for(int i=n-1;i>=n-30;i--){
            Message msg=messages[i];
            result[n-i-1]=msg.getSubject();

            }
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
//        catch(IOException ex){}
        return result;

        
        
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

    /**
     *
     * @param protocol
     * @param host
     * @param port
     * @param userName
     * @param password
     * @param index
     * @throws NoSuchProviderException
     * @throws MessagingException
     * @throws IOException
     */
    public void getAttach(String protocol,String host,String port,String userName,String password,int index) throws NoSuchProviderException, MessagingException, IOException{
        String save="/home/laxmikanth/Downloads/";
        Properties properties= getServerProperties(protocol,host,port);
        Session session=Session.getDefaultInstance(properties);
        try{
            Store store=session.getStore(protocol);
            store.connect(userName,password);
            Folder folderInbox=store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
            Message[] messages=folderInbox.getMessages();
            int n=messages.length;
            Message msg=messages[n-1-index];
            if(msg.isMimeType("text/plain")){
                System.out.println("No Attachment Exists ");
            }
            if(msg.isMimeType("multipart/*")){
                try{
                    String filename="";
                    MimeMultipart mimemultipart = (MimeMultipart)msg.getContent();
                    for(int j=0;j<mimemultipart.getCount();j++){
                        MimeBodyPart part=(MimeBodyPart)mimemultipart.getBodyPart(j);
                        if(part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())){
                            if(part.getFileName()!=null){
                                filename+=part.getFileName();
                                part.saveFile(save+part.getFileName());
                                System.out.println("saved");
                            }
                        }
                    }
                    if(filename==""){
                        System.out.println("No Attachment exists in mail");
                    }
                    
                }
                catch(IOException ex){}
                folderInbox.close();
                store.close();
            }
            
        }
        catch(NoSuchProviderException ex){
            System.out.println("No provider for protocol "+protocol);
            ex.printStackTrace();
        }
    }
     public String[] getMail(String protocol,String host,String port,String userName,String password,int index) throws NoSuchProviderException, MessagingException, IOException{
         String result[]=new String[2];
         Properties properies=getServerProperties(protocol,host,port);
         Session session=Session.getDefaultInstance(properies);
         try{
              Store store=session.getStore(protocol);    
             store.connect(userName,password);
             Folder folderInbox=store.getFolder("[Gmail]/All Mail");
             folderInbox.open(Folder.READ_WRITE);
             Message[] msgs=folderInbox.getMessages();
             int n=msgs.length;
             Message msg=msgs[n-1-(index)];
             Object content=msg.getContent();
             if(content instanceof Multipart){
                 Multipart mp=(Multipart)content;
                 for(int j=0;j<mp.getCount();j++){
                     BodyPart bp=mp.getBodyPart(j);
                     if(Pattern.compile(Pattern.quote("text/html"),Pattern.CASE_INSENSITIVE).matcher(bp.getContentType()).find()){
                         result[1]=(String)bp.getContent();
                     }
                     else{
                         System.out.println("HAI");
                     }
                 }
             }
                    InternetAddress sender = (InternetAddress)msg.getFrom()[0];
                    String from=sender.getAddress();
                    String body="";
                    String subject=msg.getSubject();
                    if(msg.isMimeType("plain/text")&&result[1]==null){
                        result[1]=msg.getContent().toString();
                        System.out.println("get_mail 221");
                    }
                    if(msg.isMimeType("Multipart/*")){
                        try{
                            MimeMultipart mimemultipart=(MimeMultipart)msg.getContent();
                            if(result[1]==null){
                                result[1]=getTextFromMimeMultipart(mimemultipart);
                                
                            }
                        }
                        catch(IOException e){}
                    }
                    result[0]=subject;
                    folderInbox.close();
                    store.close();
             
         }
         catch(NoSuchProviderException ex){
             System.out.println("No provider for protocol "+protocol);
             ex.printStackTrace();
         }
         catch(MessagingException ea){
             System.out.println("Could not connect to the Store");
             ea.printStackTrace();
         }
         catch(IOException ex){}
         return result;
     }
     public boolean attachName(String protocol,String host,String port,String userName,String password,int index) throws MessagingException{
         String save="/home/laxmikanth/Downloads";
         String res="";
         Properties properties=getServerProperties(protocol,host,port);
         Session session=Session.getDefaultInstance(properties);
         try{
             Store store=session.getStore(protocol);
             store.connect(userName,password);
             Folder folderInbox=store.getFolder("[Gmail]/All Mail");
             folderInbox.open(Folder.READ_WRITE);
             Message[] messages=folderInbox.getMessages();
             int n=messages.length;
             Message msg=messages[n-1-index];
             InternetAddress sender=(InternetAddress)msg.getFrom()[0];
             String from=sender.getAddress();
             if(msg.isMimeType("text/plain")){
                 
             }
             if(msg.isMimeType("multipart/*")){
                 try{
                     MimeMultipart mimeMultipart =(MimeMultipart)msg.getContent();
                     for(int j=0;j<mimeMultipart.getCount();j++){
                         MimeBodyPart part=(MimeBodyPart)mimeMultipart.getBodyPart(j);
                        if(part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())){
                            
                        }
                        res=part.getFileName();
                 }}
                 catch(IOException ex){}
             }
          folderInbox.close(false);
          store.close();
         }
         catch(NoSuchProviderException ex){System.out.println("No such provider for protcol "+protocol);ex.printStackTrace();}
         catch(MessagingException ex){System.out.println("Could not connect to the Store");ex.printStackTrace();}
         if("".equals(res)){
             return false;
         }
         else{
             return true;
         }
     }
     
     
    

     
     
}
