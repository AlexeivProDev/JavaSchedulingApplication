package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import scheduling.common.Appointment;
import scheduling.jdbc.AppointmentDB;

/**
 * FXML Controller class for viewing count of Appointments for Customers grouped
 * by Type and start month.
 *
 * @author Alexeiv Perez
 */
public class CustomerAppointmentsController implements Initializable {

    @FXML
    private TableView<Map> appointmentsTable;
    @FXML
    private Button homeB;
    private HashMap<Integer, String> contactMap;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Get Appointments from DB.
        String orderBy = "order by Customer_ID, Type, Start";
        List<Appointment> appointments = AppointmentDB.getAppointments(orderBy);

        //Count appointments based on Customer, Type and Month.
        //Map<Customer_ID, Map<Type, Map<Month, Count>>>
        Map<Integer, Map<String, Map<String, Integer>>> groupMap
                = countAppointmentsByCustomerAndTypeAndMonth(appointments);

        //Create List of row data for TableView
        ObservableList<Map<String, Object>> rowDataList = createRowDataListForTableView(groupMap);

        createTableView();
        populateAppointmentTable(rowDataList);

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
     * Populates the TableView with the details found in the given list.
     *
     * @param rowDataList
     */
    private void populateAppointmentTable(ObservableList<Map<String, Object>> rowDataList) {
        if ((rowDataList == null) || (rowDataList.isEmpty())) {
            return;
        }

        appointmentsTable.getItems().addAll(rowDataList);

    }

    /**
     * Creates TableView with necessary columns.
     */
    private void createTableView() {
        TableColumn<Map, Integer> customerIdTCol = new TableColumn<>("Customer ID");
        customerIdTCol.setCellValueFactory(new MapValueFactory<>("customerId"));

        TableColumn<Map, String> typeTCol = new TableColumn<>("Type");
        typeTCol.setCellValueFactory(new MapValueFactory<>("type"));

        TableColumn<Map, String> monthTCol = new TableColumn<>("Month");
        monthTCol.setCellValueFactory(new MapValueFactory<>("month"));

        TableColumn<Map, Integer> countTCol = new TableColumn<>("Count");
        countTCol.setCellValueFactory(new MapValueFactory<>("count"));

        //        appointment ID, month, type and description, start date and time, end date and time, and customer ID
        List<TableColumn<Map, ?>> tableColList = new ArrayList<>();

        tableColList.add(customerIdTCol);
        tableColList.add(typeTCol);
        tableColList.add(monthTCol);
        tableColList.add(countTCol);

        appointmentsTable.getColumns().addAll(tableColList);

        appointmentsTable.setPlaceholder(new Label("No Appointment Records found."));

    }

    /**
     * Creates and returns a Map with count of Appointments for each Customer
     * grouped by Type and start month.
     *
     * @param appointments
     * @return
     */
    private Map<Integer, Map<String, Map<String, Integer>>> countAppointmentsByCustomerAndTypeAndMonth(List<Appointment> appointments) {

        int customerId = 0;
        String type;

        //Map<Customer_ID, Map<Type, Map<Month, Count>>>
        Map<Integer, Map<String, Map<String, Integer>>> groupMap = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        for (Appointment appt : appointments) {
            customerId = appt.getCustomerId();
            type = appt.getType();

            Map<String, Map<String, Integer>> typeMap = groupMap.get(customerId);
            if (typeMap == null) {
                typeMap = new HashMap();
                groupMap.put(customerId, typeMap);
            }
            Map<String, Integer> monthMap = typeMap.get(type);
            if (monthMap == null) {
                monthMap = new HashMap();
                typeMap.put(type, monthMap);
            }
            String month = sdf.format(appt.getStartDateTime());
            Integer count = monthMap.get(month);
            if (count == null) {
                count = 0;
            }
            count++;
            monthMap.put(month, count);
        }

        return groupMap;
    }

    /**
     * Creates and returns an ObservableList of Maps containing appropriate row
     * data, based on the given input Map.
     *
     * @param groupMap
     * @return
     */
    private ObservableList<Map<String, Object>> createRowDataListForTableView(Map<Integer, Map<String, Map<String, Integer>>> groupMap) {
        //Map<Customer_ID, Map<Type, Map<Month, Count>>>
        ObservableList<Map<String, Object>> rowDataList = FXCollections.observableArrayList();

        Set<Integer> customerIdSet = groupMap.keySet();
        for (Integer customerId : customerIdSet) {
            Map<String, Map<String, Integer>> typeMap = groupMap.get(customerId);
            Set<String> typeSet = typeMap.keySet();
            for (String type : typeSet) {
                Map<String, Integer> monthMap = typeMap.get(type);
                Set<String> monthSet = monthMap.keySet();
                for (String month : monthSet) {
                    Integer count = monthMap.get(month);
                    Map<String, Object> rowMap = new HashMap<>();
                    rowMap.put("customerId", customerId);
                    rowMap.put("type", type);
                    rowMap.put("month", month);
                    rowMap.put("count", count);
                    rowDataList.add(rowMap);
                }
            }
        }
        return rowDataList;
    }

}
