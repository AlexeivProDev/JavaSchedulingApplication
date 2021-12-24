package scheduling.util;

import java.sql.Timestamp;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
//import java.util.TimeZone;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import scheduling.common.Appointment;
import scheduling.common.Constants;
import scheduling.jdbc.AppointmentDB;

/**
 * This class contains DateTime related utility methods.
 *
 * @author Alexeiv Perez
 */
public class DateTimeHelper {

    /**
     * The Date format used in SQL.
     */
    public static final String SQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * The Date format used for display purpose.`
     */
    public static final String DATE_DISPLAY_FORMAT = "yyyy-MM-dd hh:mm:ss a";

    /**
     * Returns Calendar object with EST time zone.
     *
     * @param checkDateTime
     * @return
     */
    public static Calendar getEstCalendar(Date checkDateTime) {
        Calendar estCal = new GregorianCalendar();
        estCal.setTime(checkDateTime);
        System.out.println("Local: " + DateTimeHelper.getDisplayFormat(estCal.getTime()));
        estCal.setTimeZone(Constants.NEW_YORK_TIME_ZONE);
        System.out.println("EST: " + DateTimeHelper.getDisplayFormat(estCal.getTime()));
        return estCal;
    }

    /**
     * Returns UTC Formatted date time String of the given date.
     *
     * @param date
     * @return
     */
    public static String getUtcFormatString(Date date) throws ParseException {
        String dateStr = Instant.ofEpochMilli(date.getTime()).toString();
        // Instant.now().toString() Output:
        // 2021-04-17T13:25:52.820Z
//		System.out.println(dateStr);
        String result = dateStr.substring(0, 10) + " " + dateStr.substring(11, 19);
        return result;

//        SimpleDateFormat sdf = new SimpleDateFormat(SQL_DATE_TIME_FORMAT);
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        String utcFormatString = sdf.format(date);
//       System.out.println(">>> utcFormatString: " + utcFormatString);
//       return utcFormatString;
    }

    /**
     * Returns Date object representing Office Start hours and minutes in EST
     * time zone, on the given date.
     *
     * @param yyyy
     * @param mm
     * @param dd
     * @return
     */
    public static Date getEstOfficeStartTime(int yyyy, int mm, int dd) {
        //month is zero based.
        Calendar estCal = new GregorianCalendar(yyyy, mm - 1, dd);
        estCal.setTimeZone(Constants.NEW_YORK_TIME_ZONE);
        estCal.set(Calendar.HOUR_OF_DAY, Constants.OFFICE_START_HOURS_EST);
        estCal.set(Calendar.MINUTE, Constants.OFFICE_START_MINUTES_EST);
        estCal.set(Calendar.SECOND, 0);

        return estCal.getTime();

    }

    /**
     * Returns Date object representing Office End hours and minutes in EST time
     * zone, on the given date.
     *
     * @param yyyy
     * @param mm
     * @param dd
     * @return
     */
    public static Date getEstOfficeEndTime(int yyyy, int mm, int dd) {
        //month is zero based.
        Calendar estCal = new GregorianCalendar(yyyy, mm - 1, dd);

        estCal.set(Calendar.HOUR_OF_DAY, Constants.OFFICE_END_HOURS_EST);
        estCal.set(Calendar.MINUTE, Constants.OFFICE_END_MINUTES_EST);
        estCal.set(Calendar.SECOND, 0);
        System.out.println("Local Cal: " + getDisplayFormat(estCal.getTime()));

        estCal.setTimeZone(Constants.NEW_YORK_TIME_ZONE);
        System.out.println("EST Cal: " + getDisplayFormat(estCal.getTime()));

        return estCal.getTime();

    }

    /**
     * Returns true if the given end date occurs after the given start time,
     * otherwise, returns false.
     *
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public static boolean isEndDateTimeAfterStartTime(Date startDateTime, Date endDateTime) {
        if (startDateTime.after(endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Meeting End Time is earlier than the Start Time .", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        if (startDateTime.equals(endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Meeting Start Time and the End Time can not be the same.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Returns true if the given dates occur within the office hours, otherwise,
     * returns false. This method also provides alert, if the time is outside of
     * the office hours.
     *
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public static boolean isWithinBusinessOfficeHours(Date startDateTime, Date endDateTime) {
        Calendar estCalStart = DateTimeHelper.getEstCalendar(startDateTime);
        if (isPastDate(estCalStart)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Meeting Start Date is Past date.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        if (isStartTimeBeforeOfficeHours(estCalStart)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Meeting Start Time is BEFORE office hours.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        Calendar estCalEnd = DateTimeHelper.getEstCalendar(endDateTime);
        if (isPastDate(estCalEnd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Meeting End Date is Past date.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        if (isEndTimeAfterOfficeHours(estCalEnd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Meeting End Time is AFTER office hours.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Returns true if the given date and time occur before the start of the
     * office hours, otherwise, returns false.
     *
     * @param estCal
     * @return
     */
    private static boolean isStartTimeBeforeOfficeHours(Calendar estCal) {
        if (estCal.get(Calendar.HOUR_OF_DAY) < Constants.OFFICE_START_HOURS_EST) {
            return true;
        }

        return false;
    }

    /**
     * Returns true if the given date and time happened to be a past time,
     * otherwise, returns false.
     *
     * @param estCal
     * @return
     */
    private static boolean isPastDate(Calendar estCal) {

        Calendar todayCal = new GregorianCalendar();
        todayCal.setTimeZone(Constants.NEW_YORK_TIME_ZONE);

        if (estCal.before(todayCal)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the given date and time happened to be on a weekend,
     * otherwise, returns false.
     *
     * @param estCal
     * @return
     */
    private static boolean isWeekend(Calendar estCal) {

        if ((estCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || estCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the given date and time occur after the end of the office
     * hours, otherwise, returns false.
     *
     * @param estCal
     * @return
     */
    private static boolean isEndTimeAfterOfficeHours(Calendar estCal) {
        if (estCal.get(Calendar.HOUR_OF_DAY) > Constants.OFFICE_END_HOURS_EST) {
            return true;
        } else if (estCal.get(Calendar.HOUR_OF_DAY) == Constants.OFFICE_END_HOURS_EST) {
            if (estCal.get(Calendar.MINUTE) > Constants.OFFICE_END_MINUTES_EST) {
                return true;
            }
        }

        return false;

    }

    /**
     * Returns Timestamp object based on the given input String.
     *
     * @param startDateString FORMAT: yyyy-MM-dd
     * @param startTimeString FORMAT: HH:mm:ss
     * @return
     */
    public static Timestamp getTimestamp(String startDateString, String startTimeString) {
        //FORMAT:  yyyy-MM-dd HH:mm:ss
        String startDateTimeString = startDateString + " " + startTimeString + ":00";
        return Timestamp.valueOf(startDateTimeString);
    }

    /**
     * Returns Date information in a display format specified in the
     * DISPLAY_FORMAT constant.
     *
     * @param date
     * @return
     */
    public static String getDisplayFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_DISPLAY_FORMAT);
        return sdf.format(date);
    }

    /**
     * Returns Date information in a display format specified in the
     * SQL_DATE_TIME_FORMAT constant.
     *
     * @param date
     * @return
     */
    public static String getSqlDateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(SQL_DATE_TIME_FORMAT);
        return sdf.format(date);
    }

    /**
     * This method validates if the given schedule (date and time) overlaps with
     * any of the existing appointments for the same customer. And, if yes,
     * provides alert and returns true. Otherwise returns false.
     *
     * @param startDate
     * @param endDate
     * @param customerId
     * @param excludeAppointmentId The appointment to be ignored from
     * verification - in case of updating an appointment.
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public static boolean validateForOverlapSchedule(Date startDate, Date endDate, int customerId, int excludeAppointmentId) throws SQLException, ParseException {

        //FORMAT:  yyyy-mm-dd HH:mm:ss
        String utcFormatStartDate = DateTimeHelper.getUtcFormatString(startDate);
        String utcFormatEndDate = DateTimeHelper.getUtcFormatString(endDate);

        Appointment overlap;
        if (excludeAppointmentId > 0) {
            overlap = AppointmentDB.getOverlappingAppointment(customerId, excludeAppointmentId, utcFormatStartDate, utcFormatEndDate);
        } else {
            overlap = AppointmentDB.getOverlappingAppointment(customerId, utcFormatStartDate, utcFormatEndDate);
        }
        if (overlap != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Schedule overlaps with existing Appointment:\n\n "
                    + overlap.getDisplayString(), ButtonType.OK);
            alert.showAndWait();
            return true;
        }
        return false;
    }
}
