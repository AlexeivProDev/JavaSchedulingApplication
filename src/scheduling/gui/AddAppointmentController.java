package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import scheduling.common.Contact;
import scheduling.common.Appointment;
import scheduling.common.Customer;
import scheduling.util.DateTimeHelper;
import scheduling.jdbc.ContactDB;
import scheduling.jdbc.AppointmentDB;

/**
 * FXML Controller class for adding an Appointment.
 *
 * @author Alexeiv Perez
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private TextField locationTF;
    @FXML
    private TextField typeTF;
    @FXML
    private TextField startDateTF;
    @FXML
    private Button backButton;
    @FXML
    private Button saveB;
    @FXML
    private TextField titleTF;
    @FXML
    private TextField descriptionTF;

    @FXML
    private Label customeridText;
    @FXML
    private Label customerNameText;
    @FXML
    private TextField startTimeTF;
    @FXML
    private TextField endDateTF;
    @FXML
    private TextField endTimeTF;
    @FXML
    private ComboBox<String> contactCombo;
    @FXML
    private Label userIdText;
    private DatePicker showDatePick;
    @FXML
    private Label showDateLabel;
    @FXML
    private Label showEndDLabel;
    private DatePicker showEndDatePick;
    @FXML
    private CheckBox outsideOfficeHoursCB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int currentUserId = MainController.getCurrentUserId();

        userIdText.setText(currentUserId + "");

        Customer selectedCustomer = CustomersListController.getSelectedCustomer();
        customerNameText.setText(selectedCustomer.getCustomerName());
        customeridText.setText(selectedCustomer.getCustomerId() + "");

        List<Contact> countacts = ContactDB.getContacts();
        if (countacts != null) {
            populateContactCombo(countacts);
        }
    }

    /**
     * Loops through the given list of Contacts and populates the contactCombo
     * using LAMBDA Expression.
     *
     * Justification for LAMBDA Expression: Looping through collection items
     * involves declaration of index variables and accessing them might result
     * in IndexOutOfBoundsException, hence making use of LAMBDA Expression.
     *
     * @param countacts
     */
    private void populateContactCombo(List<Contact> countacts) {
//        LAMBDA Expression.

        countacts.forEach(c -> {
            contactCombo.getItems().add(c.getContactId() + "-" + c.getContactName());
            System.out.println("......" + c);
        });
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
     * Validates the Appointment details and Provides alert in case of any
     * invalid data.. Saves the Appointment to the database, in case of valid
     * data.
     */
    @FXML
    private void saveAction(ActionEvent event) {

        String contact = contactCombo.getValue();

        if (contact == null || contact.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must select a Contact.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        int contactId = Integer.parseInt(contact.substring(0, contact.indexOf("-")));

        int customerId = Integer.parseInt(customeridText.getText());

        String location = locationTF.getText().trim();
        if (location.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Location can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String type = typeTF.getText().trim();
        if (type.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Type can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String title = titleTF.getText().trim();
        if (title.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Title can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String description = descriptionTF.getText().trim();
        if (description.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Description can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String startDateString = startDateTF.getText().trim();
        if (startDateString.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Start Date can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String startTimeString = startTimeTF.getText().trim();
        if (startTimeString.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Start Time can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String endDateString = endDateTF.getText().trim();
        if (endDateString.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "End Date can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String endTimeString = endTimeTF.getText().trim();
        if (endTimeString.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "End Time can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Timestamp startDateTime = DateTimeHelper.getTimestamp(startDateString, startTimeString);
        Timestamp endDateTime = DateTimeHelper.getTimestamp(endDateString, endTimeString);

        Date startDate = new Date(startDateTime.getTime());
        Date endDate = new Date(endDateTime.getTime());
//        
        boolean valid = false;
        valid = DateTimeHelper.isEndDateTimeAfterStartTime(startDate, endDate);
        if (!valid) {
            return;

        }
        //Check for overlapping appointments
        try {

            boolean isOverlap = DateTimeHelper.validateForOverlapSchedule(startDate, endDate, customerId, 0);
            if (isOverlap) {
                return;
            }

        } catch (ParseException | SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not verify overlapping Appointment in Database.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        //Check if outside of business hours is permitted
        if (!outsideOfficeHoursCB.isSelected()) {
            //ensure that the meeting is within the business hours.
            valid = DateTimeHelper.isWithinBusinessOfficeHours(startDate, endDate);
            if (!valid) {
                return;
            }
        }

        //---Create Appointment object
        Appointment appointment = new Appointment();
        appointment.setUserId(MainController.getCurrentUserId());
        appointment.setCustomerId(customerId);

        appointment.setContactId(contactId);
        appointment.setLocation(location);
        appointment.setType(type);
        appointment.setTitle(title);
        appointment.setDescription(description);
        appointment.setStartDateTime(startDateTime);
        appointment.setEndDateTime(endDateTime);

        //---Save Appointment object
        boolean result = AppointmentDB.addAppointment(appointment);
        if (result) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment added Successfully!", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Add Appointment Failed", ButtonType.OK);
            alert.showAndWait();
        }

    }

}
