package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import scheduling.common.Appointment;
import scheduling.common.Contact;
import scheduling.util.ContactHelper;
import scheduling.util.CustomerHelper;
import scheduling.util.DateTimeHelper;
import scheduling.jdbc.AppointmentDB;
import scheduling.jdbc.ContactDB;

/**
 * FXML Controller class for editing an Appointment record.
 *
 * @author Alexeiv Perez
 */
public class EditAppointmentController implements Initializable {

    @FXML
    private TextField descriptionTF;
    @FXML
    private TextField locationTF;
    @FXML
    private TextField startDateTF;
    @FXML
    private Button backButton;
    @FXML
    private Button saveB;
    @FXML
    private TextField titleTF;
    @FXML
    private TextField typeTF;
    @FXML
    private ComboBox<String> contactCombo;
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
    private Label userIdText;
    @FXML
    private Label appointmentIdText;

    private HashMap<Integer, String> contactMap;
    private HashMap<Integer, String> customersMap;
    @FXML
    private CheckBox outsideOfficeHoursCB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        contactMap = ContactHelper.getContactsMap();
        customersMap = CustomerHelper.getCustomersMap();

        //Get the selected appointment
        Appointment selectedAppointment = AppointmentsListController.getSelectedAppointment();
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selected Appointment is null.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        populateForm(selectedAppointment);

    }

    /**
     * Displays the Appointments List screen.
     */
    @FXML
    private void goBack(ActionEvent event) throws IOException {
        //show screen
        Parent root = FXMLLoader.load(SchedulingJBDCApplication.class.getResource("AppointmentsList.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);

    }

    /**
     * Validates the Appointment details and Provides alert in case of any
     * invalid data. Updates the Appointment to the database, in case of valid
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
        int appointmentId = Integer.parseInt(appointmentIdText.getText());
        try {
            boolean isOverlap = DateTimeHelper.validateForOverlapSchedule(startDate, endDate, customerId, appointmentId);
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

        //---Update Appointment object with the new details.
        Appointment appointment = new Appointment();

        appointment.setAppointmentId(appointmentId);
        appointment.setUserId(MainController.getCurrentUserId());
        appointment.setCustomerId(customerId);

        appointment.setContactId(contactId);
        appointment.setLocation(location);
        appointment.setType(type);
        appointment.setTitle(title);
        appointment.setDescription(description);
        appointment.setStartDateTime(startDateTime);
        appointment.setEndDateTime(endDateTime);

        //---Update Appointment in the Database.
        boolean result = AppointmentDB.updateAppointment(appointment);
        if (result) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment Updated Successfully!", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Update Appointment Failed", ButtonType.OK);
            alert.showAndWait();
        }

    }

    /**
     * Populates the form with the given Appointment details.
     *
     * @param appointment
     */
    private void populateForm(Appointment appointment) {

        appointmentIdText.setText(appointment.getAppointmentId() + "");

        userIdText.setText(appointment.getUserId() + "");

        customeridText.setText(appointment.getCustomerId() + "");
        String customerName = customersMap.get(appointment.getCustomerId());
        customerNameText.setText(customerName);

        //Populate Contact Combo
        List<Contact> countacts = ContactDB.getContacts();
        if (countacts != null) {
            for (Contact contact : countacts) {
                contactCombo.getItems().add(contact.getContactId() + "-" + contact.getContactName());
            }
        }
        //select the contact in the combo
        String contactName = contactMap.get(appointment.getContactId());
        String contactValue = appointment.getContactId() + "-" + contactName;
        contactCombo.setValue(contactValue);

        titleTF.setText(appointment.getTitle());
        typeTF.setText(appointment.getType());
        descriptionTF.setText(appointment.getDescription());
        locationTF.setText(appointment.getLocation());

        //FORMAT:  yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = sdf.format(appointment.getStartDateTime());
        String endDateString = sdf.format(appointment.getEndDateTime());

        sdf = new SimpleDateFormat("HH:mm");
        String startTimeString = sdf.format(appointment.getStartDateTime());
        String endTimeString = sdf.format(appointment.getEndDateTime());

        startDateTF.setText(startDateString);
        endDateTF.setText(endDateString);
        startTimeTF.setText(startTimeString);
        endTimeTF.setText(endTimeString);
    }

}
