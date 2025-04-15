package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Tables.Booking;
import project.scheduler.Tables.Inscription;
import project.scheduler.Tables.InscriptionsId;

@Transactional
public interface InscriptionRepository extends CrudRepository<Inscription, InscriptionsId> {

    @NativeQuery(value = "SELECT EXISTS (SELECT 1 FROM inscriptions i WHERE i.i_user_id = ?1 AND i.i_course_id = ?2)")
    long ifExists(UUID user_id, UUID course_id);

    @Modifying
    @NativeQuery(value = "DELETE FROM inscriptions i WHERE i.i_user_id = ?1 AND i.i_course_id = ?2")
    Integer deleteByIds(UUID user_id, UUID course_id);

    @NativeQuery(value = "SELECT * FROM (SELECT * FROM (SELECT i_course_id FROM inscriptions i WHERE i.i_user_id = ?1) i, bookings b WHERE b.b_course_id = i.i_course_id AND i_course_id != ?2) b, (SELECT start o_start, end o_end FROM inscriptions i, bookings b WHERE i.i_user_id = ?1 AND b.b_course_id = ?2) i WHERE i.o_start >= b.start AND i.o_end <= b.end")
    Iterable<Booking> findInscriptionConflicts(UUID user_id, UUID course_id);

}
