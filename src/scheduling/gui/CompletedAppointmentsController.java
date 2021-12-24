package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.common.Appointment;
import scheduling.util.ContactHelper;
import scheduling.util.DateTimeHelper;
import scheduling.jdbc.AppointmentDB;

/**
 * FXML Controller class for viewing list of Appointments that are Completed.
 *
 * @author Alexeiv Perez
 */
public class CompletedAppointmentsController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private Button homeB;
    private HashMap<Integer, String> contactMap;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Date currentDate = new Date();
            //FORMAT:  yyyy-mm-dd hh:mm:ss
            String utcFormatDate = DateTimeHelper.getUtcFormatString(currentDate);

            List<Appointment> appointments = AppointmentDB.getAppointmentsCompleted(utcFormatDate);

            contactMap = ContactHelper.getContactsMap();
            createTableView();
            populateAppointmentTable(appointments);
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(CompletedAppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
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
     * Creates TableView with necessary columns to display Appointments List.
     */
    private void createTableView() {

        TableColumn<Appointment, String> contactNameTCol = new TableColumn<>("Contact");
        contactNameTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));

        TableColumn<Appointment, String> appointmentIdTCol = new TableColumn<>("Appointment ID");
        appointmentIdTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appointmentId"));

        TableColumn<Appointment, String> titleTCol = new TableColumn<>("Title");
        titleTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));

        TableColumn<Appointment, String> typeTCol = new TableColumn<>("Type");
        typeTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));

        TableColumn<Appointment, String> descriptionTCol = new TableColumn<>("Description");
        descriptionTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));

        TableColumn<Appointment, Timestamp> startDateTimeTCol = new TableColumn<>("Start");
        startDateTimeTCol.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("startDateTime"));

        TableColumn<Appointment, Timestamp> endDateTimeTCol = new TableColumn<>("End");
        endDateTimeTCol.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("endDateTime"));

        TableColumn<Appointment, String> customerIdTCol = new TableColumn<>("Customer ID");
        customerIdTCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerId"));

        //        appointment ID, title, type and description, start date and time, end date and time, and customer ID
        List<TableColumn<Appointment, ?>> tableColList = new ArrayList<>();
        tableColList.add(contactNameTCol);
        tableColList.add(appointmentIdTCol);
        tableColList.add(titleTCol);
        tableColList.add(typeTCol);
        tableColList.add(descriptionTCol);

        tableColList.add(startDateTimeTCol);
        tableColList.add(endDateTimeTCol);

        tableColList.add(customerIdTCol);

        appointmentsTable.getColumns().addAll(tableColList);
        TableView.TableViewSelectionModel<Appointment> selectionModel = appointmentsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        appointmentsTable.setPlaceholder(new Label("No Appointment Records found."));

    }

}
