package softball.app.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHash {
    public static void main(String[] args) {
        System.out.println(
                new BCryptPasswordEncoder().encode("Test"));
    }
}