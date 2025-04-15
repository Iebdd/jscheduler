package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Tables.User;

@Transactional
public interface UserRepository extends CrudRepository<User, UUID> {

    @NativeQuery(value = "SELECT * FROM users u WHERE u.firstName= ?1 AND u.lastName = ?2")
    User findUserByName(String firstName, String lastName);

    @NativeQuery(value = "IF EXISTS (SELECT 1 FROM users u WHERE u.id = ?1 AND u.password = ?2)")
    Boolean verifyPassword(UUID user_id, String password);

    @NativeQuery(value = "SELECT * FROM users u WHERE u.email = lower(?1)")
    User findUserByEmail(String email);

    @NativeQuery(value = "SELECT password FROM users u WHERE u.email = ?1")
    String findPasswordByEmail(String email);

    @Modifying
    @NativeQuery(value = "UPDATE users u SET password = ?1 WHERE u.user_id = ?2")
    void updatePassword(String password, UUID user_id);

    @NativeQuery(value = "SELECT * FROM tokens t, users u WHERE t.token = ?1 AND t.t_user_id = u.user_id")
    User findUserByToken(String token);
}
