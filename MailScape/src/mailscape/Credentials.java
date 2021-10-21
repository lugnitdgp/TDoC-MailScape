package mailscape;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Credentials {
    public static String [] getCredentials() throws FileNotFoundException {
        File homedir = new File(System.getProperty("user.home"));
        Scanner sc = new Scanner(new File(homedir+"/Public/credentials.txt"));
        String email = sc.nextLine();
        String token = sc.nextLine();
        return new String[]{email, token};
    }
}
