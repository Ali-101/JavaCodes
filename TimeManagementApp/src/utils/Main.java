package utils;

import dataBase.ConnectionManager;
import gui.MainFrame;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    private static Connection conn = null;

    public static void main(String args[]) throws SQLException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
            //  new SecondMainFrame();
               new MainFrame();
            }

        });
        try {
            conn = ConnectionManager.connect();
            System.out.println("connected");
            System.out.println("done");

        } catch (SQLException ex) {
            System.err.println(ex);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
