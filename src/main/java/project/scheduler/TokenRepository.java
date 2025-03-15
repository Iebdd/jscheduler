package project.scheduler;

import java.time.Instant;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called tokenRepository
// CRUD refers Create, Read, Update, Delete

@Transactional
public interface TokenRepository extends CrudRepository<Token, Integer> {

    @NativeQuery(value = "SELECT * FROM tokens WHERE user_id= ?1")
    User[] findByForeignKey(Integer user_id);

    @NativeQuery(value = "SELECT id FROM tokens WHERE expiry < ?1")
    Integer[] findExpired(Instant now);

    @NativeQuery(value = "SELECT id > 0 FROM tokens t WHERE t.user_id = ?1 AND t.token = ?2 AND t.expiry > ?3")
    Integer findToken(Integer user_id, String token, Instant now);

    @Modifying
    @NativeQuery(value = "UPDATE tokens SET expiry = ?1 WHERE id = ?2")
    Integer refreshToken(Instant newExpiry, Integer id);

    void deleteByIdIn(Integer[] ids);
}
