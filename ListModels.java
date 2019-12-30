package FileStuff;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import static javax.swing.GroupLayout.Alignment.CENTER;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.JCheckBox;
import java.awt.event.*;   
import java.io.File;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class ListModels extends JFrame {
    private JPanel panel;
    private JTextField dir;
    private JButton search;
    //
    private JButton open;
    private JButton create;
    private JButton create_;
    private JButton delete;
    private JButton rename;
    private JButton copy;
    private JButton paste;
    private JButton zip;
    private JButton unzip;
    private JPanel option;
    //
    private JTable content;
    private DefaultTableModel tableModel;
    //
    private FolderContent folder = new FolderContent();
    private String CurrentDir;
    //
    private String copydir = "";
    private String copyname = "";
 
    public ListModels() {
        initUI();
    }
 
    private void createList() {
       String col[] = {"Name", "Location"};
       tableModel = new DefaultTableModel(col, 0);
       content = new JTable(tableModel); 
       content.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = content.getSelectedRow();
                    if(index != -1)
                    {
                        for(int row = 0; row < tableModel.getRowCount(); row++)
                        {
                            if(row == index)
                            {
                                 System.out.println(tableModel.getValueAt(row, 1));
                            }
                        }
                    }
                }
            }
        });
    }
 
    private void createButtons() {
        dir = new JTextField("C:", 35);
        search = new JButton("Search");
        panel = new JPanel(); panel.add(dir); panel.add(search);
        
        open = new JButton("Open");
        create = new JButton("Create File");
        create_ = new JButton("Create Folder");
        delete = new JButton("Delete");
        rename = new JButton("Rename");
        copy = new JButton("Copy");
        paste = new JButton("Paste");
        zip = new JButton("Zip");
        unzip = new JButton("Unzip");
        option = new JPanel(); panel.add(open); panel.add(create);
        
        unzip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = content.getSelectedRow();
                if(index != -1)
                {
                    String temp0 = String.valueOf(tableModel.getValueAt(index, 0));
                    String temp1 = String.valueOf(tableModel.getValueAt(index, 1));
                    if(!temp0.substring(temp0.length() - 4, temp0.length()).equals(".zip"))
                    {
                        JOptionPane.showMessageDialog(null, "Can only zip archives!!");
                        return;
                    }
                    ProgressBarUnzip bar = new ProgressBarUnzip();
                    bar.pls(temp1, temp0, search);
                    bar.please();
                }
            }
        });
        
        zip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = content.getSelectedRow();
                if(index != -1)
                {
                    String temp0 = String.valueOf(tableModel.getValueAt(index, 0));
                    String temp1 = String.valueOf(tableModel.getValueAt(index, 1));
                    File temp = new File(temp1 + "/" + temp0);
                    if(!temp.isFile())
                    {
                        JOptionPane.showMessageDialog(null, "Can only zip files!!");
                        return;
                    }
                    if(temp0.substring(temp0.length() - 4, temp0.length()).equals(".zip"))
                    {
                        JOptionPane.showMessageDialog(null, "File already zipped!!");
                        return;
                    }
                    ProgressBarZip bar = new ProgressBarZip();
                    bar.pls(temp1, temp0, search);
                    bar.please();
                }
            }
        });
        
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = content.getSelectedRow();
                if(index != -1)
                {
                    String tempname = copyname; String tempdir = copydir;
                    copyname = String.valueOf(tableModel.getValueAt(index, 0));
                    copydir = String.valueOf(tableModel.getValueAt(index, 1));
                    File temp = new File(copydir + "/" + copyname);
                    if(!temp.isFile())
                    {
                        JOptionPane.showMessageDialog(null, "Can only copy files!!");
                        copyname = tempname; copydir = tempdir;
                    }
                }
            }
        });
        
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(copyname.equals("") && copydir.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "No chosen files to copy!!");
                    return;
                }
                String temp = String.valueOf(tableModel.getValueAt(0, 1));
                ProgressBarDemo bar = new ProgressBarDemo();
                bar.pls(copydir, copyname, temp, search);
                bar.please();
            }
        });
        
        rename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = content.getSelectedRow();
                if(index != -1)
                {
                    String temp0 = String.valueOf(tableModel.getValueAt(index, 0));
                    String temp1 = String.valueOf(tableModel.getValueAt(index, 1));
                    System.out.println("Link: " + temp1 + "/" + temp0);
                    
                    String newname;
                    JTextField xField = new JTextField(temp1 + "/" + temp0);
                
                    JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("[New name]: "));
                    myPanel.add(xField);
                
                    int result = JOptionPane.showConfirmDialog(null, myPanel, 
                    "Rename", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        newname = xField.getText();
                    }
                    else
                        return;
                    folder.rename(temp1 + "/" + temp0, newname);
                    search.doClick();
                }
            }
        });
        
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = content.getSelectedRow();
                if(index != -1)
                {
                    String temp0 = String.valueOf(tableModel.getValueAt(index, 0));
                    String temp1 = String.valueOf(tableModel.getValueAt(index, 1));
                    System.out.println("Link: " + temp1 + "/" + temp0);
                    folder.Delete(temp1 + "/" + temp0);
                    tableModel.removeRow(index);
                }
            }
        });
        
        create_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name;
                JTextField xField = new JTextField(15);
                
                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("[Name]: "));
                myPanel.add(xField);
                
                int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Create new folder", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    name = xField.getText();
                }
                else
                    return;

                String temp1 = CurrentDir;
                String path = temp1 + File.separator + name;
                File file = new File(path);
                if(!file.exists()) {
                    file.mkdir(); 
                    Object[] obj = {name, temp1};
                    tableModel.addRow(obj);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Existed!!");
                }
            }
        });
        
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name;
                JTextField xField = new JTextField(15);
                
                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("[Name]: "));
                myPanel.add(xField);
                
                int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Create new file", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    name = xField.getText();
                }
                else
                    return;

                String temp1 = CurrentDir;
                String path = temp1 + File.separator + name;
                File f = new File(path); 
                if(!f.exists()) {
                    f.getParentFile().mkdirs(); 
                    try {
                        f.createNewFile();
                    }
                    catch(IOException E)
                    {
                        return;
                    }
                    Object[] obj = {name, temp1};
                    tableModel.addRow(obj);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Existed!!");
                }
            }
        });
       
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> temp = folder.Get(dir.getText());
                if(temp.isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Invalid directory!!");
                    return;
                }
                else if(temp.size() == 1 && temp.get(0).compareTo("empty") == 0)
                {
                    tableModel.setRowCount(0);
                }
                else
                {
                    tableModel.setRowCount(0);
                    for (String s : temp) {
                        //System.out.println(s);
                        Object[] obj = {s, dir.getText()};
                        tableModel.addRow(obj);
                    }
                }
                CurrentDir = dir.getText();
            }
        });
        
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = content.getSelectedRow();
                if(index != -1)
                {
                    String temp0 = String.valueOf(tableModel.getValueAt(index, 0));
                    String temp1 = String.valueOf(tableModel.getValueAt(index, 1));
                    File filee = new File(temp1 + "/" + temp0);
                    System.out.println("Link: " + temp1 + "/" + temp0);
                    if(filee.isFile()) {
                        folder.OpenFile(temp1 + "/" + temp0);
                        return;
                    }
                    else if(filee.isDirectory()) {
                        System.out.println("Result: " + temp1 + "/" + temp0);
                        List<String> temp = folder.Get(temp1 + "/" + temp0);
                        if(temp.isEmpty())
                        {
                            JOptionPane.showMessageDialog(null, "Invalid directory!!!!");
                            return;
                        }
                        else if(temp.size() == 1 && temp.get(0).compareTo("empty") == 0)
                        {
                            tableModel.setRowCount(0);
                            dir.setText(temp1 + "/" + temp0);
                        }
                        else
                        {
                            tableModel.setRowCount(0);
                            for (String s : temp) {
                                Object[] obj = {s, temp1 + "/" + temp0};
                                tableModel.addRow(obj);
                            }
                            dir.setText(temp1 + "/" + temp0);
                        }
                        CurrentDir = temp1 + "/" + temp0;
                        System.out.println("Current: " + CurrentDir);
                    }
                }
            }
        });
    }
 
    private void initUI() {
        createList();
        createButtons();
        JScrollPane scrollpane = new JScrollPane(content);
 
        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);
 
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
 
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup() 
                        .addComponent(panel)
                        .addComponent(scrollpane)
                )
                .addGroup(gl.createParallelGroup() 
                        .addComponent(open)
                        .addComponent(create)
                        .addComponent(create_)
                        .addComponent(delete)
                        .addComponent(rename)
                        .addComponent(copy)
                        .addComponent(paste)
                        .addComponent(zip)
                        .addComponent(unzip)
                )
        );
 
        gl.setVerticalGroup(gl.createParallelGroup(CENTER)
                .addGroup(gl.createSequentialGroup()
                        .addComponent(panel)
                        .addComponent(scrollpane)
                )
                .addGroup(gl.createSequentialGroup()
                        .addComponent(open)
                        .addComponent(create)
                        .addComponent(create_)
                        .addComponent(delete)
                        .addComponent(rename)
                        .addComponent(copy)
                        .addComponent(paste)
                        .addComponent(zip)
                        .addComponent(unzip)
                )
        );
        
        gl.linkSize(open, create, create_, delete, rename, copy, paste, zip, unzip);
 
        pack();
 
        setTitle("File Managament");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
 
    public static void main(String[] args) {
 
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ListModels ex = new ListModels();
                ex.setVisible(true);
            }
        });
    }
}
