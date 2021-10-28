


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
import static mailscape.Get_Email.getServerProperties;
import java.awt.*;
import javax.swing.*;
import mailscape.Send_Mail;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Style;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import mailscape.Get_Email;
import mailscape.get_detail;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author laxmikanth
 */
public class star extends javax.swing.JFrame implements MouseListener {
    String sub[];String body[];int length;
    /**
     * Creates new form star
     */
    public star() throws SQLException, ClassNotFoundException {
        initComponents();
        getLength();
        getList();
        jList1.addMouseListener(this);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(218, 219, 137));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Ubuntu", 0, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(246, 13, 13));
        jLabel1.setText("SUB:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(879, 150, 120, 100));

        jList1.setBackground(new java.awt.Color(223, 249, 175));
        jList1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jList1.setFont(new java.awt.Font("Yrsa Medium", 0, 20)); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 740, 660));

        jLabel2.setFont(new java.awt.Font("Ubuntu", 0, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(245, 2, 2));
        jLabel2.setText("BODY :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 400, 160, 53));

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(246, 211, 150));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Bitstream Charter", 0, 24)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jScrollPane3.setViewportView(jTextArea1);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 130, 700, 160));

        jEditorPane1.setEditable(false);
        jEditorPane1.setBackground(new java.awt.Color(246, 211, 150));
        jEditorPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jEditorPane1.setFont(new java.awt.Font("Bitstream Charter", 0, 24)); // NOI18N
        jScrollPane2.setViewportView(jEditorPane1);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 350, 750, 620));

        jPanel2.setBackground(new java.awt.Color(61, 130, 247));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel3.setFont(new java.awt.Font("Yrsa SemiBold", 1, 36)); // NOI18N
        jLabel3.setText("STARRED MAILS");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel3)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel3)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 360, 130));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-50, -50, 1990, 1260));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(star.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(star.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(star.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(star.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new star().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(star.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(star.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent me) {
         //To change body of generated methods, choose Tools | Templates.
         if(me.getSource()==jList1){
             int n=jList1.getSelectedIndex();
              jEditorPane1.setContentType("text/html");
                jEditorPane1.setText(body[n]);
        
        StyleSheet css=((HTMLEditorKit)jEditorPane1.getEditorKit()).getStyleSheet();
        Style style=css.getStyle("body");
        jTextArea1.setText(sub[n]);
         }
//         if(me.getSource()==jPanel3){
//             try {
//                 Class.forName("com.mysql.cj.jdbc.Driver");
//                 java.sql.Connection con=DriverManager.getConnection(Credentials.DB_URL,Credentials.USER,Credentials.pass);
//                 Statement stmt=con.createStatement();
//            ResultSet count=stmt.executeQuery("DELETE FROM Star_Mail");
//             } catch (ClassNotFoundException ex) {
//                 Logger.getLogger(star.class.getName()).log(Level.SEVERE, null, ex);
//             } catch (SQLException ex) {
//                 Logger.getLogger(star.class.getName()).log(Level.SEVERE, null, ex);
//             }
//         }
    }

    @Override
    public void mousePressed(MouseEvent me) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void getLength() throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con=DriverManager.getConnection(Credentials.DB_URL,Credentials.USER,Credentials.pass);
            Statement stmt=con.createStatement();
            ResultSet count=stmt.executeQuery("Select count(*) from Star_Mail");
            while(count.next()){
                length=count.getInt("count(*)");
               
            }
            con.close();}
        catch(Exception event){System.out.println(event);}
        
            
    }
    public void getList() throws ClassNotFoundException, SQLException{
        getLength();
        DefaultListModel model=new DefaultListModel();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con=DriverManager.getConnection(Credentials.DB_URL,Credentials.USER,Credentials.pass);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("Select * from Star_Mail");
            
           sub=new String[length];
           body=new String[length];
            int i=0;
            while(rs.next()){
                model.addElement(rs.getString(2));
                sub[i]=rs.getString(2);
                body[i]=rs.getString(3);
                i++;
                
            }
            jList1.setModel(model);
            con.close();
        }
        catch(Exception event){
            System.out.println(event);
        }
        
    }
}
