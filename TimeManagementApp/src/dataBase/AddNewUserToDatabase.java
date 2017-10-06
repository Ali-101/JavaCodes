package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddNewUserToDatabase {

    public static boolean addUser(String userName, String password, String email) {
        String SQL = "INSERT INTO `user` VALUES (?,?,?)";

        try (Connection conn = ConnectionManager.connect();
                PreparedStatement pst = conn.prepareStatement(SQL);) {
            pst.setString(1, userName);
            pst.setString(2, password);
            pst.setString(3, email);

            int rowEffect = pst.executeUpdate();
            if (rowEffect == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }
}
