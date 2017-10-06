package gui;

import interfaces.SignUpListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class SignFrame extends JFrame implements ActionListener {

    private SignUpListener signUpListener;

    public SignFrame() {
        initComponents();

        setLocation(350, 100);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        rememberPassword.setSelected(true);

        passwordField.setEchoChar('*');

        okBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        haveAcountBtn.addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {

        JButton click = (JButton) e.getSource();
        if (click == okBtn) {
            if (signUpListener != null) {
                signUpListener.setOkBtnEvent();
            }
        }
        if (click == cancelBtn) {
            if (signUpListener != null) {
                signUpListener.setCancelEvent();
            }
        }
        if (click == haveAcountBtn) {
            if (signUpListener != null) {
                signUpListener.setHaveAnAcountEvent();
            }

        }

    }

    public void setSignUpListener(SignUpListener s) {
        this.signUpListener = s;
    }

    public String getEmailField() {
        return emailField.getText();
    }

    public String getPasswordField() {
        return new String(passwordField.getPassword());
    }

    public String getUserField() {
        return userField.getText();
    }

    public boolean getRememberPassword() {
        return rememberPassword.isSelected();
    }

    public void setDefault(String userName, String password, String email) {
        userField.setText(userName);
        passwordField.setText(password);
        emailField.setText(email);
    }

    public void setPassword() {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        rememberPassword = new javax.swing.JCheckBox();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        emailField = new javax.swing.JTextField();
        haveAcountBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(" Register");

        jLabel4.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        jLabel4.setText("Email :");

        jLabel5.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        jLabel5.setText("User name :");

        jLabel6.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        jLabel6.setText("Password  : ");

        rememberPassword.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        rememberPassword.setText("  Remember password ");

        okBtn.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        okBtn.setText("OK");

        cancelBtn.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        cancelBtn.setText("Cancel");

        haveAcountBtn.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        haveAcountBtn.setText("Have an acount !");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(rememberPassword)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(23, 23, 23)
                                    .addComponent(jLabel4))
                                .addComponent(jLabel5))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(userField, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                .addComponent(passwordField)
                                .addComponent(emailField))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(haveAcountBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(okBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rememberPassword)
                .addGap(14, 14, 14)
                .addComponent(haveAcountBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn)
                    .addComponent(okBtn))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField emailField;
    private javax.swing.JButton haveAcountBtn;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JButton okBtn;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JCheckBox rememberPassword;
    private javax.swing.JTextField userField;
    // End of variables declaration//GEN-END:variables
}
