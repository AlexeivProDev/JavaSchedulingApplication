package scheduling.common;

import scheduling.util.DateTimeHelper;
import scheduling.util.StringHelper;
import java.sql.Timestamp;

/**
 * This class contains information of an Appointment.
 *
 * @author Alexeiv Perez
 */
public class Appointment {

    private int appointmentId;
    private int contactId;
    private int customerId;
    private int userId;
    private String contactName;

    private String location;
    private String type;
    private String title;
    private String description;
    private Timestamp startDateTime;
    private Timestamp endDateTime;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return "Appointment{" + "appointmentId=" + appointmentId + ", contactId=" + contactId + ", customerId=" + customerId + ", userId=" + userId + ", contactName=" + contactName + ", location=" + location + ", type=" + type + ", title=" + title + ", description=" + description + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + '}';
    }

    /**
     * Returns String containing info of an appointment to be displayed in the
     * alert.
     *
     * @return
     */
    public String getAlertString() {
        return "Appointment{\n\n" + "appointmentId=" + appointmentId
                + ",\n startDateTime=" + DateTimeHelper.getDisplayFormat(startDateTime)
                + ",\n endDateTime=" + DateTimeHelper.getDisplayFormat(endDateTime) + '}';
    }

    /**
     * Returns String containing info of an appointment for display purpose.
     *
     * @return
     */
    public String getDisplayString() {
        return "Appointment{\n\n" + "appointmentId=" + appointmentId
                + ",\n contactId=" + contactId + ",\n customerId=" + customerId
                + ",\n userId=" + userId
                + ",\n contactName=" + StringHelper.getNotNullValue(contactName)
                + ",\n location=" + StringHelper.getNotNullValue(location)
                + ",\n type=" + StringHelper.getNotNullValue(type)
                + ",\n title=" + StringHelper.getNotNullValue(title)
                + ",\n description=" + StringHelper.getNotNullValue(description)
                + ",\n startDateTime=" + DateTimeHelper.getDisplayFormat(startDateTime)
                + ",\n endDateTime=" + DateTimeHelper.getDisplayFormat(endDateTime) + '}';
    }

}
