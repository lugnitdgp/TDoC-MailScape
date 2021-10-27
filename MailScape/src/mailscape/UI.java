package mailscape;

import com.sun.mail.gimap.GmailMessage;
import com.sun.mail.gimap.GmailMsgIdTerm;
import org.jetbrains.annotations.NotNull;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import static mailscape.MailRetriever.getServerProperties;

class allMailDetails {
    public String from;
    public String subject;
    public String body;
    public long uid;
    public allMailDetails(String from, String subject, String body, long uid) {
        super();
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.uid = uid;
    }
}

public class UI {
    // Global State Variables
    private static final int mail_count = 10;
    private static final String[] list_from = new String[mail_count];
    private static final String[] list_subject = new String[mail_count];
    private static final String[] list_body = new String[mail_count];
    private static final long [] list_uids = new long[mail_count];
    private static int current_mail_index = -1;
    private  static int current_swing_worker_index = 0;
    private String send_from = "", send_to = "", send_subject = "", send_body = "";
    DefaultListModel<String> subjectDefaultListModel = new DefaultListModel<>();

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
    private JLabel labelRefreshedTime;
    private JLabel labelDownloadFinished;
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
            listSubjects.setModel(subjectDefaultListModel);
            SwingWorker <Boolean, allMailDetails> getListSubjects = new SwingWorker<>() {
                @Override
                protected @NotNull Boolean doInBackground() {
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
                                for (int j = 0; j < multipart.getCount(); j++) {
                                    BodyPart bodyPart = multipart.getBodyPart(j);
                                    if (Pattern.compile(Pattern.quote("text/html"), Pattern.CASE_INSENSITIVE).matcher(bodyPart.getContentType()).find()) {
                                        // html part found
                                        body.append((String) bodyPart.getContent());
                                    }
                                }
                            }

                            if (body.length() == 0 && msg.isMimeType("text/plain")) {
                                body.append(msg.getContent().toString());
                            }


                            GmailMessage gmailMessage = (GmailMessage) messages[i];
                            long uid = gmailMessage.getMsgId();
                            publish(new allMailDetails(from, subject, body.toString(), uid));
                        }

                        // disconnect
                        GmailInbox.close(false);
                        store.close();

                    } catch (MessagingException | IOException expc) {
                        expc.printStackTrace();
                    }
                    return true;
                }

                @Override
                protected void process(@NotNull List<allMailDetails> chunks) {
                    for (allMailDetails a : chunks) {
                        list_from[current_swing_worker_index] = a.from;
                        list_subject[current_swing_worker_index] = a.subject;
                        list_body[current_swing_worker_index] = a.body;
                        list_uids[current_swing_worker_index] = a.uid;
                        current_swing_worker_index++;
                        subjectDefaultListModel.addElement(a.subject);
                    }
                }

                @Override
                protected void done() {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalDateTime now = LocalDateTime.now();
                    labelRefreshedTime.setText("Last Refreshed : " + dateTimeFormatter.format(now));
                }
            };
            subjectDefaultListModel.clear();
            current_swing_worker_index = 0;
            getListSubjects.execute();
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
                        editorPaneBody.setContentType("text/html");
                        String body = list_body[i].replaceAll("<style([\\s\\S]+?)</style>","");
                        String html_body = "<html><body>" + body + "</body></html>";
                        editorPaneBody.setText(html_body);
                        labelUID.setText(String.valueOf(list_uids[i]));
                    }
                }
            }
        });
        bDownloadAttachment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> downloadWorker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
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

                            String downloadDirectory = System.getProperty("user.home") + "/Public";
                            List<String> downloadedAttachments = new ArrayList<>();
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
                        return null;
                    }

                    @Override
                    protected void done() {
                        labelDownloadFinished.setText("Download finished");
                    }
                };
                downloadWorker.execute();
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
