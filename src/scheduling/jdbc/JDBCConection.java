package scheduling.jdbc;

import java.sql.*;

/**
 * This class provides JDBC Connection object for working with Database.
 *
 * @author Alexeiv Perez
 */
public class JDBCConection {

    //JDBC URL Parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ07QsZ";

    private static final String jdbcURL = protocol + vendorName + ipAddress + "?connectionTimeZone=SERVER";

    //Driver interface reference
    private static final String mySQLDriver = "com.mysql.cj.jdbc.Driver";
    /**
     * The SQL Connection object.
     */
    public static Connection conn;

    private static final String userName = "U07QsZ"; //userName
    private static final String password = "53689101308"; // password

    /**
     * Returns the JDBC connection. Calls startConnection() if the conn object
     * is null or if the connection is closed.
     *
     * @return
     */
    public static Connection getConnection() throws SQLException {
        if ((conn == null) || conn.isClosed()) {
            initConnection();
        }
        return conn;
    }

    /**
     * Creates JDBC connection with the Database.
     *
     * @return The Connection object.
     */
    public static Connection initConnection() {
        try {
            Class.forName(mySQLDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, userName, password);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }

        return conn;
    }

    /**
     * Closes the JDBC Connection.
     */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }
    }

}
