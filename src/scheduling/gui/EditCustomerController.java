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
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import scheduling.common.Country;
import scheduling.common.Customer;
import scheduling.jdbc.CountryDB;
import scheduling.jdbc.CustomerDB;
import scheduling.jdbc.DivisionDB;
import scheduling.util.CountryHelper;
import scheduling.util.DivisionHelper;

/**
 * FXML Controller class for editing a Customer record.
 *
 * @author Alexeiv Perez
 */
public class EditCustomerController implements Initializable {

    @FXML
    private TextField customerNameTF;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private TextField phoneNumberTF;
    @FXML
    private Button backButton;
    @FXML
    private Button updateB;
    @FXML
    private ComboBox<String> divisionCombo;

    @FXML
    private Label customerIdText;
    @FXML
    private TextField cityTF;
    @FXML
    private TextField streetTF;
    @FXML
    private TextField locationTF;
    @FXML
    private ComboBox<String> countryCombo;

    /**
     * The Customer being edited.
     */
    private Customer customer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Get the selected customer
        customer = CustomersListController.getSelectedCustomer();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selected Customer is null.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        List<Country> countries = CountryDB.getCountries();
        if (countries != null) {
            for (Country country : countries) {
                countryCombo.getItems().add(country.getCountryName());
            }
        }

        //Load the divisions.
        List<FirstLevelDivision> divisionList = DivisionDB.getDivisions();
        if (divisionList == null || divisionList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "First level divisionList is empty!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

//        int divisionId = -1;
        if (divisionList != null) {
            String selectedDivision = null;
            for (FirstLevelDivision fld : divisionList) {
                String divisionString = fld.getDivisionId() + "-" + fld.getDivision();
//                divisionCombo.getItems().add(divisionString);
                if (fld.getDivisionId() == customer.getDivisionId()) {
                    selectedDivision = divisionString;
                }
            }

            //Select the division and country
            if (selectedDivision != null) {
                //Select the country
                int divisionId = Integer.parseInt(selectedDivision.substring(0, selectedDivision.indexOf("-")));
                Integer countryId = DivisionHelper.getCountryIdForDivision(divisionId);
                Country country = CountryHelper.getCountry(countryId);
                SingleSelectionModel<String> selectionModel1 = countryCombo.getSelectionModel();
                System.out.println("1. divisionCombo: " + divisionCombo.getValue());
                selectionModel1.select(country.getCountryName());
                populateDivisionForCountry();
                //Select the division
                System.out.println("2. divisionCombo: " + divisionCombo.getValue());
                SingleSelectionModel<String> selectionModel = divisionCombo.getSelectionModel();
                selectionModel.select(selectedDivision);
            }
        }

        populateForm();
    }

    /**
     * Displays the Customers List Screen.
     */
    @FXML
    private void goBack(ActionEvent event) throws IOException {
        SchedulingJBDCApplication.displayCustomerListScreen();
    }

    /**
     * Validates the Customer details and Provides alert in case of any invalid
     * data. Updates the Customer to the database, in case of valid data.
     */
    @FXML
    private void updateAction(ActionEvent event) {

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

        customer.setCustomerName(customerName);
        customer.setCustomerAddress(customerAddress);
        customer.setPostalCode(postalCode);
        customer.setPhoneNumber(phoneNumber);
        customer.setDivisionId(divisionId);

        //Update Customer in the database.
        boolean result = CustomerDB.updateCustomer(customer);
        if (result) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer updated Successfully!", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Update Customer Failed", ButtonType.OK);
            alert.showAndWait();
        }

    }

    /**
     * Populates the form with the Customer details.
     */
    private void populateForm() {
        String countryName = countryCombo.getValue();
        String address = customer.getCustomerAddress();
        String[] addressParts = address.split(", ");
        if (countryName.equalsIgnoreCase("UK") || countryName.equalsIgnoreCase("United Kingdom")) {
            //street + ", " + location + ", " + city;
            if (addressParts.length == 3) {
                streetTF.setText(addressParts[0]);
                locationTF.setText(addressParts[1]);
                cityTF.setText(addressParts[2]);
            } else {
                streetTF.setText(addressParts[0]);
                cityTF.setText(addressParts[1]);
            }
        } else {
            //street + ", " + location + ", " + city;
            if (addressParts.length == 3) {
                streetTF.setText(addressParts[0]);
                cityTF.setText(addressParts[2]);
            } else {
                streetTF.setText(addressParts[0]);
                cityTF.setText(addressParts[1]);
            }
        }

        customerIdText.setText(customer.getCustomerId() + "");
        customerNameTF.setText(customer.getCustomerName());
        postalCodeTF.setText(customer.getPostalCode());
        phoneNumberTF.setText(customer.getPhoneNumber());
    }

    @FXML
    private void onCountrySelection(ActionEvent event) {
       populateDivisionForCountry();
    }

    private void populateDivisionForCountry() {
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
