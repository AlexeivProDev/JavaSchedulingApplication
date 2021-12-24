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
import javafx.scene.layout.AnchorPane;
import scheduling.common.Customer;
import scheduling.jdbc.CustomerDB;
import scheduling.jdbc.DivisionDB;

/**
 * FXML Controller class for editing a Customer record.
 *
 * @author Alexeiv Perez
 */
public class EditCustomerController1 implements Initializable {

    @FXML
    private TextField customerNameTF;
    @FXML
    private TextField customerAddressTF;
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
    private Customer customer;
    @FXML
    private AnchorPane customerId;
    @FXML
    private Label customerIdText;

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

        //Load the divisions.
        List<FirstLevelDivision> divisionList = DivisionDB.getDivisions();
        if (divisionList == null || divisionList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "First level divisionList is empty!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (divisionList != null) {
            String selectedDivision = null;
            for (FirstLevelDivision fld : divisionList) {
                String divisionString = fld.getDivisionId() + "-" + fld.getDivision();
                divisionCombo.getItems().add(divisionString);
                if (fld.getDivisionId() == customer.getDivisionId()) {
                    selectedDivision = divisionString;
                }
            }
            SingleSelectionModel<String> selectionModel = divisionCombo.getSelectionModel();
            selectionModel.select(selectedDivision);
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

        String customerName = customerNameTF.getText().trim();
        if (customerName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Customer Name can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String customerAddress = customerAddressTF.getText().trim();
        if (customerAddress.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Address can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
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
        customerIdText.setText(customer.getCustomerId() + "");
        customerNameTF.setText(customer.getCustomerName());
        customerAddressTF.setText(customer.getCustomerAddress());
        postalCodeTF.setText(customer.getPostalCode());
        phoneNumberTF.setText(customer.getPhoneNumber());
// countacts.forEach(c -> {
         //   contactCombo.getItems().add(c.getContactId() + "-" + c.getContactName());
          //  System.out.println("......" + c);
       // });
    }

}
