package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteProject {

    public static boolean deleteProject(String title) {

        String SQL = "DELETE FROM project WHERE title = ?";
        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL)) {
            pst.setString(1, title);
            int rowEffected = pst.executeUpdate();
            if (rowEffected == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DeleteProject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
