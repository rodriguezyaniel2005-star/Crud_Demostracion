package org.example.data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * ConversionUtils: Utility class for converting between Java date/time types and MongoDB-compatible formats
 */
public class ConversionUtils {
    
    /**
     * Convert java.util.Date to LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) return null;
        return LocalDateTime.ofInstant(
            date.toInstant(),
            ZoneId.systemDefault()
        );
    }
    
    /**
     * Convert LocalDateTime to java.util.Date for MongoDB storage
     */
    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return Date.from(
            dateTime.atZone(ZoneId.systemDefault()).toInstant()
        );
    }
}
