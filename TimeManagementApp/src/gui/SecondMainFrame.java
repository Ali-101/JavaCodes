package gui;

import utils.TaskListRenderer;
import utils.ProjectListCellRenderer;
import utils.Utils;
import utils.Tasks;
import utils.Projects;
import tableUtils.FormEvent;
import tableUtils.TaskTable;
import tableUtils.Controller;
import interfaces.FormListener;
import dataBase.AddNewProject;
import dataBase.AddTaskToDatabase;
import dataBase.DeleteProject;
import dataBase.EnsureDataConsistency;
import dataBase.RetrieveDataFromDatabase;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SecondMainFrame extends javax.swing.JFrame {

    private JSplitPane splitPane;
    private JSplitPane splitPaneSearch;
    private ButtonGroup priorityGroup;
    private DefaultMutableTreeNode top;
    private JTabbedPane tabPane;
    private TaskTable taskTable;
    private String projectTitle;
    private String password;
    private Controller controller;
    private TipsFrame tips;
    private AboutFrame aboutFrame;

    public SecondMainFrame() {

        initComponents();
        setLayout(new BorderLayout());
        setSize(1050, 700);
        setResizable(true);
        addBorder();

        priorityGroup = new ButtonGroup();
        tabPane = new JTabbedPane();
        tips = new TipsFrame(SecondMainFrame.this);
        controller = new Controller();
        taskTable = new TaskTable();
        taskTable.setData(controller.getTask());

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, newTaskPanel, tabPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.01);

        splitPaneSearch = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, searchPanel);
        splitPaneSearch.setResizeWeight(0.8);
        splitPaneSearch.setEnabled(false);

        tabPane.add(" Tasks Info  ", showTaskInfo);
        tabPane.add(" Tasks  ", taskTable);
        tabPane.setFocusable(true);
        tabPane.setIconAt(0, Utils.createImage("/images/17.png"));
        tabPane.setIconAt(1, Utils.createImage("/images/43.png"));

        projectList.setCellRenderer(new ProjectListCellRenderer());
        projectList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                delete.setEnabled(true);
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Projects project = (Projects) projectList.getSelectedValue();
                DeleteProject.deleteProject(project.getTitle());
                int a = JOptionPane.showConfirmDialog(SecondMainFrame.this, " RESTART the app now to take effect !");
                if (a == 0) {
                    delete.setVisible(false);
                    restart(userField.getText(), emailField.getText(), projectField.getText(), password);
                }
                delete.setEnabled(false);
            }
        });

        titleList.setCellRenderer(new TaskListRenderer());
        titleList.addListSelectionListener((ListSelectionEvent e) -> {

            Tasks task = (Tasks) titleList.getSelectedValue();
            try {
                taskDetailsArea.setText(" Title : " + task.getTaskTitle() + "\n Description : " + task.getDesc()
                        + "\n Date : " + task.getDate() + "\n Priority level : " + task.getPriority()
                        + "\n Status : " + task.getStatus() + "\n Project name : " + projectField.getText());
            } catch (Exception ex) {
            }
        });
        taskTable.setTableListener((int row) -> {
            controller.removeTask(row);
        });

        add(splitPaneSearch, BorderLayout.CENTER);
        ////////////////////////////////////////////////////////// set radio button
        priorityGroup.add(jRadioButton1);
        priorityGroup.add(jRadioButton2);
        priorityGroup.add(jRadioButton3);
        priorityGroup.add(jRadioButton4);
        priorityGroup.add(jRadioButton5);

        becomeEnabled(false);

        addTaskBtn.addActionListener((ActionEvent e) -> {
            if (projectField.getText().length() == 0) {
                JOptionPane.showMessageDialog(SecondMainFrame.this, "  Please select a project first or create a new one  ", "Error  ): ", JOptionPane.ERROR_MESSAGE);
            } else if (titleField.getText().length() == 0 || dateField.getText().length() == 0 || descArea.getText().length() == 0) {
                JOptionPane.showMessageDialog(SecondMainFrame.this, " All fields required !", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                addNewTask();
            }

        }
        );
        setFormListener(
                (FormEvent ev) -> {
                    controller.addTask(ev);
                    taskTable.refresh();
                }
        );
        projectTree.getSelectionModel()
                .addTreeSelectionListener(new TreeSelectionListener() {
                    @Override
                    public void valueChanged(TreeSelectionEvent e
                    ) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) projectTree.getLastSelectedPathComponent();
                        try {
                            String s = node.getUserObject().toString();
                            taskListModel.removeAllElements();
                            taskDetailsArea.setText("");
                            if (s.equals("Projects")) {
                            } else {
                                refreshBtn.setEnabled(true);
                                projectField.setText(s);
                                ResultSet rs = RetrieveDataFromDatabase.retrieveTasks(s);
                                while (rs.next()) {
                                    Tasks task = new Tasks(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5), s);
                                    taskListModel.addElement(task);
                                    FormEvent ev = new FormEvent(this, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getBoolean(6));
                                    controller.addTask(ev);
                                    taskTable.refresh();
                                }
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
                );
        projectTree.setShowsRootHandles(true);
        clearBtn.addActionListener(
                (ActionEvent e) -> {
                    clearData();
                });

        searchBtn.addActionListener(
                (ActionEvent e) -> {
                    ResultSet rs = null;
                    String text = searchField.getText();
                    String comboBox = (String) titleDescComboBox.getSelectedItem();
                    if (comboBox.equals(" Search by task title ")) {
                        rs = RetrieveDataFromDatabase.retrieveSearchResult(" Search by task title ", text, projectField.getText());
                    } else if (comboBox.equals(" Search by task description  ")) {
                        rs = RetrieveDataFromDatabase.retrieveSearchResult(" Search by task description  ", text, projectField.getText());
                    }
                    try {
                        if (!rs.first()) {
                            JOptionPane.showMessageDialog(SecondMainFrame.this, "There are no results match ", "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                        while (rs.next()) {
                            searchArea.setText(" Task title : " + rs.getString(1));
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(SecondMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
        searchClearBtn.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                searchArea.setText("");
            }
        });
        setTreeIcon();

        setPriorityFilterEnabled(
                false);

        setJMenuBar(createMenu());

        addWindowListener(
                new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
                System.gc();
                try {
                    RetrieveDataFromDatabase.disconnect();
                } catch (SQLException ex) {
                    Logger.getLogger(SecondMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initData(projectField.getText(), password);
                refreshBtn.setEnabled(false);
            }
        });
    }

    public void addBorder() {
        int x = 10;
        Border outside = BorderFactory.createEmptyBorder(x, x, x, x);
        Border inner = BorderFactory.createTitledBorder("Add new task ");
        newTaskPanel.setBorder(BorderFactory.createCompoundBorder(outside, inner));

        int x2 = 11;
        Border out = BorderFactory.createEmptyBorder(x2, x2, x2, x2);
        Border in = BorderFactory.createTitledBorder("  The actual tasks ");
        showTaskInfo.setBorder(BorderFactory.createCompoundBorder(out, in));

        int x3 = 11;
        Border outt = BorderFactory.createEmptyBorder(x3, x3, x3, x3);
        Border inn = BorderFactory.createTitledBorder("  The search section ");
        searchPanel.setBorder(BorderFactory.createCompoundBorder(outt, inn));

        int x4 = 11;
        Border outtt = BorderFactory.createEmptyBorder(x4, x4, x4, x4);
        Border innn = BorderFactory.createTitledBorder("  The projects list ");
        projects.setBorder(BorderFactory.createCompoundBorder(outtt, innn));

        int x5 = 10;
        Border outsidee = BorderFactory.createEmptyBorder(x5, x5, x5, x5);
        Border innerr = BorderFactory.createTitledBorder(" Filter Tasks  ");
        filterPanel.setBorder(BorderFactory.createCompoundBorder(outsidee, innerr));

    }

    public void setPriorityFilterEnabled(boolean value) {

        jRadioButton6.setEnabled(value);
        jRadioButton7.setEnabled(value);
        jRadioButton8.setEnabled(value);
        jRadioButton9.setEnabled(value);
        jRadioButton10.setEnabled(value);
    }

    public void addNewTask() {
        int priority = 0;
        if (jRadioButton1.isSelected()) {
            priority = 1;
        } else if (jRadioButton2.isSelected()) {
            priority = 2;
        } else if (jRadioButton3.isSelected()) {
            priority = 3;
        } else if (jRadioButton4.isSelected()) {
            priority = 5;
        } else if (jRadioButton5.isSelected()) {
            priority = 4;
        }
        if (EnsureDataConsistency.ensureDublicateTaskTitle(titleField.getText())) {
            taskListModel.addElement(new Tasks(titleField.getText(), descArea.getText(), dateField.getText(), priority, achieved.isSelected(), projectField.getText()));
            FormEvent ev = new FormEvent(this, titleField.getText(), projectField.getText(), descArea.getText(), dateField.getText(), priority, achieved.isSelected());
            if (formListener != null) {
                formListener.formEventOccured(ev);
            }
        } else {
            JOptionPane.showMessageDialog(SecondMainFrame.this, "Please select another task title !", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void becomeEnabled(boolean val) {
        newTaskPanel.setEnabled(val);
        titleField.setEnabled(val);
        dateField.setEnabled(val);
        descArea.setEnabled(val);
        jRadioButton1.setEnabled(val);
        jRadioButton2.setEnabled(val);
        jRadioButton3.setEnabled(val);
        jRadioButton4.setEnabled(val);
        jRadioButton5.setEnabled(val);
        addTaskBtn.setEnabled(val);
        jLabel1.setEnabled(val);
        jLabel2.setEnabled(val);
        jLabel3.setEnabled(val);
        jLabel7.setEnabled(val);
        jLabel5.setEnabled(val);
        jLabel6.setEnabled(val);
        jScrollPane1.setEnabled(val);
        achieved.setEnabled(val);
        projectField.setEnabled(val);
        jLabel4.setEnabled(val);
        jLabel8.setEnabled(val);
        clearBtn.setEnabled(val);
        typeBtn.setEnabled(val);
        typeComboBox.setEnabled(val);
    }

    public void createProject(String title, String password) {
        DefaultMutableTreeNode branch = new DefaultMutableTreeNode(new Projects(title, password));
        top.add(branch);
    }

    private DefaultMutableTreeNode createTree() {
        top = new DefaultMutableTreeNode("Projects");
        return top;
    }

    private void clearData() {
        titleField.setText("");
        dateField.setText("");
        descArea.setText("");
        achieved.setSelected(false);
        priorityGroup.clearSelection();
    }

    public final void initData(String projectName, String password) {
        ResultSet rs = null;
        try {
            rs = RetrieveDataFromDatabase.retrieveProject(password);
            while (rs.next()) {
                becomeEnabled(true);
                DefaultMutableTreeNode branch = new DefaultMutableTreeNode(rs.getString(1));
                top.add(branch);
                projectListModel.addElement(new Projects(rs.getString(1), password));
            }
            taskTable.setTableData(projectName);
        } catch (SQLException ex) {
            Logger.getLogger(SecondMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setEmailField(String emailField) {
        this.emailField.setText(emailField);
    }

    public void setPassword(String passwordField) {
        this.password = passwordField;
    }

    public void setUserField(String userField) {
        this.userField.setText(userField);
    }

    public String getPassword() {
        return password;
    }

    public String getProjectField() {
        return projectField.getText();
    }

    public void setTreeIcon() {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) projectTree.getCellRenderer();
        Icon closedIcon = Utils.createImage("/images/26.gif");
        Icon openIcon = Utils.createImage("/images/52.gif");
        Icon leafIcon = Utils.createImage("/images/63.png");
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);
    }

    public void setFormListener(FormListener l) {
        this.formListener = l;
    }

    public void setTipsVisible(boolean val) {
        tips.setVisible(val);
    }

    public JMenuBar createMenu() {
        JMenuBar mainMenu = new JMenuBar();

        JMenu fileMenu = new JMenu(" File ");
        JMenu editMenu = new JMenu(" Edit ");
        JMenu helpMenu = new JMenu(" Help");

        JMenuItem newProject = new JMenuItem("  New Project             ", Utils.createImage("/images/52.gif"));
        JMenuItem newTask = new JMenuItem("  New Task ", Utils.createImage("/images/63.png"));
        JMenuItem about = new JMenuItem("  About ", Utils.createImage("/images/48.png"));
        JMenuItem notes = new JMenuItem("  Notes         ", Utils.createImage("/images/49.png"));
        JMenuItem editTask = new JMenuItem("   Edit Task                 ", Utils.createImage("/images/69.png"));
        JMenuItem exit = new JMenuItem("  Exit ", Utils.createImage("/images/45.png"));
        JMenuItem save = new JMenuItem("  Save ", Utils.createImage("/images/22.png"));
        JMenuItem deleteProject = new JMenuItem("   Delete Project ", Utils.createImage("/images/33.png"));
        JMenuItem restart = new JMenuItem("   Restart ", Utils.createImage("/images/75.png"));

        fileMenu.add(newProject);
        fileMenu.add(newTask);
        fileMenu.addSeparator();
        fileMenu.add(save);
        fileMenu.addSeparator();
        fileMenu.add(restart);
        fileMenu.add(exit);

        editMenu.add(editTask);
        editMenu.addSeparator();
        editMenu.add(deleteProject);

        helpMenu.add(notes);
        helpMenu.add(about);

        mainMenu.add(fileMenu);
        mainMenu.add(editMenu);
        mainMenu.add(helpMenu);

        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        newProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        newTask.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        editTask.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        deleteProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        notes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
                try {
                    RetrieveDataFromDatabase.disconnect();

                } catch (SQLException ex) {
                    Logger.getLogger(SecondMainFrame.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        newProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                projectTitle = JOptionPane.showInputDialog("Enter the title of the new project ! ");
                boolean checked = EnsureDataConsistency.ensureDuplicateProjectTitle(projectTitle);
                if (!checked && !projectTitle.equals("")) {
                    JOptionPane.showMessageDialog(SecondMainFrame.this, "  Try with another title please ! ", "Error", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    if (!projectTitle.equals("")) {
                        boolean check = AddNewProject.addNewProject(projectTitle, password);
                        if (!check) {
                            JOptionPane.showMessageDialog(SecondMainFrame.this, "  You can't leave title empty !", "Attention", JOptionPane.ERROR_MESSAGE);
                            projectTitle = JOptionPane.showInputDialog("Enter the title of the new project ! ");
                        }
                    }
                } catch (Exception ee) {
                }
                projectField.setText(projectTitle);
                createProject(projectTitle, password);
            }
        });
        newTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                becomeEnabled(true);
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int priority = 0;
                if (jRadioButton1.isSelected()) {
                    priority = 1;
                } else if (jRadioButton2.isSelected()) {
                    priority = 2;
                } else if (jRadioButton3.isSelected()) {
                    priority = 3;
                } else if (jRadioButton4.isSelected()) {
                    priority = 5;
                } else if (jRadioButton5.isSelected()) {
                    priority = 4;
                }
                if (EnsureDataConsistency.ensureDublicateTaskTitle(titleField.getText())) {
                    boolean checked = AddTaskToDatabase.saveToDatabase(projectField.getText(), titleField.getText(), descArea.getText(), dateField.getText(), priority, achieved.isSelected());

                    if (checked) {
                        JOptionPane.showMessageDialog(SecondMainFrame.this, "  Added Successfully ", "Done (: ", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(SecondMainFrame.this, "  Something wrong happened  ): \n Please check on the information ..  ", "Error  ): ", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(SecondMainFrame.this, "Please select another task title !", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        notes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tips.setVisible(true);
            }
        });
        editTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabPane.setSelectedIndex(1);

            }
        });
        deleteProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(true);
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutFrame = new AboutFrame(SecondMainFrame.this);
                aboutFrame.setVisible(true);
            }
        });
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart(userField.getText(), emailField.getText(), password, projectField.getText());
            }
        });

        return mainMenu;
    }

    public void restart(String userName, String email, String password, String projectName) {

        dispose();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(SecondMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        initData(projectName, password);
        setUserField(userName);
        setEmailField(email);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        dialog = new javax.swing.JDialog();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        projectListModel = new DefaultListModel();
        projectList = new javax.swing.JList(projectListModel);
        delete = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        newTaskPanel = new javax.swing.JPanel();
        titleField = new javax.swing.JTextField();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        descArea = new javax.swing.JTextArea();
        jRadioButton5 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        achieved = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        addTaskBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        projectField = new javax.swing.JTextField();
        clearBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox<>();
        typeBtn = new javax.swing.JLabel();
        filterPanel = new javax.swing.JPanel();
        taskShown = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        refreshBtn = new javax.swing.JButton();
        showTaskInfo = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        taskListModel = new  DefaultListModel();
        titleList = new javax.swing.JList(taskListModel);
        jScrollPane3 = new javax.swing.JScrollPane();
        taskDetailsArea = new javax.swing.JTextArea();
        searchPanel = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        titleDescComboBox = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        searchArea = new javax.swing.JTextArea();
        searchClearBtn = new javax.swing.JButton();
        projects = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        projectTree = new javax.swing.JTree(createTree());
        jSeparator1 = new javax.swing.JSeparator();

        jCheckBox1.setText("jCheckBox1");

        dialog.setTitle(" Delete Project");
        dialog.setSize(181,259);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(SecondMainFrame.this);

        jLabel10.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jLabel10.setText("Select Project ");

        jScrollPane6.setViewportView(projectList);

        delete.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        delete.setText("Delete");
        delete.setEnabled(false);
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogLayout = new javax.swing.GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(dialogLayout);
        dialogLayout.setHorizontalGroup(
            dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(dialogLayout.createSequentialGroup()
                        .addComponent(delete, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(dialogLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dialogLayout.setVerticalGroup(
            dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delete)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(" Time Managment App");

        titleField.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        titleField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        titleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleFieldActionPerformed(evt);
            }
        });

        jRadioButton3.setFont(new java.awt.Font("Serif", 3, 12)); // NOI18N
        jRadioButton3.setText(" 3");

        jLabel2.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel2.setText("Description :");

        jRadioButton4.setFont(new java.awt.Font("Serif", 3, 12)); // NOI18N
        jRadioButton4.setText(" 5");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jScrollPane1.setToolTipText("This area to add task description");

        descArea.setColumns(15);
        descArea.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        descArea.setRows(5);
        descArea.setToolTipText("");
        descArea.setWrapStyleWord(true);
        descArea.setMaximumSize(new java.awt.Dimension(20, 5));
        descArea.setSelectionEnd(20);
        jScrollPane1.setViewportView(descArea);

        jRadioButton5.setFont(new java.awt.Font("Serif", 3, 12)); // NOI18N
        jRadioButton5.setText(" 4");

        jLabel6.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText(" High priority");

        jLabel3.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel3.setText("Date :");

        jLabel7.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText(" Low priority");

        dateField.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        dateField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dateFieldMousePressed(evt);
            }
        });
        dateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateFieldActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel5.setText(" Priority :");

        jRadioButton1.setFont(new java.awt.Font("Serif", 3, 12)); // NOI18N
        jRadioButton1.setText(" 1");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel1.setText("Title  :");

        jRadioButton2.setFont(new java.awt.Font("Serif", 3, 12)); // NOI18N
        jRadioButton2.setText(" 2");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        achieved.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        achieved.setText("Achieved");

        jLabel8.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel8.setText(" Status :");

        addTaskBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        addTaskBtn.setText("   Add task");
        addTaskBtn.setIcon(Utils.createImage("/images/13.gif"));
        addTaskBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTaskBtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel4.setText("Project  :");

        projectField.setEditable(false);
        projectField.setBackground(new java.awt.Color(255, 255, 255));
        projectField.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N

        clearBtn.setIcon(Utils.createImage("/images/14.png"));
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel11.setText("Email :");

        jLabel9.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel9.setText("User name :");

        userField.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        userField.setEnabled(false);

        emailField.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        emailField.setEnabled(false);
        emailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailFieldActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel12.setText("Your Acount Information");

        DefaultComboBoxModel comboList2 =  new DefaultComboBoxModel();
        comboList2.addElement(" Personal task");
        comboList2.addElement(" Shared  task");
        typeComboBox.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        typeComboBox.setModel(comboList2);

        typeBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        typeBtn.setText("Type :");

        DefaultComboBoxModel comboList1 = new DefaultComboBoxModel();
        comboList1.addElement(" Descending order");
        comboList1.addElement(" Present day ");
        comboList1.addElement(" Present week");
        comboList1.addElement(" Selected priority");
        comboList1.addElement(" Achieved task");
        comboList1.addElement(" Unachieved task");
        taskShown.setModel(comboList1);
        taskShown.setSelectedIndex(0);

        jLabel13.setText("Show tasks depending on");

        jRadioButton6.setText("1");

        jRadioButton7.setText("2");

        jRadioButton8.setText("3");

        jRadioButton9.setText("4");

        jRadioButton10.setText("5");

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(filterPanelLayout.createSequentialGroup()
                        .addComponent(jRadioButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(taskShown, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taskShown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton7)
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButton6)
                        .addComponent(jRadioButton8)
                        .addComponent(jRadioButton9)
                        .addComponent(jRadioButton10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        refreshBtn.setIcon(Utils.createImage("/images/42.png"));

        javax.swing.GroupLayout newTaskPanelLayout = new javax.swing.GroupLayout(newTaskPanel);
        newTaskPanel.setLayout(newTaskPanelLayout);
        newTaskPanelLayout.setHorizontalGroup(
            newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newTaskPanelLayout.createSequentialGroup()
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(newTaskPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(newTaskPanelLayout.createSequentialGroup()
                                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton3)
                                    .addComponent(jRadioButton4)
                                    .addComponent(jRadioButton5)
                                    .addComponent(jRadioButton1)
                                    .addComponent(jRadioButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)))
                            .addComponent(achieved))))
                .addGap(81, 81, 81))
            .addGroup(newTaskPanelLayout.createSequentialGroup()
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newTaskPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newTaskPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel1))
                            .addGroup(newTaskPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(3, 3, 3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateField)
                            .addComponent(titleField)
                            .addComponent(projectField)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newTaskPanelLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(typeBtn)
                        .addGap(18, 18, 18)
                        .addComponent(typeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(filterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(newTaskPanelLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(addTaskBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newTaskPanelLayout.createSequentialGroup()
                        .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(refreshBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newTaskPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel11))))
                        .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(newTaskPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(emailField)
                                    .addComponent(userField)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newTaskPanelLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        newTaskPanelLayout.setVerticalGroup(
            newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newTaskPanelLayout.createSequentialGroup()
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(newTaskPanelLayout.createSequentialGroup()
                        .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(29, 29, 29))
                    .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(projectField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newTaskPanelLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeComboBox)
                    .addComponent(typeBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(achieved)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton4)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(newTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addTaskBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,taskDetailsArea,titleList);
        add(jSplitPane1,BorderLayout.CENTER);
        jSplitPane1.setResizeWeight(0.7);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jSplitPane1.setOneTouchExpandable(true);

        titleList.setFont(new java.awt.Font("Serif", 3, 14)); // NOI18N
        titleList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        titleList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(titleList);

        jSplitPane1.setTopComponent(jScrollPane2);

        taskDetailsArea.setEditable(false);
        taskDetailsArea.setColumns(20);
        taskDetailsArea.setFont(new java.awt.Font("Serif", 3, 18)); // NOI18N
        taskDetailsArea.setRows(5);
        jScrollPane3.setViewportView(taskDetailsArea);

        jSplitPane1.setRightComponent(jScrollPane3);

        javax.swing.GroupLayout showTaskInfoLayout = new javax.swing.GroupLayout(showTaskInfo);
        showTaskInfo.setLayout(showTaskInfoLayout);
        showTaskInfoLayout.setHorizontalGroup(
            showTaskInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(showTaskInfoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );
        showTaskInfoLayout.setVerticalGroup(
            showTaskInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(showTaskInfoLayout.createSequentialGroup()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );

        add(projects,BorderLayout.PAGE_START);

        searchField.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        searchField.setAlignmentX(2.0F);

        searchBtn.setIcon(Utils.createImage("/images/65.gif"));

        DefaultComboBoxModel comboList = new DefaultComboBoxModel();
        comboList.addElement(" Search by task title ");
        comboList.addElement(" Search by task description  ");
        titleDescComboBox.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        titleDescComboBox.setModel(comboList);
        titleDescComboBox.setSelectedIndex(0);

        searchArea.setEditable(false);
        searchArea.setColumns(20);
        searchArea.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        searchArea.setRows(5);
        jScrollPane4.setViewportView(searchArea);

        searchClearBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        searchClearBtn.setText("    Clear");
        searchClearBtn.setIcon(Utils.createImage("/images/59.gif"));

        add(projects,BorderLayout.PAGE_START);

        projectTree.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jScrollPane5.setViewportView(projectTree);

        javax.swing.GroupLayout projectsLayout = new javax.swing.GroupLayout(projects);
        projects.setLayout(projectsLayout);
        projectsLayout.setHorizontalGroup(
            projectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(projectsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        projectsLayout.setVerticalGroup(
            projectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(projectsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(projects, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, searchPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchClearBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(titleDescComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(searchPanelLayout.createSequentialGroup()
                                .addComponent(searchField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projects, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(titleDescComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchClearBtn)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(newTaskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showTaskInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(showTaskInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(newTaskPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void titleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleFieldActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void dateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateFieldActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void addTaskBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTaskBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addTaskBtnActionPerformed
    int sum = 0;
    private void dateFieldMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateFieldMousePressed

        if (sum == 0) {
            JOptionPane.showMessageDialog(SecondMainFrame.this, "Please Enter the date like your system date to avoid unexpected error . ", "Attention", JOptionPane.WARNING_MESSAGE);
        }
        sum++;

    }//GEN-LAST:event_dateFieldMousePressed

    private void emailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailFieldActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearBtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dialog.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
    }//GEN-LAST:event_deleteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox achieved;
    private javax.swing.JButton addTaskBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JTextField dateField;
    private javax.swing.JButton delete;
    private javax.swing.JTextArea descArea;
    private javax.swing.JDialog dialog;
    private javax.swing.JTextField emailField;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel newTaskPanel;
    private FormListener formListener;
    private javax.swing.JTextField projectField;
    private javax.swing.JList projectList;
    private DefaultListModel projectListModel;
    private javax.swing.JTree projectTree;
    private javax.swing.JPanel projects;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JTextArea searchArea;
    private javax.swing.JButton searchBtn;
    private javax.swing.JButton searchClearBtn;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel showTaskInfo;
    private JSplitPane middle;
    private javax.swing.JTextArea taskDetailsArea;
    private javax.swing.JComboBox<String> taskShown;
    private javax.swing.JComboBox titleDescComboBox;
    private javax.swing.JTextField titleField;
    private javax.swing.JList titleList;
    private javax.swing.DefaultListModel taskListModel;
    private javax.swing.JLabel typeBtn;
    private javax.swing.JComboBox<String> typeComboBox;
    private javax.swing.JTextField userField;
    // End of variables declaration//GEN-END:variables

}
