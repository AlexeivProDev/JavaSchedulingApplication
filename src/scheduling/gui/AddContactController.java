package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import scheduling.common.Contact;
import scheduling.jdbc.ContactDB;

/**
 * FXML Controller class for adding a Contact.
 *
 * @author Alexeiv Perez
 */
public class AddContactController implements Initializable {

    @FXML
    private TextField emailTF;
    @FXML
    private Button backButton;
    @FXML
    private Button saveB;
    @FXML
    private TextField contactNameTF;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
     * Validates the Contact details and Provides alert in case of any invalid
     * data. Saves the Contact to the database, in case of valid data.
     */
    @FXML
    private void saveAction(ActionEvent event) {
        String contactName = contactNameTF.getText().trim();
        if (contactName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Contact Name can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String email = emailTF.getText().trim();
        if (email.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Email can not be empty or blank.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Contact contact = new Contact();
        contact.setContactName(contactName);
        contact.setEmail(email);

        boolean result = ContactDB.addContact(contact);
        if (result) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Contact added Successfully!", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Add Contact Failed", ButtonType.OK);
            alert.showAndWait();
        }

    }

}
