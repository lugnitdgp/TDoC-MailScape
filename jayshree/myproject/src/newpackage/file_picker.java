
package newpackage;
import javax.swing.JFileChooser;
public class file_picker {
	public String get_path() {
		String path="";
		JFileChooser chooser=new JFileChooser();
		int res=chooser.showOpenDialog(null);
		if(res==JFileChooser.APPROVE_OPTION)
			path=chooser.getSelectedFile().getAbsolutePath();
		
		return path;
	}

}
