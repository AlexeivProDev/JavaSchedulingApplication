package scheduling.util;

import java.util.HashMap;
import java.util.List;
import scheduling.common.FirstLevelDivision;
import scheduling.jdbc.DivisionDB;

/**
 * This class contains FirstLevelDivision related utility methods.
 *
 * @author Alexeiv Perez
 */
public class DivisionHelper {

    /**
     * FirstLevelDivision id and it's country id.
     */
    private static HashMap<Integer, Integer> divisionCountryMap = null;

    /**
     * Returns a Map containing divisionId as key and countryId as value.
     *
     * @return
     */
    private static HashMap<Integer, Integer> getDivisionCountryMap() {
        /**
         * FirstLevelDivision id and it's country id.
         */
        HashMap<Integer, Integer> divisionCountryMap = new HashMap<Integer, Integer>();

        List<FirstLevelDivision> divisions = DivisionDB.getDivisions();
        if (divisions != null) {
            for (FirstLevelDivision division : divisions) {
                divisionCountryMap.put(division.getDivisionId(), division.getCountryId());
            }
        }

        return divisionCountryMap;

    }

    /**
     * Returns countryId for the given divisionId.
     *
     * @return
     */
    public static Integer getCountryIdForDivision(int divisionId) {
        if (divisionCountryMap == null) {
            divisionCountryMap = getDivisionCountryMap();
        }

        return divisionCountryMap.get(divisionId);

    }
}
