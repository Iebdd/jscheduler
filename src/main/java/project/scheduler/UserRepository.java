package project.scheduler;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

    @NativeQuery(value = "SELECT * FROM users u WHERE u.name= ?1")
    User findByName(String name);

    @NativeQuery(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM users u WHERE u.id = ?1 AND u.password = ?2")
    Boolean verifyPassword(Integer user_id, String password);

    @NativeQuery(value = "SELECT password FROM users u WHERE u.email = ?1")
    String findPasswordByEmail(String email);


}
