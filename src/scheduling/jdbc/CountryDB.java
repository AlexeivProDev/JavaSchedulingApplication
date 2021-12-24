package scheduling.jdbc;

import scheduling.common.Country;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods to create, access, modify and delete Country
 * records in the Database.
 *
 * @author Alexeiv Perez
 */
public class CountryDB {

    /**
     * Returns a Country Object matching the given coutnryName, if found.
     *
     * @param countryName
     * @return
     */
    public static Country getCountry(String countryName) {

        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT * FROM countries WHERE country = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, countryName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Country country = new Country();
                country.setCountryId(rs.getInt("country_id"));
                country.setCountryName(rs.getString("country"));
                return country;
            }

            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns list of all the Country records found in the Database.
     *
     * @return
     */
    public static List<Country> getCountries() {

        try {
            Connection conn = JDBCConection.getConnection();

            String sql = "SELECT * FROM countries";

            Statement stmt = conn.createStatement();

            List<Country> list = new ArrayList<>();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Country country = new Country();
                country.setCountryId(rs.getInt("country_id"));
                country.setCountryName(rs.getString("country"));
                list.add(country);
            }

            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
