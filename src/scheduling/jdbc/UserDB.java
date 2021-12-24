package scheduling.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides methods to validate user credentials against User records
 * in the Database.
 *
 * @author Alexeiv Perez
 */
public class UserDB {

    /**
     * Validates given user credentials and returns userId if matching record
     * found, otherwise, returns -1.
     *
     * @param userName
     * @param password
     * @return
     * @throws SQLException
     */
    public static int isValidUser(String userName, String password) throws SQLException {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT user_id FROM users WHERE user_name = ? AND password = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
            return -1;
        } catch (Exception ex) {
            return -1;
        }
    }

}
