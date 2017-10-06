package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteTaskFromDatabase {

    public static boolean deleteTask(String taskTitle) {
        String SQL = "DELETE FROM tasks WHERE taskTitle = ? ";
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {
            pst.setString(1, taskTitle);
            int rowEffect = pst.executeUpdate();
            if (rowEffect == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DeleteTaskFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
