package project.scheduler.Services;

import java.util.UUID;

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

    public void inscribe(User user, Course course) {
        inscriptionRepository.save(new Inscription(user, course));
    }

    public boolean ifExists(UUID user_id, UUID course_id) {
        return inscriptionRepository.ifExists(user_id, course_id) == 1;
    }
    
}
