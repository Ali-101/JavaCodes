package gui;

import interfaces.LoginListener;
import interfaces.SignUpListener;
import dataBase.AddNewUserToDatabase;
import dataBase.EnsureDataConsistency;
import dataBase.RetrieveDataFromDatabase;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

public class MainFrame extends javax.swing.JFrame {

    private SecondMainFrame secondFrame;
    private SignFrame signFrame;
    private LoginFrame loginFrame;
    private Preferences prefs_util;

    public MainFrame() {
        initComponents();

        setVisible(true);
        setLocation(350, 100);

        secondFrame = new SecondMainFrame();
        prefs_util = Preferences.userRoot().node(this.getClass().getName());
        signFrame = new SignFrame();
        loginFrame = new LoginFrame();

        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signFrame.setVisible(true);
                setEnabled(false);
            }
        });
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
                System.gc();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
                System.gc();
            }

        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        signFrame.setSignUpListener(new SignUpListener() {
            public void setOkBtnEvent() {
                String user = signFrame.getUserField();
                String password = signFrame.getPasswordField();
                String email = signFrame.getEmailField();

                if ((user.equals("") || (email.equals("")) || (password.equals("")))) {
                    JOptionPane.showMessageDialog(MainFrame.this, " All fields are required !\n Please fill the empty ones .", " :( Warning Message", JOptionPane.WARNING_MESSAGE);
                }
                boolean checkPassword = false;
                boolean checkUser = false;

                if (!user.equals("") && !email.equals("") && !password.equals("")) {
                    checkPassword = EnsureDataConsistency.ensureDuplicatePassword(password);
                    checkUser = AddNewUserToDatabase.addUser(user, password, email);
                }
                if (checkPassword == false) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Try with another password please ! ", " :(  Error Message", JOptionPane.ERROR_MESSAGE);

                }
                if (checkUser == false) {
                    JOptionPane.showMessageDialog(MainFrame.this, "You are not sign in \n Please check the information ! ", " :(  Warning Message", JOptionPane.WARNING_MESSAGE);

                }
                if (checkUser && checkPassword) {
                    JOptionPane.showMessageDialog(MainFrame.this, " By clicking OK you are agree on our privacy setting ! ", "   Attention ", JOptionPane.NO_OPTION);
                    JOptionPane.showMessageDialog(MainFrame.this, "You are registered successfully ! ", "  :)  Congratulation", JOptionPane.NO_OPTION);
                    setVisible(false);

                    secondFrame.setUserField(user);
                    secondFrame.setEmailField(email);
                    secondFrame.setPassword(password);
                    secondFrame.setVisible(true);
                    secondFrame.setTipsVisible(true);
                    signFrame.setVisible(false);
                    setVisible(false);
                }
                if (signFrame.getRememberPassword()) {

                    setRememberSetting(user, password, true);
                } else {
                    setRememberSetting(user, password, false);

                }

            }

            public void setCancelEvent() {

                signFrame.setVisible(false);
                setVisible(true);
                setEnabled(true);

            }

            @Override
            public void setHaveAnAcountEvent() {
                signFrame.setVisible(false);
                loginFrame.setVisible(true);
            }
        });

        signFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setEnabled(true);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        loginFrame.setLoginFrameListener(new LoginListener() {

            public void setOkBtnEvent(String _userName, String _password) {

                String user = loginFrame.getUserField();
                String password = new String(loginFrame.getPasswordField());
                boolean checkUser = EnsureDataConsistency.ensureLoginSetting(user, password);

                if (loginFrame.getRememberPassword()) {
                    setRememberSetting(_userName, _password, true);

                } else {
                    setRememberSetting(_userName, _password, false);

                }
                if (loginFrame.getUserField().equals("") || loginFrame.getPasswordField().equals("")) {
                    JOptionPane.showMessageDialog(MainFrame.this, " All field is required ", "Error Message ", JOptionPane.ERROR_MESSAGE);
                } else if (checkUser) {

                    secondFrame.setUserField(user);
                    secondFrame.setEmailField(RetrieveDataFromDatabase.retrieveEmail(password));
                    secondFrame.setPassword(password);

                    loginFrame.setVisible(false);
                    secondFrame.setVisible(true);
                    secondFrame.setTipsVisible(true);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, " Your acount is not existing \n Try with another acount please .", "Error", JOptionPane.WARNING_MESSAGE);
                }

            }

            public void setCancelEvent() {
                loginFrame.setVisible(false);
                signFrame.setVisible(true);
            }

            public void setForgetPasswordEvent() {
                String restorePassword = JOptionPane.showInputDialog(MainFrame.this, "Enter your email please ", "Restore password !", JOptionPane.INFORMATION_MESSAGE);
            }

        });
        loginFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginFrame.setVisible(false);
                signFrame.setVisible(true);
            }
        });
        String user = prefs_util.get("userName", "");
        String password = prefs_util.get("password", "");
        loginFrame.setDefault(user, password);

    }

    public void setRememberSetting(String userName, String password, boolean checked) {
        if (checked) {

            prefs_util.put("userName", userName);
            prefs_util.put("password", password);
            loginFrame.setDefault(userName, password);

        } else {
            prefs_util.remove("userName");
            prefs_util.remove("password");
            prefs_util.remove("email");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        exitBtn = new javax.swing.JButton();
        startBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(" Welcome ");
        setResizable(false);

        exitBtn.setFont(new java.awt.Font("Serif", 3, 24)); // NOI18N
        exitBtn.setForeground(new java.awt.Color(51, 51, 51));
        exitBtn.setText("Exit");

        startBtn.setFont(new java.awt.Font("Serif", 3, 24)); // NOI18N
        startBtn.setForeground(new java.awt.Color(51, 51, 51));
        startBtn.setText("Start");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton startBtn;
    // End of variables declaration//GEN-END:variables
}
