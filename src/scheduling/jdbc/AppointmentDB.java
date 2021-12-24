package scheduling.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import scheduling.common.Appointment;

/**
 * This class provides methods to create, access, modify and delete Appointment
 * records in the Database.
 *
 * @author Alexeiv Perez
 */
public class AppointmentDB {

    /**
     * Adds the given Appointment to the Database.
     *
     * @param appointment
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean addAppointment(Appointment appointment) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "INSERT INTO appointments (Customer_ID, Title, "
                    + "Description, Location, Contact_ID, Type, Start, End, User_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, appointment.getCustomerId());
            pstmt.setString(2, appointment.getTitle());
            pstmt.setString(3, appointment.getDescription());
            pstmt.setString(4, appointment.getLocation());
            pstmt.setInt(5, appointment.getContactId());
            pstmt.setString(6, appointment.getType());
            pstmt.setTimestamp(7, appointment.getStartDateTime());
            pstmt.setTimestamp(8, appointment.getEndDateTime());
            pstmt.setInt(9, appointment.getUserId());

            int count = pstmt.executeUpdate();
            boolean success = false;
            if (count == 1) {
                success = true;
            }
            return success;
        } catch (SQLException ex) {
            ex.printStackTrace();;
            return false;
        }
    }

    /**
     * Returns list of all the Appointment records found in the Database.
     *
     * @return
     */
    public static List<Appointment> getAppointments() {
        return getAppointments(null);
    }

    /**
     * Returns list of all the Appointment records found in the Database, with
     * the records ordered by the given order by clause.
     *
     * @param orderBy The SQL order by clause to order the records by.
     * @return
     */
    public static List<Appointment> getAppointments(String orderBy) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT * FROM appointments ";
            if (orderBy != null) {
                sql += orderBy;
            }
            Statement stmt = conn.createStatement();
            List<Appointment> list = new ArrayList<>();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setAppointmentId(rs.getInt("Appointment_Id"));
                appt.setCustomerId(rs.getInt("Customer_ID"));
                appt.setContactId(rs.getInt("Contact_ID"));
                appt.setUserId(rs.getInt("User_ID"));
                appt.setTitle(rs.getString("Title"));
                appt.setType(rs.getString("type"));
                appt.setDescription(rs.getString("Description"));
                appt.setLocation(rs.getString("Location"));
                appt.setStartDateTime(rs.getTimestamp("start"));
                appt.setEndDateTime(rs.getTimestamp("end"));

                list.add(appt);
            }

            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes given Appointment record from the database if matching record
     * found.
     *
     * @param appointmentId
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean delete(int appointmentId) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "DELETE FROM appointments WHERE Appointment_Id = ? ";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, appointmentId);

            int count = pstmt.executeUpdate();
            boolean success = false;
            if (count == 1) {
                success = true;
            }
            return success;
        } catch (SQLException ex) {
            ex.printStackTrace();;
            return false;
        }
    }

    /**
     * Updates the given appointment in the Database, if matching record found.
     *
     * @param appointment
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean updateAppointment(Appointment appointment) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "UPDATE appointments SET Customer_ID = ?, Title = ?, "
                    + "Description = ?, Location = ?, Contact_ID = ?, Type = ?, Start = ?, End = ?, User_ID = ?"
                    + " WHERE Appointment_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, appointment.getCustomerId());
            pstmt.setString(2, appointment.getTitle());
            pstmt.setString(3, appointment.getDescription());
            pstmt.setString(4, appointment.getLocation());
            pstmt.setInt(5, appointment.getContactId());
            pstmt.setString(6, appointment.getType());
            pstmt.setTimestamp(7, appointment.getStartDateTime());
            pstmt.setTimestamp(8, appointment.getEndDateTime());
            pstmt.setInt(9, appointment.getUserId());
            pstmt.setInt(10, appointment.getAppointmentId());

            int count = pstmt.executeUpdate();
            boolean success = false;
            if (count == 1) {
                success = true;
            }
            return success;
        } catch (SQLException ex) {
            ex.printStackTrace();;
            return false;
        }
    }

    /**
     * Returns list of Appointment records that are within the given start and
     * end time, if found in the Database.
     *
     * @param startDateTimeString
     * @param endDateTimeString
     * @return
     * @throws SQLException
     */
    public static List<Appointment> getAppointments(String startDateTimeString, String endDateTimeString) throws SQLException {
        Connection conn = JDBCConection.getConnection();

        String sql = "SELECT * FROM appointments WHERE start >= '"
                + startDateTimeString
                + "' AND end < '" + endDateTimeString
                + "'";
        Statement stmt = conn.createStatement();
        List<Appointment> list = new ArrayList<>();

        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setAppointmentId(rs.getInt("Appointment_Id"));
            appt.setCustomerId(rs.getInt("Customer_ID"));
            appt.setContactId(rs.getInt("Contact_ID"));
            appt.setUserId(rs.getInt("User_ID"));
            appt.setTitle(rs.getString("Title"));
            appt.setType(rs.getString("type"));
            appt.setDescription(rs.getString("Description"));
            appt.setLocation(rs.getString("Location"));
            appt.setStartDateTime(rs.getTimestamp("start"));
            appt.setEndDateTime(rs.getTimestamp("end"));

            list.add(appt);
        }

        return list;

    }

    /**
     * Returns list of Upcoming Appointment records for the given userId, that
     * happen after the given start time, if found in the Database.
     *
     * @param userId
     * @param startDateTimeString
     * @return
     * @throws SQLException
     */
    public static List<Appointment> getUpcomingAppointments(int userId, String startDateTimeString) throws SQLException {
        Connection conn = JDBCConection.getConnection();

        String sql = "SELECT * FROM appointments WHERE user_id = " + userId
                + " AND start >= '" + startDateTimeString + "'"
                + " ORDER BY START";

        Statement stmt = conn.createStatement();

        List<Appointment> list = new ArrayList<>();

        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setAppointmentId(rs.getInt("Appointment_Id"));
            appt.setCustomerId(rs.getInt("Customer_ID"));
            appt.setContactId(rs.getInt("Contact_ID"));
            appt.setUserId(rs.getInt("User_ID"));
            appt.setTitle(rs.getString("Title"));
            appt.setType(rs.getString("type"));
            appt.setDescription(rs.getString("Description"));
            appt.setLocation(rs.getString("Location"));
            appt.setStartDateTime(rs.getTimestamp("start"));
            appt.setEndDateTime(rs.getTimestamp("end"));

            list.add(appt);
        }

        return list;

    }

    /**
     * Returns list of all the Appointment records found in the Database,
     * ordered by contact id and start time.
     *
     * @return
     */
    public static List<Appointment> getAppointmentsOrderByContact() {

        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT a.*, c.Contact_Name FROM WJ07QsZ.appointments a, WJ07QsZ.contacts c "
                    + "WHERE a.Contact_ID = c.Contact_ID ORDER BY c.Contact_ID, a.Start;";

            Statement stmt = conn.createStatement();
            List<Appointment> list = new ArrayList<>();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setAppointmentId(rs.getInt("Appointment_Id"));
                appt.setCustomerId(rs.getInt("Customer_ID"));
                appt.setContactId(rs.getInt("Contact_ID"));
                appt.setUserId(rs.getInt("User_ID"));
                appt.setTitle(rs.getString("Title"));
                appt.setType(rs.getString("type"));
                appt.setDescription(rs.getString("Description"));
                appt.setLocation(rs.getString("Location"));
                appt.setStartDateTime(rs.getTimestamp("start"));
                appt.setEndDateTime(rs.getTimestamp("end"));
                appt.setContactName(rs.getString("Contact_Name"));

                list.add(appt);
            }

            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns list of Appointments records that happened before the given end
     * time, if found in the Database.
     *
     * @param endDateTimeUntil
     * @return
     * @throws SQLException
     */
    public static List<Appointment> getAppointmentsCompleted(String endDateTimeUntil) throws SQLException {
        Connection conn = JDBCConection.getConnection();

        String sql = "SELECT * FROM appointments WHERE "
                + " end < '" + endDateTimeUntil + "'"
                + " ORDER BY START";

        Statement stmt = conn.createStatement();

        List<Appointment> list = new ArrayList<>();

        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setAppointmentId(rs.getInt("Appointment_Id"));
            appt.setCustomerId(rs.getInt("Customer_ID"));
            appt.setContactId(rs.getInt("Contact_ID"));
            appt.setUserId(rs.getInt("User_ID"));
            appt.setTitle(rs.getString("Title"));
            appt.setType(rs.getString("type"));
            appt.setDescription(rs.getString("Description"));
            appt.setLocation(rs.getString("Location"));
            appt.setStartDateTime(rs.getTimestamp("start"));
            appt.setEndDateTime(rs.getTimestamp("end"));

            list.add(appt);
        }

        return list;

    }

    /**
     * Returns list of Appointment records for the given customer, that overlaps
     * with the given start and end time, if found in the Database.
     *
     * @param customerId
     * @param startDateTimeString
     * @param endDateTimeString
     * @return
     * @throws SQLException
     */
    public static Appointment getOverlappingAppointment(int customerId,
            String startDateTimeString, String endDateTimeString) throws SQLException {
        Connection conn = JDBCConection.getConnection();

//        String sql = "SELECT * FROM appointments WHERE "
//                + " Customer_ID = " + customerId
//                + " AND "
//                + "("
//                + "( START = '" + startDateTimeString + "' AND "
//                + " START BETWEEN '" + startDateTimeString + "' AND '" + endDateTimeString + "'"
//                + " ) "
//                + " OR "
//                + "( END = '" + endDateTimeString + "' AND "
//                + " END BETWEEN '" + startDateTimeString + "' AND '" + endDateTimeString + "'"
//                + " ) "
//                + "OR ("
//                + " END <> '" + startDateTimeString + "'"
//                + " AND '" + startDateTimeString + "' BETWEEN START AND END"
//                + " ) "
//                + "OR ("
//                + " END <> '" + endDateTimeString + "'"
//                + " AND '" + endDateTimeString + "' BETWEEN START AND END"
//                + " ) "
//                + " ) ";
        String sql = getOverlappingAppointmentSQL(customerId, startDateTimeString, endDateTimeString, -1);

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            Appointment appt = new Appointment();
            appt.setAppointmentId(rs.getInt("Appointment_Id"));
            appt.setCustomerId(rs.getInt("Customer_ID"));
            appt.setContactId(rs.getInt("Contact_ID"));
            appt.setUserId(rs.getInt("User_ID"));
            appt.setTitle(rs.getString("Title"));
            appt.setType(rs.getString("type"));
            appt.setDescription(rs.getString("Description"));
            appt.setLocation(rs.getString("Location"));
            appt.setStartDateTime(rs.getTimestamp("start"));
            appt.setEndDateTime(rs.getTimestamp("end"));

            return appt;
        }
        return null;
    }

    /**
     * Returns list of Appointment records for the given customer, excluding the
     * given appointment id, that overlaps with the given start and end time, if
     * found in the Database.
     *
     * @param customerId
     * @param excludeAppointmentId
     * @param startDateTimeString
     * @param endDateTimeString
     * @return
     * @throws SQLException
     */
    public static Appointment getOverlappingAppointment(int customerId, int excludeAppointmentId,
            String startDateTimeString, String endDateTimeString) throws SQLException {
        Connection conn = JDBCConection.getConnection();

//        String sql = "SELECT * FROM appointments WHERE "
//                + " Customer_ID = " + customerId
//                + " AND Appointment_Id <> " + excludeAppointmentId
//                + " AND "
//                + "("
//                + "( START = '" + startDateTimeString + "' AND "
//                + " START BETWEEN '" + startDateTimeString + "' AND '" + endDateTimeString + "'"
//                + " ) "
//                + " OR "
//                + "( END = '" + endDateTimeString + "' AND "
//                + " END BETWEEN '" + startDateTimeString + "' AND '" + endDateTimeString + "'"
//                + " ) "
//                + "OR ("
//                + " END <> '" + startDateTimeString + "'"
//                + " AND '" + startDateTimeString + "' BETWEEN START AND END"
//                + " ) "
//                + "OR ("
//                + " END <> '" + endDateTimeString + "'"
//                + " AND '" + endDateTimeString + "' BETWEEN START AND END"
//                + " ) "
//                + " ) ";
        String sql = getOverlappingAppointmentSQL(customerId, startDateTimeString, endDateTimeString, excludeAppointmentId);

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            Appointment appt = new Appointment();
            appt.setAppointmentId(rs.getInt("Appointment_Id"));
            appt.setCustomerId(rs.getInt("Customer_ID"));
            appt.setContactId(rs.getInt("Contact_ID"));
            appt.setUserId(rs.getInt("User_ID"));
            appt.setTitle(rs.getString("Title"));
            appt.setType(rs.getString("type"));
            appt.setDescription(rs.getString("Description"));
            appt.setLocation(rs.getString("Location"));
            appt.setStartDateTime(rs.getTimestamp("start"));
            appt.setEndDateTime(rs.getTimestamp("end"));

            return appt;
        }
        return null;
    }

    /**
     * Returns SQL for overlapping appointments.
     * 
     * @param customerId
     * @param startDateTimeString
     * @param endDateTimeString
     * @param excludeAppointmentId
     * @return 
     */
    private static String getOverlappingAppointmentSQL(int customerId, String startDateTimeString, String endDateTimeString, int excludeAppointmentId) {

        String sql = "SELECT * FROM appointments WHERE "
                + " Customer_ID = " + customerId;

        if (excludeAppointmentId > 0) {
            sql += " AND Appointment_Id <> " + excludeAppointmentId;
        }

        sql += " AND "
                + "("
                + "( "
//                + "START = '" + startDateTimeString + "' AND "
                + " START BETWEEN '" + startDateTimeString + "' AND '" + endDateTimeString + "'"
                + " ) "
                + " OR "
                + "( "
//                + "END = '" + endDateTimeString + "' AND "
                + " END BETWEEN '" + startDateTimeString + "' AND '" + endDateTimeString + "'"
                + " ) "
                + "OR ("
                + " END <> '" + startDateTimeString + "'"
                + " AND '" + startDateTimeString + "' BETWEEN START AND END"
                + " ) "
                + "OR ("
                + " START <> '" + endDateTimeString + "'"
                + " AND '" + endDateTimeString + "' BETWEEN START AND END"
                + " ) "
                + " ) ";

        return sql;
    }

}
