package project.scheduler.Util;


import org.springframework.security.crypto.bcrypt.BCrypt;

public class Password {
    private String password;

    @SuppressWarnings("unused")
    public Password() {}

    public Password(String new_password) {
        this.password = BCrypt.hashpw(new_password, BCrypt.gensalt());
    }

    public Password(String new_password, Boolean check) {
        this.password = new_password;
    }

    public String getPassword() {
        return this.password;
    }

    public Boolean compare(String new_password) {
        return BCrypt.checkpw(new_password, this.password);
    }
}
