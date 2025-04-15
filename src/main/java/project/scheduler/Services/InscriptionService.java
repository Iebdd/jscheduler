package project.scheduler.Services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.InscriptionRepository;
import project.scheduler.Tables.Booking;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Inscription;
import project.scheduler.Tables.User;
import project.scheduler.Util.UserBooking;

@Service
public class InscriptionService {

    @Inject
    private InscriptionRepository inscriptionRepository;

    public ResponseEntity<String> remove(UUID user_id, UUID course_id) {
        try {
            int rows = inscriptionRepository.deleteByIds(user_id, course_id);
            if(rows == 0) {
              return new ResponseEntity<>(String.format("Could not remove User: %s from Course: %s", user_id, course_id), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
              return ResponseEntity.ok(String.format("Removed User: %s from Course: %s", user_id, course_id));
            }
        } catch (Exception e) {
            return new ResponseEntity<>(String.format("Could not remove User: %s from Course: %s", user_id, course_id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public UserBooking inscribe(User user, Course course) {
        UserBooking conflicts = verifyInscription(user.getUserId(), course.getId());
        inscriptionRepository.save(new Inscription(user, course));
        return conflicts;
    }

    public boolean ifExists(UUID user_id, UUID course_id) {
        return inscriptionRepository.ifExists(user_id, course_id) == 1;
    }

    public UserBooking verifyInscription(UUID user_id, UUID course_id) {
        Iterable<Booking> conflicts = inscriptionRepository.findInscriptionConflicts(user_id, course_id);
        if(IterableUtils.size(conflicts) != 0) {
            UserBooking bookings = new UserBooking();
            HashSet<UUID> t_conflicts = new HashSet<>();
            for (Booking id : conflicts) {
                t_conflicts.add(id.getCourse().getId());
            }
            bookings.setTimeConflicts(new ArrayList<>(t_conflicts));
            return bookings;
        }
        return new UserBooking();
    }
    
}
