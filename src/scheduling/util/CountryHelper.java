package scheduling.util;

import java.util.HashMap;
import java.util.List;
import scheduling.common.Country;
import scheduling.jdbc.CountryDB;

/**
 * This class contains Country related utility methods.
 *
 * @author Alexeiv Perez
 */
public class CountryHelper {

    /**
     * Country id and the Country.
     */
    private static HashMap<Integer, Country> countryMap = null;

    /**
     * Returns a Map containing countryId as key and Country as value.
     *
     * @return
     */
    private static HashMap<Integer, Country> getCountrysMap() {

        if (countryMap == null) {
            countryMap = new HashMap<Integer, Country>();

            List<Country> countries = CountryDB.getCountries();
            if (countries != null) {
                for (Country country : countries) {
                    countryMap.put(country.getCountryId(), country);
                }
            }
        }
        return countryMap;
    }

    /**
     * Returns Country object for the given countryId.
     *
     * @param countryId
     * @return
     */
    public static Country getCountry(Integer countryId) {
        if(countryMap == null){
            countryMap = getCountrysMap();
        }
        return countryMap.get(countryId);
    }
}
