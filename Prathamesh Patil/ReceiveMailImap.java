package project;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class ReceiveMailImap {

  public ReceiveMailImap() {}

  //
  // inspired by :
  // http://www.mikedesjardins.net/content/2008/03/using-javamail-to-read-and-extract/
  //

  public static void doit() throws MessagingException, IOException {
    Folder folder = null;
    Store store = null;
    try {
      Properties props = System.getProperties();
      props.setProperty("mail.store.protocol", "imaps");

      Session session = Session.getDefaultInstance(props, null);
      // session.setDebug(true);
      store = session.getStore("imaps");
      store.connect("imap.gmail.com","prathameshpatil2267@gmail.com","dxfssndbqeohyoyb" );
      folder = store.getFolder("Inbox");
      /* Others GMail folders :
       * [Gmail]/All Mail   This folder contains all of your Gmail messages.
       * [Gmail]/Drafts     Your drafts.
       * [Gmail]/Sent Mail  Messages you sent to other people.
       * [Gmail]/Spam       Messages marked as spam.
       * [Gmail]/Starred    Starred messages.
       * [Gmail]/Trash      Messages deleted from Gmail.
       */
      folder.open(Folder.READ_WRITE);
      Message messages[] = folder.getMessages();
      System.out.println("No of Messages : " + folder.getMessageCount());
      System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
      int n = folder.getMessageCount();
      for (int i=n-1; i > n-3; i--) {
        System.out.println("MESSAGE #" + (i + 1) + ":");
        Message msg = messages[i];
        /*
          if we don''t want to fetch messages already processed
          if (!msg.isSet(Flags.Flag.SEEN)) {
             String from = "unknown";
             ...
          }
        */
        String from = "unknown";
        if (msg.getReplyTo().length >= 1) {
          from = msg.getReplyTo()[0].toString();
        }
        else if (msg.getFrom().length >= 1) {
          from = msg.getFrom()[0].toString();
        }
        String subject = msg.getSubject();
        String body = "";
        
        if(msg.isMimeType("text/plain")) {
        	body = msg.getContent().toString();
        }
        if(msg.isMimeType("multipart/*")) {
        	MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
        	
        	body = getTextFromMimeMultipart(mimeMultipart);
        }
        System.out.println("Subject : " + subject +"\n From : " + from + "\n body : " + body);
        // you may want to replace the spaces with "_"
        // the TEMP directory is used to store the files
//        String filename = "body : " ;
//        saveParts(msg.getContent(), filename);
        msg.setFlag(Flags.Flag.SEEN,true);
        // to delete the message
        // msg.setFlag(Flags.Flag.DELETED, true);
      }
    }
    finally {
      if (folder != null) { folder.close(true); }
      if (store != null) { store.close(); }
    }
  }

  private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException {
	  
	 String ans = "";
  	int multipartCount = mimeMultipart.getCount();
  	
  	try {
  	for(int j=0; j< multipartCount ; j++) {
  		
  		BodyPart bodyPart = mimeMultipart.getBodyPart(j);
  		
  		if(bodyPart.isMimeType("text/plain")) {
  			ans = ans + bodyPart.getContent();
  			break;
  		}else if(bodyPart.isMimeType("text/html")) {
  			String html = (String) bodyPart.getContent();
  			
  			ans = ans + org.jsoup.Jsoup.parse(html).text();
  		}else if(bodyPart.getContent() instanceof  MimeMultipart) {
  			ans = ans + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
  		}
  	}
  	}catch (Exception e) {
		// TODO: handle exception
	}
  	
  	return ans;
	
}

public static void saveParts(Object content, String filename)
  throws IOException, MessagingException
  {
    OutputStream out = null;
    InputStream in = null;
    try {
      if (content instanceof Multipart) {
        Multipart multi = ((Multipart)content);
        int parts = multi.getCount();
        for (int j=0; j < parts; ++j) {
          MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
          if (part.getContent() instanceof Multipart) {
            // part-within-a-part, do some recursion...
            saveParts(part.getContent(), filename);
          }
          else {
            String extension = "";
            if (part.isMimeType("text/html")) {
              extension = "html";
            }
            else {
              if (part.isMimeType("text/plain")) {
                extension = "txt";
              }
              else {
                //  Try to get the name of the attachment
                extension = part.getDataHandler().getName();
              }
              filename = filename + "." + extension;
              System.out.println("... " + filename);
              out = new FileOutputStream(new File(filename));
              in = part.getInputStream();
              int k;
              while ((k = in.read()) != -1) {
                out.write(k);
              }
            }
          }
        }
      }
    }
    finally {
      if (in != null) { in.close(); }
      if (out != null) { out.flush(); out.close(); }
    }
  }

  public static void main(String args[]) throws Exception {
    ReceiveMailImap.doit();
  }
}
