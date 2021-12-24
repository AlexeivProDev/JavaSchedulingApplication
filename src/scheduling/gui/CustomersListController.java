package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.common.Customer;
import scheduling.jdbc.CustomerDB;

/**
 * FXML Controller class for viewing list of Customers.
 *
 * @author Alexeiv Perez
 */
public class CustomersListController implements Initializable {

    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private Button editB;
    @FXML
    private Button deleteB;
    @FXML
    private Button homeB;

    /**
     * List of Customers.
     */
    private List<Customer> customerList;

    /**
     * Selected Customer
     */
    private static Customer selectedCustomer;
    @FXML
    private Button addAppointmentB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createTableView();
        populateCustomerTable();

    }

    /**
     * Allows the user to edit the customer as long as user selects a customer
     */
    @FXML
    private void editAction(ActionEvent event) throws IOException {
        TableView.TableViewSelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        selectedCustomer = selectionModel.getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a row.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        //show edit screen
        Parent root = FXMLLoader.load(getClass().getResource("editCustomer.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);
    }

    /**
     * This method allows the user to delete a customer but the user must select
     * the customer or the program will throw an ERROR
     */
    @FXML
    private void deleteAction(ActionEvent event) {
        TableView.TableViewSelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        Customer customer = selectionModel.getSelectedItem();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a row.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String customerInfo = customer.toString();
        boolean success = CustomerDB.delete(customer.getCustomerId());
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Deleted:\n\n" + customerInfo, ButtonType.OK);
            alert.showAndWait();
            //refresh table again.
            //remove all items
            customersTable.getItems().clear();
            //reload customers
            populateCustomerTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Delete Customer Failed", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Displays the home screen.
     */
    @FXML
    private void homeAction(ActionEvent event) throws IOException {
        //show home screen
        SchedulingJBDCApplication.displayHomeScreen();
    }

    private void populateCustomerTable() {
        customerList = CustomerDB.getCustomers();
        if (customerList == null || customerList.isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "CustomerList is empty!", ButtonType.OK);
//            alert.showAndWait();
            return;
        }

        for (Customer customer : customerList) {
            customersTable.getItems().add(customer);
        }
    }
    
    /**
     * This method initializes the columns for the tableview
     */
    private void createTableView() {

        TableColumn<Customer, String> customerNameTCol = new TableColumn<>("Customer Name");
        customerNameTCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));

        TableColumn<Customer, String> customerIdTCol = new TableColumn<>("Customer ID");
        customerIdTCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerId"));

        TableColumn<Customer, String> customerAddressTCol = new TableColumn<>("Address");
        customerAddressTCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));

        TableColumn<Customer, String> postalCodeTCol = new TableColumn<>("Postal Code");
        postalCodeTCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));

        TableColumn<Customer, String> phoneNumberTCol = new TableColumn<>("Phone Number");
        phoneNumberTCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        TableColumn<Customer, String> divisionIdTCol = new TableColumn<>("Division Id");
        divisionIdTCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("divisionId"));

        List<TableColumn<Customer, ?>> tableColList = new ArrayList<>();
        tableColList.add(customerIdTCol);
        tableColList.add(customerNameTCol);
        tableColList.add(customerAddressTCol);
        tableColList.add(postalCodeTCol);
        tableColList.add(phoneNumberTCol);
        tableColList.add(divisionIdTCol);

        customersTable.getColumns().addAll(tableColList);
        TableView.TableViewSelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        customersTable.setPlaceholder(new Label("No Customer Records found."));

    }

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * this method allows the user to add appointment, but the user must select
     * a customer or program will throw an ERROR
     */
    @FXML
    private void addAppointmentAction(ActionEvent event) throws IOException {
        TableView.TableViewSelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        selectedCustomer = selectionModel.getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a row.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        //show screen
        Parent root = FXMLLoader.load(getClass().getResource("addAppointment.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);
    }
}
