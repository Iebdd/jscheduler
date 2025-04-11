package project.scheduler.Repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Tables.User;

@Transactional
public interface UserRepository extends CrudRepository<User, Integer> {

    @NativeQuery(value = "SELECT * FROM user u WHERE u.name= ?1")
    User findByName(String name);

    @NativeQuery(value = "IF EXISTS (SELECT 1 FROM user u WHERE u.id = ?1 AND u.password = ?2)")
    Boolean verifyPassword(Integer user_id, String password);

    @NativeQuery(value = "SELECT * FROM user u WHERE u.email = ?1")
    User findUserByEmail(String email);

    @NativeQuery(value = "SELECT password FROM user u WHERE u.email = ?1")
    String findPasswordByEmail(String email);

    @Modifying
    @NativeQuery(value = "UPDATE user SET password = ?1 WHERE id = ?2")
    void updatePassword(String password, Integer user_id);


}
