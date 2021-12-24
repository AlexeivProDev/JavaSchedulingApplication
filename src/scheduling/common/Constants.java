package scheduling.common;

import java.util.TimeZone;

/**
 * This class contains Constants being used in the application.
 *
 * @author Alexeiv Perez
 */
public class Constants {

    /**
     * The Office start hours, in EST timezone.
     */
    public static final int OFFICE_START_HOURS_EST = 8;
    /**
     * * The Office start minutes, in EST timezone.
     */
    public static final int OFFICE_START_MINUTES_EST = 0;

    /**
     * * The Office end hours, in EST timezone.
     */
    public static final int OFFICE_END_HOURS_EST = 22;
    /**
     * * The Office end minutes, in EST timezone.
     */
    public static final int OFFICE_END_MINUTES_EST = 0;

    /**
     * The New York time zone representing EST time zone.
     */
    public static final TimeZone NEW_YORK_TIME_ZONE = TimeZone.getTimeZone("America/New_York");
    /**
     * The UTC time zone.
     */
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
}
