package mailscape;

import com.sun.mail.gimap.GmailMessage;
import com.sun.mail.gimap.GmailMsgIdTerm;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import static mailscape.MailRetriever.getServerProperties;
import static mailscape.MailRetriever.getTextFromMimeMultipart;

public class UI {
    // Global State Variables
    private final int mail_count = 10;
    private final String[] list_from = new String[mail_count];
    private final String[] list_subject = new String[mail_count];
    private final String[] list_body = new String[mail_count];
    private final long [] list_uids = new long[mail_count];
    private int current_mail_index = -1;
    private String send_from = "", send_to = "", send_subject = "", send_body = "";

    // UI elements
    private JTabbedPane Tabs;
    private JLabel Heading;
    private JTextField tFieldTo;
    private JTextField tFieldSubject;
    private JTextField tFieldBody;
    private JButton bAttachment;
    private JButton bSend;
    private JPanel MainWindow;
    private JList<String> listAttachments;
    private JLabel sendingMailState;
    private JList<String> listSubjects;
    private JButton bStarMail;
    private JEditorPane editorPaneBody;
    private JButton bDownloadAttachment;
    private JPanel all_mail_left;
    private JLabel labelSubjects;
    private JLabel labelDetails;
    private JLabel labelSubject;
    private JLabel labelBody;
    private JTextPane textPaneSubject;
    private JButton buttonRefreshList;
    private JLabel labelFrom;
    private JTextPane textPaneFrom;
    private JPanel all_mail_right;
    private JLabel labelUID;
    private File[] send_file_paths;

    public UI() {
        bAttachment.addActionListener(e -> {
            FilePicker ob = new FilePicker();
            send_file_paths = ob.getPath();
            String[] path_strings = new String[send_file_paths.length];
            for (int i = 0; i < send_file_paths.length; i++) path_strings[i] = send_file_paths[i].toString();
            listAttachments.setVisible(true);
            listAttachments.setListData(path_strings);
        });
        bSend.addActionListener(e -> {
            boolean success = false;
            sendingMailState.setText("Sending mail...");
            try {
                send_from = Credentials.getCredentials()[0];
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            send_to = tFieldTo.getText();
            send_subject = tFieldSubject.getText();
            send_body = tFieldBody.getText();
            try {
                success = MailSender.actuallySendMail(send_from, send_to, send_subject, send_body, send_file_paths);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            if (success) {
                sendingMailState.setText("Mail sent!");
            } else {
                sendingMailState.setText("Couldn't send mail");
            }
        });
        buttonRefreshList.addActionListener(e -> {
            try {
                String protocol = "gimap";
                String host = "imap.gmail.com";
                String user_name = Credentials.getCredentials()[0];
                String password = Credentials.getCredentials()[1];
                String port = "993";
                Properties properties = getServerProperties(protocol, host, port);
                Session session = Session.getDefaultInstance(properties);

                //connects to the message store
                Store store = session.getStore(protocol);
                store.connect(user_name, password);

                //opens the inbox folder
                Folder GmailInbox = store.getFolder("[Gmail]/All Mail");
                GmailInbox.open(Folder.READ_WRITE);

                //fetches new messages from server
                Message[] messages = GmailInbox.getMessages();
                int n = messages.length;

                for (int i = n - 1; i >= n - mail_count; i--) {
                    Message msg = messages[i];

                    InternetAddress sender = (InternetAddress) msg.getFrom()[0];
                    String from = sender.getAddress();
                    String subject = msg.getSubject();
                    StringBuilder body = new StringBuilder();

                    Object content = msg.getContent();
                    if (content instanceof Multipart) {
                        Multipart multipart = (Multipart) content;
                        for(int j = 0; j < multipart.getCount(); j++) {
                            BodyPart bodyPart = multipart.getBodyPart(j);
                            if(Pattern.compile(Pattern.quote("text/html"), Pattern.CASE_INSENSITIVE).matcher(bodyPart.getContentType()).find()) {
                                // html part found
                                body.append((String) bodyPart.getContent());
                            }
                        }
                    }

                    if(body.length() == 0 && msg.isMimeType("text/plain")) {
                        body.append(msg.getContent().toString());
                    }



                    GmailMessage gmailMessage = (GmailMessage)messages[i];
                    long uid = gmailMessage.getMsgId();

                    list_from[n - 1 - i] = from;
                    list_subject[n - 1 - i] = subject;
                    list_body[n - 1 - i] = body.toString();
                    list_uids[n-1-i] = uid;
                }

                // disconnect
                GmailInbox.close(false);
                store.close();

            } catch (MessagingException | IOException expc) {
                expc.printStackTrace();
            }
            listSubjects.setListData(list_subject);
        });
        listSubjects.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    if (listSubjects.getSelectedIndex() == -1) {
                        textPaneFrom.setText("");
                        textPaneSubject.setText("");
                        editorPaneBody.setText("");
                    } else {
                        int i = listSubjects.getSelectedIndex();
                        current_mail_index = i;
                        textPaneFrom.setText(list_from[i]);
                        textPaneSubject.setText(list_subject[i]);
                        editorPaneBody.setText(list_body[i]);
                        labelUID.setText(String.valueOf(list_uids[i]));
                    }
                }
            }
        });
        bDownloadAttachment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String protocol = "gimap";
                    String host = "imap.gmail.com";
                    String user_name = Credentials.getCredentials()[0];
                    String password = Credentials.getCredentials()[1];
                    String port = "993";
                    Properties properties = getServerProperties(protocol, host, port);
                    Session session = Session.getDefaultInstance(properties);

                    //connects to the message store
                    Store store = session.getStore(protocol);
                    store.connect(user_name, password);

                    //opens the inbox folder
                    Folder GmailInbox = store.getFolder("[Gmail]/All Mail");
                    GmailInbox.open(Folder.READ_WRITE);

                    long msgid = list_uids[current_mail_index];
                    Message[] messages = GmailInbox.search(new GmailMsgIdTerm(msgid));
                    Message msg = messages[0];

                    String downloadDirectory = System.getProperty("user.home");
                    List<String> downloadedAttachments = new ArrayList<String>();
                    Multipart multiPart = (Multipart) msg.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String file = part.getFileName();
                            part.saveFile(downloadDirectory + File.separator + part.getFileName());
                            downloadedAttachments.add(file);
                        }
                    }

                    // disconnect
                    GmailInbox.close(false);
                    store.close();

                } catch (MessagingException | IOException expc) {
                    expc.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("UI");
        frame.setContentPane(new UI().MainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        listAttachments = new JList<>();
        listAttachments.setVisible(false);
    }
}
