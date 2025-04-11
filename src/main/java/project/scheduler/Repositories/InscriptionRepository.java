package project.scheduler.Repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Tables.Inscription;
import project.scheduler.Tables.InscriptionId;

@Transactional
public interface InscriptionRepository extends CrudRepository<Inscription, InscriptionId> {

    @NativeQuery(value = "SELECT EXISTS (SELECT 1 FROM inscription i WHERE i.i_user_id = ?1 AND i.i_course_id = ?2)")
    long ifExists(int user_id, int course_id);

    @Modifying
    @NativeQuery(value = "DELETE FROM inscription i WHERE i.i_user_id = ?1 AND i.i_course_id = ?2")
    Integer deleteByIds(int user_id, int course_id);

}
