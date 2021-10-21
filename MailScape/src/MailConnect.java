import javax.mail.*;
import javax.activation.*;
import java.util.*;
import javax.mail.internet.*;
import java.io.*;

public class MailConnect {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        String from = getCredentials()[0];
        String to = sc.nextLine();
        String sub = sc.nextLine();
        String body = sc.nextLine();
        actuallySendMail(from, to, sub, body);
    }

    public static String [] getCredentials() throws FileNotFoundException {
        File homedir = new File(System.getProperty("user.home"));
        Scanner sc = new Scanner(new File(homedir+"/Public/credentials.txt"));
        String email = sc.nextLine();
        String token = sc.nextLine();
        return new String[]{email, token};
    }

    public static void actuallySendMail(String from, String to, String sub, String body) throws FileNotFoundException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        String[] credentials = getCredentials();
        String username = credentials[0];
        String password = credentials[1];

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(sub);
            message.setContent(body, "text/html");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
