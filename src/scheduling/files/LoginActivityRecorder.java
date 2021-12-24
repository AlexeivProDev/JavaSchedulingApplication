package scheduling.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains methods to write login activity to a file.
 *
 * @author Alexeiv Perez
 */
public class LoginActivityRecorder {

    /**
     * The file name of the login activity.
     */
    public static final String LOGIN_ACTIVITY_FILE_NAME = "login_activity.txt";

    /**
     * Appends the given login activity to the file.
     *
     * @param info
     */
    public static void addActivity(String info) {
        File f = new File(LOGIN_ACTIVITY_FILE_NAME);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(info);
            bw.append("\n");
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(LoginActivityRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
