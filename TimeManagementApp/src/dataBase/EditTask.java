package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditTask {

    public static void editTaskTitle(String title, String preTaskTitle) {
        String SQL = "UPDATE `tasks` SET taskTitle = ? WHERE taskTitle =?";
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, title);
            pst.setString(2, preTaskTitle);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EditTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void editProjectTitle(String title, String taskTitle) {
        String SQL = "UPDATE `tasks` SET projectTitle = ? WHERE taskTitle = ?";
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, title);
            pst.setString(2, taskTitle);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EditTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void editDate(String date, String taskTitle) {
        String SQL = "UPDATE `tasks` SET date=? WHERE taskTitle = ?";
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, date);
            pst.setString(2, taskTitle);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EditTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void editStatus(boolean status, String taskTitle) {
        String SQL = "UPDATE `tasks`SET status = ? WHERE taskTitle = ?";
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setBoolean(1, status);
            pst.setString(2, taskTitle);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EditTask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static boolean editDesc(String desc, String taskTitle) {
        String SQL = "UPDATE `tasks` SET description=? WHERE taskTitle = ?";

        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, desc);
            pst.setString(2, taskTitle);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EditTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void editPriority(int priority, String taskTitle) {
        String SQL = "UPDATE `tasks` SET priority=? WHERE taskTitle = ?";
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setInt(1, priority);
            pst.setString(2, taskTitle);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EditTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
