package util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserialize {

    public static LocalTime parsLocalDate(String time) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH-mm");
        LocalTime datetime = null;
        try {
            datetime = LocalTime.parse(time, pattern);
        } catch (DateTimeParseException e) {
            //log.error("Error! LocalDate can not parse!");
        }
        return datetime;
    }

    public static String parsFromLocalDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
