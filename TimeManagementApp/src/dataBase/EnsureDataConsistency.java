package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnsureDataConsistency {

    public static boolean ensureDuplicatePassword(String password) {
        String SQL = "SELECT password FROM user WHERE password = ?";
        ResultSet rs = null;
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, password);
            rs = pst.executeQuery();
            if (!rs.first()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("error");
            Logger.getLogger(EnsureDataConsistency.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnsureDataConsistency.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    public static boolean ensureDuplicateProjectTitle(String title) {
        String SQL = "SELECT title FROM project WHERE title=?";

        ResultSet rs = null;
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, title);
            rs = pst.executeQuery();
            if (!rs.first()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(EnsureDataConsistency.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnsureDataConsistency.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    public static boolean ensureLoginSetting(String userName, String password) {

        String SQL = "SELECT userName,password FROM user WHERE (userName,password)=(?,?)";

        ResultSet rs = null;

        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, userName);
            pst.setString(2, password);
            rs = pst.executeQuery();
            if (rs.first()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnsureDataConsistency.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EnsureDataConsistency.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return false;
    }

    public static boolean ensureExistingProject(String password) {
        String SQL = "SELECT title FROM project WHERE password=?";

        ResultSet rs = null;

        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, password);
            rs = pst.executeQuery();
            if (rs.first()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnsureDataConsistency.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public static boolean ensureDublicateTaskTitle(String taskTitle) {
        String SQL = "SELECT taskTitle FROM tasks WHERE taskTitle=?";

        ResultSet rs = null;

        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, taskTitle);
            rs = pst.executeQuery();
            if (rs.first()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnsureDataConsistency.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
