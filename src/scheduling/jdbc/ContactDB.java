package scheduling.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import scheduling.common.Contact;

/**
 * This class provides methods to create, access, modify and delete Contact
 * records in the Database.
 *
 * @author Alexeiv Perez
 */
public class ContactDB {

    /**
     * Adds the given Contact record to the database.
     *
     * @param contact
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean addContact(Contact contact) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "INSERT INTO contacts (contact_name, Email) "
                    + "VALUES (?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, contact.getContactName());
            pstmt.setString(2, contact.getEmail());

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
     * Returns list of all the Contact records found in the Database.
     *
     * @return
     */
    public static List<Contact> getContacts() {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT * FROM contacts";
            Statement stmt = conn.createStatement();
            List<Contact> list = new ArrayList<>();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Contact cust = new Contact();
                cust.setContactId(rs.getInt("contact_Id"));
                cust.setContactName(rs.getString("contact_name"));
                cust.setEmail(rs.getString("Email"));
                list.add(cust);
            }

            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes given contact record from the database if matching record found.
     *
     * @param contactId
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean delete(int contactId) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "DELETE FROM contacts WHERE contact_Id = ? ";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, contactId);

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
     * Updates the given Contact in the Database, if matching record found.
     *
     * @param contact
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean updateContact(Contact contact) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "UPDATE contacts SET contact_name = ?, Email = ? "
                    + "WHERE Contact_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, contact.getContactName());
            pstmt.setString(3, contact.getEmail());
            pstmt.setInt(6, contact.getContactId());

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
}
