/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mailscape;

/**
 *
 * @author laxmikanth
 */
public class Credentials {
  
     static String username="";
    static String password="";
    MailScape ob=new MailScape(a,b);
    public Credentials(String a,String b){
    username=a;
    password=b;
}
   
    final static String DB_URL="jdbc:mysql://localhost:3306/mailscape";
    final static String USER="mailscape";
    final static String pass="Test@123";
}
