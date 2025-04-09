package project.scheduler.Util;


import org.springframework.security.crypto.bcrypt.BCrypt;

public class Password {
    private String password;

    @SuppressWarnings("unused")
    public Password() {}

    public Password(String new_password) {
        this.password = BCrypt.hashpw(new_password, BCrypt.gensalt());
    }

    public String getPassword() {
        return this.password;
    }

    public static boolean compare(String new_password, String old_password) {
        return BCrypt.checkpw(new_password, old_password);
    }
}
