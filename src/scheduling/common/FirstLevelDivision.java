package scheduling.common;

/**
 * This class contains information of a FirstLevelDivision.
 *
 * @author Alexeiv Perez
 */
public class FirstLevelDivision {

    private int divisionId;
    private String division;
    private int countryId;

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

}
