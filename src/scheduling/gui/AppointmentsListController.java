package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.common.Appointment;
import scheduling.jdbc.AppointmentDB;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import scheduling.util.ContactHelper;

/**
 * FXML Controller class for viewing list of Appointments.
 *
 * @author Alexeiv Perez
 */
public class AppointmentsListController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private Button editB;
    @FXML
    private Button homeB;

    /**
     * Contact id and their names.
     */
    private HashMap<Integer, String> contactMap = null;

    /**
     * Selected Appointment
     */
    private static Appointment selectedAppointment;
    @FXML
    private Button cancelAppointmentB;
    @FXML
    private RadioButton byMonthRadio;
    @FXML
    private RadioButton byWeekRadio;
    @FXML
    private Button filterB;

    /**
     * Radio buttons toggle group.
     */
    ToggleGroup filterRadioButtonGroup = new ToggleGroup();
    @FXML
    private TextField byMonthTF;
    @FXML
    private TextField byWeekTF;
    @FXML
    private Button showAllB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        byMonthRadio.setToggleGroup(filterRadioButtonGroup);
        byWeekRadio.setToggleGroup(filterRadioButtonGroup);

        contactMap = ContactHelper.getContactsMap();
        createTableView();
        populateAppointmentTable();
    }

    /**
     * Displays Edit Appointment screen with the selected Appointment. The user
     * must select an appointment in order to edit it, otherwise an error is
     * displayed.
     */
    @FXML
    private void editAction(ActionEvent event) throws IOException {
        TableView.TableViewSelectionModel<Appointment> selectionModel = appointmentsTable.getSelectionModel();
        selectedAppointment = selectionModel.getSelectedItem();
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a row.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        //show edit screen
        Parent root = FXMLLoader.load(getClass().getResource("editAppointment.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);
    }

    /**
     * Deletes the appointment selected by the user from the database. The user
     * must select an appointment to cancel the appointment, otherwise an error
     * will be displayed.
     */
    @FXML
    private void cancelAppointmentAction(ActionEvent event) {
        TableView.TableViewSelectionModel<Appointment> selectionModel = appointmentsTable.getSelectionModel();
        Appointment appointment = selectionModel.getSelectedItem();
        if (appointment == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a row.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String appointmentInfo = "Appointment ID: " + appointment.getAppointmentId() + ", \n\nType: " + appointment.getType();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure want to Cancel: \n\n" + appointment.getDisplayString(),
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.get() != ButtonType.YES) {
            return;
        }

        boolean success = AppointmentDB.delete(appointment.getAppointmentId());
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION, "CANCELLED:\n\n" + appointmentInfo, ButtonType.OK);
            alert.showAndWait();

            //refresh table again.
            //remove all items
            appointmentsTable.getItems().clear();
            //reload appointments

            populateAppointmentTable();
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Delete Appointment Failed", ButtonType.OK);
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

    /**
     * Populates the TableView with the appointment details from the given
     * appointmentList.
     *
     * @param appointmentList
     */
    private void populateAppointmentTable(List<Appointment> appointmentList) {
        if ((appointmentList == null) || (appointmentList.isEmpty())) {
            return;
        }

        appointmentsTable.getItems().clear();

        for (Appointment appointment : appointmentList) {
            String contactName = contactMap.get(appointment.getContactId());
            appointment.setContactName(contactName);
            appointmentsTable.getItems().add(appointment);
        }
    }

    /**
     * This method initializes the columns in the table view, and sets up the
     * rows with appropriate data element attributes.
     */
    private void createTableView() {
        TableColumn<Appointment, String> appointmentIdTCol = new TableColumn<>("Appointment ID");
        appointmentIdTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appointmentId"));

        TableColumn<Appointment, String> customerIdTCol = new TableColumn<>("Customer ID");
        customerIdTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerId"));

//        TableColumn<Appointment, String> userIdTCol = new TableColumn<>("User ID");
//        userIdTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("userId"));
        TableColumn<Appointment, String> contactNameTCol = new TableColumn<>("Contact");
        contactNameTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));

        TableColumn<Appointment, String> titleTCol = new TableColumn<>("Title");
        titleTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        TableColumn<Appointment, String> descriptionTCol = new TableColumn<>("Description");
        descriptionTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));

        TableColumn<Appointment, String> locationTCol = new TableColumn<>("Location");
        locationTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));

        TableColumn<Appointment, String> typeTCol = new TableColumn<>("Type");
        typeTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));

        TableColumn<Appointment, Timestamp> startDateTimeTCol = new TableColumn<>("Start");
        startDateTimeTCol.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("startDateTime"));

        TableColumn<Appointment, Timestamp> endDateTimeTCol = new TableColumn<>("End");
        endDateTimeTCol.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("endDateTime"));

        List<TableColumn<Appointment, ?>> tableColList = new ArrayList<>();
        tableColList.add(appointmentIdTCol);
        tableColList.add(customerIdTCol);
//        tableColList.add(userIdTCol);
        tableColList.add(contactNameTCol);
        tableColList.add(titleTCol);

        tableColList.add(descriptionTCol);
        tableColList.add(locationTCol);
        tableColList.add(typeTCol);
        tableColList.add(startDateTimeTCol);
        tableColList.add(endDateTimeTCol);

        appointmentsTable.getColumns().addAll(tableColList);
        TableView.TableViewSelectionModel<Appointment> selectionModel = appointmentsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        appointmentsTable.setPlaceholder(new Label("No Appointment Records found."));

    }

    /**
     * Returns the appointment selected by the user from the list of
     * appointments.
     *
     * @return
     */
    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    /**
     * Filters the appointments list based on the user selected filter criteria.
     *
     * This method prevents the user from leaving the text field blank or if
     * they click the filter button it will display an error because one of the
     * radio buttons must be selected.
     */
    @FXML
    private void filterAction(ActionEvent event) {
        RadioButton selectedButton = (RadioButton) filterRadioButtonGroup.getSelectedToggle();

        if (byMonthRadio == selectedButton) {
            String yyyyMM = byMonthTF.getText().trim();
            if (yyyyMM.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "By Month (yyyy-mm) can not be empty or blank.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            filterByMonth(yyyyMM);

        } else if (byWeekRadio == selectedButton) {
            String yyyyMMdd = byWeekTF.getText().trim();
            if (yyyyMMdd.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "By Week (yyyy-mm-dd) can not be empty or blank.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            filterByWeek(yyyyMMdd);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must select either By Week or By Month.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    /**
     * This method filters the list by week of 7 days, starting from the given
     * date.
     */
    private void filterByWeek(String yyyyMMdd) {

        try {
            int hypenPosition = yyyyMMdd.indexOf("-");
            int lastHypenPosition = yyyyMMdd.lastIndexOf("-");

            int yyyy = Integer.parseInt(yyyyMMdd.substring(0, hypenPosition));
            int mm = Integer.parseInt(yyyyMMdd.substring(hypenPosition + 1, lastHypenPosition));
            int dd = Integer.parseInt(yyyyMMdd.substring(lastHypenPosition + 1));

            Calendar cal = new GregorianCalendar(yyyy, mm, dd);
            cal.add(Calendar.DAY_OF_MONTH, 7);

            int endYyyy = cal.get(Calendar.YEAR);
            int endMm = cal.get(Calendar.MONTH);
            int endDd = cal.get(Calendar.DAY_OF_MONTH);

            //FORMAT:  yyyy-mm-dd hh:mm:ss
            String startDateTimeString = yyyy + "-" + (mm < 10 ? "0" + mm : mm) + "-"
                    + (dd < 10 ? "0" + dd : dd)
                    + " 00:00:00";
            String endDateTimeString = endYyyy + "-" + (endMm < 10 ? "0" + endMm : endMm) + "-"
                    + (endDd < 10 ? "0" + endDd : endDd)
                    + " 00:00:00";

            try {
                List<Appointment> appointmentList = AppointmentDB.getAppointments(startDateTimeString, endDateTimeString);
                if ((appointmentList == null) || (appointmentList.isEmpty())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No matching records found.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

                populateAppointmentTable(appointmentList);
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentsListController.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could NOT load appointments, from Database.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input. Start of Week, must be of format: yyyy-MM-dd", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    /**
     * This method filters the list by the given month.
     */
    private void filterByMonth(String yyyyMM) {

        try {
            int hypenPosition = yyyyMM.indexOf("-");
            int yyyy = Integer.parseInt(yyyyMM.substring(0, hypenPosition));
            int mm = Integer.parseInt(yyyyMM.substring(hypenPosition + 1));

            Calendar cal = new GregorianCalendar(yyyy, mm, 1);
            cal.add(Calendar.MONTH, 1);

            int endYyyy = cal.get(Calendar.YEAR);
            int endMm = cal.get(Calendar.MONTH);

            //FORMAT:  yyyy-mm-dd hh:mm:ss
            String startDateTimeString = yyyy + "-" + (mm < 10 ? "0" + mm : mm) + "-01 00:00:00";
            String endDateTimeString = endYyyy + "-" + (endMm < 10 ? "0" + endMm : endMm) + "-01 00:00:00";
            try {
                List<Appointment> appointmentList = AppointmentDB.getAppointments(startDateTimeString, endDateTimeString);
                if ((appointmentList == null) || (appointmentList.isEmpty())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No matching records found.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

                populateAppointmentTable(appointmentList);
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentsListController.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could NOT load appointments, from Database.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input. Start Month, must be of format: yyyy-MM", ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    /**
     * Displays all the appointments - without any filtering.
     *
     * @param event
     */
    @FXML
    private void showAllAction(ActionEvent event) {
        populateAppointmentTable();
    }

    /**
     * Retrieves all the appointments list from the DB and populates the table
     * view.
     */
    private void populateAppointmentTable() {
        List<Appointment> appointmentList = AppointmentDB.getAppointments();

        populateAppointmentTable(appointmentList);
    }

}
