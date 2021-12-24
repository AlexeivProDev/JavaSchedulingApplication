package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import scheduling.common.Appointment;
import scheduling.util.DateTimeHelper;
import scheduling.jdbc.AppointmentDB;

/**
 * FXML Controller class for home Screen of the logged in user.
 *
 * @author Alexeiv Perez
 */
public class HomeController implements Initializable {

    @FXML
    private Button addCustomerB;
    @FXML
    private Button viewCustomersB;
    @FXML
    private Button logoutB;
    @FXML
    private Button viewAppointmentB;
    @FXML
    private Button viewReportB;
    @FXML
    private Button addContactB1;
    @FXML
    private RadioButton customerAppointmentsRB;

    /**
     * Radio buttons toggle group.
     */
    ToggleGroup reportRadioButtonGroup = new ToggleGroup();
    @FXML
    private RadioButton contactAppointmentsRB;
    @FXML
    private RadioButton completedAppointmentsRB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayUpcomingMeetingAlert();

        customerAppointmentsRB.setToggleGroup(reportRadioButtonGroup);
        contactAppointmentsRB.setToggleGroup(reportRadioButtonGroup);
        completedAppointmentsRB.setToggleGroup(reportRadioButtonGroup);

    }

    /**
     * Displays the Add Customer Screen.
     */
    @FXML
    private void addCustomerAction(ActionEvent event) throws IOException {
        //show screen
        Parent root = FXMLLoader.load(getClass().getResource("addCustomer.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);
    }

    /**
     * Displays the Customers List Screen.
     */
    @FXML
    private void viewCustomersAction(ActionEvent event) throws IOException {

        SchedulingJBDCApplication.displayCustomerListScreen();
    }

    /**
     * Closes the application, upon confirmation by the user.
     */
    @FXML
    private void logoutAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to CLOSE Application?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.get() == ButtonType.YES) {
            System.exit(0);
        }
    }

    /**
     * Displays the add Contact Screen.
     */
    @FXML
    private void addContactAction(ActionEvent event) throws IOException {
        //show screen
        Parent root = FXMLLoader.load(getClass().getResource("addContact.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);
    }

    /**
     * Displays the Appointments List Screen.
     */
    @FXML
    private void viewAppointmentsAction(ActionEvent event) throws IOException {
        //show screen
        Parent root = FXMLLoader.load(SchedulingJBDCApplication.class.getResource("AppointmentsList.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);
    }

    /**
     * Displays an alert about any meeting within the next 15 minutes.
     */
    private void displayUpcomingMeetingAlert() {
        Date currentDate = new Date();

        try {
            //FORMAT:  yyyy-mm-dd hh:mm:ss
            String utcFormatString = DateTimeHelper.getUtcFormatString(currentDate);

            List<Appointment> appointments
                    = AppointmentDB.getUpcomingAppointments(MainController.getCurrentUserId(), utcFormatString);
            boolean hasMeeting = false;
            Appointment appt = null;
            if (appointments.size() > 0) {
                appt = appointments.get(0);
                long startMilliseconds = appt.getStartDateTime().getTime();
                long currentTimeMilliseconds = currentDate.getTime();
                if (startMilliseconds == currentTimeMilliseconds) {
                    hasMeeting = true;
                } else {
                    long diff = startMilliseconds - currentTimeMilliseconds;
                    long minutesToMeeting = diff / 1000 / 60;
                    if (minutesToMeeting <= 15) {
                        hasMeeting = true;
                    }
                }
            }

            if (hasMeeting) {
                String info = appt.getAlertString();
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "You have meeting starting within the next 15 minutes, at: " + info, ButtonType.OK);
                alert.showAndWait();
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You do not have any meeting starting within the next 15 minutes.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could NOT load appointments, from Database.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    /**
     * Displays appropriate Report screen, based on the user selection of report
     * type.
     */
    @FXML
    private void onViewReportAction(ActionEvent event) throws IOException {

        RadioButton selectedButton = (RadioButton) reportRadioButtonGroup.getSelectedToggle();
        Parent root = null;
        if (selectedButton == contactAppointmentsRB) {
            root = FXMLLoader.load(getClass().getResource("contactAppointments.fxml"));
        } else if (selectedButton == customerAppointmentsRB) {
            root = FXMLLoader.load(getClass().getResource("customerAppointments.fxml"));
        } else if (selectedButton == completedAppointmentsRB) {
            root = FXMLLoader.load(getClass().getResource("completedAppointments.fxml"));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must select one of the report types (radio buttons).", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        //show screen
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);

    }

}
