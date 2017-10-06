package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RetrieveDataFromDatabase {

    private static Connection conn = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    public static String retrieveEmail(String password) {

        String SQL = "SELECT Email FROM user WHERE password=?";
        try {
            conn = ConnectionManager.connect();
            pst = conn.prepareStatement(SQL);
            pst.setString(1, password);
            rs = pst.executeQuery();
            while (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RetrieveDataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not found";
    }

    public static ResultSet retrieveProject(String password) {
        String SQL = "SELECT title FROM project,user WHERE user.password=? AND user.password=project.password";

        try {
            conn = ConnectionManager.connect();
            pst = conn.prepareStatement(SQL);
            pst.setString(1, password);
            rs = pst.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(RetrieveDataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet retrieveTasks(String projectTitle) {

        String SQL = "SELECT taskTitle,description,date,priority,status FROM tasks,project WHERE project.title=? and project.title=tasks.projectTitle;";

        try {
            conn = ConnectionManager.connect();
            pst = conn.prepareStatement(SQL);
            pst.setString(1, projectTitle);
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(RetrieveDataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet retrieveSearchResult(String text, String val, String projectTitle) {
        String SQL = null;
        if (text.equals(" Search by task title ")) {
            SQL = "SELECT taskTitle FROM tasks,project WHERE taskTitle like ? AND project.title=? AND project.title=tasks.projectTitle ;";
        } else if (text.equals(" Search by task description  ")) {
            SQL = "SELECT taskTitle FROM tasks,project WHERE description like ? AND project.title=? AND project.title=tasks.projectTitle ;";
        }

        try {
            conn = ConnectionManager.connect();
            pst = conn.prepareStatement(SQL);
            pst.setString(1, val);
            pst.setString(2, projectTitle);
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(RetrieveDataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static void disconnect() throws SQLException {
        if (conn != null) {
            conn.close();
        }
        if (rs != null) {
            rs.close();
        }
        if (pst != null) {
            pst.close();
        }
    }

}
