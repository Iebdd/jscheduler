package project.scheduler;


import org.springframework.security.crypto.bcrypt.BCrypt;

public class Password {
    private String password;
    private String salt;

    @SuppressWarnings("unused")
    public Password() {}

    public Password(String password) {
        System.out.println(password);
        this.salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, this.salt);
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public Boolean compare(String password) {
        System.out.println(BCrypt.hashpw(password, this.salt));
        System.out.println(this.password);
        return BCrypt.checkpw(password, this.password);
    }
}
