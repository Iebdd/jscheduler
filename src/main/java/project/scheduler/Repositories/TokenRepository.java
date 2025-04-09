package project.scheduler.Repositories;

import java.time.Instant;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Tables.Token;


// This will be AUTO IMPLEMENTED by Spring into a Bean called tokenRepository
// CRUD refers Create, Read, Update, Delete

@Transactional
public interface TokenRepository extends CrudRepository<Token, Integer> {

    @NativeQuery(value = "SELECT token FROM tokens WHERE user_id= ?1")
    String[] findTokenByUser(Integer user_id);

    @Modifying
    @NativeQuery(value = "DELETE FROM tokens t WHERE t.expiry < ?1")
    void deleteExpired(Instant now);

    @NativeQuery(value = "SELECT id > 0 FROM tokens t WHERE t.user_id = ?1 AND t.token = ?2 AND t.expiry > ?3")
    Integer findToken(Integer user_id, String token, Instant now);

    @Modifying
    @NativeQuery(value = "UPDATE tokens SET expiry = ?1 WHERE id = ?2")
    Integer refreshToken(Instant newExpiry, Integer id);

    void deleteByIdIn(Integer[] ids);
}
