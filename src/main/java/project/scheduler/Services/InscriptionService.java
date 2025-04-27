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

/**
 * The service responsible for inscription tasks
 */
@Service
public class InscriptionService {

    @Inject
    private InscriptionRepository inscriptionRepository;

    /**
     *  Removes a user from a course
     * 
     * @param user_id   The id of the user to be removed as a UUID object
     * @param course_id The id of the course to be removed from as a UUID object
     * 
     * @return  A String containing feedback on the success or failure of the operation
     */
    public ResponseEntity<Boolean> remove(UUID user_id, UUID course_id) {
        try {
            int rows = inscriptionRepository.deleteByIds(user_id, course_id);
            if(rows == 0) {
              return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
              return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  Inscribes a user into a course
     * 
     * @param user      A User object representing the user to be inscribed
     * @param course    A Course object representing the course to be inscribed into
     * 
     * @return  A UserBooking object of other courses the user is inscribed into which conflict with the chosen one
     */
    public UserBooking inscribe(User user, Course course) {
        UserBooking conflicts = verifyInscription(user.getUserId(), course.getId());
        inscriptionRepository.save(new Inscription(user, course));
        return conflicts;
    }

    /**
     *  Checks if a user is inscribed into a certain course
     * 
     * @param user_id   The id of the user to be checked for as a UUID object
     * @param course_id The id of the course to be checked for as a UUID object
     * 
     * @return  True if the user is inscribed into the course, false if not
     */
    public boolean ifExists(UUID user_id, UUID course_id) {
        return inscriptionRepository.ifExists(user_id, course_id) == 1;
    }

    /**
     *  Checks possible conflicts based on the user's inscriptions and the course bookings
     * 
     * @param user_id   The id of the user to be checked for as a UUID object
     * @param course_id The id of the course to be checked for as a UUID object
     * 
     * @return  A UserBooking object containing all conflicts found
     */
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

    public Iterable<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }
    
}
