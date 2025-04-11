package project.scheduler.Util;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserToken extends Object{
    private final String[] tokens;
    private final boolean check;

    public UserToken(String[] tokens, boolean check) {
        this.tokens = tokens;
        this.check = check;
    }

    public UserToken(String token, boolean check) {
        this.tokens = new String[]{token};
        this.check = check;
    }

    public UserToken() {
        this.tokens = new String[]{createToken()};
        this.check = false;
    }

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

    private String createToken() {
        RandomStringUtils tok_gen = RandomStringUtils.secure();
        return tok_gen.next(25, true, true);
    }
}
