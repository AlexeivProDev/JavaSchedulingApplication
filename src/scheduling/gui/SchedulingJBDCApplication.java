package scheduling.gui;

import java.io.IOException;
import java.sql.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Driver class for running the Application.
 *
 * @author Alexeiv Perez
 */
public class SchedulingJBDCApplication extends Application {

    private static Stage primaryStage;

    /**
     * Displays the home screen for the logged in user.
     *
     * @throws IOException
     */
    public static void displayHomeScreen() throws IOException {
        //show home screen
        Parent root = FXMLLoader.load(SchedulingJBDCApplication.class.getResource("home.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);
    }

    /**
     * Displays the Customer List screen.
     *
     * @throws IOException
     */
    public static void displayCustomerListScreen() throws IOException {
        //show screen
        Parent root = FXMLLoader.load(SchedulingJBDCApplication.class.getResource("CustomersList.fxml"));
        Scene scene = new Scene(root);
        SchedulingJBDCApplication.setScene(scene);
    }

    /**
     * The starting method to display the screen.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the given scene to the primaryStage.
     *
     * @param scene
     */
    public static void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    /**
     * The main method that launches the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {

        launch(args);
    }

}
