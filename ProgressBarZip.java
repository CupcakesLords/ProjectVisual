/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileStuff;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.io.FileInputStream;
import java.nio.file.*;
import java.util.zip.*;

public class ProgressBarZip extends JPanel
                             implements ActionListener, 
                                        PropertyChangeListener {

    private JProgressBar progressBar;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea taskOutput;
    private Task task;
    private String filePath;
    private String buf;
    private JButton settle;

    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            String sourceFile = filePath;
            try {
                int add = filePath.lastIndexOf(".");
                String destname;
                destname = filePath.substring(0, add) + ".zip";
                buf = destname;
           
                FileOutputStream fos = new FileOutputStream(destname);
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                File fileToZip = new File(sourceFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1];
                int length;
                //
                setProgress(0);
                long total = fileToZip.length(); 
                double token = (1.0 / total) * 100.0;
                double progress = 0;
                //
                while((length = fis.read(bytes)) >= 0 && !isCancelled()) {
                    zipOut.write(bytes, 0, length);
                    progress += token;
                    setProgress((int)progress);
                }
                zipOut.close();
                fis.close();
                fos.close();
            }
            catch(Exception e) {System.out.println("Fuck me");}
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            setCursor(null); //turn off the wait cursor
            if(!isCancelled()){
                taskOutput.append("Done!\n");
                settle.doClick();
                
            }
            else
            {
                taskOutput.append("Canceled!\n");
                //buffer.delete();
                File temp = new File(buf);
                boolean dm = temp.delete();
                if(dm)
                    System.out.println("Yay zip");
            }
        }
    }

    public ProgressBarZip() {
        super(new BorderLayout());

        //Create the demo's UI.
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        
        stopButton = new JButton("Cancel");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                task.cancel(true);
            }
        });

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
    }

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task();
        task.addPropertyChangeListener(this);
        System.out.println("MEOW");
        task.execute();
    }

    /**
     * Invoked when task's progress property changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            //taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
        } 
    }


    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Zip Thread");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = this;
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void please() {
        //dir = d; name = f;
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    public void pls(String d, String f, JButton s)
    {
        filePath = d + "/" + f;
        settle = s;
    }
}

