package project.scheduler.Util;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserToken implements Serializable{
    private String[] tokens;
    private UUID userId;
    private boolean check;

    public UserToken(String[] tokens, UUID userId, boolean check) {
        this.tokens = tokens;
        this.userId = userId;
        this.check = check;
    }

    public UserToken(String token, UUID userId, boolean check) {
        this.tokens = new String[]{token};
        this.userId = userId;
        this.check = check;
    }

    public UserToken(UUID userId) {
        this.tokens = new String[]{createToken()};
        this.userId = userId;
        this.check = false;
    }

    public UserToken() {};

    public String[] getTokens() {
        return this.tokens;
    }
    
    @JsonIgnore
    public String getFirstToken() {
        return this.tokens[0];
    }

    public boolean isCheck() {
        return this.check;
    }

    public boolean getCheck() {
        return this.check;
    }

    private String createToken() {
        RandomStringUtils tok_gen = RandomStringUtils.secure();
        return tok_gen.next(25, true, true);
    }

    public UUID getUserId() {
        return userId;
    }
}
