package cz.muni.fi.DebugDbAnalyzerApp.Application;

import cz.muni.fi.DebugDbAnalyzerApp.Database.DatabaseAccessManagerImpl;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.TextAreaLoggerHandler;
import cz.muni.fi.DebugDbAnalyzerApp.XmlOutput.Visualizer;
import cz.muni.fi.DebugDbAnalyzerApp.XmlOutput.XSLTProcessor;
import java.awt.BorderLayout;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

/**
 * Class which define front-end of DebugDbAnalyzerApp in java swing.
 * @author Miroslav Kubus
 */
public class AnalyzerApp extends javax.swing.JFrame {

    private VisualizeResultsSwingWorker visualizeSwingWorker = null;
    private DatabaseWorkSwingWorker databaseSwingWorker = null;
    private SpecificProcessSwingWorker specificProcessSwingWorker = null;
    private OpenXmlFileSwingWorker openXmlSwingWorker = null;
    private String databaseFilePath = null;
    private DatabaseAccessManagerImpl databaseManager = null;
    private Visualizer visualizer = null;
    private TextAreaLoggerHandler textAreaHandler = null;
    
    /**
     * Creates new form AnalyzerApp
     */
    public AnalyzerApp() {
        initComponents();
        visualizer = new Visualizer();
        textAreaHandler = new TextAreaLoggerHandler();
        textAreaHandler.initTextAreaHandler(loggerJTextArea);
    }
    
    /**
     * Class which serve as file filter in file chooser. It allows only .db files.
     */
    private class DatabaseFileFilter extends FileFilter {

        @Override
        public boolean accept(File file) {
            return file.isDirectory() || file.getAbsolutePath().endsWith(".db");
        }

        @Override
        public String getDescription() {
            return "Database files (*.db)";
        }
    }
    
    
    /**
     * Class which runs analyze just for specific selected process.
     */
    private class SpecificProcessSwingWorker extends SwingWorker<Void, Void> {

        private final String selectedProcess;
        
        public SpecificProcessSwingWorker(String selectedProcess) throws ServiceFailureException {
            this.selectedProcess = selectedProcess; 
        }
        
        public SpecificProcessSwingWorker(String selectedProcess, int aroundError) throws ServiceFailureException {
            this.selectedProcess = selectedProcess; 
            databaseManager = new DatabaseAccessManagerImpl(databaseFilePath, textAreaHandler, aroundError);
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            databaseManager.accessDebugLogTable(Collections.singletonList(selectedProcess));
            databaseManager.dropProcessNameIndex();
            visualizer.toWeb(textAreaHandler);
            return null;
        }
        
        @Override
        protected void done() {
            try {
                specificProcessJButton.setEnabled(true);
                specificProcessSwingWorker = null;
                this.get();
            } catch (InterruptedException | ExecutionException ex) {
                printException(ex);
            }
        }  
    }
    
    /**
     * Class which runs analyze for all process names in database. If is checked
     * specificProcessNameJCheckBox it creates index and retrieves all process names
     * from database.
     */
    private class DatabaseWorkSwingWorker extends SwingWorker<List<String>, Void> {

        private final boolean specificProcess;
        
        public DatabaseWorkSwingWorker(boolean specificProcess) {
            this.specificProcess = specificProcess;
        }
        
        @Override
        protected List<String> doInBackground() throws Exception {
            List<String> processes = null;
            
            if(databaseFilePath != null) {
                databaseManager = new DatabaseAccessManagerImpl(databaseFilePath, textAreaHandler);
                databaseManager.createIndexOnProcessName();
                processes = databaseManager.getAllProcessNamesFromDatabase();

                if(!specificProcess) {
                    databaseManager.accessDebugLogTable(processes);
                    databaseManager.dropProcessNameIndex();
                    visualizer.toWeb(textAreaHandler);
                } 
            }
            
            return processes;
        }
        
        @Override
        protected void done() {
            try {        
                analyzeJButton.setEnabled(true);
                databaseSwingWorker = null;
                this.get();
            } catch (InterruptedException | ExecutionException ex) {
                printException(ex);
            }
        }       
    }
    
    /**
     * Method which creates jDialog for analyze of specific process.
     * @param processes list of string representing process names in database.
     */
    private void createSpecificProcessDialog(List<String> processes) {
        selectProcessJDialog.setSize(405, 235);
        selectProcessJDialog.setResizable(false);
                    
        processes.stream().forEach((name) -> {
            processNameJComboBox.addItem(name);
        });
                    
        selectProcessJDialog.setVisible(true);
    }
    
    /**
     * Class which opens last xml output of application.
     */
    private class OpenXmlFileSwingWorker extends SwingWorker<Void, Void> {
        private final XSLTProcessor pro;
        
        public OpenXmlFileSwingWorker() {
            pro = new XSLTProcessor(textAreaHandler);
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            pro.openFile(Visualizer.getPATH_TO_XML());
            return null;
        }
        
        @Override
        protected void done() {
            try {
                lastXmlOutputJMenuItem.setEnabled(true);
                openXmlSwingWorker = null;
                this.get();
            } catch (InterruptedException | ExecutionException ex) {
                printException(ex);
            }
        }
        
    }
    
    /**
     * Class which opens last html output of application.
     */
    private class VisualizeResultsSwingWorker extends SwingWorker<Void, Void> {
        
        private final XSLTProcessor pro;
        private final String htmlPath;
        
        public VisualizeResultsSwingWorker() {
            pro = new XSLTProcessor(textAreaHandler);
            htmlPath = "src" + File.separator + "main" + File.separator 
                + "resources" + File.separator + "htmlOutput.html";
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            pro.openFile(htmlPath);
            return null;
        }
        
        @Override
        protected void done() {
            try {
                lastHtmlOutputJMenuItem.setEnabled(true);
                visualizeSwingWorker = null;
                this.get();
            } catch (InterruptedException | ExecutionException ex) {
                printException(ex);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectProcessJDialog = new javax.swing.JDialog();
        selectProcessJPanel = new javax.swing.JPanel();
        processNameJComboBox = new javax.swing.JComboBox<>();
        specificProcessJButton = new javax.swing.JButton();
        infoSelectNameJLabel = new javax.swing.JLabel();
        outputAllJCheckBox = new javax.swing.JCheckBox();
        databaseJFileChooser = new javax.swing.JFileChooser();
        applicationJTabbedPane = new javax.swing.JTabbedPane();
        analyzerJPanel = new javax.swing.JPanel();
        chooseFileJButton = new javax.swing.JButton();
        choosenDatabaseFileJLabel = new javax.swing.JLabel();
        justInfoJLabel = new javax.swing.JLabel();
        specificProcessNameJCheckBox = new javax.swing.JCheckBox();
        specifyNumberOfGroupsJCheckBox = new javax.swing.JCheckBox();
        analyzeJButton = new javax.swing.JButton();
        loggerOutputJScrollPane = new javax.swing.JScrollPane();
        loggerJTextArea = new javax.swing.JTextArea();
        jMenuBar = new javax.swing.JMenuBar();
        fileJMenu = new javax.swing.JMenu();
        exitJMenuItem = new javax.swing.JMenuItem();
        visualizeJMenu = new javax.swing.JMenu();
        lastHtmlOutputJMenuItem = new javax.swing.JMenuItem();
        lastXmlOutputJMenuItem = new javax.swing.JMenuItem();
        helpJMenu = new javax.swing.JMenu();
        manualJMenuItem = new javax.swing.JMenuItem();
        creditsJMenuItem = new javax.swing.JMenuItem();

        selectProcessJDialog.setTitle("Select specific process to analyze");

        selectProcessJPanel.setMaximumSize(null);
        selectProcessJPanel.setPreferredSize(new java.awt.Dimension(400, 200));

        specificProcessJButton.setText("Analyze process");
        specificProcessJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                specificProcessJButtonMouseClicked(evt);
            }
        });

        infoSelectNameJLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        infoSelectNameJLabel.setText("Select name of process which you want to analyze:");

        outputAllJCheckBox.setText("write all groups associated with process to output");

        javax.swing.GroupLayout selectProcessJPanelLayout = new javax.swing.GroupLayout(selectProcessJPanel);
        selectProcessJPanel.setLayout(selectProcessJPanelLayout);
        selectProcessJPanelLayout.setHorizontalGroup(
            selectProcessJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectProcessJPanelLayout.createSequentialGroup()
                .addGroup(selectProcessJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(selectProcessJPanelLayout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(specificProcessJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(selectProcessJPanelLayout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(processNameJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(selectProcessJPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(infoSelectNameJLabel))
                    .addGroup(selectProcessJPanelLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(outputAllJCheckBox)))
                .addContainerGap())
        );
        selectProcessJPanelLayout.setVerticalGroup(
            selectProcessJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectProcessJPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(infoSelectNameJLabel)
                .addGap(18, 18, 18)
                .addComponent(processNameJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(outputAllJCheckBox)
                .addGap(18, 18, 18)
                .addComponent(specificProcessJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        selectProcessJDialog.getContentPane().add(selectProcessJPanel, java.awt.BorderLayout.PAGE_START);

        selectProcessJDialog.getAccessibleContext().setAccessibleDescription("");

        databaseJFileChooser.setDialogTitle("Database file chooser");
        databaseJFileChooser.setFileFilter(new DatabaseFileFilter());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Debug database analyzer");

        applicationJTabbedPane.setPreferredSize(new java.awt.Dimension(655, 465));

        chooseFileJButton.setText("Choose file");
        chooseFileJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseFileJButtonMouseClicked(evt);
            }
        });

        choosenDatabaseFileJLabel.setBackground(new java.awt.Color(255, 255, 255));
        choosenDatabaseFileJLabel.setText(System.getProperty("user.home") + System.getProperty("file.separator")+ "Desktop");
        choosenDatabaseFileJLabel.setAutoscrolls(true);
        choosenDatabaseFileJLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        choosenDatabaseFileJLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        choosenDatabaseFileJLabel.setEnabled(false);
        choosenDatabaseFileJLabel.setOpaque(true);

        justInfoJLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        justInfoJLabel.setText("Absolute path to chosen database file:");

        specificProcessNameJCheckBox.setText("analyze specific process name");

        specifyNumberOfGroupsJCheckBox.setText("specify the number of groups around errors ");

        analyzeJButton.setText("Analyze");
        analyzeJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                analyzeJButtonMouseClicked(evt);
            }
        });

        loggerJTextArea.setEditable(false);
        loggerJTextArea.setColumns(20);
        loggerJTextArea.setRows(5);
        loggerOutputJScrollPane.setViewportView(loggerJTextArea);

        javax.swing.GroupLayout analyzerJPanelLayout = new javax.swing.GroupLayout(analyzerJPanel);
        analyzerJPanel.setLayout(analyzerJPanelLayout);
        analyzerJPanelLayout.setHorizontalGroup(
            analyzerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(analyzerJPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(analyzerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(justInfoJLabel)
                    .addGroup(analyzerJPanelLayout.createSequentialGroup()
                        .addGroup(analyzerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(specificProcessNameJCheckBox)
                            .addComponent(specifyNumberOfGroupsJCheckBox)
                            .addComponent(choosenDatabaseFileJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(analyzerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(chooseFileJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(analyzeJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
            .addComponent(loggerOutputJScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        analyzerJPanelLayout.setVerticalGroup(
            analyzerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(analyzerJPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(justInfoJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(analyzerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choosenDatabaseFileJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseFileJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(analyzerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(analyzerJPanelLayout.createSequentialGroup()
                        .addComponent(specificProcessNameJCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(specifyNumberOfGroupsJCheckBox))
                    .addComponent(analyzeJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(loggerOutputJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
        );

        applicationJTabbedPane.addTab("Analyzer", analyzerJPanel);

        getContentPane().add(applicationJTabbedPane, java.awt.BorderLayout.CENTER);

        fileJMenu.setText("File");

        exitJMenuItem.setText("Exit");
        exitJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(exitJMenuItem);

        jMenuBar.add(fileJMenu);

        visualizeJMenu.setText("Open");

        lastHtmlOutputJMenuItem.setText("Last html output");
        lastHtmlOutputJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastHtmlOutputJMenuItemActionPerformed(evt);
            }
        });
        visualizeJMenu.add(lastHtmlOutputJMenuItem);

        lastXmlOutputJMenuItem.setText("Last xml output");
        lastXmlOutputJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastXmlOutputJMenuItemActionPerformed(evt);
            }
        });
        visualizeJMenu.add(lastXmlOutputJMenuItem);

        jMenuBar.add(visualizeJMenu);

        helpJMenu.setText("Help");

        manualJMenuItem.setText("Manual");
        manualJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualJMenuItemActionPerformed(evt);
            }
        });
        helpJMenu.add(manualJMenuItem);

        creditsJMenuItem.setText("Credits");
        creditsJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditsJMenuItemActionPerformed(evt);
            }
        });
        helpJMenu.add(creditsJMenuItem);

        jMenuBar.add(helpJMenu);

        setJMenuBar(jMenuBar);

        getAccessibleContext().setAccessibleName("appFrame");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void manualJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualJMenuItemActionPerformed
        JFrame manualFrame = new JFrame();
        manualFrame.setTitle("How to use database analyzer");
        
        JLabel manual = new JLabel();
        String text = "<b>Debug database analyzer.</b><br> <br>"
                + "Application analyses debug database. It creates <br>"
                + "index on process_name attribute in debug_log table <br>"
                + "in chosen database for more effective data loading. <br> <br>"
                + "Application loads data from database, calculates statistics <br> "
                + "and performs aggregation according to value of info attribute. <br> <br>"
                + "Output is visualized in <b> default browser for html files. </b> <br> <br>"
                + "<b>To run application select path to debug database <br> "
                + "and click analyze. It is possible to turn on filtering.<b>";
        
        setUpJLabel(manual, 18.0f, text);
        addComponentToFrame(manualFrame, manual, 600, 400);
    }//GEN-LAST:event_manualJMenuItemActionPerformed

    private void creditsJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditsJMenuItemActionPerformed
        JFrame creditFrame = new JFrame();
        creditFrame.setTitle("About database analyzer");
        
        JLabel info = new JLabel();
        String text = "<b>Debug database analyzer.</b><br> <br>"
                + "Application created as a part of bachelor thesis. <br>"
                + "It is used as a tool for troubleshooting of processes. <br> <br>"
                + "Miroslav Kubus <br> Masaryk University, Faculty of Informatics"; 
        
        setUpJLabel(info, 20.0f, text);
        addComponentToFrame(creditFrame, info, 500, 300);
    }//GEN-LAST:event_creditsJMenuItemActionPerformed

    private void lastHtmlOutputJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastHtmlOutputJMenuItemActionPerformed
        if (visualizeSwingWorker != null) {
            printOperationInProgress();
            return;
        }
        
        lastHtmlOutputJMenuItem.setEnabled(false);
        visualizeSwingWorker = new VisualizeResultsSwingWorker();
        visualizeSwingWorker.execute();
    }//GEN-LAST:event_lastHtmlOutputJMenuItemActionPerformed

    private void exitJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitJMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitJMenuItemActionPerformed

    private void specificProcessJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_specificProcessJButtonMouseClicked
        if (specificProcessSwingWorker != null) {
            printOperationInProgress();
            return;
        }
        
        String selectedProcess = (String) processNameJComboBox.getSelectedItem();
        selectProcessJDialog.dispose();
        boolean allGroups = outputAllJCheckBox.isSelected();
        
        try {
            if(allGroups) {
                specificProcessSwingWorker = new SpecificProcessSwingWorker(selectedProcess, -1);
            } else {
                specificProcessSwingWorker = new SpecificProcessSwingWorker(selectedProcess);
            }
            
            specificProcessSwingWorker.execute();
        } catch (ServiceFailureException ex) {
            this.printException(ex);
        }
        
        outputAllJCheckBox.setSelected(false);
    }//GEN-LAST:event_specificProcessJButtonMouseClicked

    private void analyzeJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_analyzeJButtonMouseClicked
        if (databaseSwingWorker != null) {
            printOperationInProgress();
            return;
        }

        boolean specificProcess = specificProcessNameJCheckBox.isSelected();
        analyzeJButton.setEnabled(false);
        databaseSwingWorker = new DatabaseWorkSwingWorker(specificProcess);
        databaseSwingWorker.execute();

        try {
            if(specificProcess) {
                createSpecificProcessDialog(databaseSwingWorker.get());
            }
        } catch (InterruptedException | ExecutionException ex) {
            printException(ex);
        }
    }//GEN-LAST:event_analyzeJButtonMouseClicked

    private void chooseFileJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chooseFileJButtonMouseClicked
        databaseJFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")
            + File.separator + "Desktop"));
    int returnValue = databaseJFileChooser.showDialog(this, "Choose");

    if(returnValue == JFileChooser.APPROVE_OPTION) {
        File file = databaseJFileChooser.getSelectedFile();
        databaseFilePath = file.getAbsolutePath();
        choosenDatabaseFileJLabel.setText(databaseFilePath);
        }
    }//GEN-LAST:event_chooseFileJButtonMouseClicked

    private void lastXmlOutputJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastXmlOutputJMenuItemActionPerformed
        if(openXmlSwingWorker != null){
            printOperationInProgress();
            return;
        }
        
        lastXmlOutputJMenuItem.setEnabled(false);
        openXmlSwingWorker = new OpenXmlFileSwingWorker();
        openXmlSwingWorker.execute();
    }//GEN-LAST:event_lastXmlOutputJMenuItemActionPerformed

    /**
     * Method which prints information that operation is already in progress.
     */
    private void printOperationInProgress() {
        JOptionPane.showMessageDialog(null, "Operation is already in progress", "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Method which print details about exception.
     * @param ex represents throwed exception
     */
    private void printException(Throwable ex) {
        JOptionPane.showMessageDialog(null, ex.getCause().getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Method which set up jLabel in Help menu item.
     * @param label represents jLabel to be set
     * @param fontSize represents size of font in label
     * @param text rperesents text which will be written out in label
     */
    private void setUpJLabel(JLabel label, float fontSize, String text) {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
        label.setFont(label.getFont().deriveFont(fontSize));
    }
    
    /**
     * Method which add some component to frame
     * @param frame represents frame where compononent will be added
     * @param component represents  compononent to be added to frame
     * @param xSize represents x size of component in frame
     * @param ySize represents y size of component in frame
     */
    private void addComponentToFrame(JFrame frame, JComponent component, int xSize, int ySize) {
        frame.add(component, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(xSize, ySize);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
    }
    
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
            java.util.logging.Logger.getLogger(AnalyzerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnalyzerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnalyzerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnalyzerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new AnalyzerApp().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton analyzeJButton;
    private javax.swing.JPanel analyzerJPanel;
    private javax.swing.JTabbedPane applicationJTabbedPane;
    private javax.swing.JButton chooseFileJButton;
    private javax.swing.JLabel choosenDatabaseFileJLabel;
    private javax.swing.JMenuItem creditsJMenuItem;
    private javax.swing.JFileChooser databaseJFileChooser;
    private javax.swing.JMenuItem exitJMenuItem;
    private javax.swing.JMenu fileJMenu;
    private javax.swing.JMenu helpJMenu;
    private javax.swing.JLabel infoSelectNameJLabel;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JLabel justInfoJLabel;
    private javax.swing.JMenuItem lastHtmlOutputJMenuItem;
    private javax.swing.JMenuItem lastXmlOutputJMenuItem;
    private javax.swing.JTextArea loggerJTextArea;
    private javax.swing.JScrollPane loggerOutputJScrollPane;
    private javax.swing.JMenuItem manualJMenuItem;
    private javax.swing.JCheckBox outputAllJCheckBox;
    private javax.swing.JComboBox<String> processNameJComboBox;
    private javax.swing.JDialog selectProcessJDialog;
    private javax.swing.JPanel selectProcessJPanel;
    private javax.swing.JButton specificProcessJButton;
    private javax.swing.JCheckBox specificProcessNameJCheckBox;
    private javax.swing.JCheckBox specifyNumberOfGroupsJCheckBox;
    private javax.swing.JMenu visualizeJMenu;
    // End of variables declaration//GEN-END:variables
}
