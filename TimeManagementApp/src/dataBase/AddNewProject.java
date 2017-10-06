package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddNewProject {

    public static boolean addNewProject(String title, String userPassword) {
        String SQL = "INSERT INTO project VALUES (?,?)";

        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL);) {
            pst.setString(1, title);
            pst.setString(2, userPassword);
            int rowEffect = pst.executeUpdate();
            if (rowEffect == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("error2");
            System.err.println(ex.getMessage());
        }
        return false;
    }
}
