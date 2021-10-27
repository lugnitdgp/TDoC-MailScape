/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mailscape;
import javax.swing.JFileChooser;
/**
 *
 * @author laxmikanth
 */
public class file_picker {
    public String get_path(){
        String path="";
        JFileChooser chooser=new JFileChooser();
        int res=chooser.showOpenDialog(null);
        if(res==JFileChooser.APPROVE_OPTION){
            path=chooser.getSelectedFile().getAbsolutePath();
            
        }
        return path;
    }
}
