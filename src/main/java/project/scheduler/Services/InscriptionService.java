package project.scheduler.Services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.InscriptionRepository;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Inscription;
import project.scheduler.Tables.User;

@Service
public class InscriptionService {

    @Inject
    private InscriptionRepository inscriptionRepository;

    public ResponseEntity<String> remove(int user_id, int course_id) {
        try {
            int rows = inscriptionRepository.deleteByIds(user_id, course_id);
            if(rows == 0) {
              return new ResponseEntity<>(String.format("Could not remove User: %d from Course: %d", user_id, course_id), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
              return ResponseEntity.ok(String.format("Removed User: %d from Course: %d", user_id, course_id));
            }
        } catch (Exception e) {
            return new ResponseEntity<>(String.format("Could not remove User: %d from Course: %d", user_id, course_id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void inscribe(User user, Course course) {
        inscriptionRepository.save(new Inscription(user, course));
    }

    public boolean ifExists(int user_id, int course_id) {
        return inscriptionRepository.ifExists(user_id, course_id) == 1;
    }
    
}
