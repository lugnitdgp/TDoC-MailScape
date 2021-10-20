/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mailscape;

/**
 *
 * @author aruno
 */
import javax.swing.JFileChooser;
public class file_picker 
{
    public String getPath()
    {
        String path="";
        JFileChooser chooser=new JFileChooser();
        int res=chooser.showOpenDialog(null);
        if(res==JFileChooser.APPROVE_OPTION)
            path = chooser.getSelectedFile().getAbsolutePath();
        return path;
    }
}
