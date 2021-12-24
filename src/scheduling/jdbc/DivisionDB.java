package scheduling.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import scheduling.common.FirstLevelDivision;

/**
 * This class provides methods to access FirstLevelDivision records in the
 * Database.
 *
 * @author Alexeiv Perez
 */
public class DivisionDB {

    /**
     * Returns list of all the FirstLevelDivision records found in the Database.
     *
     * @return
     */
    public static List<FirstLevelDivision> getDivisions() {

        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT * FROM first_level_divisions";

            Statement stmt = conn.createStatement();

            List<FirstLevelDivision> list = new ArrayList<>();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                FirstLevelDivision fld = new FirstLevelDivision();
                fld.setDivisionId(rs.getInt("division_id"));
                fld.setDivision(rs.getString("division"));
                fld.setCountryId(rs.getInt("country_id"));
                list.add(fld);
            }

            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns list of FirstLevelDivision records for the given countryId, if
     * found.
     *
     * @param countryId
     * @return
     */
    public static List<FirstLevelDivision> getDivisionsForCountry(int countryId) {

        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT * FROM first_level_divisions WHERE country_id = ? ";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, countryId);

            ResultSet rs = pstmt.executeQuery();

            List<FirstLevelDivision> list = new ArrayList<>();
            while (rs.next()) {
                FirstLevelDivision fld = new FirstLevelDivision();
                fld.setDivisionId(rs.getInt("division_id"));
                fld.setDivision(rs.getString("division"));
                fld.setCountryId(rs.getInt("country_id"));
                list.add(fld);
            }

            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
