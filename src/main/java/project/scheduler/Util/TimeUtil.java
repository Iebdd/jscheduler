package project.scheduler.Util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;


public class TimeUtil {

    // Taken from this StackOverflow Question: 
    // https://stackoverflow.com/questions/3553964/how-to-round-time-to-the-nearest-quarter-hour-in-java

    public static LocalDateTime roundToQuarter(Instant time) {
        LocalDateTime new_time = LocalDateTime.ofInstant(time, ZoneOffset.UTC);
        return new_time.truncatedTo(ChronoUnit.HOURS).plusMinutes(15 * (new_time.getMinute() / 15));
    }

    public static LocalDateTime roundToQuarter(LocalDateTime time) {
        return time.truncatedTo(ChronoUnit.HOURS).plusMinutes(15 * (time.getMinute() / 15));
    }

    public static LocalTime roundToQuarter(LocalTime time) {
        return time.truncatedTo(ChronoUnit.HOURS).plusMinutes(15 * (time.getMinute() / 15));
    }


}
