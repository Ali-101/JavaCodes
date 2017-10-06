package gui;

import interfaces.LoginListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class LoginFrame extends javax.swing.JFrame implements ActionListener {

    private LoginListener loginListener;

    public LoginFrame() {
        initComponents();

        setLocation(350, 100);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        rememberPassword.setSelected(true);
        passwordField.setEchoChar('*');

        okBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        forgetPasswordBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton clicked = (JButton) e.getSource();
        if (clicked == okBtn) {
            if (loginListener != null) {

                loginListener.setOkBtnEvent(userField.getText(), new String(passwordField.getPassword()));
            }
        }
        if (clicked == cancelBtn) {
            if (loginListener != null) {
                loginListener.setCancelEvent();
            }
        }
        if (clicked == forgetPasswordBtn) {
            if (loginListener != null) {
                loginListener.setForgetPasswordEvent();
            }
        }
    }

    public void setLoginFrameListener(LoginListener l) {
        this.loginListener = l;
    }

    public void setDefault(String user, String password) {
        userField.setText(user);
        passwordField.setText(password);

    }

    public char[] getPasswordField() {
        return passwordField.getPassword();
    }

    public String getUserField() {
        return userField.getText();
    }

    public boolean getRememberPassword() {
        return rememberPassword.isSelected();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        forgetPasswordBtn = new javax.swing.JButton();
        rememberPassword = new javax.swing.JCheckBox();

        setTitle("Login");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        jLabel1.setText("User Name :");

        jLabel2.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        jLabel2.setText("  Password :");

        userField.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N

        passwordField.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N

        okBtn.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        okBtn.setText("OK");

        cancelBtn.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        cancelBtn.setText("Cancel");

        forgetPasswordBtn.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        forgetPasswordBtn.setText("forget the password !");

        rememberPassword.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        rememberPassword.setText(" Remember password");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rememberPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(passwordField)
                            .addComponent(userField)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(forgetPasswordBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(okBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(userField, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rememberPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okBtn)
                    .addComponent(cancelBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forgetPasswordBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton forgetPasswordBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton okBtn;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JCheckBox rememberPassword;
    private javax.swing.JTextField userField;
    // End of variables declaration//GEN-END:variables

}
