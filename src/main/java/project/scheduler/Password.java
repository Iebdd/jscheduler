package project.scheduler;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Password {
    static final String chrs = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final SecureRandom secureRandom = new SecureRandom();
    private String password;
    private String salt;

    public Password() {}

    public Password(String password) {

        this.salt = BCrypt.gensalt();
        System.out.printf("Personal salt: %s%n", this.salt);
        this.password = BCrypt.hashpw(password, this.salt);
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public Boolean compare(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
