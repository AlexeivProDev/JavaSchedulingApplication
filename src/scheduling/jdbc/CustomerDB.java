package scheduling.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import scheduling.common.Customer;

/**
 * This class provides methods to create, access, modify and delete Customer
 * records in the Database.
 *
 * @author Alexeiv Perez
 */
public class CustomerDB {

    /**
     * Adds the given customer to the Database.
     *
     * @param customer
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean addCustomer(Customer customer) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "INSERT INTO customers (customer_name, address, postal_code, phone, Division_ID) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getCustomerAddress());
            pstmt.setString(3, customer.getPostalCode());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setInt(5, customer.getDivisionId());

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
     * Returns list of all the Customer records found in the Database.
     *
     * @return
     */
    public static List<Customer> getCustomers() {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT * FROM customers";
            Statement stmt = conn.createStatement();
            List<Customer> list = new ArrayList<>();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Customer cust = new Customer();
                cust.setCustomerId(rs.getInt("customer_Id"));
                cust.setDivisionId(rs.getInt("division_id"));
                cust.setCustomerName(rs.getString("customer_name"));
                cust.setCustomerAddress(rs.getString("address"));
                cust.setPostalCode(rs.getString("postal_code"));
                cust.setPhoneNumber(rs.getString("phone"));
                list.add(cust);
            }

            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes given customer record from the database if matching record found.
     * Before deleting the customer records, all associated Appointment records
     * are deleted, if any.
     *
     * @param customerId
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean delete(int customerId) {
        Connection conn = null;

        try {
            conn = JDBCConection.getConnection();
            conn.setAutoCommit(false);

            //----DELETE Appointments for the CUSTOMER
            String sql = "DELETE FROM appointments WHERE customer_Id = ? ";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);

            pstmt.executeUpdate();
            //----DELETE CUSTOMER
            String sql2 = "DELETE FROM customers WHERE customer_Id = ? ";

            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, customerId);

            int count = pstmt2.executeUpdate();
            boolean success = false;
            if (count == 1) {
                success = true;
            }
            conn.commit();
            return success;
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(CustomerDB.class.getName()).log(Level.SEVERE, null, ex1);
            }
            ex.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(CustomerDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Updates the given customer in the Database, if matching record found.
     *
     * @param customer
     * @return Returns true if the operation is success, otherwise, returns
     * false.
     */
    public static boolean updateCustomer(Customer customer) {
        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "UPDATE customers SET customer_name = ?, address = ?, postal_code = ?, phone = ?, Division_ID = ? "
                    + "WHERE Customer_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getCustomerAddress());
            pstmt.setString(3, customer.getPostalCode());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setInt(5, customer.getDivisionId());
            pstmt.setInt(6, customer.getCustomerId());

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
