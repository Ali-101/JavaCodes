package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddTaskToDatabase {

    public static boolean saveToDatabase(String projectTitle, String taskTitle, String desc, String date, int priority, boolean achieved) {
        String SQL = "INSERT INTO `tasks`(`projectTitle`,`taskTitle`,`description`,`date`,`priority`,`status`) VALUES (?,?,?,?,?,?)";

        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {

            pst.setString(1, projectTitle);
            pst.setString(2, taskTitle);
            pst.setString(3, desc);
            pst.setString(4, date);
            pst.setInt(5, priority);
            pst.setBoolean(6, achieved);

            int rowEffect = pst.executeUpdate();
            if (rowEffect == 1) {
                return true;

            } else {
                return false;
            }

        } catch (SQLException ex) {
        }
        return false;
    }
}
