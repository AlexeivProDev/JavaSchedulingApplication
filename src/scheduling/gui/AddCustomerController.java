package scheduling.gui;

import scheduling.common.FirstLevelDivision;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import scheduling.common.Country;
import scheduling.common.Customer;
import scheduling.jdbc.CountryDB;
import scheduling.jdbc.CustomerDB;
import scheduling.jdbc.DivisionDB;

/**
 * FXML Controller class for adding a Customer.
 *
 * @author Alexeiv Perez
 */
public class AddCustomerController implements Initializable {

    @FXML
    private TextField customerNameTF;
    @FXML
    private TextField cityTF;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private TextField phoneNumberTF;
    @FXML
    private Button backButton;
    @FXML
    private Button saveB;
    @FXML
    private ComboBox<String> divisionCombo;
    @FXML
    private TextField streetTF;
    @FXML
    private TextField locationTF;
    @FXML
    private ComboBox<String> countryCombo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Country> countries = CountryDB.getCountries();
        if (countries != null) {
            for (Country country : countries) {
                countryCombo.getItems().add(country.getCountryName());
            }
        }

    }

   /**
     * Displays the home screen.
     */
    @FXML 
    private void goBack(ActionEvent event) throws IOException {
        //show home screen
        SchedulingJBDCApplication.displayHomeScreen();
    }
    
   /**
     * Validates the Customer details and Provides alert in case of any invalid
     * data. Saves the Customer to the database, in case of valid data.
     */
    @FXML
    private void saveAction(ActionEvent event) {
        String countryName = countryCombo.getValue();
        if ((countryName == null) || countryName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Country must be selected.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String customerName = customerNameTF.getText().trim();
        if (customerName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Customer Name can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String street = streetTF.getText().trim();
        if (street.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Street can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String city = cityTF.getText().trim();
        if (city.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "City can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String customerAddress;
        if (countryName.equalsIgnoreCase("UK") || countryName.equalsIgnoreCase("United Kingdom")) {
            String location = locationTF.getText().trim();
            if (location.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Location can not be empty or blank.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            customerAddress = street + ", " + location + ", " + city;
        } else {
            customerAddress = street + ", " + city;
        }
        String postalCode = postalCodeTF.getText().trim();
        if (postalCode.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Postal Code can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String phoneNumber = phoneNumberTF.getText().trim();
        if (phoneNumber.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Phone Number can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String division = divisionCombo.getValue();
        if ((division == null) || division.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Division must be selected.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        int divisionId = Integer.parseInt(division.substring(0, division.indexOf("-")));

        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setCustomerAddress(customerAddress);
        customer.setPostalCode(postalCode);
        customer.setPhoneNumber(phoneNumber);
        customer.setDivisionId(divisionId);

        boolean result = CustomerDB.addCustomer(customer);
        if (result) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer added Successfully!", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Add Customer Failed", ButtonType.OK);
            alert.showAndWait();
        }

    }
    
   /**
     * Populates the divisionCombo with corresponding data Upon selecting the
     * country.
     */
    @FXML   
    private void onCountrySelection(ActionEvent event) {
        divisionCombo.getItems().clear();

        String countryName = countryCombo.getValue();

        Country country = CountryDB.getCountry(countryName);

        if (country == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid country: " + countryName, ButtonType.OK);
            alert.showAndWait();
            return;
        }
        List<FirstLevelDivision> divisionList = DivisionDB.getDivisionsForCountry(country.getCountryId());
        if (divisionList == null || divisionList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "First level divisionList is empty!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (divisionList != null) {
            for (FirstLevelDivision fld : divisionList) {
                divisionCombo.getItems().add(fld.getDivisionId() + "-" + fld.getDivision());
            }
        }
    }

}
