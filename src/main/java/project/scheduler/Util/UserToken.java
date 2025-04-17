package project.scheduler.Util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A class responsible for transmitting new and existing tokens and prompting for validation
 */
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

    public UUID getUserId() {
        return userId;
    }
    
    @JsonIgnore
    public String getFirstToken() {
        return this.tokens[0];
    }

    public boolean isCheck() {
        return this.check;
    }

    /**
     * Creates a new token
     * 
     * @param token_length  The length of the new token
     * 
     * @return  A String representation of the new token
     */
    private String createToken(int token_length) {
        RandomStringUtils tok_gen = RandomStringUtils.secure();
        return tok_gen.next(token_length, true, true);
    }
}
