package FileStuff;
import java.util.*;
import java.io.*;
import java.awt.Desktop;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FolderContent {
    public void rename(String Link, String New)
    {
        File f1 = new File(Link);
        File f2 = new File(New);
        boolean b = f1.renameTo(f2);
        if(b)
            JOptionPane.showMessageDialog(null, "Rename successfully!!!!");
        else
            JOptionPane.showMessageDialog(null, "Rename failed!!!!");
    }
    public static void deleteFolder(File file)
    	throws IOException{
 
    	if(file.isDirectory()){
            if(file.list().length == 0)
            {			
                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());	
            }
            else
            {
                String files[] = file.list();
    
                for (String temp : files) {
                    File fileDelete = new File(file, temp);
                    deleteFolder(fileDelete);
                }
        		
                //check the directory again, if empty then delete it
                if(file.list().length == 0){
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }	
    	}
        else
        {
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
    	}
    }
    public void Delete(String Link)
    {
        File file = new File(Link);
        if(file.isFile())
        {
            boolean b = file.delete();
            if(b == true)
                JOptionPane.showMessageDialog(null, "Deleted successfully!!!!");
            return;
        }
        else if(file.isDirectory())
        {
            try{ deleteFolder(file); }
            catch(IOException e){ }
            JOptionPane.showMessageDialog(null, "Deleted successfully!!!!");
        }
    }
    public void OpenFile(String Link)
    {
        File file = new File(Link);
        
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }
        
        if(!file.isFile()){
            System.out.println("Not a file!");
            return;
        }
        try {
            Desktop desktop = Desktop.getDesktop();
            if(file.exists()) 
                desktop.open(file);
        }
        catch(IOException e)
        {
            
        }
    }
    public List<String> Get(String Directory)
    {
        final File folder = new File(Directory);

        List<String> result = new ArrayList<>(); 
        
        if(folder.isDirectory() == false)
            return result;

        search(folder, result);
        
        if(result.isEmpty())
        {
            result.add("empty");
        }

        return result;
    }
    public static void search(final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {
            if(f.isDirectory())
            {
                result.add(0, f.getName());
            }
            if(f.isFile())
            {
                result.add(f.getName());
            }
        }
    }
}
