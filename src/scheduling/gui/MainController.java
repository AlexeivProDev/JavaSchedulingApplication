package scheduling.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import scheduling.util.DateTimeHelper;
import scheduling.files.LoginActivityRecorder;
import scheduling.jdbc.UserDB;
import scheduling.util.LocaleHelper;

/**
 * FXML Controller class for Login Screen.
 *
 * @author Alexeiv Perez
 */
public class MainController implements Initializable {

    private static int currentUserId;

    @FXML
    private TextField userNameLogin;
    @FXML
    private PasswordField passwordLogin;
    @FXML
    private Button submitB;

    private static Locale currentLocale;
    private static String currentLanguage;

    @FXML
    private Label userNameL;
    @FXML
    private Label passwordL;
    @FXML
    private Text loginL;
    private LocaleHelper localeHelper;
    @FXML
    private AnchorPane zoneId;
    @FXML
    private Label zoneIdL;
    @FXML
    private Label zoneIdValueL;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //    Locale.setDefault(Locale.CANADA_FRENCH);
//        Locale.setDefault(Locale.JAPANESE);
        ZoneId systemDefaultZoneId = ZoneId.systemDefault();
        System.out.println("systemDefaultZoneId: " + systemDefaultZoneId);
        zoneIdValueL.setText(systemDefaultZoneId.toString());

        currentLocale = Locale.getDefault();
        localeHelper = new LocaleHelper(currentLocale);

        //get Calendar instance
        Calendar now = Calendar.getInstance();

        //get current TimeZone using getTimeZone method of Calendar class
        currentLanguage = currentLocale.getLanguage();

        if (!currentLanguage.equals(Locale.ENGLISH)) {
            changeTextLanguageTo(currentLanguage);
        }

    }

    /**
     * Validates the given user credentials and displays home screen, if valid.
     * In case of invalid user credentials, an error message is displayed. The
     * user login activity is recorded along with success or failure status, for
     * each attempt.
     */
    @FXML
    private void onSubmit(ActionEvent event) {
        String userName = userNameLogin.getText().trim();
        if (userName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, localeHelper.getLocalizedText("Username.empty"), ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String password = passwordLogin.getText();
        if (password.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, localeHelper.getLocalizedText("Password.empty"), ButtonType.OK);
            alert.showAndWait();
            return;
        }

        StringBuffer loginInfo = new StringBuffer();
        loginInfo.append("Login attempt by user: " + userName + ", at: "
                + DateTimeHelper.getDisplayFormat(new Date()) + ", was ");
        try {
            currentUserId = UserDB.isValidUser(userName, password);
            if (currentUserId > 0) {
                loginInfo.append("Success.");
                //show home screen
                Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
                Scene scene = new Scene(root);
                SchedulingJBDCApplication.setScene(scene);
            } else {
                loginInfo.append("Failure.");
                Alert alert = new Alert(Alert.AlertType.ERROR, localeHelper.getLocalizedText("Incorrect.login"), ButtonType.OK);
                alert.showAndWait();
            }
            LoginActivityRecorder.addActivity(loginInfo.toString());
        } catch (IOException | SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Returns the currently logged in user id.
     *
     * @return
     */
    public static int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Sets the text of labels based on the current language settings of the
     * computer.
     *
     * @param languageCode
     */
    private void changeTextLanguageTo(String languageCode) {
        zoneIdL.setText(localeHelper.getLocalizedText("userZoneId"));
        userNameL.setText(localeHelper.getLocalizedText("userName"));
        passwordL.setText(localeHelper.getLocalizedText("password"));
        loginL.setText(localeHelper.getLocalizedText("login"));
        submitB.setText(localeHelper.getLocalizedText("submit"));

    }

}
