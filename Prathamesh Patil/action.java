package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class action {
	
	public static void main(String[] args) throws IOException {
		
		Scanner sc = new Scanner(System.in);
		
		String from = "prathameshpatil2267@gmail.com";
		String to = "prathameshpatil0000000@gmail.com";
		System.out.print("Give body : ");
		String sub = "testing2";
		String body = sc.nextLine();
		
		sendMail.send_now(from,to ,sub,body);
		
	}

}
