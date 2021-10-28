
package newpackage;
import javax.mail.*;

import javax.activation.*;
import java.util.*;
import java.io.*;
import javax.swing.JFileChooser;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.regex.Pattern;
import newpackage.credentialsstar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class getmailattatchment {
     static String fromList[]=new String[5];
     static String subList[]=new String[5];
     static String bodyList[]=new String[5];
     static int length=0;
     
	public static void main(String[] args) 
	{
		String protocol="imap",host="imap.gmail.com",username=credentialsstar.username,password=credentialsstar.password,port="993";
		downloadEmails(protocol,host,port,username,password);
		}
	public static Properties getServerProperties(String protocol,String host,String port)
	{
		Properties properties =new Properties();
		properties.put(String.format("mail.%s.host",protocol),host);
		properties.put(String.format("mail.%s.port",protocol),port);
		properties.setProperty(String.format("mail.%s.socketFactory.class",protocol),"javax.net.ssl.SSLSocketFactory");
		properties.setProperty(String.format("mail.%s.socketFactory.fallback",protocol),"false");
		properties.setProperty(String.format("mail.%s.socketFactory.port",protocol),String.valueOf(port));
		properties.setProperty("mail.imaps.partialfetch", "false");
		return properties;
	}
	public static void downloadEmails(String protocol,String host,String port,String username,String password)
	{
		String result[]=new String[30];
		Properties properties =getServerProperties(protocol, host, port);
		Session session =Session.getDefaultInstance(properties);
		try
		{
			Store store=session.getStore(protocol);
			store.connect(username,password);
			Folder folderinbox=store.getFolder("[Gmail]/All Mail");
			folderinbox.open(Folder.READ_WRITE);
			Message[] messages=folderinbox.getMessages();
			int n=messages.length;
			for(int i=n-1;i>=n-5;i--)
			{
				Message msg=messages[i];
				InternetAddress sender=(InternetAddress)msg.getFrom()[0];
				String from=sender.getAddress();
                                fromList[n-1-i]=from;
				String subject=msg.getSubject();
                                subList[n-1-i]=subject;
				String bodypart="";
				if(msg.isMimeType("text/plain"))
				{
					bodypart=msg.getContent().toString();
				}
				if(msg.isMimeType("multipart/*"))
				{
					try
					{
						MimeMultipart mimeMultipart=(MimeMultipart)msg.getContent();
							bodypart= getTextFromMimeMultipart(mimeMultipart);
					}
				
					catch(IOException e){
					}
				}
                                bodyList[n-1-i]=bodypart;
				System.out.println();
				System.out.println(n-i+")");
				System.out.println("FROM :-" + from);
				System.out.println("SUBJECT :-" + subject);
				System.out.println("BODY :-" + bodypart);
				System.out.println();
			}
                        Scanner sc=new Scanner(System.in);
                        /*System.out.println("Enter mail no. to star");
                        int x =sc.nextInt();
                        starMail(x-1);*/
                        System.out.println("Enter mail no. to download attatchment");
                        int x =sc.nextInt();
                        getAttach(protocol,host,port,username,password,x-1);
			folderinbox.close(false);store.close();
		}
		catch(NoSuchProviderException ex) {
			System.out.println("No provider for protocol: " + protocol);
			ex.printStackTrace();
		}
		catch(MessagingException ex)
		{
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		}
		catch(IOException ex) {
	}

}
	public static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException
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
					result+=bodyPart.getContent();
					break;
				}
				else if(bodyPart.isMimeType("text/html"))
				{
					String html=(String)bodyPart.getContent();
					result+= org.jsoup.Jsoup.parse(html).text();
				}
				else if( bodyPart.getContent() instanceof MimeMultipart)
				{
					result+=getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
				}
			
		}
	}catch(IOException e) {}
	return	result;
	}
        public static void starMail(int x)
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con=DriverManager.getConnection(credentialsstar.DB_URL,credentialsstar.USER,credentialsstar.PASS);
                Statement stmt=con.createStatement();
                bodyList[x]=bodyList[x].replace("'","\\'");
                String sqlInsert="INSERT INTO Star_Mail(name,sub,body)"+"VALUES ('"+ fromList[x]+"','"+subList[x]+"','"+ bodyList[x]+"')";
                int countInserted=stmt.executeUpdate(sqlInsert);
                if(countInserted !=0)
                {
                    System.out.println("Email is starred successfully");
                }
                con.close();
                }
            catch(Exception event){
                System.out.println(event);
            }
        }
        public static void getAttach(String protocol,String host,String port,String username,String password,int index)
	{
		String save="/home/jayshree/Downloads/";
		Properties properties =getServerProperties(protocol, host, port);
		Session session =Session.getDefaultInstance(properties);
		try
		{
			Store store=session.getStore(protocol);
			store.connect(username,password);
			Folder folderinbox=store.getFolder("[Gmail]/All Mail");
			folderinbox.open(Folder.READ_WRITE);
			Message[] messages=folderinbox.getMessages();
			int n=messages.length;
				Message msg=messages[n-1-index];
				
				if(msg.isMimeType("text/plain"))
				{
					System.out.println("no attatchment exists in mail");
				}
				if(msg.isMimeType("multipart/*"))
				{
					try
					{
                                            String filename="";
						MimeMultipart mimeMultipart=(MimeMultipart)msg.getContent();
						for (int j=0;j<mimeMultipart.getCount();j++ )
                                                {
                                                    MimeBodyPart part=(MimeBodyPart) mimeMultipart.getBodyPart(j);
                                                    if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())){
                                                        if(part.getFileName()!=null){
                                                            filename+=part.getFileName();
                                                            part.saveFile(save + part.getFileName());
                                                            System.out.println("saved");
                                                        }
                                                    }
                                                }
                                                if (filename=="")
                                                    System.out.println("no attatchment exists in mail");
					}
				
					catch(IOException e){
					}
				}
                                
			folderinbox.close(false);store.close();
		}
		catch(NoSuchProviderException ex) {
			System.out.println("No provider for protocol: " + protocol);
			ex.printStackTrace();
		}
		catch(MessagingException ex)
		{
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
                }

       }
}
