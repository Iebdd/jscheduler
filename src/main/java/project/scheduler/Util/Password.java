package project.scheduler.Util;


import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * A class responsible for creating new passwords
 */
public class Password {
    private String password;

    public Password() {}

    public Password(String new_password) {
        this.password = BCrypt.hashpw(new_password, BCrypt.gensalt());
    }

    public String getPassword() {
        return this.password;
    }

    /**
     * Compares an existing password with a passed
     * 
     * @param new_password  The passed password in plain text
     * @param old_password  The existing password hashed by BCrypt
     * 
     * @return  True if the passwords match, false if not
     */
    public static boolean compare(String new_password, String old_password) {
        return BCrypt.checkpw(new_password, old_password);
    }
}
