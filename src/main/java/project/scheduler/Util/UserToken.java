package project.scheduler.Util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserToken extends Object{
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

    public UserToken(UUID userId, int token_length) {
        this.tokens = new String[]{createToken(token_length)};
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

    private String createToken(int token_length) {
        RandomStringUtils tok_gen = RandomStringUtils.secure();
        return tok_gen.next(token_length, true, true);
    }

    public UUID getUserId() {
        return userId;
    }
}
