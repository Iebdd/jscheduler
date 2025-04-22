package project.scheduler.Util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import project.scheduler.Tables.Booking;


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

    public static Iterable<UUID> getTimeline(Iterable<Booking> bookings, LocalTime start, LocalTime end) {
        long sections = start.until(end, ChronoUnit.HOURS) * 4;
        LocalTime current_time = start;
        ArrayList<UUID> timeline = new ArrayList<>();
        Iterator<Booking> bookings_it = bookings.iterator();
        Booking current;
        boolean started = false;
        if(bookings_it.hasNext()) {
            current = bookings_it.next();
        } else {
            for(int index = 0; index <= sections; index++) {
                timeline.add(null);
            }
            return timeline;
        }
        for (int index = 0; index <= sections; index++) {
            if(current == null) {
                timeline.add(null);
                current_time = current_time.plusMinutes(15);
                continue;
            }
            if(current_time.equals(LocalTime.from(current.getStart()))) {
                timeline.add(current.getBookings_id());
                current_time = current_time.plusMinutes(15);
                started = true;
            } else if (started) {
                if(current_time.equals(LocalTime.from(current.getEnd()))) {
                    timeline.add(current.getBookings_id());
                    current_time = current_time.plusMinutes(15);
                    started = false;
                    if(bookings_it.hasNext()) {
                        current = bookings_it.next();
                    } else {
                        current = null;
                    }
                } else {
                    timeline.add(current.getBookings_id());
                    current_time = current_time.plusMinutes(15);
                }
            } else {
                timeline.add(null);
                current_time = current_time.plusMinutes(15);
            }
        }
        return timeline;
    }


}
